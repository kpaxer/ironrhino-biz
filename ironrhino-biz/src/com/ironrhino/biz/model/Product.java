package com.ironrhino.biz.model;

import java.math.BigDecimal;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.metadata.RecordAware;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;

@RecordAware
@AutoConfig
@Searchable(alias = "product")
public class Product extends Entity<Long> implements Ordered {

	private static final long serialVersionUID = 1876365527076787416L;

	@SearchableId(converter = "long")
	private Long id;

	@SearchableProperty(boost = 3)
	private String name;

	private int stock;

	@NotInCopy
	@NotInJson
	private Spec spec;

	private BigDecimal price;

	private int displayOrder;

	@NotInCopy
	@NotInJson
	private Category category;

	@NotInCopy
	@NotInJson
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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
	@SearchableProperty(boost = 3)
	public String getFullname() {
		StringBuilder sb = new StringBuilder();
		if (brand != null)
			sb.append(brand.getName());
		if (category != null)
			sb.append(category.getName());
		sb.append(name);
		if (spec != null) {
			sb.append('(');
			sb.append(spec.getName());
			sb.append(')');
		}
		return sb.toString();
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
