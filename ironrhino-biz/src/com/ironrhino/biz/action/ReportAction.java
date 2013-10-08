package com.ironrhino.biz.action;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.Region;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.service.EntityManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.DateUtils;
import org.ironrhino.core.util.ErrorMessage;

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.EmployeeType;
import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.OrderItem;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.SaleType;
import com.ironrhino.biz.model.Stuffflow;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.CustomerManager;
import com.ironrhino.biz.service.EmployeeManager;
import com.ironrhino.biz.service.OrderManager;
import com.ironrhino.biz.service.ProductManager;
import com.ironrhino.biz.service.RewardManager;
import com.ironrhino.biz.service.StuffManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR)
@SuppressWarnings("rawtypes")
public class ReportAction extends BaseAction {

	private static final long serialVersionUID = -7256690227585867617L;

	private static final String datePattern = "yyyy年MM月dd日";

	private String type;

	private String title = "";

	private Date date;

	private Date from;

	private Date to;

	private boolean negative;

	private String format = "PDF";

	private Map<String, Object> reportParameters = new HashMap<String, Object>();

	private List<? extends Serializable> list;

	private List<Employee> employeeList;

	private List<Employee> salesmanList;

	private List<Employee> deliverymanList;

	@Autowired
	private transient RewardManager rewardManager;

	@Autowired
	private transient EmployeeManager employeeManager;

	@Autowired
	private transient CustomerManager customerManager;

	@Autowired
	private transient OrderManager orderManager;

	@Autowired
	private transient StuffManager stuffManager;

	@Autowired
	private transient ProductManager productManager;

	@Autowired
	private transient EntityManager entityManager;

	@Autowired
	private transient RegionTreeControl regionTreeControl;

	public void setNegative(Boolean negative) {
		this.negative = negative;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		if (date == null)
			date = new Date();
		return date;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Date getFrom() {
		if (from == null)
			from = getDate();
		return from;
	}

	public Date getTo() {
		if (to == null) {
			to = getFrom();
		}
		return to;
	}

	public String getFormat() {
		return format.toUpperCase();
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public List<Employee> getSalesmanList() {
		return salesmanList;
	}

	public List<Employee> getDeliverymanList() {
		return deliverymanList;
	}

	public String getDocumentName() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(URLEncoder.encode(title, "UTF-8"));
			if (date != null)
				sb.append(DateUtils.formatDate8(date));
			else if (from != null && to != null)
				if (!DateUtils.isSameDay(from, to))
					sb.append(DateUtils.formatDate8(from)).append('-')
							.append(DateUtils.formatDate8(to));
				else
					sb.append(DateUtils.formatDate8(from));
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public Map<String, Object> getReportParameters() {
		reportParameters.put("title", title);
		if (date != null)
			reportParameters.put("date", DateUtils.format(date, datePattern));
		else if (from != null && to != null)
			if (!DateUtils.isSameDay(from, to))
				reportParameters.put(
						"date",
						DateUtils.format(from, datePattern) + "-"
								+ DateUtils.format(to, datePattern));
			else
				reportParameters.put("date",
						DateUtils.format(from, datePattern));
		else
			reportParameters.put("date", "");
		reportParameters.put("SUBREPORT_DIR", ServletActionContext
				.getServletContext().getRealPath("/WEB-INF/view/jasper/"));
		JRAbstractLRUVirtualizer virtualizer = new JRGzipVirtualizer(2);
		reportParameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		return reportParameters;
	}

	public String getLocation() {
		return "/WEB-INF/view/jasper/" + type + ".jasper";
	}

	public List<?> getList() {
		return list;
	}

	@Override
	public String execute() {
		DetachedCriteria dc = employeeManager.detachedCriteria();
		dc.add(Restrictions.eq("dimission", false));
		dc.addOrder(org.hibernate.criterion.Order.asc("type"));
		employeeList = employeeManager.findListByCriteria(dc);
		dc = employeeManager.detachedCriteria();
		dc.add(Restrictions.eq("dimission", false));
		dc.addOrder(org.hibernate.criterion.Order.asc("type"));
		employeeList = employeeManager.findListByCriteria(dc);
		dc = employeeManager.detachedCriteria();
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
		return SUCCESS;
	}

	public String jasper() {
		if (type != null) {
			try {
				Method method = getClass().getDeclaredMethod(type);
				method.invoke(this, new Object[0]);
			} catch (NoSuchMethodException e) {
				throw new ErrorMessage("没有此报表");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (list == null || list.isEmpty()) {
			addActionError("没有数据");
			return ERROR;
		}
		return "jasper";
	}

	public void product() {
		date = new Date();
		DetachedCriteria dc = productManager.detachedCriteria();
		if (!negative) {
			title = "产品库存单";
			dc.add(Restrictions.gt("stock", 0));
			dc.addOrder(org.hibernate.criterion.Order.desc("stock"));
			list = productManager.findListByCriteria(dc);
		} else {
			title = "产品欠货单";
			dc.add(Restrictions.lt("stock", 0));
			dc.addOrder(org.hibernate.criterion.Order.asc("stock"));
			List<Product> pl = productManager.findListByCriteria(dc);
			for (Product p : pl)
				p.setStock(-p.getStock());
			list = pl;
		}

	}

	public void stuff() {
		date = new Date();
		DetachedCriteria dc = stuffManager.detachedCriteria();
		title = "原料库存单";
		dc.add(Restrictions.gt("stock", 0));
		dc.addOrder(org.hibernate.criterion.Order.desc("stock"));
		list = productManager.findListByCriteria(dc);

	}

	@SuppressWarnings("unchecked")
	public void stuffflow() {
		title = "出入库统计";
		entityManager.setEntityClass(Stuffflow.class);
		DetachedCriteria dc = entityManager.detachedCriteria();
		dc.add(Restrictions.between("date", DateUtils.beginOfDay(getFrom()),
				DateUtils.endOfDay(getTo())));
		dc.createAlias("stuff", "s").addOrder(
				org.hibernate.criterion.Order.asc("s.name"));
		dc.addOrder(org.hibernate.criterion.Order.desc("quantity"));
		list = rewardManager.findListByCriteria(dc);
	}

	public void customer() {
		title = "客户信息";
		DetachedCriteria dc = customerManager.detachedCriteria();
		String id = getUid();
		if (StringUtils.isNotBlank(id)) {
			Region r = regionTreeControl.getRegionTree()
					.getDescendantOrSelfById(Long.valueOf(id));
			if (r == null)
				return;
			if (r.isLeaf()) {
				dc.createAlias("region", "r").add(
						Restrictions.eq("r.id", r.getId()));
			} else {
				dc.createAlias("region", "r").add(
						Restrictions.or(
								Restrictions.eq("r.id", r.getId()),
								Restrictions.like("r.fullId", r.getFullId()
										+ ".", MatchMode.START)));
			}
		} else {
			dc.add(Restrictions.between("createDate",
					DateUtils.beginOfDay(getFrom()),
					DateUtils.endOfDay(getTo())));
		}
		dc.addOrder(org.hibernate.criterion.Order.asc("name"));
		List<Customer> cl = customerManager.findListByCriteria(dc);
		for (Customer c : cl) {
			if (c.getRegion() != null) {
				String address = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(c.getRegion().getId())
						.getFullname()
						+ c.getAddress();
				c.setAddress(address);
				c.setRegion(null);
			}
		}
		list = cl;
	}

	public void reward() {
		title = "日工资结单";
		DetachedCriteria dc = rewardManager.detachedCriteria();
		dc.add(Restrictions.between("rewardDate",
				DateUtils.beginOfDay(getFrom()), DateUtils.endOfDay(getTo())));
		if (StringUtils.isNotBlank(ServletActionContext.getRequest()
				.getParameter("negative")))
			if (!negative) {
				dc.add(Restrictions.gt("amount", new BigDecimal(0)));
				title += "(收入)";
			} else {
				dc.add(Restrictions.lt("amount", new BigDecimal(0)));
				title += "(支出)";
			}
		dc.addOrder(org.hibernate.criterion.Order.desc("rewardDate"));
		dc.addOrder(org.hibernate.criterion.Order.asc("type"));
		dc.addOrder(org.hibernate.criterion.Order.desc("amount"));
		list = rewardManager.findListByCriteria(dc);
	}

	public void privatereward() {
		title = "工资详单";
		Employee employee = null;
		String id = getUid();
		if (StringUtils.isNumeric(id))
			employee = employeeManager.get(Long.valueOf(id));
		else if (StringUtils.isNotBlank(id))
			employee = employeeManager.findByNaturalId(id);
		DetachedCriteria dc = rewardManager.detachedCriteria();
		dc.createAlias("employee", "e");
		if (employee != null) {
			title = employee.getName() + title;
			dc.add(Restrictions.eq("e.id", employee.getId()));
		}
		dc.add(Restrictions.between("rewardDate",
				DateUtils.beginOfDay(getFrom()), DateUtils.endOfDay(getTo())));
		if (StringUtils.isNotBlank(ServletActionContext.getRequest()
				.getParameter("negative")))
			if (!negative) {
				dc.add(Restrictions.gt("amount", new BigDecimal(0)));
				title += "(收入)";
			} else {
				dc.add(Restrictions.lt("amount", new BigDecimal(0)));
				title += "(支出)";
			}
		dc.addOrder(org.hibernate.criterion.Order.asc("e.id"));
		dc.addOrder(org.hibernate.criterion.Order.asc("rewardDate"));
		dc.addOrder(org.hibernate.criterion.Order.asc("type"));
		list = rewardManager.findListByCriteria(dc);
	}

	public void aggregationreward() {
		title = "工资汇总单";
		DetachedCriteria dc = rewardManager.detachedCriteria();
		dc.add(Restrictions.between("rewardDate",
				DateUtils.beginOfDay(getFrom()), DateUtils.endOfDay(getTo())));
		if (StringUtils.isNotBlank(ServletActionContext.getRequest()
				.getParameter("negative")))
			if (!negative) {
				dc.add(Restrictions.gt("amount", new BigDecimal(0)));
				title += "(收入)";
			} else {
				dc.add(Restrictions.lt("amount", new BigDecimal(0)));
				title += "(支出)";
			}
		dc.createAlias("employee", "e").addOrder(
				org.hibernate.criterion.Order.asc("e.name"));
		list = rewardManager.findListByCriteria(dc);
	}

	public void order() {
		title = "订单详细报表";
		DetachedCriteria dc = prepareOrderCriteria();
		dc.addOrder(org.hibernate.criterion.Order.asc("orderDate"));
		dc.addOrder(org.hibernate.criterion.Order.asc("code"));
		list = orderManager.findListByCriteria(dc);
	}

	public void dailysales() {
		title = "按天统计销量报表";
		DetachedCriteria dc = prepareOrderCriteria();
		dc.addOrder(org.hibernate.criterion.Order.asc("orderDate"));
		list = orderManager.findListByCriteria(dc);
	}

	public void productsales() {
		title = "产品销量报表";
		DetachedCriteria dc = prepareOrderCriteria();
		List<Order> ol = orderManager.findListByCriteria(dc);
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (Order o : ol) {
			List<OrderItem> items = o.getItems();
			for (int i = 0; i < items.size(); i++) {
				OrderItem item = items.get(i);
				if (i > 0) {
					Order temp = new Order();
					temp.setOrderDate(item.getOrder().getOrderDate());
					temp.setDiscount(new BigDecimal(0.00));
					temp.setFreight(new BigDecimal(0.00));
					temp.setGrandTotal(new BigDecimal(0.00));
					item.setOrder(temp);
				}
				orderItems.add(item);
			}
		}
		list = orderItems;
	}

	private DetachedCriteria prepareOrderCriteria() {
		DetachedCriteria dc = orderManager.detachedCriteria();
		String customer = ServletActionContext.getRequest().getParameter(
				"customer");
		if (StringUtils.isNotBlank(customer)) {
			Customer cust;
			if (StringUtils.isNumeric(customer))
				cust = customerManager.get(Long.valueOf(customer));
			else
				cust = customerManager.findByNaturalId(customer);
			if (cust != null)
				title = cust.getName() + title;
			dc.add(Restrictions.eq("customer", cust));
		}
		String salesman = ServletActionContext.getRequest().getParameter(
				"salesman");
		if (StringUtils.isNotBlank(salesman)) {
			Employee employee;
			if (StringUtils.isNumeric(salesman))
				employee = employeeManager.get(Long.valueOf(salesman));
			else
				employee = employeeManager.findByNaturalId(salesman);
			if (employee != null)
				title = employee.getName() + title;
			dc.add(Restrictions.eq("salesman", employee));

		}
		String deliveryman = ServletActionContext.getRequest().getParameter(
				"deliveryman");
		if (StringUtils.isNotBlank(deliveryman)) {
			Employee employee;
			if (StringUtils.isNumeric(deliveryman))
				employee = employeeManager.get(Long.valueOf(deliveryman));
			else
				employee = employeeManager.findByNaturalId(deliveryman);
			if (employee != null)
				title = employee.getName() + title;
			dc.add(Restrictions.eq("deliveryman", employee));
		}
		String saletype = ServletActionContext.getRequest().getParameter(
				"saletype");
		if (StringUtils.isNotBlank(saletype)) {
			SaleType st = SaleType.parse(saletype);
			if (st != null)
				title = st.getDisplayName() + title;
			dc.add(Restrictions.eq("saleType", st));
		}
		dc.add(Restrictions.between("orderDate",
				DateUtils.beginOfDay(getFrom()), DateUtils.endOfDay(getTo())));
		return dc;
	}
}
