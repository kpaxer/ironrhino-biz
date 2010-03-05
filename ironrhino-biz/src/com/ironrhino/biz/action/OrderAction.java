package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.OrderItem;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.service.CustomerManager;
import com.ironrhino.biz.service.OrderManager;
import com.ironrhino.biz.service.ProductManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR + ","
		+ Constants.ROLE_SALESMAN)
public class OrderAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Order order;

	private ResultPage<Order> resultPage;

	private Customer customer;

	private String keyword;

	private Long[] productId;

	private List<Product> productList;

	@Inject
	private transient OrderManager orderManager;

	@Inject
	private transient CustomerManager customerManager;

	@Inject
	private transient ProductManager productManager;

	@Inject
	private transient CompassSearchService compassSearchService;

	public ResultPage<Order> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Order> resultPage) {
		this.resultPage = resultPage;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public Long[] getProductId() {
		return productId;
	}

	public void setProductId(Long[] productId) {
		this.productId = productId;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword)) {
			DetachedCriteria dc = orderManager.detachedCriteria();
			if (resultPage == null)
				resultPage = new ResultPage<Order>();
			resultPage.setDetachedCriteria(dc);
			if (customer != null && customer.getId() != null)
				dc.createAlias("customer", "c").add(
						Restrictions.eq("c.id", customer.getId()));
			resultPage.addOrder(org.hibernate.criterion.Order.desc("code"));
			resultPage = orderManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			if (query.matches("^\\d{4}-\\d{2}-\\d{2}$"))
				query = "orderDate:" + query;
			if (query.matches("^\\d{4}年\\d{2}月\\d{2}日$")) {
				StringBuilder sb = new StringBuilder();
				sb.append("orderDate:");
				sb.append(query.substring(0, 4));
				sb.append('-');
				sb.append(query.substring(5, 7));
				sb.append('-');
				sb.append(query.substring(8, 10));
				query = sb.toString();
			}
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "order" });
			if (resultPage == null)
				resultPage = new ResultPage<Order>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			CompassSearchResults searchResults = compassSearchService
					.search(cc);
			int totalHits = searchResults.getTotalHits();
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Order> list = new ArrayList<Order>(hits.length);
				for (CompassHit ch : searchResults.getHits()) {
					Order c = (Order) ch.getData();
					c = orderManager.get(c.getId());
					if (c != null)
						list.add(c);
					else
						totalHits--;
				}
				resultPage.setResult(list);
			} else {
				resultPage.setResult(Collections.EMPTY_LIST);
			}
			resultPage.setTotalRecord(totalHits);
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNotBlank(id))
			order = orderManager.get(id);
		if (order == null) {
			order = new Order();
			if (customer != null && customer.getId() != null)
				customer = customerManager.get(customer.getId());
			productList = productManager.findAll(org.hibernate.criterion.Order
					.asc("displayOrder"));
		} else {
			customer = order.getCustomer();
		}
		return INPUT;
	}

	@Override
	public String save() {
		if (order == null)
			return INPUT;
		if (order.isNew()) {
			String customerName = customer.getName().trim();
			customer = customerManager.findByNaturalId(customerName);
			if (customer == null) {
				customer = new Customer(customerName);
				customerManager.save(customer);
			}
			order.setCustomer(customer);
			if (productId != null) {
				for (int i = 0; i < order.getItems().size(); i++) {
					if (i >= productId.length)
						break;
					OrderItem item = order.getItems().get(i);
					if (item == null)
						continue;
					if (item.getQuantity() > 0 && item.getPrice() != null)
						item.setProduct(productManager.get(productId[i]));
					else
						order.getItems().remove(i);
				}
			}
		} else {
			Order temp = order;
			order = orderManager.get(temp.getId());
			order.setOrderDate(temp.getOrderDate());
			order.setMemo(temp.getMemo());
			order.setPaid(temp.isPaid());
			order.setShipped(temp.isShipped());
			order.setCancelled(temp.isCancelled());
		}
		orderManager.save(order);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String view() {
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			order = orderManager.get(id);
		} else {
			return ACCESSDENIED;
		}
		return VIEW;
	}

	@Override
	public String delete() {
		String[] id = getId();
		if (id != null) {
			DetachedCriteria dc = orderManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Order> list = orderManager.findListByCriteria(dc);
			if (list.size() > 0) {
				boolean deletable = true;
				for (Order temp : list) {
					if (!temp.isCancelled()) {
						addActionError("请先取消订单" + temp.getCode());
						deletable = false;
						break;
					}
				}
				if (deletable) {
					for (Order temp : list)
						orderManager.delete(temp);
					addActionMessage(getText("delete.success"));
				}
			}
		}
		return SUCCESS;
	}

}