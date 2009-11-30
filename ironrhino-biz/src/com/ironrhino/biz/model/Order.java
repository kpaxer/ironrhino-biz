package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.RecordAware;
import org.ironrhino.core.model.BaseEntity;

import com.opensymphony.xwork2.util.CreateIfNull;

@RecordAware
@Searchable(alias = "order")
@AutoConfig
public class Order extends BaseEntity {

	private static final long serialVersionUID = -3191022860732749749L;

	@NaturalId
	@SearchableProperty(boost = 3)
	private String code;

	@SearchableProperty
	private String description;

	@NotInCopy
	private Date orderDate;

	private Customer customer;

	private boolean printed;

	@CreateIfNull
	private List<OrderItem> items = new ArrayList<OrderItem>(0);

	public Order() {
		orderDate = new Date();
	}

	public boolean isPrinted() {
		return printed;
	}

	public void setPrinted(boolean printed) {
		this.printed = printed;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public BigDecimal getGrandTotal() {
		BigDecimal bd = new BigDecimal(0.0);
		for (OrderItem item : items)
			bd = bd.add(item.getSubtotal());
		return bd;
	}

	@Override
	public String toString() {
		return this.code;
	}

}
