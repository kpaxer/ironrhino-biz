package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.Date;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;

@AutoConfig
public class Reward extends BaseEntity {

	private static final long serialVersionUID = 1361468983711747618L;

	private BigDecimal amount;

	private Date rewardDate = new Date();

	private String memo;

	@NotInCopy
	private Employee employee;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
