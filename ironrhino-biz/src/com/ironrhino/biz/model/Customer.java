package com.ironrhino.biz.model;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

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

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;
import org.ironrhino.common.model.Region;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.search.elasticsearch.annotations.Index;
import org.ironrhino.core.search.elasticsearch.annotations.Searchable;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableComponent;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableId;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableProperty;
import org.ironrhino.core.util.StringUtils;

@Searchable(type = "customer")
@AutoConfig
@javax.persistence.Entity
@Table(name = "customer")
public class Customer extends Entity<Long> {

	private static final long serialVersionUID = 5061457998732482283L;

	@SearchableId
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "customer_seq")
	@SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq")
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty(boost = 3, index = Index.NOT_ANALYZED)
	@Column(nullable = false)
	private String name;

	@SearchableProperty(boost = 3)
	private String address;

	@SearchableProperty
	private String phone;

	@SearchableProperty
	private String mobile;

	@SearchableProperty
	private String fax;

	@SearchableProperty(boost = 3, index = Index.NOT_ANALYZED)
	private String linkman;

	@SearchableProperty
	@Column(length = 2500)
	private String memo;

	@NotInCopy
	@NotInJson
	private Date activeDate = new Date();

	@NotInCopy
	@NotInJson
	private Date createDate = new Date();

	@NotInCopy
	@SearchableComponent
	@JoinColumn(name = "regionId")
	@ForeignKey(name = "none")
	@ManyToOne
	private Region region;

	@NotInCopy
	@SearchableProperty(index = Index.NOT_ANALYZED)
	@Transient
	private Set<String> tags = new LinkedHashSet<String>(0);

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

	@SearchableProperty(boost = 3, index = Index.NOT_ANALYZED)
	public String getNameAsPinyin() {
		return StringUtils.pinyin(name);
	}

	@SearchableProperty(boost = 3, index = Index.NOT_ANALYZED)
	public String getNameAsPinyinAbbr() {
		return StringUtils.pinyinAbbr(name);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	@NotInJson
	@Access(AccessType.PROPERTY)
	@Column(name = "tags", length = 500)
	public String getTagsAsString() {
		if (tags.size() > 0)
			return org.apache.commons.lang3.StringUtils.join(tags.iterator(),
					',');
		return null;
	}

	public void setTagsAsString(String tagsAsString) {
		tags.clear();
		if (org.apache.commons.lang3.StringUtils.isNotBlank(tagsAsString))
			tags.addAll(Arrays.asList(org.ironrhino.core.util.StringUtils
					.trimTail(tagsAsString, ",").split("\\s*,\\s*")));
	}

	@Transient
	public String getFullAddress() {
		if (region == null)
			return address;
		else
			return region.getFullname() + ((address != null) ? address : "");
	}

	@Override
	public String toString() {
		return this.name;
	}

}
