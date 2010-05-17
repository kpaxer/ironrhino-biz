package com.ironrhino.biz.service;

import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.model.Order;

public interface OrderManager extends BaseManager<Order> {

	public void save(Order order);

	public void place(Order order);
	
	public void modify(Order order);
	
	public void cancel(Order order);

	public void pay(Order order);

	public void ship(Order order);

}
