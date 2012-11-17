package com.ironrhino.biz.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.service.EntityManager;
import org.ironrhino.core.util.ErrorMessage;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.Stuffflow;

@Singleton
@Named("stuffManager")
public class StuffManagerImpl extends BaseManagerImpl<Stuff> implements
		StuffManager {
	@Inject
	private transient EntityManager<Stuffflow> entityManager;

	@Override
	@Transactional(readOnly = true)
	public void checkDelete(Stuff e) {
		entityManager.setEntityClass(Stuffflow.class);
		DetachedCriteria dc = entityManager.detachedCriteria();
		dc.createAlias("stuff", "s").add(Restrictions.eq("s.id", e.getId()));
		if (entityManager.countByCriteria(dc) > 0)
			throw new ErrorMessage("delete.forbidden", new Object[] { e },
					"此原料有出入库记录");
	}
}
