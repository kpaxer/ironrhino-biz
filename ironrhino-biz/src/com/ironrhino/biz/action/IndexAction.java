package com.ironrhino.biz.action;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.struts.BaseAction;

import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.Plan;
import com.ironrhino.biz.service.OrderManager;
import com.ironrhino.biz.service.PlanManager;

@AutoConfig
public class IndexAction extends BaseAction {

	private static final long serialVersionUID = 29792886600858873L;

	private List<Order> unpaidOrders;

	private List<Order> unshippedOrders;

	private List<Plan> uncompletedPlans;

	public List<Order> getUnpaidOrders() {
		return unpaidOrders;
	}

	public List<Order> getUnshippedOrders() {
		return unshippedOrders;
	}

	public List<Plan> getUncompletedPlans() {
		return uncompletedPlans;
	}

	@Inject
	private OrderManager orderManager;

	@Inject
	private PlanManager planManager;

	@Override
	public String execute() {
		DetachedCriteria dc = orderManager.detachedCriteria();
		dc.add(Restrictions.eq("paid", false));
		unpaidOrders = orderManager.findListByCriteria(dc);
		dc = orderManager.detachedCriteria();
		dc.add(Restrictions.eq("shipped", false));
		unshippedOrders = orderManager.findListByCriteria(dc);
		dc = planManager.detachedCriteria();
		dc.add(Restrictions.eq("completed", false));
		uncompletedPlans = planManager.findListByCriteria(dc);
		return SUCCESS;
	}

}
