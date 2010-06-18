package com.ironrhino.biz.service;

import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.model.Customer;

public interface CustomerManager extends BaseManager<Customer> {

	public void merge(Customer source, Customer target);

}
