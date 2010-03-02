package com.ironrhino.biz.model;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.metadata.RecordAware;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;

@RecordAware
@AutoConfig
public class Product extends Entity<Long> implements Ordered {

	private static final long serialVersionUID = 1876365527076787416L;

	private Long id;

	@NaturalId
	private String name;

	@NotInCopy
	@NaturalId
	private Spec spec;

	private int displayOrder;

	@NotInCopy
	private Category category;

	@NotInCopy
	private Brand brand;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	@NotInJson
	public boolean isNew() {
		return id == null || id == 0;
	}

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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@NotInCopy
	public String getFullname() {
		return name + "(" + spec.getName() + ")";
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public int compareTo(Object object) {
		if (!(object instanceof Product))
			return 0;
		Product entity = (Product) object;
		if (this.getDisplayOrder() != entity.getDisplayOrder())
			return this.getDisplayOrder() - entity.getDisplayOrder();
		return this.getName().compareTo(entity.getName());
	}

	@Override
	public String toString() {
		return getFullname();
	}
}
