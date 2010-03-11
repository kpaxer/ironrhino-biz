package com.ironrhino.biz.model;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;

@AutoConfig
@Searchable(alias = "stuff")
public class Stuff extends Entity<Long> implements Ordered {

	private static final long serialVersionUID = 8649365520461282768L;

	@SearchableId(converter = "long", index = Index.NOT_ANALYZED)
	private Long id;

	@NaturalId
	@SearchableProperty(boost = 3)
	private String name;

	private int stock;

	private int displayOrder;

	public Stuff() {
	}

	public Stuff(String name) {
		this.name = name;
	}

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
		return getName();
	}

}
