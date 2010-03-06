package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;

import com.opensymphony.xwork2.util.CreateIfNull;

@Searchable(alias = "order")
@AutoConfig
public class Order extends BaseEntity {

	private static final long serialVersionUID = -3191022860732749749L;

	@NaturalId
	@SearchableProperty(boost = 3)
	private String code;

	private BigDecimal discount;

	@SearchableProperty
	private String memo;

	@NotInCopy
	@SearchableProperty(converter = "date", format = "yyyy-MM-dd")
	private Date orderDate = new Date();

	@NotInCopy
	private Date createDate = new Date();

	private boolean paid;

	private boolean shipped;

	private boolean cancelled;

	private BigDecimal grandTotal;

	@SearchableComponent
	private Customer customer;

	@CreateIfNull
	private List<OrderItem> items = new ArrayList<OrderItem>(0);

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public boolean isShipped() {
		return shipped;
	}

	public void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	public BigDecimal getGrandTotal() {
		if (grandTotal == null) {
			grandTotal = new BigDecimal(0.0);
			for (OrderItem item : items)
				grandTotal = grandTotal.add(item.getSubtotal());
			if (discount != null)
				grandTotal = grandTotal.subtract(discount);
		}
		return grandTotal;
	}

	@Override
	public String toString() {
		return this.code;
	}

}
