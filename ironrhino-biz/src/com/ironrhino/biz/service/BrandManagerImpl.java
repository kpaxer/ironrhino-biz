package com.ironrhino.biz.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.util.ErrorMessage;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Brand;

@Singleton
@Named("brandManager")
public class BrandManagerImpl extends BaseManagerImpl<Brand> implements
		BrandManager {

	@Inject
	private ProductManager productManager;

	@Override
	@Transactional(readOnly = true)
	public void checkDelete(Brand b) {
		DetachedCriteria dc = productManager.detachedCriteria();
		dc.createAlias("brand", "b").add(Restrictions.eq("b.id", b.getId()));
		if (productManager.countByCriteria(dc) > 0)
			throw new ErrorMessage("delete.forbidden", new Object[] { b },
					"此品牌下面有产品");
	}

}
