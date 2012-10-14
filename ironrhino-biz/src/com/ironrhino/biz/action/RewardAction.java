package com.ironrhino.biz.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.elasticsearch.ElasticSearchCriteria;
import org.ironrhino.core.search.elasticsearch.ElasticSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.ironrhino.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.Reward;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.EmployeeManager;
import com.ironrhino.biz.service.RewardManager;
import com.opensymphony.xwork2.util.CreateIfNull;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + "," + UserRole.ROLE_HR)
public class RewardAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Boolean negative;

	private Reward reward;

	private List<Reward> rewardList;

	private ResultPage<Reward> resultPage;

	private Employee employee;

	private List<Employee> employeeList;

	private int threshold = 7;

	private List<Date> uninputedDates;

	@Inject
	private transient RewardManager rewardManager;

	@Inject
	private transient EmployeeManager employeeManager;

	@Autowired(required = false)
	private transient ElasticSearchService<Reward> elasticSearchService;

	@CreateIfNull
	public List<Reward> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
	}

	public ResultPage<Reward> getResultPage() {
		return resultPage;
	}

	public Boolean isNegative() {
		return negative;
	}

	public void setNegative(Boolean negative) {
		this.negative = negative;
	}

	public void setResultPage(ResultPage<Reward> resultPage) {
		this.resultPage = resultPage;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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
		String view = ServletActionContext.getRequest().getParameter("view");
		if (StringUtils.isNotBlank(view))
			return view;
		if (StringUtils.isBlank(keyword) || elasticSearchService == null) {
			DetachedCriteria dc = rewardManager.detachedCriteria();
			dc.createAlias("employee", "employee");
			Criterion filtering = CriterionUtils.filter(reward, "id",
					"rewardDate", "type");
			if (filtering != null)
				dc.add(filtering);
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE,
						"employee.name", "memo"));
			if (employee != null && employee.getId() != null)
				dc.add(Restrictions.eq("employee.id", employee.getId()));
			if (negative != null)
				if (!negative)
					dc.add(Restrictions.gt("amount", new BigDecimal(0)));
				else
					dc.add(Restrictions.lt("amount", new BigDecimal(0)));
			dc.addOrder(org.hibernate.criterion.Order.desc("rewardDate"));
			dc.addOrder(org.hibernate.criterion.Order.asc("type"));
			if (resultPage == null)
				resultPage = new ResultPage<Reward>();
			resultPage.setCriteria(dc);
			resultPage = rewardManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			if (query.matches("^\\d{4}-\\d{2}-\\d{2}$"))
				query = "rewardDate:" + query;
			if (query.matches("^\\d{4}年\\d{2}月\\d{2}日$")) {
				StringBuilder sb = new StringBuilder();
				sb.append("rewardDate:");
				sb.append(query.substring(0, 4));
				sb.append('-');
				sb.append(query.substring(5, 7));
				sb.append('-');
				sb.append(query.substring(8, 10));
				query = sb.toString();
			}
			ElasticSearchCriteria criteria = new ElasticSearchCriteria();
			criteria.setQuery(query);
			criteria.setTypes(new String[] { "reward" });
			if (resultPage == null)
				resultPage = new ResultPage<Reward>();
			resultPage.setCriteria(criteria);
			resultPage = elasticSearchService.search(resultPage);
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNotBlank(id))
			reward = rewardManager.get(id);
		if (reward == null) {
			reward = new Reward();
			if (employee != null && employee.getId() != null) {
				employee = employeeManager.get(employee.getId());
			}
			DetachedCriteria dc = employeeManager.detachedCriteria();
			dc.add(Restrictions.eq("dimission", false));
			dc.addOrder(Order.asc("type"));
			employeeList = employeeManager.findListByCriteria(dc);
		} else {
			negative = reward.getAmount().doubleValue() < 0;
			if (negative)
				reward.setAmount(reward.getAmount().negate());
			employee = reward.getEmployee();
		}
		return INPUT;
	}

	@Override
	public String save() {
		if (employee != null)
			employee = employeeManager.get(employee.getId());
		if (rewardList != null) {
			if (employee == null)
				return INPUT;
			for (Reward r : rewardList) {
				if (r == null || r.getAmount() == null)
					continue;
				r.setEmployee(employee);
				r.setRewardDate(reward.getRewardDate());
				if (negative != null && negative)
					r.setAmount(r.getAmount().negate());
				rewardManager.save(r);
			}
			addActionMessage(getText("save.success"));
		} else {
			if (reward == null)
				return INPUT;
			if (negative != null && negative)
				reward.setAmount(reward.getAmount().negate());
			if (reward.isNew()) {
				if (employee == null)
					return INPUT;
				reward.setEmployee(employee);
			} else {
				Reward temp = reward;
				reward = rewardManager.get(temp.getId());
				BeanUtils.copyProperties(temp, reward);
			}
			rewardManager.save(reward);
			addActionMessage(getText("save.success"));
		}
		return SUCCESS;
	}

	@Override
	public String delete() {
		String[] id = getId();
		if (id != null) {
			List<Reward> list;
			if (id.length == 1) {
				list = new ArrayList<Reward>(1);
				reward = rewardManager.get(id[0]);
				if (reward != null)
					list.add(reward);
			} else {
				DetachedCriteria dc = rewardManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = rewardManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				for (Reward temp : list)
					rewardManager.delete(temp);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

	public String uninputed() {
		uninputedDates = new ArrayList<Date>();
		Date today = new Date();
		for (int i = 0; i < threshold; i++) {
			Date rewardDate = DateUtils
					.beginOfDay(DateUtils.addDays(today, -i));
			DetachedCriteria dc = rewardManager.detachedCriteria();
			dc.add(Restrictions.eq("rewardDate", rewardDate));
			if (rewardManager.countByCriteria(dc) == 0)
				uninputedDates.add(rewardDate);
		}
		return "uninputed";
	}

}
