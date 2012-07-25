package com.ironrhino.biz.model;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.metadata.UiConfig;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;

@AutoConfig(searchable = true)
@Authorize(ifAnyGranted = org.ironrhino.security.model.UserRole.ROLE_ADMINISTRATOR)
@Searchable(alias = "brand")
public class Brand extends Entity<Long> implements Ordered {

	private static final long serialVersionUID = 6728147186060800090L;

	@SearchableId(converter = "long", index = Index.NOT_ANALYZED)
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty
	@UiConfig(displayOrder = 1)
	private String name;

	@UiConfig(displayOrder = 2)
	private int displayOrder;

	public Brand() {

	}

	public Brand(String name) {
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
		if (!(object instanceof Ordered))
			return 0;
		Ordered ordered = (Ordered) object;
		if (this.getDisplayOrder() != ordered.getDisplayOrder())
			return this.getDisplayOrder() - ordered.getDisplayOrder();
		return this.toString().compareTo(ordered.toString());
	}

	@Override
	public String toString() {
		return this.name;
	}

}
