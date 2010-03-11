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
@Searchable(alias = "stuffflow")
public class Stuffflow extends BaseEntity {

	private static final long serialVersionUID = -1796500601044183359L;

	private BigDecimal amount;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	private int quantity;

	@SearchableProperty(converter = "date", format = "yyyy-MM-dd")
	private Date date = new Date();

	@SearchableProperty
	private String memo;

	@NotInCopy
	private Date createDate = new Date();

	@SearchableComponent
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

}
