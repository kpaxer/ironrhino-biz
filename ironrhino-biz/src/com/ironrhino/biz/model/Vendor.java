package com.ironrhino.biz.model;

import java.util.Date;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;

@AutoConfig
@Searchable(alias = "vendor")
public class Vendor extends BaseEntity {

	@NaturalId
	@SearchableProperty(boost = 3)
	private String name;

	@SearchableProperty
	private String address;

	@SearchableProperty
	private String postCode;

	@SearchableProperty
	private String phone;

	@SearchableProperty
	private String fax;

	@SearchableProperty
	private String description;

	@NotInCopy
	private Date createDate;

	@NotInCopy
	private Region region;

	public Vendor() {
		createDate = new Date();
	}

	public Vendor(String name) {
		this.name = name;
		createDate = new Date();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String toString() {
		return this.name;
	}

}
