package com.ironrhino.biz.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;
import org.ironrhino.core.search.elasticsearch.annotations.Searchable;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableId;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@AutoConfig
@Searchable
@javax.persistence.Entity
@Table(name = "stuff")
public class Stuff extends Entity<Long> implements Ordered<Stuff> {

	private static final long serialVersionUID = 8649365520461282768L;

	@SearchableId
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "stuff_seq")
	@SequenceGenerator(name = "stuff_seq", sequenceName = "stuff_seq", allocationSize = 1)
	private Long id;

	@NaturalId(mutable = true)
	@SearchableProperty(boost = 3)
	@Column(nullable = false)
	private String name;

	private int stock;

	private BigDecimal weight;

	@SearchableProperty
	private int displayOrder;

	public Stuff() {
	}

	public Stuff(String name) {
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

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	@Override
	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public int compareTo(Stuff stuff) {
		if (stuff == null)
			return 1;
		if (this.getDisplayOrder() != stuff.getDisplayOrder())
			return this.getDisplayOrder() - stuff.getDisplayOrder();
		return this.toString().compareTo(stuff.toString());
	}

	@Override
	public String toString() {
		return getName();
	}

}
