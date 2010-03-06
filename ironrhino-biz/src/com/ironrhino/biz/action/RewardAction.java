package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.Reward;
import com.ironrhino.biz.service.EmployeeManager;
import com.ironrhino.biz.service.RewardManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR + "," + Constants.ROLE_HR)
public class RewardAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Reward reward;

	private ResultPage<Reward> resultPage;

	private Employee employee;

	private List<Employee> employeeList;

	@Inject
	private transient RewardManager rewardManager;

	@Inject
	private transient EmployeeManager employeeManager;

	@Inject
	private transient CompassSearchService compassSearchService;

	public ResultPage<Reward> getResultPage() {
		return resultPage;
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

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword)) {
			DetachedCriteria dc = rewardManager.detachedCriteria();
			if (resultPage == null)
				resultPage = new ResultPage<Reward>();
			resultPage.setDetachedCriteria(dc);
			if (employee != null && employee.getId() != null)
				dc.createAlias("employee", "c").add(
						Restrictions.eq("c.id", employee.getId()));
			resultPage.addOrder(org.hibernate.criterion.Order
					.desc("rewardDate"));
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
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "reward" });
			if (resultPage == null)
				resultPage = new ResultPage<Reward>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			CompassSearchResults searchResults = compassSearchService
					.search(cc);
			int totalHits = searchResults.getTotalHits();
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Reward> list = new ArrayList<Reward>(hits.length);
				for (CompassHit ch : searchResults.getHits())
					list.add((Reward) ch.data());
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
			reward = rewardManager.get(id);
		if (reward == null) {
			reward = new Reward();
			if (employee != null && employee.getId() != null) {
				employee = employeeManager.get(employee.getId());
			}
			DetachedCriteria dc = employeeManager.detachedCriteria();
			dc.add(Restrictions.eq("dimission", false));
			dc.addOrder(Order.asc("name"));
			employeeList = employeeManager.findListByCriteria(dc);
		} else {
			employee = reward.getEmployee();
		}
		return INPUT;
	}

	@Override
	public String save() {
		if (reward == null)
			return INPUT;
		if (reward.isNew()) {
			reward.setEmployee(employeeManager.get(employee.getId()));
		} else {
			Reward temp = reward;
			reward = rewardManager.get(temp.getId());
			BeanUtils.copyProperties(temp, reward);
		}
		rewardManager.save(reward);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String delete() {
		String[] id = getId();
		if (id != null) {
			DetachedCriteria dc = rewardManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Reward> list = rewardManager.findListByCriteria(dc);
			if (list.size() > 0) {
				for (Reward temp : list)
					rewardManager.delete(temp);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

}
