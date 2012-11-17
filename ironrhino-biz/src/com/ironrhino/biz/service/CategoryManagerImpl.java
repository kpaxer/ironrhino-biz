package com.ironrhino.biz.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.util.ErrorMessage;
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
	public void checkDelete(Category c) {
		DetachedCriteria dc = productManager.detachedCriteria();
		dc.createAlias("category", "c").add(Restrictions.eq("c.id", c.getId()));
		if (productManager.countByCriteria(dc) > 0) {
			throw new ErrorMessage("delete.forbidden", new Object[] { c },
					"此品种下面有产品");
		}
	}

}
