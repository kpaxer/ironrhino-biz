package com.ironrhino.biz.action;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.service.EmployeeManager;
import com.ironrhino.biz.service.RewardManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class EmployeeAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Employee employee;

	private ResultPage<Employee> resultPage;

	@Inject
	private transient EmployeeManager employeeManager;

	@Inject
	private transient RewardManager rewardManager;

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
		DetachedCriteria dc = employeeManager.detachedCriteria();
		if (resultPage == null)
			resultPage = new ResultPage<Employee>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(org.hibernate.criterion.Order.desc("disabled"));
		resultPage.addOrder(org.hibernate.criterion.Order.asc("id"));
		resultPage = employeeManager.findByResultPage(resultPage);
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNumeric(id))
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
			DetachedCriteria dc = employeeManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Employee> list = employeeManager.findListByCriteria(dc);
			if (list.size() > 0) {
				boolean deletable = true;
				for (Employee c : list) {
					dc = rewardManager.detachedCriteria();
					dc.createAlias("employee", "c").add(
							Restrictions.eq("c.id", c.getId()));
					if (rewardManager.countByCriteria(dc) > 0) {
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
