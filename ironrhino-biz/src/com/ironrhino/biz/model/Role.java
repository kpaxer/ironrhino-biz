package com.ironrhino.biz.model;

import java.util.HashSet;
import java.util.Set;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.RecordAware;
import org.ironrhino.core.model.BaseEntity;

@RecordAware
@AutoConfig
public class Role extends BaseEntity {

	private static final long serialVersionUID = 7032926389872778447L;

	@NaturalId
	private String name;

	@NotInCopy
	private Set<User> users = new HashSet<User>(0);

	public Role() {
	}

	public Role(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String toString() {
		return this.name;
	}
}
