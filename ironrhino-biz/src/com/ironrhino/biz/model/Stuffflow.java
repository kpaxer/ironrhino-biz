package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.Date;

import org.ironrhino.common.model.Status;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.model.BaseEntity;

@AutoConfig
public class Stuffflow extends BaseEntity {

	private BigDecimal amount;

	private int quantity;

	private Status status;

	private User requestUser;

	private Date requestDate;

	private User auditUser;

	private Date auditDate;

	private Stuff stuff;

	public Stuffflow() {
		status = Status.REQUESTED;
		requestDate = new Date();
	}

	public Stuffflow(String name) {
		requestDate = new Date();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public User getRequestUser() {
		return requestUser;
	}

	public void setRequestUser(User requestUser) {
		this.requestUser = requestUser;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Stuff getStuff() {
		return stuff;
	}

	public void setStuff(Stuff stuff) {
		this.stuff = stuff;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

}
