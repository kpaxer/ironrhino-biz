package com.ironrhino.biz.model;

import java.util.Date;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.search.elasticsearch.annotations.Index;
import org.ironrhino.core.search.elasticsearch.annotations.Searchable;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableId;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableProperty;

@AutoConfig
@Searchable(type = "employee")
public class Employee extends Entity<Long> {

	private static final long serialVersionUID = 4207375657699283494L;

	@SearchableId(converter = "long", index = Index.NOT_ANALYZED)
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty(boost = 3)
	private String name;

	private EmployeeType type;

	private boolean dimission;

	@SearchableProperty(boost = 2)
	private String phone;

	@SearchableProperty(boost = 2)
	private String address;

	@SearchableProperty(boost = 1)
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
