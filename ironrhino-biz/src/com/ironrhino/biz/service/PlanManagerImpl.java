package com.ironrhino.biz.service;

import java.util.Date;

import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.util.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Plan;
import com.ironrhino.biz.model.Product;

@Component
public class PlanManagerImpl extends BaseManagerImpl<Plan> implements
		PlanManager {

	@Autowired
	private ProductManager productManager;

	@Override
	@Transactional(readOnly = true)
	public void checkDelete(Plan plan) {
		super.checkDelete(plan);
		if (plan.isCompleted())
			throw new ErrorMessage("delete.forbidden",
					new Object[] { plan.getId() }, "此计划已经完成");
	}

	@Override
	public void complete(Plan plan) {
		if (plan.isCompleted())
			return;
		plan.setCompleted(true);
		plan.setCompleteDate(new Date());
		Product product = plan.getProduct();
		product.setStock(product.getStock() + plan.getQuantity());
		productManager.save(product);
	}
}
