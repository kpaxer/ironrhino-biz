package com.ironrhino.biz.action;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.hibernate.CriteriaState;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.Persistable;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.SearchService.Mapper;
import org.ironrhino.core.search.elasticsearch.ElasticSearchCriteria;
import org.ironrhino.core.search.elasticsearch.ElasticSearchService;
import org.ironrhino.core.service.EntityManager;
import org.ironrhino.core.struts.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.EmployeeType;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.Returning;
import com.ironrhino.biz.model.ReturningItem;
import com.ironrhino.biz.model.Station;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.CustomerManager;
import com.ironrhino.biz.service.EmployeeManager;
import com.ironrhino.biz.service.ProductManager;
import com.ironrhino.biz.service.StationManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + ","
		+ UserRole.ROLE_SALESMAN)
public class ReturningAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Returning returning;

	private ResultPage<Returning> resultPage;

	private Customer customer;

	private Employee salesman;

	private Long[] productId;

	private Long stationId;

	private List<Station> stationList;

	private List<Product> productList;

	private List<Employee> salesmanList;

	private transient EntityManager<Returning> entityManager;

	@Autowired
	private transient CustomerManager customerManager;

	@Autowired
	private transient EmployeeManager employeeManager;

	@Autowired
	private transient ProductManager productManager;

	@Autowired
	private transient StationManager stationManager;

	@Autowired(required = false)
	private transient ElasticSearchService<Returning> elasticSearchService;

	public Class<? extends Persistable<?>> getEntityClass() {
		return Returning.class;
	}

	public ResultPage<Returning> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Returning> resultPage) {
		this.resultPage = resultPage;
	}

	public void setEntityManager(EntityManager<Returning> entityManager) {
		entityManager.setEntityClass(Returning.class);
		this.entityManager = entityManager;
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

	public Returning getReturning() {
		return returning;
	}

	public void setReturning(Returning returning) {
		this.returning = returning;
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
		if (StringUtils.isBlank(keyword) || elasticSearchService == null) {
			DetachedCriteria dc = entityManager.detachedCriteria();
			CriteriaState criteriaState = CriterionUtils.filter(dc,
					getEntityClass());
			if (StringUtils.isNotBlank(keyword)) {
				if (!criteriaState.getAliases().containsKey("customer")) {
					dc.createAlias("customer", "c");
					criteriaState.getAliases().put("customer", "c");
				}
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE,
						criteriaState.getAliases().get("customer") + ".name",
						"memo"));
			}
			if (criteriaState.getOrderings().isEmpty())
				dc.addOrder(org.hibernate.criterion.Order.desc("returnDate"));
			if (resultPage == null)
				resultPage = new ResultPage<Returning>();
			resultPage.setCriteria(dc);
			resultPage = entityManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			if (query.matches("^\\d{4}-\\d{2}-\\d{2}$"))
				query = "returnDate:" + query;
			if (query.matches("^\\d{4}年\\d{2}月\\d{2}日$")) {
				StringBuilder sb = new StringBuilder();
				sb.append("returnDate:");
				sb.append(query.substring(0, 4));
				sb.append('-');
				sb.append(query.substring(5, 7));
				sb.append('-');
				sb.append(query.substring(8, 10));
				query = sb.toString();
			}
			ElasticSearchCriteria criteria = new ElasticSearchCriteria();
			criteria.addSort("returnDate", true);
			criteria.setQuery(query);
			criteria.setTypes(new String[] { "returning" });
			if (resultPage == null)
				resultPage = new ResultPage<Returning>();
			resultPage.setCriteria(criteria);
			resultPage = elasticSearchService.search(resultPage,
					new Mapper<Returning>() {
						@Override
						public Returning map(Returning source) {
							return entityManager.get(source.getId());
						}
					});
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			returning = entityManager.get(id);
			if (returning == null)
				returning = entityManager.findByNaturalId(id);
		}
		if (returning == null) {
			returning = new Returning();
			if (customer != null && customer.getId() != null)
				customer = customerManager.get(customer.getId());
			if (salesman != null && salesman.getId() != null)
				salesman = employeeManager.get(salesman.getId());
		} else {
			customer = returning.getCustomer();
			salesman = returning.getSalesman();
			if (returning.getStation() != null)
				stationId = returning.getStation().getId();
			productId = new Long[returning.getItems().size()];
			for (int i = 0; i < productId.length; i++)
				productId[i] = returning.getItems().get(i).getProduct().getId();
		}
		productList = productManager.findAll();
		DetachedCriteria dc = employeeManager.detachedCriteria();
		// dc.add(Restrictions.eq("type", EmployeeType.SALESMAN));
		dc.add(Restrictions.eq("dimission", false));
		// dc.addReturning(org.hibernate.criterion.Returning.asc("name"));
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
		stationList = stationManager.findAll(org.hibernate.criterion.Order
				.asc("id"));
		return INPUT;
	}

	@Override
	public String save() {
		if (returning == null)
			return INPUT;
		Returning temp = returning;
		if (!returning.isNew()) {
			returning = entityManager.get(temp.getId());
			if (temp.getVersion() > -1
					&& temp.getVersion() < returning.getVersion()) {
				addActionError(getText("validation.version.conflict"));
				return INPUT;
			}
			returning.setCustomer(customer);
			returning.setReturnDate(temp.getReturnDate());
			returning.setFreight(temp.getFreight());
			returning.setMemo(temp.getMemo());
		}
		String customerName = customer.getName().trim();
		customer = customerManager.findByNaturalId(customerName);
		if (customer == null) {
			customer = new Customer(customerName);
			customerManager.save(customer);
		}
		returning.setCustomer(customer);
		if (salesman != null && salesman.getId() != null) {
			salesman = employeeManager.get(salesman.getId());
			returning.setSalesman(salesman);
		} else {
			returning.setSalesman(null);
		}

		if (stationId != null)
			returning.setStation(stationManager.get(stationId));
		else
			returning.setStation(null);
		returning.setItems(temp.getItems());
		if (productId != null) {
			for (int i = 0; i < returning.getItems().size(); i++) {
				if (i >= productId.length)
					break;
				ReturningItem item = returning.getItems().get(i);
				if (item == null) {
					returning.getItems().remove(i);
					continue;
				}
				if (item.getQuantity() > 0 && item.getPrice() != null)
					item.setProduct(productManager.get(productId[i]));
				else
					returning.getItems().remove(i);
			}
		}
		if (returning.getItems().size() == 0) {
			addActionError("没有订单项");
			return INPUT;
		}
		entityManager.save(returning);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String view() {
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			returning = entityManager.get(id);
			if (returning == null)
				returning = entityManager.findByNaturalId(id);
		}
		if (returning == null)
			return ACCESSDENIED;
		else
			return VIEW;
	}

	@Override
	public String delete() {
		String[] id = getId();
		if (id != null) {
			entityManager.setEntityClass(Returning.class);
			entityManager.delete((Serializable[]) id);
			addActionMessage(getText("delete.success"));
		}
		return SUCCESS;
	}

}
