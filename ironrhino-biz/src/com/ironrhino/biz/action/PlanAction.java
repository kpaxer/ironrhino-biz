package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.struts.EntityAction;

import com.ironrhino.biz.model.Plan;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.PlanManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + ","
		+ UserRole.ROLE_PLANMANAGER)
public class PlanAction extends EntityAction<Plan> {

	private static final long serialVersionUID = 4331302727890834065L;

	private List<Product> productList;

	private List<Plan> uncompletedPlans;

	@Autowired
	private transient PlanManager planManager;

	public List<Plan> getUncompletedPlans() {
		return uncompletedPlans;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public String complete() {
		String[] id = getId();
		if (id != null) {
			List<Plan> list;
			if (id.length == 1) {
				list = new ArrayList<Plan>(1);
				list.add(planManager.get(id[0]));
			} else {
				DetachedCriteria dc = planManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = planManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				for (Plan temp : list)
					planManager.complete(temp);
				addActionMessage(getText("operate.success"));
			}
		}
		return REFERER;
	}

	public String uncompleted() {
		DetachedCriteria dc = planManager.detachedCriteria();
		dc.add(Restrictions.eq("completed", false));
		uncompletedPlans = planManager.findListByCriteria(dc);
		return "uncompleted";
	}

}
