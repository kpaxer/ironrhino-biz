package com.ironrhino.biz.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.Hidden;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.Readonly;
import org.ironrhino.core.metadata.Richtable;
import org.ironrhino.core.metadata.UiConfig;
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
@Richtable(order = "planDate desc")
public class Plan extends BaseEntity {

	private static final long serialVersionUID = -4137689927315849975L;

	@NotInCopy
	@SearchableComponent
	@JoinColumn(name = "productId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@UiConfig(displayOrder = 1, readonly = @Readonly(expression = "entity.completed"))
	private Product product;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	@UiConfig(displayOrder = 2, readonly = @Readonly(expression = "entity.completed"), cssClass = "positive")
	private int quantity;

	@SearchableProperty
	@UiConfig(displayOrder = 3, readonly = @Readonly(expression = "entity.completed"))
	private Date planDate = new Date();

	@SearchableProperty
	@NotInCopy
	@UiConfig(displayOrder = 4, hiddenInInput = @Hidden(true))
	private Date completeDate;

	@NotInCopy
	@SearchableProperty(index = Index.NO, store = Store.YES)
	@UiConfig(displayOrder = 5, hiddenInInput = @Hidden(true))
	boolean completed;

	@SearchableProperty
	@Column(length = 4000)
	@UiConfig(displayOrder = 6, type = "textarea")
	private String memo;

	@NotInCopy
	@UiConfig(displayOrder = 7, hidden = true)
	@Column(updatable = false)
	private Date createDate = new Date();

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
