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
import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.EmployeeType;
import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.OrderItem;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.service.CustomerManager;
import com.ironrhino.biz.service.EmployeeManager;
import com.ironrhino.biz.service.OrderManager;
import com.ironrhino.biz.service.ProductManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR + ","
		+ Constants.ROLE_SALESMAN)
public class OrderAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Order order;

	private ResultPage<Order> resultPage;

	private Customer customer;

	private Employee employee;

	private Long[] productId;

	private List<Product> productList;

	private List<Employee> salesmanList;

	@Inject
	private transient OrderManager orderManager;

	@Inject
	private transient CustomerManager customerManager;

	@Inject
	private transient EmployeeManager employeeManager;

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

	public List<Employee> getSalesmanList() {
		return salesmanList;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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
			if (employee != null && employee.getId() != null)
				dc.createAlias("employee", "e").add(
						Restrictions.eq("e.id", employee.getId()));
			//resultPage.addOrder(org.hibernate.criterion.Order.asc("paid"));
			//resultPage.addOrder(org.hibernate.criterion.Order.asc("shipped"));
			resultPage.addOrder(org.hibernate.criterion.Order.desc("orderDate"));
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
			if (employee != null && employee.getId() != null)
				employee = employeeManager.get(employee.getId());
			productList = productManager.findAll(org.hibernate.criterion.Order
					.asc("displayOrder"));
		} else {
			customer = order.getCustomer();
			employee = order.getEmployee();
		}
		DetachedCriteria dc = employeeManager.detachedCriteria();
		dc.add(Restrictions.eq("type", EmployeeType.SALESMAN));
		dc.add(Restrictions.eq("dimission", false));
		salesmanList = employeeManager.findListByCriteria(dc);
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
			if (employee != null && employee.getId() != null) {
				employee = employeeManager.get(employee.getId());
				order.setEmployee(employee);
			}
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
			if (order.getItems().size() == 0) {
				addActionError("没有订单项");
				return INPUT;
			}
			orderManager.place(order);
		} else {
			Order temp = order;
			order = orderManager.get(temp.getId());
			order.setOrderDate(temp.getOrderDate());
			order.setFreight(temp.getFreight());
			order.setSaleType(temp.getSaleType());
			order.setMemo(temp.getMemo());
			if (employee != null && employee.getId() != null) {
				if (order.getEmployee() == null
						|| !order.getEmployee().getId()
								.equals(employee.getId())) {
					employee = employeeManager.get(employee.getId());
					order.setEmployee(employee);
				}
			} else {
				order.setEmployee(null);
			}
			orderManager.save(order);
		}

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

}
