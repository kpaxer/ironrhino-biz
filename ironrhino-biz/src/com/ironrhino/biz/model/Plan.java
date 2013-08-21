package com.ironrhino.biz.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Searchable
@Entity
@Table(name = "plan")
public class Plan extends BaseEntity {

	private static final long serialVersionUID = -4137689927315849975L;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	private int quantity;

	@SearchableProperty
	private Date planDate = new Date();

	@SearchableProperty
	@NotInCopy
	private Date completeDate;

	@NotInCopy
	@SearchableProperty(index = Index.NO, store = Store.YES)
	boolean completed;

	@NotInCopy
	private Date createDate = new Date();

	@SearchableProperty
	@Column(length = 2500)
	private String memo;

	@NotInCopy
	@SearchableComponent
	@JoinColumn(name = "productId")
	@ForeignKey(name = "none")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Product product;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public boolean isCompleted() {
		return this.completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

}
