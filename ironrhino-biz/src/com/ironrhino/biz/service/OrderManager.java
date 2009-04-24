package com.ironrhino.biz.service;

import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.model.Order;

public interface OrderManager extends BaseManager<Order> {

	public void save(Order order);

}
