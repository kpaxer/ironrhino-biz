package com.ironrhino.biz.model;

import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;

@AutoConfig
@Authorize(ifAnyGranted = org.ironrhino.security.model.UserRole.ROLE_ADMINISTRATOR)
public class Brand extends Entity<Long> implements Ordered {

	private static final long serialVersionUID = 6728147186060800090L;

	private Long id;

	@NaturalId
	private String name;

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
		if (!(object instanceof Brand))
			return 0;
		Brand entity = (Brand) object;
		if (this.getDisplayOrder() != entity.getDisplayOrder())
			return this.getDisplayOrder() - entity.getDisplayOrder();
		return this.getName().compareTo(entity.getName());
	}

	@Override
	public String toString() {
		return this.name;
	}

}
