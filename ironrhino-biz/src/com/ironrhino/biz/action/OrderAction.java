package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.SearchService.Mapper;
import org.ironrhino.core.search.elasticsearch.ElasticSearchCriteria;
import org.ironrhino.core.search.elasticsearch.ElasticSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + ","
		+ UserRole.ROLE_SALESMAN)
public class OrderAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Order order;

	private ResultPage<Order> resultPage;

	private Customer customer;

	private Employee salesman;

	private Employee deliveryman;

	private Long[] productId;

	private Long stationId;

	private int threshold = 7;

	private List<Station> stationList;

	private List<Product> productList;

	private List<Employee> salesmanList;

	private List<Employee> deliverymanList;

	private List<Order> unpaidOrders;

	private List<Order> unshippedOrders;

	private List<Date> uninputedDates;

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

	@Autowired(required = false)
	private transient ElasticSearchService<Order> elasticSearchService;

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

	public List<Employee> getDeliverymanList() {
		return deliverymanList;
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

	public Employee getDeliveryman() {
		return deliveryman;
	}

	public void setDeliveryman(Employee deliveryman) {
		this.deliveryman = deliveryman;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public List<Date> getUninputedDates() {
		return uninputedDates;
	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword) || elasticSearchService == null) {
			DetachedCriteria dc = orderManager.detachedCriteria();
			Criterion filtering = CriterionUtils.filter(order, "id", "code");
			if (filtering != null)
				dc.add(filtering);
			if (StringUtils.isNotBlank(keyword))
				dc.createAlias("customer", "customer").add(
						CriterionUtils.like(keyword, MatchMode.ANYWHERE,
								"customer.name", "code", "memo"));
			if (customer != null && customer.getId() != null)
				dc.createAlias("customer", "c").add(
						Restrictions.eq("c.id", customer.getId()));
			if (stationId != null)
				dc.createAlias("station", "st").add(
						Restrictions.eq("st.id", stationId));
			if (salesman != null && salesman.getId() != null)
				dc.createAlias("salesman", "e").add(
						Restrictions.eq("e.id", salesman.getId()));
			if (deliveryman != null && deliveryman.getId() != null)
				dc.createAlias("deliveryman", "d").add(
						Restrictions.eq("d.id", deliveryman.getId()));
			dc.addOrder(org.hibernate.criterion.Order.desc("orderDate"));
			dc.addOrder(org.hibernate.criterion.Order.desc("code"));
			if (resultPage == null)
				resultPage = new ResultPage<Order>();
			resultPage.setCriteria(dc);
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
			ElasticSearchCriteria criteria = new ElasticSearchCriteria();
			// criteria.setMinScore(2.0f);
			criteria.addSort("orderDate", true);
			criteria.setQuery(query);
			criteria.setTypes(new String[] { "order" });
			if (resultPage == null)
				resultPage = new ResultPage<Order>();
			resultPage.setCriteria(criteria);
			resultPage = elasticSearchService.search(resultPage,
					new Mapper<Order>() {
						public Order map(Order source) {
							return orderManager.get(source.getId());
						}
					});
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			order = orderManager.get(id);
			if (order == null)
				order = orderManager.findByNaturalId(id);
		}
		if (order == null) {
			order = new Order();
			if (customer != null && customer.getId() != null)
				customer = customerManager.get(customer.getId());
			if (salesman != null && salesman.getId() != null)
				salesman = employeeManager.get(salesman.getId());
			if (deliveryman != null && deliveryman.getId() != null)
				deliveryman = employeeManager.get(deliveryman.getId());
		} else {
			customer = order.getCustomer();
			salesman = order.getSalesman();
			deliveryman = order.getDeliveryman();
			if (order.getStation() != null)
				stationId = order.getStation().getId();
			productId = new Long[order.getItems().size()];
			for (int i = 0; i < productId.length; i++)
				productId[i] = order.getItems().get(i).getProduct().getId();
		}
		productList = productManager.findAll();
		DetachedCriteria dc = employeeManager.detachedCriteria();
		// dc.add(Restrictions.eq("type", EmployeeType.SALESMAN));
		dc.add(Restrictions.eq("dimission", false));
		// dc.addOrder(org.hibernate.criterion.Order.asc("name"));
		salesmanList = employeeManager.findListByCriteria(dc);
		Collections.sort(salesmanList, new Comparator<Employee>() {
			@Override
			public int compare(Employee o1, Employee o2) {
				if (o1.getType() == o2.getType())
					return o1.getName().compareTo(o2.getName());
				else {
					if (o1.getType() == EmployeeType.SALESMAN
							&& o2.getType() != EmployeeType.SALESMAN)
						return -1;
					else if (o1.getType() != EmployeeType.SALESMAN
							&& o2.getType() == EmployeeType.SALESMAN)
						return 1;
					else
						return o1.getName().compareTo(o2.getName());
				}
			}
		});
		dc = employeeManager.detachedCriteria();
		// dc.add(Restrictions.eq("type", EmployeeType.DELIVERYMAN));
		dc.add(Restrictions.eq("dimission", false));
		// dc.addOrder(org.hibernate.criterion.Order.asc("name"));
		deliverymanList = employeeManager.findListByCriteria(dc);
		Collections.sort(deliverymanList, new Comparator<Employee>() {
			@Override
			public int compare(Employee o1, Employee o2) {
				if (o1.getType() == o2.getType())
					return o1.getName().compareTo(o2.getName());
				else {
					if (o1.getType() == EmployeeType.DELIVERYMAN
							&& o2.getType() != EmployeeType.DELIVERYMAN)
						return -1;
					else if (o1.getType() != EmployeeType.DELIVERYMAN
							&& o2.getType() == EmployeeType.DELIVERYMAN)
						return 1;
					else
						return o1.getName().compareTo(o2.getName());
				}
			}
		});
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
			order.setDiscount(temp.getDiscount());
			order.setOrderDate(temp.getOrderDate());
			order.setSaleType(temp.getSaleType());
			order.setPaid(temp.isPaid());
			order.setPayDate(temp.getPayDate());
			order.setShipped(temp.isShipped());
			order.setShipDate(temp.getShipDate());
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
		if (deliveryman != null && deliveryman.getId() != null) {
			deliveryman = employeeManager.get(deliveryman.getId());
			order.setDeliveryman(deliveryman);
		} else {
			order.setDeliveryman(null);
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
				if (item.getQuantity() > 0 && item.getPrice() != null) {
					Product p = productManager.get(productId[i]);
					if (p == null) {
						addActionError("请选择产品");
						return INPUT;
					} else
						item.setProduct(p);
				} else
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
			if (order == null)
				order = orderManager.findByNaturalId(id);

		}
		if (order == null)
			return ACCESSDENIED;
		else
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
		Collections.sort(unpaidOrders, new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				int value = Boolean.valueOf(o2.isCashable()).compareTo(
						Boolean.valueOf(o1.isCashable()));
				if (o1.isCashable() && value == 0)
					return o2.getStation().getName()
							.compareTo(o1.getStation().getName());
				else
					return value;
			}
		});
		return "unpaid";
	}

	public String unshipped() {
		DetachedCriteria dc = orderManager.detachedCriteria();
		dc.add(Restrictions.eq("shipped", false));
		unshippedOrders = orderManager.findListByCriteria(dc);
		return "unshipped";
	}

	public String uninputed() {
		uninputedDates = new ArrayList<Date>();
		Date today = new Date();
		for (int i = 0; i < threshold; i++) {
			Date orderDate = DateUtils.beginOfDay(DateUtils.addDays(today, -i));
			DetachedCriteria dc = orderManager.detachedCriteria();
			dc.add(Restrictions.eq("orderDate", orderDate));
			if (orderManager.countByCriteria(dc) == 0)
				uninputedDates.add(orderDate);
		}
		return "uninputed";
	}

}
