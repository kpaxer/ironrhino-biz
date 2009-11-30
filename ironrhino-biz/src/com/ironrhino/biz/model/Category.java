package com.ironrhino.biz.model;

import java.util.Collection;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.PublishAware;
import org.ironrhino.core.metadata.RecordAware;
import org.ironrhino.core.model.BaseTreeableEntity;

@RecordAware
@PublishAware
@AutoConfig
public class Category extends BaseTreeableEntity<Category> {

	private static final long serialVersionUID = 2394113154991389750L;

	public Category() {

	}

	public Category(String name) {
		this.name = name;
	}

	@Override
	@NaturalId
	public String getName() {
		return this.name;
	}

	@Override
	@NotInCopy
	// @NotInJson
	public Collection<Category> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
