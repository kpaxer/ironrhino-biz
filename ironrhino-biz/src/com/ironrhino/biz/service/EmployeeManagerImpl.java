package com.ironrhino.biz.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Employee;

@Singleton
@Named("employeeManager")
public class EmployeeManagerImpl extends BaseManagerImpl<Employee> implements
		EmployeeManager {
	@Inject
	private transient RewardManager rewardManager;

	@Override
	@Transactional(readOnly = true)
	public boolean canDelete(Employee e) {
		DetachedCriteria dc = rewardManager.detachedCriteria();
		dc.createAlias("employee", "e").add(Restrictions.eq("e.id", e.getId()));
		return rewardManager.countByCriteria(dc) == 0;
	}
}
