package com.ironrhino.biz.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.util.DateUtils;
import org.ironrhino.common.util.NumberUtils;
import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Order;

@Component("orderManager")
public class OrderManagerImpl extends BaseManagerImpl<Order> implements
		OrderManager {

	@Transactional
	public void save(Order order) {
		if (order.isNew())
			order.setCode(nextCode());
		super.save(order);
	}

	private synchronized String nextCode() {
		String prefix = DateUtils.getDate8(new Date());
		DetachedCriteria dc = detachedCriteria();
		dc.add(Restrictions.like("code", prefix + "%"));
		dc.addOrder(org.hibernate.criterion.Order.desc("code"));
		List<Order> results = getListByCriteria(dc, 1, 1);
		int sequence = 0;
		if (results.size() > 0) {
			String lastId = results.get(0).getCode();
			String lastSeq = lastId.substring(8);
			lastSeq = lastSeq.substring(0, lastSeq.length() - 5);
			sequence = Integer.parseInt(lastSeq);
		}
		return prefix + NumberUtils.format(sequence + 1, 5);
	}

}
