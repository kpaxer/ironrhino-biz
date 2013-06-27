package com.ironrhino.biz.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Order;

@Singleton
@Named("customerManager")
public class CustomerManagerImpl extends BaseManagerImpl<Customer> implements
		CustomerManager {

	@Inject
	private OrderManager orderManager;

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
