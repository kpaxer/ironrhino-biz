package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.Date;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;
import org.ironrhino.core.search.elasticsearch.annotations.Index;
import org.ironrhino.core.search.elasticsearch.annotations.Searchable;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableComponent;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableProperty;
import org.ironrhino.core.search.elasticsearch.annotations.Store;

@AutoConfig
@Searchable(type = "reward")
public class Reward extends BaseEntity {

	private static final long serialVersionUID = 1361468983711747618L;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	private BigDecimal amount;

	private RewardType type;

	@SearchableProperty(converter = "date", format = "yyyy-MM-dd HH:mm:ss")
	private Date rewardDate = new Date();

	@NotInCopy
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

	public RewardType getType() {
		return type;
	}

	public void setType(RewardType type) {
		this.type = type;
	}

}
