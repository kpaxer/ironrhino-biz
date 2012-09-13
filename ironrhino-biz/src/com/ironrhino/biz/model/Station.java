package com.ironrhino.biz.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ironrhino.common.model.Region;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.search.elasticsearch.annotations.Searchable;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableComponent;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableId;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableProperty;
import org.ironrhino.core.util.JsonUtils;

import com.fasterxml.jackson.core.type.TypeReference;

@Searchable(type = "station")
@AutoConfig
public class Station extends Entity<Long> {

	private static final long serialVersionUID = 5061457998732482283L;

	@SearchableId(converter = "long")
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty(boost = 3)
	private String name;

	@SearchableProperty(boost = 3)
	private String address;

	@SearchableProperty(boost = 3)
	private String destination;

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
	private List<String> cashCondition = new ArrayList<String>();

	@NotInCopy
	@NotInJson
	private Date createDate = new Date();;

	@NotInCopy
	@NotInJson
	@SearchableComponent
	private Region region;

	public Station() {
	}

	public Station(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getCashConditionAsString() {
		if (cashCondition != null && cashCondition.size() > 0)
			return JsonUtils.toJson(cashCondition);
		return null;
	}

	public void setCashConditionAsString(String cashConditionAsString) {
		if (StringUtils.isNotBlank(cashConditionAsString))
			try {
				this.cashCondition = JsonUtils.fromJson(cashConditionAsString,
						new TypeReference<List<String>>() {
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public List<String> getCashCondition() {
		return cashCondition;
	}

	public void setCashCondition(List<String> cashCondition) {
		this.cashCondition = cashCondition;
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
