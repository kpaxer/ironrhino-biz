package com.ironrhino.biz.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Category;

@Singleton
@Named("categoryManager")
public class CategoryManagerImpl extends BaseManagerImpl<Category> implements
		CategoryManager {

	@Inject
	private ProductManager productManager;

	@Override
	@Transactional(readOnly = true)
	public boolean canDelete(Category c) {
		DetachedCriteria dc = productManager.detachedCriteria();
		dc.createAlias("category", "c").add(Restrictions.eq("c.id", c.getId()));
		return productManager.countByCriteria(dc) == 0;
	}

}