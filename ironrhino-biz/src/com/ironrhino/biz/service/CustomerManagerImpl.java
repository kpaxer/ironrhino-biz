package com.ironrhino.biz.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Order;

@Component
public class CustomerManagerImpl extends BaseManagerImpl<Customer> implements
		CustomerManager {

	@Autowired
	private OrderManager orderManager;

	@Override
	@Transactional
	public void merge(Customer source, Customer target) {
		if (source == null || target == null)
			throw new IllegalArgumentException("source or target is null");
		DetachedCriteria dc = orderManager.detachedCriteria();
		dc.createAlias("customer", "c").add(
				Restrictions.eq("c.id", source.getId()));
		List<Order> orders = orderManager.findListByCriteria(dc);
		for (Order temp : orders) {
			temp.setCustomer(target);
			orderManager.save(temp);
		}
		delete(source);
	}

}
