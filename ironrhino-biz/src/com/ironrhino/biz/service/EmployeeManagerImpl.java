package com.ironrhino.biz.service;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.service.BaseManagerImpl;

import com.ironrhino.biz.model.Employee;

@Singleton
@Named
public class EmployeeManagerImpl extends BaseManagerImpl<Employee> implements
		EmployeeManager {

}
