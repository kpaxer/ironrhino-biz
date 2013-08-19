package com.ironrhino.biz.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.metadata.RichtableConfig;
import org.ironrhino.core.metadata.UiConfig;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.search.elasticsearch.annotations.Index;
import org.ironrhino.core.search.elasticsearch.annotations.Searchable;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableId;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableProperty;

@AutoConfig
@Searchable(type = "employee")
@javax.persistence.Entity
@Table(name = "employee")
@RichtableConfig(order = "dimission,type,name", actionColumnButtons = "<button type=\"button\" class=\"btn\" data-view=\"input\">${action.getText('edit')}</button> <a class=\"btn ajax view\" href=\"${getUrl('/biz/reward/tabs?employee.id='+entity.id)}\">${action.getText('reward')}</a> <a class=\"btn\" href=\"${getUrl('/biz/reward/input?negative=true&employee.id='+entity.id)}\" rel=\"richtable\" data-windowoptions=\"{'reloadonclose':false}\">${action.getText('reward.out')}</a> <a class=\"btn\" href=\"${getUrl('/biz/reward/input?employee.id='+entity.id)}\" rel=\"richtable\" data-windowoptions=\"{'reloadonclose':false}\">${action.getText('reward.in')}</a><#if entity.type??&&entity.type.name()=='SALESMAN'> <a class=\"btn ajax view\" href=\"${getUrl('/biz/order?salesman.id='+entity.id)}\">${action.getText('salesman')}${action.getText('order')}</a></#if><#if entity.type??&&entity.type.name()=='DELIVERYMAN'> <a class=\"btn ajax view\" href=\"${getUrl('/biz/order?deliveryman.id='+entity.id)}\">${action.getText('deliveryman')}${action.getText('order')}</a></#if>")
public class Employee extends Entity<Long> {

	private static final long serialVersionUID = 4207375657699283494L;

	@SearchableId
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "employee_seq")
	@SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 1)
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty(boost = 3, index = Index.NOT_ANALYZED)
	@Column(nullable = false)
	@UiConfig(displayOrder = 1, width = "80px")
	private String name;

	@Enumerated
	@UiConfig(displayOrder = 2, width = "80px")
	@SearchableProperty(boost = 2, index = Index.NOT_ANALYZED)
	private EmployeeType type;

	@UiConfig(displayOrder = 3, width = "60px")
	@SearchableProperty(boost = 2, index = Index.NOT_ANALYZED)
	private boolean dimission;

	@SearchableProperty(boost = 2)
	@UiConfig(displayOrder = 4, width = "80px")
	private String phone;

	@SearchableProperty(boost = 2)
	@UiConfig(displayOrder = 5)
	private String address;

	@SearchableProperty(boost = 1)
	@Column(length = 4000)
	@UiConfig(displayOrder = 6, type = "textarea", maxlength = 4000, hiddenInList = true)
	private String memo;

	@NotInCopy
	@NotInJson
	@UiConfig(hidden = true)
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

	@Override
	@NotInJson
	public boolean isNew() {
		return id == null || id == 0;
	}

	@Override
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
