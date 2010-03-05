package com.ironrhino.biz.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.DateUtils;

import com.ironrhino.biz.model.Reward;
import com.ironrhino.biz.service.RewardManager;

public class ReportAction extends BaseAction {

	private static final long serialVersionUID = -7256690227585867617L;

	private String type = "dailyreward";

	private Date date;

	private Date from;

	private Date to;

	private String format = "PDF";

	private String dataSource;

	private Map<String, Object> reportParameters = new HashMap<String, Object>();

	private List<Reward> rewardList;

	@Inject
	private transient RewardManager rewardManager;

	public Map<String, Object> getReportParameters() {
		return reportParameters;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getLocation() {
		return "/WEB-INF/view/jasper/" + type + ".jasper";
	}

	public List<Reward> getRewardList() {
		return rewardList;
	}

	@Override
	public String execute() {
		if ("dailyreward".equals(type)) {
			DetachedCriteria dc = rewardManager.detachedCriteria();
			dc.add(
					Restrictions.between("rewardDate", getFrom(), DateUtils
							.addDays(getTo(), 1))).add(
					Restrictions.gt("amount", new BigDecimal(0)));
			dc.addOrder(org.hibernate.criterion.Order.desc("amount"));
			rewardList = rewardManager.findListByCriteria(dc);
			dataSource = "rewardList";
			reportParameters.put("title", "日工资结单");
		}
		return SUCCESS;
	}
}
