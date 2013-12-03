package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;
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
@Table(name = "orders")
public class Order extends BaseEntity implements Recordable<User> {

	private static final long serialVersionUID = -3191022860732749749L;

	@NaturalId
	@SearchableProperty(boost = 3, index = Index.NOT_ANALYZED)
	@Column(nullable = false)
	private String code;

	private BigDecimal discount;

	@Enumerated
	private SaleType saleType = SaleType.FACTORY;

	@SearchableProperty
	@Column(length = 2500)
	private String memo;

	@SearchableProperty
	private Date orderDate = new Date();

	@NotInCopy
	@Column(updatable = false)
	private Date createDate = new Date();

	@NotInCopy
	@Column(insertable = false)
	private Date modifyDate;

	private boolean paid;

	private boolean shipped;

	private Date shipDate;

	private Date payDate;

	@SearchableProperty(index = Index.NO, store = Store.YES)
	private BigDecimal grandTotal;

	private BigDecimal freight;

	@SearchableComponent
	@NotInCopy
	@JoinColumn(name = "customerId")
	@ForeignKey(name = "none")
	@ManyToOne(optional = false)
	private Customer customer;

	@SearchableComponent
	@NotInCopy
	@JoinColumn(name = "stationId")
	@ForeignKey(name = "none")
	@ManyToOne(fetch = FetchType.LAZY)
	private Station station;

	@SearchableComponent
	@NotInCopy
	@JoinColumn(name = "salesmanId")
	@ForeignKey(name = "none")
	@ManyToOne(fetch = FetchType.LAZY)
	private Employee salesman;

	@SearchableComponent
	@NotInCopy
	@JoinColumn(name = "deliverymanId")
	@ForeignKey(name = "none")
	@ManyToOne(fetch = FetchType.LAZY)
	private Employee deliveryman;

	@NotInCopy
	@JoinColumn(name = "createUser", updatable = false)
	@ForeignKey(name = "none")
	@ManyToOne(fetch = FetchType.LAZY)
	private User createUser;

	@NotInCopy
	@JoinColumn(name = "modifyUser", insertable = false)
	@ForeignKey(name = "none")
	@ManyToOne(fetch = FetchType.LAZY)
	private User modifyUser;

	@CreateIfNull
	@ElementCollection(fetch = FetchType.LAZY, targetClass = OrderItem.class)
	@CollectionTable(name = "orderitem", joinColumns = @JoinColumn(name = "orderId"))
	@OrderColumn(name = "lineNumber", nullable = false)
	@Fetch(FetchMode.SUBSELECT)
	private List<OrderItem> items = new ArrayList<OrderItem>(0);

	@NotInCopy
	@Version
	private int version = -1;

	public BigDecimal getAmount() {
		BigDecimal amount = new BigDecimal(0.0);
		for (OrderItem item : items)
			amount = amount.add(item.getSubtotal());
		return amount;
	}

	public String getCode() {
		return code;
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

	public BigDecimal getDiscount() {
		return discount;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public BigDecimal getGrandTotal() {
		if (grandTotal == null) {
			grandTotal = new BigDecimal(0.0);
			for (OrderItem item : items)
				grandTotal = grandTotal.add(item.getSubtotal());
			if (discount != null)
				grandTotal = grandTotal.subtract(discount);
			if (freight != null)
				grandTotal = grandTotal.subtract(freight);
		}
		return grandTotal;
	}

	public List<OrderItem> getItems() {
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

	public Date getOrderDate() {
		return orderDate;
	}

	public Employee getSalesman() {
		return salesman;
	}

	public SaleType getSaleType() {
		return saleType;
	}

	public Station getStation() {
		return station;
	}

	public boolean isPaid() {
		return paid;
	}

	public boolean isShipped() {
		return shipped;
	}

	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public void setCode(String code) {
		this.code = code;
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

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
		grandTotal = null;
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

	@Override
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public void setSalesman(Employee salesman) {
		this.salesman = salesman;
	}

	public void setSaleType(SaleType saleType) {
		this.saleType = saleType;
	}

	public void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Employee getDeliveryman() {
		return deliveryman;
	}

	public void setDeliveryman(Employee deliveryman) {
		this.deliveryman = deliveryman;
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

	public boolean isCashable() {
		if (paid || station == null)
			return false;
		try {
			List<String> list = station.getCashCondition();
			if (list == null || list.isEmpty())
				return false;
			Calendar cal = Calendar.getInstance();
			for (String line : list) {
				if (StringUtils.isBlank(line))
					continue;
				String[] arr = line.split(",");
				int type = Integer.parseInt(arr[0]);
				int value = Integer.parseInt(arr[1]);
				boolean b = cal.get(type) == value;
				if (type == Calendar.DAY_OF_MONTH) {
					int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
					if (maxDay < value)
						b = cal.get(type) == maxDay;
				}
				if (b)
					return b;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String toString() {
		return this.code;
	}

}
