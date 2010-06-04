package com.ironrhino.biz.model;

import java.util.Date;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;

@AutoConfig
@Searchable(alias = "employee")
public class Employee extends Entity<Long> {

	private static final long serialVersionUID = 4207375657699283494L;

	@SearchableId(converter = "long", index = Index.NOT_ANALYZED)
	private Long id;

	@NaturalId
	@SearchableProperty(boost = 3)
	private String name;

	private EmployeeType type;

	private boolean dimission;

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

	public boolean isDimission() {
		return dimission;
	}

	public void setDimission(boolean dimission) {
		this.dimission = dimission;
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

	public EmployeeType getType() {
		return type;
	}

	public void setType(EmployeeType type) {
		this.type = type;
	}

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
