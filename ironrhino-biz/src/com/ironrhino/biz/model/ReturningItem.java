package com.ironrhino.biz.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

@Embeddable
public class ReturningItem implements Serializable {

	private static final long serialVersionUID = -5603601574402741053L;

	private int quantity;

	private BigDecimal price;

	@JoinColumn(name = "productId")
	@ForeignKey(name = "none")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Product product;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if (quantity > 0)
			this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		if (price != null && price.doubleValue() >= 0)
			this.price = price;
	}

	public BigDecimal getSubtotal() {
		return price.multiply(new BigDecimal(quantity));
	}

}
