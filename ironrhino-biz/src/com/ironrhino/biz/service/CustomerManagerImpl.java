package com.ironrhino.biz.service;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.service.BaseManagerImpl;

import com.ironrhino.biz.model.Customer;

@Singleton
@Named("customerManager")
public class CustomerManagerImpl extends BaseManagerImpl<Customer> implements
		CustomerManager {

}
