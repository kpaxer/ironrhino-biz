package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
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
@Entity
@Table(name = "reward")
public class Reward extends BaseEntity {

	private static final long serialVersionUID = 1361468983711747618L;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	private BigDecimal amount;

	@Enumerated
	private RewardType type;

	@SearchableProperty
	private Date rewardDate = new Date();

	@NotInCopy
	private Date createDate = new Date();

	@SearchableProperty
	@Column(length = 2500)
	private String memo;

	@NotInCopy
	@SearchableComponent
	@JoinColumn(name = "employeeId")
	@ForeignKey(name = "none")
	@ManyToOne(fetch = FetchType.EAGER)
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
