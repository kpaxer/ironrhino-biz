package com.ironrhino.biz.service;

import java.util.Date;

import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.util.CodecUtils;
import org.ironrhino.core.util.DateUtils;
import org.ironrhino.core.util.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Order;

@Component("orderManager")
public class OrderManagerImpl extends BaseManagerImpl<Order> implements
		OrderManager {

	@Autowired
	@Qualifier("orderCodeSequence")
	private DataFieldMaxValueIncrementer orderCodeSequence;

	@Transactional
	public void save(Order order) {
		if (order.isNew())
			order.setCode(nextCode());
		super.save(order);
	}

	private String nextCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtils.formatDate8(new Date()));
		sb.append(NumberUtils.format(orderCodeSequence.nextIntValue(), 5));
		sb.append(CodecUtils.randomString(5));
		return sb.toString();
	}

}
