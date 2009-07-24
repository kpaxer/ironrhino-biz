package com.ironrhino.biz.model;

import org.ironrhino.core.annotation.AutoConfig;
import org.ironrhino.core.annotation.PublishAware;
import org.ironrhino.core.model.BaseTreeableEntity;

@PublishAware
@AutoConfig
public class Region extends BaseTreeableEntity<Region> {

	private Double latitude;

	private Double longitude;

	public Region() {

	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Region(String name) {
		this.name = name;
	}

	public Region(String name, int displayOrder) {
		this.name = name;
		this.displayOrder = displayOrder;
	}

	public String getFullname() {
		String fullname = name;
		Region r = this;
		while ((r = r.getParent()) != null) {
			fullname = r.getName() + fullname;
		}
		return fullname;
	}

	public String toString() {
		return getFullname();
	}

}
