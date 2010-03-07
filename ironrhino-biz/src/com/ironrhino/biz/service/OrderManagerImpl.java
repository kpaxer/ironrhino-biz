package com.ironrhino.biz.service;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.util.DateUtils;
import org.ironrhino.core.util.NumberUtils;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Order;

@Singleton
@Named("orderManager")
public class OrderManagerImpl extends BaseManagerImpl<Order> implements
		OrderManager {

	@Inject
	@Named("orderCodeSequence")
	private DataFieldMaxValueIncrementer orderCodeSequence;

	@Override
	@Transactional
	public void save(Order order) {
		if (order.isNew())
			order.setCode(nextCode());
		super.save(order);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean canDelete(Order order) {
		return order.isCancelled();
	}

	@Override
	@Transactional
	public void pay(Order order) {
		order.setPaid(true);
		save(order);
	}

	@Override
	@Transactional
	public void ship(Order order) {
		order.setShipped(true);
		save(order);
	}

	@Override
	@Transactional
	public void cancel(Order order) {
		order.setCancelled(true);
		save(order);
	}

	private String nextCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtils.formatDate8(new Date()));
		sb.append(NumberUtils.format(orderCodeSequence.nextIntValue(), 4));
		return sb.toString();
	}

}
