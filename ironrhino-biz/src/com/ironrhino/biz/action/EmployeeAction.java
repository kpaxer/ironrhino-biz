package com.ironrhino.biz.action;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.elasticsearch.ElasticSearchCriteria;
import org.ironrhino.core.search.elasticsearch.ElasticSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.EmployeeManager;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + "," + UserRole.ROLE_HR)
public class EmployeeAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Employee employee;

	private ResultPage<Employee> resultPage;

	@Inject
	private transient EmployeeManager employeeManager;

	@Autowired(required = false)
	private transient ElasticSearchService<Employee> elasticSearchService;

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
		if (StringUtils.isBlank(keyword) || elasticSearchService == null) {
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
			ElasticSearchCriteria criteria = new ElasticSearchCriteria();
			criteria.setQuery(query);
			criteria.setTypes(new String[] { "employee" });
			if (resultPage == null)
				resultPage = new ResultPage<Employee>();
			resultPage.setCriteria(criteria);
			resultPage = elasticSearchService.search(resultPage);
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNotBlank(id))
			if (StringUtils.isNumeric(id))
				employee = employeeManager.get(Long.valueOf(id));
			else
				employee = employeeManager.findByNaturalId(id);
		if (employee == null)
			employee = new Employee();
		return INPUT;
	}

	@Override
	@Validations(requiredStrings = { @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "employee.name", trim = true, key = "validation.required") })
	public String save() {
		if (!makeEntityValid())
			return INPUT;
		employeeManager.save(employee);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	public String checkavailable() {
		return makeEntityValid() ? NONE : INPUT;
	}

	private boolean makeEntityValid() {
		if (employee == null) {
			addActionError(getText("access.denied"));
			return false;
		}
		if (employee.isNew()) {
			if (StringUtils.isNotBlank(employee.getName())) {
				if (employeeManager.findByNaturalId(employee.getName()) != null) {
					addFieldError("employee.name",
							getText("validation.already.exists"));
					return false;
				}
			}

		} else {
			Employee temp = employee;
			employee = employeeManager.get(temp.getId());
			if (StringUtils.isNotBlank(temp.getName())
					&& !employee.getName().equals(temp.getName())) {
				if (employeeManager.findByNaturalId(temp.getName()) != null) {
					addFieldError("employee.name",
							getText("validation.already.exists"));
					return false;
				}
			}
			if (temp.getMemo() == null)
				temp.setMemo(employee.getMemo());
			BeanUtils.copyProperties(temp, employee);
			employeeManager.evict(employee);
		}
		return true;
	}

	@Override
	public String delete() {
		String[] id = getId();
		if (id != null) {
			employeeManager.delete((Serializable[]) id);
			addActionMessage(getText("delete.success"));
		}
		return SUCCESS;
	}

	@JsonConfig(root = "employee")
	public String json() {
		String id = getUid();
		if (StringUtils.isNumeric(id)) {
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
