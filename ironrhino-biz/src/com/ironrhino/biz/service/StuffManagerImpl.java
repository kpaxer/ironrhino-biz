package com.ironrhino.biz.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.Stuffflow;

@Singleton
@Named("stuffManager")
public class StuffManagerImpl extends BaseManagerImpl<Stuff> implements
		StuffManager {
	@Inject
	private transient BaseManager baseManager;

	@Override
	@Transactional(readOnly = true)
	public boolean canDelete(Stuff e) {
		baseManager.setEntityClass(Stuffflow.class);
		DetachedCriteria dc = baseManager.detachedCriteria();
		dc.createAlias("stuff", "s").add(Restrictions.eq("s.id", e.getId()));
		return baseManager.countByCriteria(dc) == 0;
	}
}
