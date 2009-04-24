package com.ironrhino.biz.model;

import java.util.HashSet;
import java.util.Set;

import org.ironrhino.core.annotation.AutoConfig;
import org.ironrhino.core.annotation.NaturalId;
import org.ironrhino.core.annotation.NotInCopy;
import org.ironrhino.core.annotation.Recordable;
import org.ironrhino.core.model.BaseEntity;

@Recordable
@AutoConfig
public class Role extends BaseEntity {

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
