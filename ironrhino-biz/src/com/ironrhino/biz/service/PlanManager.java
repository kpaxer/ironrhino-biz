package com.ironrhino.biz.service;

import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.model.Plan;

public interface PlanManager extends BaseManager<Plan> {

	public void complete(Plan plan);

}
