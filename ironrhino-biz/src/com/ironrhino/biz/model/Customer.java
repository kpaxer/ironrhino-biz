package com.ironrhino.biz.model;

import java.util.Date;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.common.model.Region;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;

@Searchable(alias = "customer")
@AutoConfig
public class Customer extends BaseEntity {

	private static final long serialVersionUID = 4207375657699283494L;

	@NaturalId
	@SearchableProperty(boost = 3)
	private String code;

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
	private String mobile;

	@SearchableProperty
	private String linkman;

	@NotInCopy
	private Date createDate;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return this.code;
	}

}
