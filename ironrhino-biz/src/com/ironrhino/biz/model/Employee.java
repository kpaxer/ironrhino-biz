package com.ironrhino.biz.model;

import java.util.Date;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;

@AutoConfig
public class Employee extends Entity<Long> {

	private static final long serialVersionUID = 4207375657699283494L;

	private Long id;

	@NaturalId
	private String name;

	private boolean disabled;

	private String phone;

	private String memo;

	@NotInCopy
	@NotInJson
	private Date createDate = new Date();;

	public Employee() {
	}

	public Employee(String name) {
		this.name = name;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	@NotInJson
	public boolean isNew() {
		return id == null || id == 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
