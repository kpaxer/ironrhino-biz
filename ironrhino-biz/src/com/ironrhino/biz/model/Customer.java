package com.ironrhino.biz.model;

import java.util.Date;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.common.model.Region;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.util.StringUtils;

@Searchable(alias = "customer")
@AutoConfig
public class Customer extends Entity<Long> {

	private static final long serialVersionUID = 5061457998732482283L;

	@SearchableId(converter = "long")
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty(boost = 3)
	private String name;

	@SearchableProperty(boost = 3)
	private String nameAsPinyin;

	@SearchableProperty(boost = 3)
	private String nameAsPinyinAbbr;

	@SearchableProperty(boost = 3)
	private String address;

	@SearchableProperty
	private String phone;

	@SearchableProperty
	private String mobile;

	@SearchableProperty
	private String fax;

	@SearchableProperty(boost = 3)
	private String linkman;

	@SearchableProperty
	private String memo;

	@NotInCopy
	@NotInJson
	private Date activeDate = new Date();

	@NotInCopy
	@NotInJson
	private Date createDate = new Date();

	@NotInCopy
	@NotInJson
	@SearchableComponent
	private Region region;

	public Customer() {
	}

	public Customer(String name) {
		this.name = name;
	}

	public Date getActiveDate() {
		return activeDate;
	}

	public String getAddress() {
		return address;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getFax() {
		return fax;
	}

	public Long getId() {
		return id;
	}

	public String getLinkman() {
		return linkman;
	}

	public String getMemo() {
		return memo;
	}

	public String getMobile() {
		return mobile;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public Region getRegion() {
		return region;
	}

	@NotInJson
	public boolean isNew() {
		return id == null || id == 0;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameAsPinyin() {
		nameAsPinyin = StringUtils.pinyin(name);
		return nameAsPinyin;
	}

	public void setNameAsPinyin(String nameAsPinyin) {
		this.nameAsPinyin = nameAsPinyin;
	}

	public String getNameAsPinyinAbbr() {
		nameAsPinyinAbbr = StringUtils.pinyinAbbr(name);
		return nameAsPinyinAbbr;
	}

	public void setNameAsPinyinAbbr(String nameAsPinyinAbbr) {
		this.nameAsPinyinAbbr = nameAsPinyinAbbr;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
