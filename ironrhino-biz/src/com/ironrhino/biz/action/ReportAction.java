package com.ironrhino.biz.action;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.DateUtils;

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.service.CustomerManager;
import com.ironrhino.biz.service.EmployeeManager;
import com.ironrhino.biz.service.OrderManager;
import com.ironrhino.biz.service.ProductManager;
import com.ironrhino.biz.service.RewardManager;

public class ReportAction extends BaseAction {

	private static final long serialVersionUID = -7256690227585867617L;

	private static final String datePattern = "yyyy年MM月dd日";

	private String type;

	private String title = "";

	private Date date;

	private Date from;

	private Date to;

	private boolean includePaid;

	private boolean negative;

	private String format = "PDF";

	private String dataSource;

	private Map<String, Object> reportParameters = new HashMap<String, Object>();

	private List<?> list;

	private List<Employee> employeeList;

	@Inject
	private transient RewardManager rewardManager;

	@Inject
	private transient EmployeeManager employeeManager;

	@Inject
	private transient CustomerManager customerManager;

	@Inject
	private transient OrderManager orderManager;

	@Inject
	private transient ProductManager productManager;

	@Inject
	private transient RegionTreeControl regionTreeControl;

	public void setNegative(boolean negative) {
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

	public void setIncludePaid(boolean includePaid) {
		this.includePaid = includePaid;
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

	public String getDataSource() {
		return dataSource;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public String getDocumentName() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(URLEncoder.encode(title, "UTF-8"));
			if (date != null)
				sb.append(DateUtils.formatDate8(date));
			else if (from != null && to != null)
				if (!DateUtils.isSameDay(from, to))
					sb.append(DateUtils.formatDate8(from)).append('-').append(
							DateUtils.formatDate8(to));
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
				reportParameters.put("date", DateUtils
						.format(from, datePattern)
						+ "-" + DateUtils.format(to, datePattern));
			else
				reportParameters.put("date", DateUtils
						.format(from, datePattern));
		reportParameters.put("SUBREPORT_DIR", ServletActionContext
				.getServletContext().getRealPath("/WEB-INF/view/jasper/"));
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
		dc.addOrder(org.hibernate.criterion.Order.asc("name"));
		employeeList = employeeManager.findListByCriteria(dc);
		return SUCCESS;
	}

	public String jasper() {
		if (type != null) {
			try {
				Method method = getClass().getDeclaredMethod(type);
				method.invoke(this, new Object[0]);
			} catch (Exception e) {
			}
		}
		if (list == null || list.isEmpty()) {
			addActionMessage("没有数据");
			return SUCCESS;
		}

		return "jasper";
	}

	public void product() {
		date = new Date();
		DetachedCriteria dc = productManager.detachedCriteria();
		if (!negative) {
			title = "产品库存量";
			dc.add(Restrictions.gt("stock", 0));
			dc.addOrder(org.hibernate.criterion.Order.desc("stock"));
			list = productManager.findListByCriteria(dc);
		} else {
			title = "产品欠货量";
			dc.add(Restrictions.lt("stock", 0));
			dc.addOrder(org.hibernate.criterion.Order.asc("stock"));
			List<Product> pl = productManager.findListByCriteria(dc);
			for (Product p : pl)
				p.setStock(-p.getStock());
			list = pl;
		}

	}

	public void customer() {
		title = "客户信息";
		DetachedCriteria dc = customerManager.detachedCriteria();
		dc.add(Restrictions.between("createDate", DateUtils
				.beginOfDay(getFrom()), DateUtils.endOfDay(getTo())));
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
		dc.add(Restrictions.between("rewardDate", DateUtils
				.beginOfDay(getFrom()), DateUtils.endOfDay(getTo())));
		if (!includePaid)
			dc.add(Restrictions.gt("amount", new BigDecimal(0)));
		else
			title += "(包括支出)";
		dc.addOrder(org.hibernate.criterion.Order.desc("rewardDate"));
		dc.addOrder(org.hibernate.criterion.Order.desc("amount"));
		list = rewardManager.findListByCriteria(dc);
	}

	public void personalreward() {
		Employee employee = null;
		String id = getUid();
		if (StringUtils.isNumeric(id))
			employee = employeeManager.get(Long.valueOf(id));
		else if (StringUtils.isNotBlank(id))
			employee = employeeManager.findByNaturalId(id);
		if (employee != null) {
			title = employee.getName() + "工资详单";
			DetachedCriteria dc = rewardManager.detachedCriteria();
			dc.createAlias("employee", "e").add(
					Restrictions.eq("e.id", employee.getId()));
			dc.add(Restrictions.between("rewardDate", DateUtils
					.beginOfDay(getFrom()), DateUtils.endOfDay(getTo())));
			if (!includePaid)
				dc.add(Restrictions.gt("amount", new BigDecimal(0)));
			else
				title += "(包括支出)";
			dc.addOrder(org.hibernate.criterion.Order.asc("rewardDate"));
			list = rewardManager.findListByCriteria(dc);
		}
	}

	public void aggregationreward() {
		title = "工资汇总单";
		DetachedCriteria dc = rewardManager.detachedCriteria();
		dc.add(Restrictions.between("rewardDate", DateUtils
				.beginOfDay(getFrom()), DateUtils.endOfDay(getTo())));
		if (!includePaid)
			dc.add(Restrictions.gt("amount", new BigDecimal(0)));
		else
			title += "(包括支出)";
		dc.createAlias("employee", "e").addOrder(
				org.hibernate.criterion.Order.asc("e.name"));
		list = rewardManager.findListByCriteria(dc);
	}

	public void order() {
		title = "详细订单报表";
		DetachedCriteria dc = orderManager.detachedCriteria();
		dc.add(Restrictions.between("orderDate", DateUtils
				.beginOfDay(getFrom()), DateUtils.endOfDay(getTo())));
		dc.addOrder(org.hibernate.criterion.Order.asc("code"));
		list = orderManager.findListByCriteria(dc);
	}
}
