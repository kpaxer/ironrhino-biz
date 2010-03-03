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

	private transient CompassSearchResults searchResults;

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

	public CompassSearchResults getSearchResults() {
		return searchResults;
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
			resultPage
					.addOrder(org.hibernate.criterion.Order.desc("code"));
			resultPage = orderManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "order" });
			if (resultPage == null)
				resultPage = new ResultPage<Order>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			searchResults = compassSearchService.search(cc);
			resultPage.setTotalRecord(searchResults.getTotalHits());
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Order> list = new ArrayList<Order>(hits.length);
				for (CompassHit ch : searchResults.getHits()) {
					Order c = (Order) ch.getData();
					c = orderManager.get(c.getId());
					list.add(c);
				}
				resultPage.setResult(list);
			} else {
				resultPage.setResult(Collections.EMPTY_LIST);
			}
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
			customer = customerManager.get(customer.getId());
			order.setCustomer(customer);
			if (productId != null) {
				for (int i = 0; i < productId.length; i++)
					order.getItems().get(i).setProduct(
							productManager.get(productId[i]));
			}
		} else {
			Order temp = order;
			order = orderManager.get(temp.getId());
			order.setMemo(temp.getMemo());
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
				for (Order order : list)
					if (order.isCancelled())
						orderManager.delete(order);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

	public String pay() {
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			order = orderManager.get(id);
			order.setPaid(true);
			orderManager.save(order);
		} else {
			return ACCESSDENIED;
		}
		return VIEW;
	}

	public String ship() {
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			order = orderManager.get(id);
			order.setShipped(true);
			orderManager.save(order);
		} else {
			return ACCESSDENIED;
		}
		return VIEW;
	}

	public String cancel() {
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			order = orderManager.get(id);
			order.setCancelled(true);
			orderManager.save(order);
		} else {
			return ACCESSDENIED;
		}
		return VIEW;
	}

}
