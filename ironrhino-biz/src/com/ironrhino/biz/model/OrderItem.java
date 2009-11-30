package com.ironrhino.biz.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class OrderItem implements Serializable {

	private static final long serialVersionUID = -5603601574402741053L;

	private int quantity;

	private String unit;

	private String name;

	private BigDecimal price;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getSubtotal() {
		return price.multiply(new BigDecimal(quantity));
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.quantity).append(this.name)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OrderItem))
			return false;
		OrderItem that = (OrderItem) obj;
		return new EqualsBuilder().append(this.quantity, that.quantity).append(
				this.name, that.name).isEquals();
	}

}
