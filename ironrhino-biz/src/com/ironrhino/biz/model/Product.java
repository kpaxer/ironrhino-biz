package com.ironrhino.biz.model;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.RecordAware;
import org.ironrhino.core.model.BaseEntity;

@RecordAware
@AutoConfig
public class Product extends BaseEntity {

	private static final long serialVersionUID = 1876365527076787416L;

	@NaturalId
	private String name;

	@NotInCopy
	@NaturalId
	private Spec spec;

	private int stock;

	private Integer criticalStock;

	@NotInCopy
	private Category category;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getCriticalStock() {
		return criticalStock;
	}

	public void setCriticalStock(Integer criticalStock) {
		this.criticalStock = criticalStock;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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
