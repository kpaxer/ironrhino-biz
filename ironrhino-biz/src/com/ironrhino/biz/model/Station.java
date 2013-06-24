package com.ironrhino.biz.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;
import org.ironrhino.common.model.Region;
import org.ironrhino.core.metadata.AutoConfig;
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
@javax.persistence.Entity
@Table(name = "station")
public class Station extends Entity<Long> {

	private static final long serialVersionUID = 5061457998732482283L;

	@SearchableId
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "station_seq")
	@SequenceGenerator(name = "station_seq", sequenceName = "station_seq", allocationSize = 1)
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty(boost = 3)
	@Column(nullable = false)
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
	@Column(length = 2500)
	private String memo;

	@NotInCopy
	@NotInJson
	@Transient
	private List<String> cashCondition = new ArrayList<String>();

	@NotInCopy
	@NotInJson
	private Date createDate = new Date();;

	@NotInCopy
	@SearchableComponent
	@JoinColumn(name = "regionId")
	@ForeignKey(name = "none")
	@ManyToOne
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

	@Access(AccessType.PROPERTY)
	@Column(length = 2500)
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
