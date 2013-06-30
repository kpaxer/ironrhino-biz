package com.ironrhino.biz.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.sequence.CyclicSequence;
import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.util.DateUtils;
import org.ironrhino.core.util.ErrorMessage;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.OrderItem;
import com.ironrhino.biz.model.Plan;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.SaleType;

@Singleton
@Named("orderManager")
public class OrderManagerImpl extends BaseManagerImpl<Order> implements
		OrderManager {

	@Inject
	@Named("orderCodeSequence")
	private CyclicSequence orderCodeSequence;

	@Inject
	private CustomerManager customerManager;

	@Inject
	private ProductManager productManager;

	@Inject
	private PlanManager planManager;

	@Override
	@Transactional
	public void save(Order order) {
		if (order.getItems().isEmpty())
			throw new IllegalArgumentException("must have item");
		Date orderDate = order.getOrderDate();
		Customer customer = order.getCustomer();
		boolean customerModified = false;
		for (OrderItem oi : order.getItems()) {
			String categoryName = oi.getProduct().getCategory().getName();
			if (oi.isFreegift() || categoryName.equals("促销品"))
				continue;
			if (!customer.getTags().contains(categoryName)) {
				customer.getTags().add(categoryName);
				customerModified = true;
			}
		}
		if (customer.getActiveDate() == null
				|| customer.getActiveDate().before(orderDate)) {
			customer.setActiveDate(orderDate);
			customerModified = true;
		}
		if (customerModified)
			customerManager.save(customer);
		if (order.isNew())
			place(order);
		else
			modify(order);

	}

	@Override
	@Transactional
	public void place(Order order) {
		order.setCode(nextCode());
		super.save(order);
		createPlanAndModifyStock(order);
	}

	@Override
	@Transactional
	public void modify(Order order) {
		sessionFactory.getCurrentSession().evict(order);
		Order old = get(order.getId());
		boolean onlyModifyPrice = true;
		List<OrderItem> oldItems = old.getItems();
		List<OrderItem> newItems = order.getItems();
		if (oldItems.size() == newItems.size()) {
			for (int i = 0; i < oldItems.size(); i++) {
				OrderItem oldItem = oldItems.get(i);
				OrderItem newItem = newItems.get(i);
				if (!oldItem.getProduct().getId()
						.equals(newItem.getProduct().getId())
						|| oldItem.getQuantity() != newItem.getQuantity()) {
					onlyModifyPrice = false;
					break;
				}
			}
		} else {
			onlyModifyPrice = false;
		}
		if (!onlyModifyPrice) {
			cancelPlanAndRestoreStock(old);
			createPlanAndModifyStock(order);
		}
		sessionFactory.getCurrentSession().evict(old);
		super.save(order);
	}

	@Override
	@Transactional
	public void cancel(Order order) {
		cancelPlanAndRestoreStock(order);
		delete(order);
	}

	private void cancelPlanAndRestoreStock(Order order) {
		for (OrderItem item : order.getItems()) {
			Product p = item.getProduct();
			p.setStock(p.getStock() + item.getQuantity());
			if (order.getSaleType() == SaleType.SHOP)
				p.setShopStock(p.getShopStock() + item.getQuantity());
			productManager.save(p);
		}
		DetachedCriteria dc = planManager.detachedCriteria();
		dc.add(Restrictions.eq("completed", false));
		dc.add(Restrictions.like("memo", order.getCode(), MatchMode.ANYWHERE));
		List<Plan> plans = planManager.findListByCriteria(dc);
		for (Plan p : plans)
			planManager.delete(p);
	}

	private void createPlanAndModifyStock(Order order) {
		for (OrderItem item : order.getItems()) {
			Product p = item.getProduct();
			p.setStock(p.getStock() - item.getQuantity());
			if (order.getSaleType() == SaleType.SHOP)
				p.setShopStock(p.getShopStock() - item.getQuantity());
			if (item.getPrice().doubleValue() > 0) {
				p.setPrice(item.getPrice());
				productManager.save(p);
			}
			if (p.getStock() < 0) {
				Plan plan = new Plan();
				plan.setProduct(p);
				plan.setQuantity(-p.getStock());
				plan.setPlanDate(DateUtils.addDays(new Date(), 1));
				plan.setMemo("根据订单" + order.getCode() + "自动生成");
				planManager.save(plan);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public void checkDelete(Order order) {
		super.checkDelete(order);
		if (order.isPaid() || order.isShipped())
			throw new ErrorMessage("delete.forbidden",
					new Object[] { order.getCode() }, "此订单已经付款或已经发货");
	}

	@Override
	@Transactional
	public void pay(Order order) {
		if (order.isPaid())
			return;
		order.setPaid(true);
		order.setPayDate(new Date());
		save(order);
	}

	@Override
	@Transactional
	public void ship(Order order) {
		if (order.isShipped())
			return;
		order.setShipped(true);
		order.setShipDate(new Date());
		save(order);
	}

	private String nextCode() {
		return orderCodeSequence.nextStringValue();
	}

}
