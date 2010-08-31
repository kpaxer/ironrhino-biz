package com.ironrhino.biz.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;
import org.ironrhino.core.model.Recordable;
import org.ironrhino.security.model.User;

import com.opensymphony.xwork2.util.CreateIfNull;

@Searchable(alias = "order")
@AutoConfig
public class Order extends BaseEntity implements Recordable<User> {

	private static final long serialVersionUID = -3191022860732749749L;

	@NaturalId
	@SearchableProperty(boost = 3, index = Index.NOT_ANALYZED)
	private String code;

	private BigDecimal discount;

	private SaleType saleType = SaleType.FACTORY;

	@SearchableProperty
	private String memo;

	@SearchableProperty(converter = "date", format = "yyyy-MM-dd")
	private Date orderDate = new Date();

	@NotInCopy
	private Date createDate = new Date();

	@NotInCopy
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
	private Customer customer;

	@SearchableComponent
	@NotInCopy
	private Station station;

	@SearchableComponent
	@NotInCopy
	private Employee salesman;

	@SearchableComponent
	@NotInCopy
	private Employee deliveryman;

	@SearchableComponent
	@NotInCopy
	private User createUser;

	@SearchableComponent
	@NotInCopy
	private User modifyUser;

	@CreateIfNull
	private List<OrderItem> items = new ArrayList<OrderItem>(0);

	public BigDecimal getAmount() {
		BigDecimal amount = new BigDecimal(0.0);
		for (OrderItem item : items)
			amount = amount.add(item.getSubtotal());
		return amount;
	}

	public String getCode() {
		return code;
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

	public boolean isCashable() {
		if (paid || station == null)
			return false;
		try {
			List<String> list = station.getCashCondition();
			if (list == null || list.isEmpty())
				return false;
			Calendar cal = Calendar.getInstance();
			for (String line : list) {
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
