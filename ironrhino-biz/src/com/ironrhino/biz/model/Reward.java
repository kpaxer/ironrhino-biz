package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.Date;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;

@AutoConfig
@Searchable(alias = "reward")
public class Reward extends BaseEntity {

	private static final long serialVersionUID = 1361468983711747618L;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	private BigDecimal amount;

	@SearchableProperty(converter = "date", format = "yyyy-MM-dd")
	private Date rewardDate = new Date();

	private Date createDate = new Date();

	@SearchableProperty
	private String memo;

	@NotInCopy
	@SearchableComponent
	private Employee employee;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getRewardDate() {
		return rewardDate;
	}

	public void setRewardDate(Date rewardDate) {
		this.rewardDate = rewardDate;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
