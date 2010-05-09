package com.ironrhino.biz.service;

import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.model.Station;

public interface StationManager extends BaseManager<Station> {

	public void merge(Station source, Station target);
}
