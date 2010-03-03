package com.ironrhino.biz.model;

import java.util.Date;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.common.model.Region;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;

@Searchable(alias = "customer")
@AutoConfig
public class Customer extends Entity<Long> {

	private static final long serialVersionUID = 4207375657699283494L;

	@SearchableId(converter = "long")
	private Long id;

	@SearchableProperty(boost = 3)
	private String name;

	@SearchableProperty(boost = 3)
	private String address;

	@SearchableProperty
	private String phone;

	@SearchableProperty
	private String fax;

	@SearchableProperty(boost = 3)
	private String linkman;

	@SearchableProperty
	private String memo;

	@NotInCopy
	@NotInJson
	private Date createDate;

	@NotInCopy
	@NotInJson
	private Region region;

	public Customer() {
		createDate = new Date();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
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
