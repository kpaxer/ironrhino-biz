package com.ironrhino.biz.action;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.struts.BaseAction;

import com.ironrhino.biz.service.OrderManager;

@AutoConfig
public class IndexAction extends BaseAction {

	private static final long serialVersionUID = 29792886600858873L;

	@Inject
	private OrderManager orderManager;

	@Override
	public String execute() {
		DetachedCriteria dc = orderManager.detachedCriteria();
		dc.add(Restrictions.eq("paid", false));
		int unpaid = orderManager.countByCriteria(dc);
		if(unpaid>0)
			addActionMessage("有"+unpaid+"个订单还未付款");
		dc = orderManager.detachedCriteria();
		dc.add(Restrictions.eq("shipped", false));
		int unshipped = orderManager.countByCriteria(dc);
		if(unshipped>0)
			addActionMessage("有"+unshipped+"个订单还未发货");
		return SUCCESS;
	}

}
