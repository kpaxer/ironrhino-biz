package com.ironrhino.biz.service;

import java.sql.SQLException;

import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.util.ErrorMessage;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Product;

@Singleton
@Named("productManager")
public class ProductManagerImpl extends BaseManagerImpl<Product> implements
		ProductManager {

	@Override
	@Transactional(readOnly = true)
	public void checkDelete(final Product product) {
		final String hql = "select count(o) from Order o join o.items item join item.product p where p.id = ?";
		Long count = (Long) executeFind(new HibernateCallback<Long>() {

			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(hql.toString());
				q.setParameter(0, product.getId());
				return (Long) q.uniqueResult();
			}
		});
		if (count > 0)
			throw new ErrorMessage("delete.forbidden",
					new Object[] { product }, "此产品下面有订单");
	}
}
