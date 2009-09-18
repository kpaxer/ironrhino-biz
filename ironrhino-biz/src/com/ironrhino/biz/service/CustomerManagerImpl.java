package com.ironrhino.biz.service;

import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.stereotype.Component;

import com.ironrhino.biz.model.Customer;

@Component("customerManager")
public class CustomerManagerImpl extends BaseManagerImpl<Customer> implements
		CustomerManager {

}
