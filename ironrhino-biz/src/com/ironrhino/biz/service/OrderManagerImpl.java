package com.ironrhino.biz.service;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.util.DateUtils;
import org.ironrhino.core.util.NumberUtils;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.transaction.annotation.Transactional;

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
	private DataFieldMaxValueIncrementer orderCodeSequence;

	@Inject
	private ProductManager productManager;

	@Inject
	private PlanManager planManager;

	@Override
	@Transactional
	public void save(Order order) {
		if (order.getItems().isEmpty())
			throw new IllegalArgumentException("must have item");
		if (order.isNew())
			place(order);
		else
			modify(order);

	}

	@Transactional
	public void place(Order order) {
		order.setCode(nextCode());
		super.save(order);
		for (OrderItem item : order.getItems()) {
			Product p = item.getProduct();
			p.setStock(p.getStock() - item.getQuantity());
			if (order.getSaleType() == SaleType.SHOP)
				p.setShopStock(p.getShopStock() - item.getQuantity());
			p.setPrice(item.getPrice());
			productManager.save(p);
			if (p.getStock() < 0) {
				Plan plan = new Plan();
				plan.setProduct(p);
				plan.setQuantity(-p.getStock());
				plan.setPlanDate(DateUtils.addDays(new Date(), 1));
				plan.setMemo("根据订单" + order + "自动生成");
				planManager.save(plan);
			}
		}
	}

	@Transactional
	public void modify(Order order) {
		Order old = get(order.getId());
		for (OrderItem item : old.getItems()) {
			Product p = item.getProduct();
			p.setStock(p.getStock() + item.getQuantity());
			if (order.getSaleType() == SaleType.SHOP)
				p.setShopStock(p.getShopStock() + item.getQuantity());
			productManager.save(p);
		}
		sessionFactory.getCurrentSession().evict(old);
		super.save(order);
		for (OrderItem item : order.getItems()) {
			Product p = item.getProduct();
			p.setStock(p.getStock() - item.getQuantity());
			if (order.getSaleType() == SaleType.SHOP)
				p.setShopStock(p.getShopStock() - item.getQuantity());
			p.setPrice(item.getPrice());
			productManager.save(p);
			if (p.getStock() < 0) {
				Plan plan = new Plan();
				plan.setProduct(p);
				plan.setQuantity(-p.getStock());
				plan.setPlanDate(DateUtils.addDays(new Date(), 1));
				plan.setMemo("根据订单" + order + "自动生成");
				planManager.save(plan);
			}
		}

	}

	@Transactional
	public void cancel(Order order) {
		for (OrderItem item : order.getItems()) {
			Product p = item.getProduct();
			p.setStock(p.getStock() + item.getQuantity());
			if (order.getSaleType() == SaleType.SHOP)
				p.setShopStock(p.getShopStock() + item.getQuantity());
			productManager.save(p);
		}
		delete(order);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean canDelete(Order order) {
		return !(order.isPaid() || order.isShipped());
	}

	@Override
	@Transactional
	public void pay(Order order) {
		if (order.isPaid())
			return;
		order.setPaid(true);
		save(order);
	}

	@Override
	@Transactional
	public void ship(Order order) {
		if (order.isShipped())
			return;
		order.setShipped(true);
		save(order);
	}

	private String nextCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtils.formatDate8(new Date()));
		sb.append(NumberUtils.format(orderCodeSequence.nextIntValue(), 4));
		return sb.toString();
	}

}
