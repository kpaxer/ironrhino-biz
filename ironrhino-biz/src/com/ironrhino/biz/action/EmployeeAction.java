package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.EmployeeManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR)
public class EmployeeAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Employee employee;

	private ResultPage<Employee> resultPage;

	@Inject
	private transient EmployeeManager employeeManager;

	@Autowired(required = false)
	private transient CompassSearchService compassSearchService;

	public ResultPage<Employee> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Employee> resultPage) {
		this.resultPage = resultPage;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword) || compassSearchService == null) {
			DetachedCriteria dc = employeeManager.detachedCriteria();
			Criterion filtering = CriterionUtils.filter(employee, "id", "name",
					"type", "dimission");
			if (filtering != null)
				dc.add(filtering);
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "name",
						"phone", "address"));
			dc.addOrder(org.hibernate.criterion.Order.asc("dimission"));
			dc.addOrder(org.hibernate.criterion.Order.asc("type"));
			if (resultPage == null)
				resultPage = new ResultPage<Employee>();
			resultPage.setCriteria(dc);
			resultPage = employeeManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "employee" });
			if (resultPage == null)
				resultPage = new ResultPage<Employee>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			CompassSearchResults searchResults = compassSearchService
					.search(cc);
			int totalHits = searchResults.getTotalHits();
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Employee> list = new ArrayList<Employee>(hits.length);
				for (CompassHit ch : searchResults.getHits()) {
					Employee e = (Employee) ch.getData();
					e = employeeManager.get(e.getId());
					list.add(e);
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
		if (org.ironrhino.core.util.StringUtils.isNumericOnly(id))
			employee = employeeManager.get(Long.valueOf(id));
		if (employee == null)
			employee = new Employee();
		return INPUT;
	}

	@Override
	public String save() {
		if (employee == null)
			return INPUT;
		if (employee.isNew()) {
			if (employeeManager.findByNaturalId(employee.getName()) != null) {
				addFieldError("employee.name",
						getText("validation.already.exists"));
				return INPUT;
			}
		} else {
			Employee temp = employee;
			employee = employeeManager.get(temp.getId());
			if (!employee.getName().equals(temp.getName())) {
				if (employeeManager.findByNaturalId(temp.getName()) != null) {
					addFieldError("employee.name",
							getText("validation.already.exists"));
					return INPUT;
				}
			}
			if (temp.getMemo() == null)
				temp.setMemo(employee.getMemo());
			BeanUtils.copyProperties(temp, employee);
		}
		employeeManager.save(employee);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String delete() {
		String[] _id = getId();
		if (_id != null) {
			Long[] id = new Long[_id.length];
			for (int i = 0; i < _id.length; i++)
				id[i] = Long.valueOf(_id[i]);
			List<Employee> list;
			if (id.length == 1) {
				list = new ArrayList<Employee>(1);
				list.add(employeeManager.get(id[0]));
			} else {
				DetachedCriteria dc = employeeManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = employeeManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				boolean deletable = true;
				for (Employee c : list) {
					if (!employeeManager.canDelete(c)) {
						deletable = false;
						addActionError(c.getName() + "有工资,不能删除");
						break;
					}
				}
				if (deletable) {
					for (Employee employee : list)
						employeeManager.delete(employee);
					addActionMessage(getText("delete.success"));
				}
			}
		}
		return SUCCESS;
	}

	@JsonConfig(root = "employee")
	public String json() {
		String id = getUid();
		if (org.ironrhino.core.util.StringUtils.isNumericOnly(id)) {
			employee = employeeManager.get(Long.valueOf(id));
		} else if (StringUtils.isNotBlank(id)) {
			id = id.trim();
			employee = employeeManager.findByNaturalId(id);
		}
		if (employee == null)
			employee = new Employee();
		return JSON;
	}

}
