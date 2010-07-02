package com.ironrhino.biz.model;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;

@AutoConfig(searchable = true)
@Authorize(ifAnyGranted = org.ironrhino.security.model.UserRole.ROLE_ADMINISTRATOR)
@Searchable(alias = "category")
public class Category extends Entity<Long> implements Ordered {

	private static final long serialVersionUID = 2084288046799489929L;

	@SearchableId(converter = "long", index = Index.NOT_ANALYZED)
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty
	private String name;

	private int displayOrder;

	public Category() {

	}

	public Category(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotInJson
	public boolean isNew() {
		return id == null || id == 0;
	}

	public String getName() {
		return this.name;
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
		if (!(object instanceof Category))
			return 0;
		Category entity = (Category) object;
		if (this.getDisplayOrder() != entity.getDisplayOrder())
			return this.getDisplayOrder() - entity.getDisplayOrder();
		return this.getName().compareTo(entity.getName());
	}

	@Override
	public String toString() {
		return this.name;
	}

}
