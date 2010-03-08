package com.ironrhino.biz.model;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;

@AutoConfig
public class Stuff extends BaseEntity {

	private static final long serialVersionUID = 8649365520461282768L;

	@NaturalId
	private String name;

	private int stock;

	private Vendor vendor;

	public Stuff() {
	}

	public Stuff(String name) {
		this.name = name;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	@NotInCopy
	public String getFullname() {
		if (vendor != null)
			return name + "(" + vendor.getName() + ")";
		return name;
	}

	@Override
	public String toString() {
		return getFullname();
	}

}
