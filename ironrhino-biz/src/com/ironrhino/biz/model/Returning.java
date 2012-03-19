package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;
import org.ironrhino.core.model.Recordable;
import org.ironrhino.security.model.User;

import com.opensymphony.xwork2.util.CreateIfNull;

@Searchable(alias = "returning")
@AutoConfig
public class Returning extends BaseEntity implements Recordable<User> {

	private static final long serialVersionUID = -2299864231084541294L;

	@SearchableProperty
	private String memo;

	@SearchableProperty(converter = "date", format = "yyyy-MM-dd")
	private Date returnDate = new Date();

	@NotInCopy
	private Date createDate = new Date();

	@NotInCopy
	private Date modifyDate;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	private BigDecimal grandTotal;

	private BigDecimal freight;

	@SearchableComponent
	@NotInCopy
	private Customer customer;

	@SearchableComponent
	@NotInCopy
	private Station station;

	@SearchableComponent
	@NotInCopy
	private Employee salesman;

	@SearchableComponent
	@NotInCopy
	private User createUser;

	@SearchableComponent
	@NotInCopy
	private User modifyUser;

	@CreateIfNull
	private List<OrderItem> items = new ArrayList<OrderItem>(0);

	@NotInCopy
	protected int version;

	public BigDecimal getAmount() {
		BigDecimal amount = new BigDecimal(0.0);
		for (OrderItem item : items)
			amount = amount.add(item.getSubtotal());
		return amount;
	}

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
			for (OrderItem item : items)
				grandTotal = grandTotal.add(item.getSubtotal());
			if (freight != null)
				grandTotal = grandTotal.add(freight);
		}
		return grandTotal;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public String getMemo() {
		return memo;
	}

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

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

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
	public String toString() {
		return "";
	}

}
