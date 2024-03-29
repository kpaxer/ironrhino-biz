package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;
import org.ironrhino.core.model.Recordable;
import org.ironrhino.core.search.elasticsearch.annotations.Index;
import org.ironrhino.core.search.elasticsearch.annotations.Searchable;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableComponent;
import org.ironrhino.core.search.elasticsearch.annotations.SearchableProperty;
import org.ironrhino.core.search.elasticsearch.annotations.Store;
import org.ironrhino.security.model.User;

import com.opensymphony.xwork2.util.CreateIfNull;

@Searchable
@AutoConfig
@Entity
@Table(name = "`returning`", indexes = { @javax.persistence.Index(columnList = "returnDate desc") })
public class Returning extends BaseEntity implements Recordable<User> {

	private static final long serialVersionUID = -2299864231084541294L;

	@SearchableProperty
	@Column(length = 2500)
	private String memo;

	@SearchableProperty
	private Date returnDate = new Date();

	@NotInCopy
	@Column(updatable = false)
	private Date createDate = new Date();

	@NotInCopy
	@Column(insertable = false)
	private Date modifyDate;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	private BigDecimal grandTotal;

	private BigDecimal freight;

	@SearchableComponent
	@NotInCopy
	@JoinColumn(name = "customerId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@ManyToOne(optional = false)
	private Customer customer;

	@SearchableComponent
	@NotInCopy
	@JoinColumn(name = "stationId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@ManyToOne(fetch = FetchType.LAZY)
	private Station station;

	@SearchableComponent
	@NotInCopy
	@JoinColumn(name = "salesmanId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@ManyToOne(fetch = FetchType.LAZY)
	private Employee salesman;

	@SearchableComponent
	@NotInCopy
	@JoinColumn(name = "createUser", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@ManyToOne(fetch = FetchType.LAZY)
	private User createUser;

	@SearchableComponent
	@NotInCopy
	@JoinColumn(name = "modifyUser", insertable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@ManyToOne(fetch = FetchType.LAZY)
	private User modifyUser;

	@CreateIfNull
	@ElementCollection(fetch = FetchType.LAZY, targetClass = ReturningItem.class)
	@CollectionTable(name = "returningitem", joinColumns = @JoinColumn(name = "returningId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)))
	@OrderColumn(name = "lineNumber", nullable = false)
	@Fetch(FetchMode.SUBSELECT)
	private List<ReturningItem> items = new ArrayList<ReturningItem>(0);

	@NotInCopy
	@Version
	private int version = -1;

	public BigDecimal getAmount() {
		BigDecimal amount = new BigDecimal(0.0);
		for (ReturningItem item : items)
			amount = amount.add(item.getSubtotal());
		return amount;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	public User getCreateUser() {
		return createUser;
	}

	public Customer getCustomer() {
		return customer;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public BigDecimal getGrandTotal() {
		if (grandTotal == null) {
			grandTotal = new BigDecimal(0.0);
			for (ReturningItem item : items)
				grandTotal = grandTotal.add(item.getSubtotal());
			if (freight != null)
				grandTotal = grandTotal.add(freight);
		}
		return grandTotal;
	}

	public List<ReturningItem> getItems() {
		return items;
	}

	public String getMemo() {
		return memo;
	}

	@Override
	public Date getModifyDate() {
		return modifyDate;
	}

	public User getModifyUser() {
		return modifyUser;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public Employee getSalesman() {
		return salesman;
	}

	public Station getStation() {
		return station;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
		grandTotal = null;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	public void setItems(List<ReturningItem> items) {
		this.items = items;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	public void setReturnDate(Date returningDate) {
		this.returnDate = returningDate;
	}

	public void setSalesman(Employee salesman) {
		this.salesman = salesman;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	@Override
	public void setCreateUserDetails(User createUser) {
		this.createUser = createUser;
	}

	@Override
	public void setModifyUserDetails(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "";
	}

}
