package com.ironrhino.biz.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.UiConfig;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;
import org.ironrhino.core.search.elasticsearch.annotations.Index;
import org.ironrhino.core.search.elasticsearch.annotations.Searchable;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableId;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@AutoConfig
@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + ","
		+ UserRole.ROLE_PRODUCTMANAGER)
@Searchable
@javax.persistence.Entity
@Table(name = "category", indexes = { @javax.persistence.Index(columnList = "displayOrder") })
public class Category extends Entity<Long> implements Ordered<Category> {

	private static final long serialVersionUID = 2084288046799489929L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "category_seq")
	@SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
	@SearchableId
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty(index = Index.NOT_ANALYZED)
	@UiConfig(displayOrder = 1)
	@Column(nullable = false)
	private String name;

	@UiConfig(displayOrder = 2, width = "100px")
	@SearchableProperty
	private int displayOrder;

	public Category() {

	}

	public Category(String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return id == null || id == 0;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public int compareTo(Category category) {
		if (category == null)
			return 1;
		if (this.getDisplayOrder() != category.getDisplayOrder())
			return this.getDisplayOrder() - category.getDisplayOrder();
		return this.toString().compareTo(category.toString());
	}

	@Override
	public String toString() {
		return this.name;
	}

}
