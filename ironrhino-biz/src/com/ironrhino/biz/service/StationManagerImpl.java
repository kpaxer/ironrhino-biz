package com.ironrhino.biz.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.Station;

@Singleton
@Named("stationManager")
public class StationManagerImpl extends BaseManagerImpl<Station> implements
		StationManager {

	@Inject
	private OrderManager orderManager;

	@Override
	@Transactional
	public void merge(Station source, Station target) {
		DetachedCriteria dc = orderManager.detachedCriteria();
		dc.createAlias("station", "c").add(
				Restrictions.eq("c.id", source.getId()));
		List<Order> orders = orderManager.findListByCriteria(dc);
		for (Order temp : orders) {
			temp.setStation(target);
			orderManager.save(temp);
		}
		delete(source);
	}

}
