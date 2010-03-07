package com.ironrhino.biz.model;

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
@Searchable(alias = "plan")
public class Plan extends BaseEntity {

	private static final long serialVersionUID = -4137689927315849975L;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	private int quantity;

	@SearchableProperty(converter = "date", format = "yyyy-MM-dd")
	private Date planDate = new Date();

	@SearchableProperty(converter = "date", format = "yyyy-MM-dd")
	@NotInCopy
	private Date completeDate;

	@NotInCopy
	boolean completed;

	@NotInCopy
	private Date createDate = new Date();

	@SearchableProperty
	private String memo;

	@NotInCopy
	@SearchableComponent
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
