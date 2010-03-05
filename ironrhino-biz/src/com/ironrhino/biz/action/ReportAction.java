package com.ironrhino.biz.action;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.DateUtils;

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.service.CustomerManager;
import com.ironrhino.biz.service.RewardManager;

public class ReportAction extends BaseAction {

	private static final long serialVersionUID = -7256690227585867617L;

	private static final String datePattern = "yyyy年MM月dd日";

	private String type = "dailyreward";

	private String title = "";

	private Date date;

	private Date from;

	private Date to;

	private String format = "PDF";

	private String dataSource;

	private Map<String, Object> reportParameters = new HashMap<String, Object>();

	private List<?> list;

	@Inject
	private transient RewardManager rewardManager;

	@Inject
	private transient CustomerManager customerManager;

	@Inject
	private transient RegionTreeControl regionTreeControl;

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		if (date == null)
			date = DateUtils.justDate(new Date());
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
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getDataSource() {
		return dataSource;
	}

	public String getDocumentName() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(URLEncoder.encode(title, "UTF-8"));
			if (date != null)
				sb.append(DateUtils.formatDate8(date));
			else if (from != null && to != null)
				sb.append(DateUtils.formatDate8(from)).append('-').append(
						DateUtils.formatDate8(to));
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
			reportParameters.put("date", DateUtils.format(from, datePattern)
					+ "-" + DateUtils.format(to, datePattern));
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
		if ("dailyreward".equals(type)) {
			title = "日工资结单";
			DetachedCriteria dc = rewardManager.detachedCriteria();
			dc.add(
					Restrictions.between("rewardDate", getFrom(), DateUtils
							.addDays(getTo(), 1))).add(
					Restrictions.gt("amount", new BigDecimal(0))).addOrder(
					org.hibernate.criterion.Order.desc("rewardDate")).addOrder(
					org.hibernate.criterion.Order.desc("amount"));
			list = rewardManager.findListByCriteria(dc);
		} else if ("dailycustomer".equals(type)) {
			title = "客户信息";
			DetachedCriteria dc = customerManager.detachedCriteria();
			dc.add(
					Restrictions.between("createDate", getFrom(), DateUtils
							.addDays(getTo(), 1))).addOrder(
					org.hibernate.criterion.Order.asc("name"));
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
		return SUCCESS;
	}
}
