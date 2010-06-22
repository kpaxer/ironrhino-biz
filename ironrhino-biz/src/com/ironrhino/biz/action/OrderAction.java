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

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.EmployeeType;
import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.OrderItem;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.Station;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.CustomerManager;
import com.ironrhino.biz.service.EmployeeManager;
import com.ironrhino.biz.service.OrderManager;
import com.ironrhino.biz.service.ProductManager;
import com.ironrhino.biz.service.StationManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR)
public class OrderAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Order order;

	private ResultPage<Order> resultPage;

	private Customer customer;

	private Employee salesman;

	private Long[] productId;

	private Long stationId;

	private List<Station> stationList;

	private List<Product> productList;

	private List<Employee> salesmanList;

	private List<Order> unpaidOrders;

	private List<Order> unshippedOrders;

	@Inject
	private transient OrderManager orderManager;

	@Inject
	private transient CustomerManager customerManager;

	@Inject
	private transient EmployeeManager employeeManager;

	@Inject
	private transient ProductManager productManager;

	@Inject
	private transient StationManager stationManager;

	@Inject
	private transient CompassSearchService compassSearchService;

	public ResultPage<Order> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Order> resultPage) {
		this.resultPage = resultPage;
	}

	public List<Order> getUnpaidOrders() {
		return unpaidOrders;
	}

	public List<Order> getUnshippedOrders() {
		return unshippedOrders;
	}

	public List<Employee> getSalesmanList() {
		return salesmanList;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public List<Station> getStationList() {
		return stationList;
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

	public Employee getSalesman() {
		return salesman;
	}

	public void setSalesman(Employee salesman) {
		this.salesman = salesman;
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
			if (salesman != null && salesman.getId() != null)
				dc.createAlias("salesman", "e").add(
						Restrictions.eq("e.id", salesman.getId()));
			// resultPage.addOrder(org.hibernate.criterion.Order.asc("paid"));
			// resultPage.addOrder(org.hibernate.criterion.Order.asc("shipped"));
			resultPage
					.addOrder(org.hibernate.criterion.Order.desc("orderDate"));
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
			cc.addSort("orderDate", null, true);
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
			if (salesman != null && salesman.getId() != null)
				salesman = employeeManager.get(salesman.getId());

		} else {
			customer = order.getCustomer();
			salesman = order.getSalesman();
			if (order.getStation() != null)
				stationId = order.getStation().getId();
			productId = new Long[order.getItems().size()];
			for (int i = 0; i < productId.length; i++)
				productId[i] = order.getItems().get(i).getProduct().getId();
		}
		productList = productManager.findAll(org.hibernate.criterion.Order
				.asc("displayOrder"));
		DetachedCriteria dc = employeeManager.detachedCriteria();
		dc.add(Restrictions.eq("type", EmployeeType.SALESMAN));
		dc.add(Restrictions.eq("dimission", false));
		salesmanList = employeeManager.findListByCriteria(dc);
		stationList = stationManager.findAll(org.hibernate.criterion.Order
				.asc("id"));
		return INPUT;
	}

	@Override
	public String save() {
		if (order == null)
			return INPUT;
		Order temp = order;
		if (!order.isNew()) {
			order = orderManager.get(temp.getId());
			order.setCustomer(customer);
			order.setOrderDate(temp.getOrderDate());
			order.setSaleType(temp.getSaleType());
			order.setPaid(temp.isPaid());
			order.setShipped(temp.isShipped());
			order.setFreight(temp.getFreight());
			order.setMemo(temp.getMemo());
		}
		String customerName = customer.getName().trim();
		customer = customerManager.findByNaturalId(customerName);
		if (customer == null) {
			customer = new Customer(customerName);
			customerManager.save(customer);
		}
		order.setCustomer(customer);
		if (salesman != null && salesman.getId() != null) {
			salesman = employeeManager.get(salesman.getId());
			order.setSalesman(salesman);
		} else {
			order.setSalesman(null);
		}
		if (stationId != null)
			order.setStation(stationManager.get(stationId));
		else
			order.setStation(null);
		order.setItems(temp.getItems());
		if (productId != null) {
			for (int i = 0; i < order.getItems().size(); i++) {
				if (i >= productId.length)
					break;
				OrderItem item = order.getItems().get(i);
				if (item == null) {
					order.getItems().remove(i);
					continue;
				}
				if (item.getQuantity() > 0 && item.getPrice() != null)
					item.setProduct(productManager.get(productId[i]));
				else
					order.getItems().remove(i);
			}
		}
		if (order.getItems().size() == 0) {
			addActionError("没有订单项");
			return INPUT;
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
			List<Order> list;
			if (id.length == 1) {
				list = new ArrayList<Order>(1);
				list.add(orderManager.get(id[0]));
			} else {
				DetachedCriteria dc = orderManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = orderManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				boolean deletable = true;
				for (Order temp : list) {
					if (!orderManager.canDelete(temp)) {
						addActionError("订单" + temp.getCode() + "已付款或已发货,不能删除");
						deletable = false;
						break;
					}
				}
				if (deletable) {
					for (Order temp : list)
						orderManager.cancel(temp);
					addActionMessage(getText("delete.success"));
				}
			}
		}
		return SUCCESS;
	}

	public String pay() {
		String[] id = getId();
		if (id != null) {
			List<Order> list;
			if (id.length == 1) {
				list = new ArrayList<Order>(1);
				list.add(orderManager.get(id[0]));
			} else {
				DetachedCriteria dc = orderManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = orderManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				for (Order temp : list)
					orderManager.pay(temp);
				addActionMessage(getText("operate.success"));
			}
		}
		return REFERER;
	}

	public String ship() {
		String[] id = getId();
		if (id != null) {
			List<Order> list;
			if (id.length == 1) {
				list = new ArrayList<Order>(1);
				list.add(orderManager.get(id[0]));
			} else {
				DetachedCriteria dc = orderManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = orderManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				for (Order temp : list)
					orderManager.ship(temp);
				addActionMessage(getText("operate.success"));
			}
		}
		return REFERER;
	}

	public String unpaid() {
		DetachedCriteria dc = orderManager.detachedCriteria();
		dc.add(Restrictions.eq("paid", false));
		unpaidOrders = orderManager.findListByCriteria(dc);
		return "unpaid";
	}

	public String unshipped() {
		DetachedCriteria dc = orderManager.detachedCriteria();
		dc.add(Restrictions.eq("shipped", false));
		unshippedOrders = orderManager.findListByCriteria(dc);
		return "unshipped";
	}

}
