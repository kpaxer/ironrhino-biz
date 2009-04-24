package com.ironrhino.biz.model;

import org.ironrhino.core.annotation.AutoConfig;
import org.ironrhino.core.annotation.NaturalId;
import org.ironrhino.core.annotation.NotInCopy;
import org.ironrhino.core.model.BaseEntity;

@AutoConfig
public class Stuff extends BaseEntity {

	@NaturalId
	private String name;

	@NotInCopy
	@NaturalId
	private Spec spec;

	private int stock;

	private Integer criticalStock;

	private Vendor vendor;

	public Stuff() {
	}

	public Stuff(String name, Spec spec) {
		this.name = name;
		this.spec = spec;
	}

	public Spec getSpec() {
		return spec;
	}

	public void setSpec(Spec spec) {
		this.spec = spec;
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

	public Integer getCriticalStock() {
		return criticalStock;
	}

	public void setCriticalStock(Integer criticalStock) {
		this.criticalStock = criticalStock;
	}

	@NotInCopy
	public boolean isCritical() {
		if (criticalStock == null)
			return false;
		return stock <= criticalStock;
	}

	@NotInCopy
	public String getFullname() {
		return name + "(" + spec.getName() + ")";
	}

	public String toString() {
		return getFullname();
	}

}
