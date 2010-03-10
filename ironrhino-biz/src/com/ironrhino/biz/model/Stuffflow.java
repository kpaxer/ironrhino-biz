package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.Date;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;

@AutoConfig
public class Stuffflow extends BaseEntity {

	private static final long serialVersionUID = -1796500601044183359L;

	private BigDecimal amount;

	private int quantity;

	private Date when = new Date();

	@NotInCopy
	private Date createDate = new Date();

	private Stuff stuff;

	public Stuffflow() {
	}

	public Stuffflow(String name) {
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date when) {
		this.when = when;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
