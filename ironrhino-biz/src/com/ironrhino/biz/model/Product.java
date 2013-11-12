package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Attributable;
import org.ironrhino.core.model.Attribute;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;
import org.ironrhino.core.search.elasticsearch.annotations.Searchable;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableComponent;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableId;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableProperty;
import org.ironrhino.core.util.JsonUtils;

import com.fasterxml.jackson.core.type.TypeReference;

@AutoConfig
@Searchable
@javax.persistence.Entity
@Table(name = "product")
public class Product extends Entity<Long> implements Ordered<Product>,
		Attributable {

	private static final long serialVersionUID = 1876365527076787416L;

	private static final TypeReference<List<Attribute>> LIST_TYPE = new TypeReference<List<Attribute>>() {
	};

	@SearchableId
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "product_seq")
	@SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
	private Long id;

	@SearchableProperty(boost = 3)
	@NaturalId(mutable = true)
	private String name;

	private int stock;

	private int shopStock;

	private BigDecimal weight;

	private BigDecimal price;

	private int displayOrder;

	@NotInCopy
	@NotInJson
	@SearchableComponent
	@NaturalId(mutable = true)
	@JoinColumn(name = "categoryId")
	@ForeignKey(name = "none")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Category category;

	@NotInCopy
	@NotInJson
	@SearchableComponent
	@NaturalId(mutable = true)
	@JoinColumn(name = "brandId")
	@ForeignKey(name = "none")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Brand brand;

	@SearchableComponent
	@Transient
	private List<Attribute> attributes = new ArrayList<Attribute>();

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	@NotInJson
	public boolean isNew() {
		return id == null || id == 0;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getShopStock() {
		return shopStock;
	}

	public void setShopStock(int shopStock) {
		this.shopStock = shopStock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@NotInCopy
	@SearchableProperty(boost = 3)
	public String getFullname() {
		StringBuilder sb = new StringBuilder();
		if (brand != null)
			sb.append(brand.getName());
		if (category != null)
			sb.append(category.getName());
		sb.append(name);
		return sb.toString();
	}

	@Override
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public String getAttribute(String name) {
		if (attributes == null)
			return null;
		for (Attribute attr : attributes) {
			if (StringUtils.isNotBlank(attr.getName())
					&& attr.getName().equals(name))
				return attr.getValue();
		}
		return null;
	}

	@Override
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	@NotInCopy
	@Access(AccessType.PROPERTY)
	@Column(name = "attributes", length = 1024)
	public String getAttributesAsString() {
		if (attributes == null || attributes.isEmpty()
				|| attributes.size() == 1 && attributes.get(0).isBlank())
			return null;
		return JsonUtils.toJson(attributes);
	}

	@Override
	public void setAttributesAsString(String str) {
		if (StringUtils.isNotBlank(str))
			try {
				attributes = JsonUtils.fromJson(str, LIST_TYPE);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public int compareTo(Product product) {
		if (product == null)
			return 1;
		if (this.getDisplayOrder() != product.getDisplayOrder())
			return this.getDisplayOrder() - product.getDisplayOrder();
		return this.toString().compareTo(product.toString());
	}

	@Override
	public String toString() {
		return getFullname();
	}
}
