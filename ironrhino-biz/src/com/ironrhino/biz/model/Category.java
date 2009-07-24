package com.ironrhino.biz.model;

import java.util.Collection;

import org.ironrhino.core.annotation.AutoConfig;
import org.ironrhino.core.annotation.NaturalId;
import org.ironrhino.core.annotation.NotInCopy;
import org.ironrhino.core.annotation.PublishAware;
import org.ironrhino.core.annotation.RecordAware;
import org.ironrhino.core.model.BaseTreeableEntity;

@RecordAware
@PublishAware
@AutoConfig
public class Category extends BaseTreeableEntity<Category> {

	public Category() {

	}

	public Category(String name) {
		this.name = name;
	}

	@NaturalId
	public String getName() {
		return this.name;
	}

	@NotInCopy
	// @NotInJson
	public Collection<Category> getChildren() {
		return children;
	}
	
	public String toString(){
		return this.name;
	}

}
