package com.ironrhino.biz.action;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.Region;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.chart.openflashchart.Chart;
import org.ironrhino.core.chart.openflashchart.ChartUtils;
import org.ironrhino.core.chart.openflashchart.Text;
import org.ironrhino.core.chart.openflashchart.axis.XAxis;
import org.ironrhino.core.chart.openflashchart.axis.XAxisLabels;
import org.ironrhino.core.chart.openflashchart.axis.YAxis;
import org.ironrhino.core.chart.openflashchart.axis.Label.Rotate;
import org.ironrhino.core.chart.openflashchart.elements.BarChart;
import org.ironrhino.core.chart.openflashchart.elements.LineChart;
import org.ironrhino.core.chart.openflashchart.elements.Point;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.DateUtils;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.ironrhino.biz.model.Brand;
import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.OrderItem;
import com.ironrhino.biz.model.SaleType;
import com.ironrhino.biz.model.Product;
import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.Stuffflow;
import com.ironrhino.biz.service.BrandManager;
import com.ironrhino.biz.service.CategoryManager;
import com.ironrhino.biz.service.OrderManager;
import com.ironrhino.biz.service.ProductManager;
import com.ironrhino.biz.service.StuffManager;

@AutoConfig
public class ChartAction extends BaseAction {

	private static final long serialVersionUID = -7256690227585867617L;

	private static final String datePattern = "yyyy年MM月dd日";

	private String type;

	private Date date;

	private Date from;

	private Date to;

	private Chart chart;

	private String location = "湖南省";

	private String title;

	@Inject
	private transient BrandManager brandManager;

	@Inject
	private transient CategoryManager categoryManager;

	@Inject
	private transient OrderManager orderManager;

	@Inject
	private transient ProductManager productManager;

	@Inject
	private transient StuffManager stuffManager;

	@Inject
	private transient BaseManager baseManager;

	@Inject
	private transient RegionTreeControl regionTreeControl;

	public Chart getChart() {
		return chart;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		if (date == null)
			date = new Date();
		return date;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Date getFrom() {
		if (from == null) {
			if (date != null)
				from = date;
			else
				from = DateUtils.addDays(new Date(), -60);
		}
		return from;
	}

	public Date getTo() {
		if (to == null) {
			if (date != null)
				to = date;
			else
				to = new Date();
		}
		return to;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDateRange() {
		if (date != null)
			return DateUtils.format(date, datePattern);
		else if (from != null && to != null)
			if (!DateUtils.isSameDay(from, to))
				return DateUtils.format(from, datePattern) + "-"
						+ DateUtils.format(to, datePattern);
			else
				return DateUtils.format(from, datePattern);
		return "";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Region getRegionTree() {
		return regionTreeControl.getRegionTree();
	}

	@Override
	public String execute() {
		return SUCCESS;
	}

	@Override
	public String view() {
		return VIEW;
	}

	public String geo() {
		return "geo";
	}

	@JsonConfig(root = "chart")
	public String data() {
		if (type != null) {
			try {
				Method method = getClass().getDeclaredMethod(type);
				method.invoke(this, new Object[0]);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("没有此图表");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return JSON;
	}

	public void brand() {
		List<Brand> brands = brandManager.findAll(org.hibernate.criterion.Order
				.asc("displayOrder"));
		List<Category> cates = categoryManager
				.findAll(org.hibernate.criterion.Order.asc("displayOrder"));
		List<String> labels = new ArrayList<String>();
		for (Brand b : brands)
			labels.add(b.getName());
		List<Order> orders;
		Category category = null;
		final String id = getUid();
		String str;
		if (StringUtils.isNumeric(id)) {
			category = categoryManager.get(Long.valueOf(id));
			str = "select distinct o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.id = ?";
		} else if (StringUtils.isNotBlank(id)) {
			category = categoryManager.findByNaturalId(id);
			str = "select distinct o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.name = ?";
		} else {
			str = "select distinct o from Order o join o.items item join item.product where o.orderDate between ? and ?";
		}
		final String hql = str;
		orders = (List<Order>) orderManager
				.executeFind(new HibernateCallback<List<Order>>() {
					@Override
					public List<Order> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql.toString());
						q.setParameter(0, DateUtils.beginOfDay(getFrom()));
						q.setParameter(1, DateUtils.endOfDay(getTo()));
						if (StringUtils.isNotBlank(id))
							q.setParameter(2, StringUtils.isNumeric(id) ? Long
									.valueOf(id) : id);
						return q.list();
					}
				});
		title = (category != null ? category.getName() : "") + "销量根据商标统计";
		chart = new Chart(title + "(" + getDateRange() + ")",
				"font-size: 15px;");
		chart.setY_legend(new Text("销量", "{font-size: 12px; color: #778877}"));
		chart.setX_legend(new Text("商标", "{font-size: 12px; color: #778877}"));

		if (category != null) {
			Map<String, BigDecimal> sales = new HashMap<String, BigDecimal>();
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {
					if (!item.getProduct().getCategory().equals(category))
						continue;
					String brand = item.getProduct().getBrand().getName();
					BigDecimal total = sales.get(brand);
					if (total == null) {
						sales.put(brand, item.getSubtotal());
					} else {
						sales.put(brand, total.add(item.getSubtotal()));
					}
				}
			}
			Iterator<String> it = labels.iterator();
			while (it.hasNext()) {
				String label = it.next();
				BigDecimal total = sales.get(label);
				if (total == null || total.doubleValue() == 0)
					it.remove();
			}
			Double[] values = new Double[labels.size()];
			double max = 0;
			for (int i = 0; i < labels.size(); i++) {
				BigDecimal total = sales.get(labels.get(i));
				if (total == null)
					total = new BigDecimal(0.00);
				if (max == 0)
					max = total.doubleValue();
				else if (max < total.doubleValue())
					max = total.doubleValue();
				values[i] = total.doubleValue();
			}

			XAxis x = new XAxis();
			YAxis y = new YAxis();
			XAxisLabels xAxisLabels = new XAxisLabels(labels);
			xAxisLabels.setSize(12);
			x.setXAxisLabels(xAxisLabels);
			y.setSteps(ChartUtils.caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.addValues(values);
			chart.addElements(element);
		} else {
			Map<String, BigDecimal> sales = new HashMap<String, BigDecimal>();
			Map<String, Map<String, BigDecimal>> salesByCate = new HashMap<String, Map<String, BigDecimal>>();
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {

					String brand = item.getProduct().getBrand().getName();
					BigDecimal total = sales.get(brand);
					if (total == null) {
						sales.put(brand, item.getSubtotal());
					} else {
						sales.put(brand, total.add(item.getSubtotal()));
					}

					String cate = item.getProduct().getCategory().getName();
					Map<String, BigDecimal> map = salesByCate.get(cate);
					if (map == null) {
						map = new HashMap<String, BigDecimal>();
						salesByCate.put(cate, map);
					}
					total = map.get(brand);
					if (total == null) {
						map.put(brand, item.getSubtotal());
					} else {
						map.put(brand, total.add(item.getSubtotal()));
					}
				}
			}
			Iterator<String> it = labels.iterator();
			while (it.hasNext()) {
				String label = it.next();
				BigDecimal total = sales.get(label);
				if (total == null || total.doubleValue() == 0)
					it.remove();
			}
			Double[] values = new Double[labels.size()];
			double max = 0;
			for (int i = 0; i < labels.size(); i++) {
				BigDecimal total = sales.get(labels.get(i));
				if (total == null)
					total = new BigDecimal(0.00);
				if (max == 0)
					max = total.doubleValue();
				else if (max < total.doubleValue())
					max = total.doubleValue();
				values[i] = total.doubleValue();
			}

			XAxis x = new XAxis();
			YAxis y = new YAxis();
			XAxisLabels xAxisLabels = new XAxisLabels(labels);
			xAxisLabels.setSize(12);
			x.setXAxisLabels(xAxisLabels);
			y.setSteps(ChartUtils.caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.setText("总销量");
			element.setTip("总销量 #val#");
			element.addValues(values);
			chart.addElements(element);
			int colorSeed = 0;
			for (Category cate : cates) {
				Map<String, BigDecimal> map = salesByCate.get(cate.getName());
				if (map == null)
					continue;
				values = new Double[labels.size()];
				for (int i = 0; i < labels.size(); i++) {
					BigDecimal total = map.get(labels.get(i));
					if (total == null)
						total = new BigDecimal(0.00);
					values[i] = total.doubleValue();
				}
				element = new BarChart();
				element.setColour(ChartUtils.caculateColor(colorSeed++));
				element.setText(cate.getName());
				element.setTip(cate.getName() + "销量 #val#");
				element.addValues(values);
				chart.addElements(element);
			}
		}

	}

	public void category() {
		List<Brand> brands = brandManager.findAll(org.hibernate.criterion.Order
				.asc("displayOrder"));
		List<Category> cates = categoryManager
				.findAll(org.hibernate.criterion.Order.asc("displayOrder"));
		List<String> labels = new ArrayList<String>();
		for (Category c : cates)
			labels.add(c.getName());
		List<Order> orders;
		Brand brand = null;
		final String id = getUid();
		String str;
		if (StringUtils.isNumeric(id)) {
			brand = brandManager.get(Long.valueOf(id));
			str = "select distinct o from Order o join o.items item join item.product p join p.brand b where (o.orderDate between ? and ?) and b.id = ?";
		} else if (StringUtils.isNotBlank(id)) {
			brand = brandManager.findByNaturalId(id);
			str = "select distinct o from Order o join o.items item join item.product p join p.brand b where (o.orderDate between ? and ?) and b.name = ?";
		} else {
			str = "select distinct o from Order o join o.items item join item.product where o.orderDate between ? and ?";
		}
		final String hql = str;
		orders = (List<Order>) orderManager
				.executeFind(new HibernateCallback<List<Order>>() {
					@Override
					public List<Order> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql.toString());
						q.setParameter(0, DateUtils.beginOfDay(getFrom()));
						q.setParameter(1, DateUtils.endOfDay(getTo()));
						if (StringUtils.isNotBlank(id))
							q.setParameter(2, StringUtils.isNumeric(id) ? Long
									.valueOf(id) : id);
						return q.list();
					}
				});
		title = (brand != null ? brand.getName() : "") + "销量根据品种统计";
		chart = new Chart(title + "(" + getDateRange() + ")",
				"font-size: 15px;");
		chart.setY_legend(new Text("销量", "{font-size: 12px; color: #778877}"));
		chart.setX_legend(new Text("品种", "{font-size: 12px; color: #778877}"));

		if (brand != null) {
			Map<String, BigDecimal> sales = new HashMap<String, BigDecimal>();
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {
					if (!item.getProduct().getBrand().equals(brand))
						continue;
					String category = item.getProduct().getCategory().getName();
					BigDecimal total = sales.get(category);
					if (total == null) {
						sales.put(category, item.getSubtotal());
					} else {
						sales.put(category, total.add(item.getSubtotal()));
					}
				}
			}
			Iterator<String> it = labels.iterator();
			while (it.hasNext()) {
				String label = it.next();
				BigDecimal total = sales.get(label);
				if (total == null || total.doubleValue() == 0)
					it.remove();
			}
			Double[] values = new Double[labels.size()];
			double max = 0;
			for (int i = 0; i < labels.size(); i++) {
				BigDecimal total = sales.get(labels.get(i));
				if (total == null)
					total = new BigDecimal(0.00);
				if (max == 0)
					max = total.doubleValue();
				else if (max < total.doubleValue())
					max = total.doubleValue();
				values[i] = total.doubleValue();
			}

			XAxis x = new XAxis();
			YAxis y = new YAxis();
			XAxisLabels xAxisLabels = new XAxisLabels(labels);
			xAxisLabels.setSize(12);
			x.setXAxisLabels(xAxisLabels);
			y.setSteps(ChartUtils.caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.addValues(values);
			chart.addElements(element);
		} else {
			Map<String, BigDecimal> sales = new HashMap<String, BigDecimal>();
			Map<String, Map<String, BigDecimal>> salesByBrand = new HashMap<String, Map<String, BigDecimal>>();
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {

					String category = item.getProduct().getCategory().getName();
					BigDecimal total = sales.get(category);
					if (total == null) {
						sales.put(category, item.getSubtotal());
					} else {
						sales.put(category, total.add(item.getSubtotal()));
					}

					String brandName = item.getProduct().getBrand().getName();
					Map<String, BigDecimal> map = salesByBrand.get(brandName);
					if (map == null) {
						map = new HashMap<String, BigDecimal>();
						salesByBrand.put(brandName, map);
					}
					total = map.get(category);
					if (total == null) {
						map.put(category, item.getSubtotal());
					} else {
						map.put(category, total.add(item.getSubtotal()));
					}
				}
			}
			Iterator<String> it = labels.iterator();
			while (it.hasNext()) {
				String label = it.next();
				BigDecimal total = sales.get(label);
				if (total == null || total.doubleValue() == 0)
					it.remove();
			}
			Double[] values = new Double[labels.size()];
			double max = 0;
			for (int i = 0; i < labels.size(); i++) {
				BigDecimal total = sales.get(labels.get(i));
				if (total == null)
					total = new BigDecimal(0.00);
				if (max == 0)
					max = total.doubleValue();
				else if (max < total.doubleValue())
					max = total.doubleValue();
				values[i] = total.doubleValue();
			}

			XAxis x = new XAxis();
			YAxis y = new YAxis();
			XAxisLabels xAxisLabels = new XAxisLabels(labels);
			xAxisLabels.setSize(12);
			x.setXAxisLabels(xAxisLabels);
			y.setSteps(ChartUtils.caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.setText("总销量");
			element.setTip("总销量 #val#");
			element.addValues(values);
			chart.addElements(element);
			int colorSeed = 0;
			for (Brand b : brands) {
				Map<String, BigDecimal> map = salesByBrand.get(b.getName());
				if (map == null)
					continue;
				values = new Double[labels.size()];
				for (int i = 0; i < labels.size(); i++) {
					BigDecimal total = map.get(labels.get(i));
					if (total == null)
						total = new BigDecimal(0.00);
					values[i] = total.doubleValue();
				}
				element = new BarChart();
				element.setColour(ChartUtils.caculateColor(colorSeed++));
				element.setText(b.getName());
				element.setTip(b.getName() + "销量 #val#");
				element.addValues(values);
				chart.addElements(element);
			}
		}

	}

	public void saletype() {
		List<Category> cates = categoryManager
				.findAll(org.hibernate.criterion.Order.asc("displayOrder"));
		List<String> labels = new ArrayList<String>();
		for (SaleType st : SaleType.values())
			labels.add(st.getDisplayName());
		List<Order> orders;
		Category category = null;
		final String id = getUid();
		String str;
		if (StringUtils.isNumeric(id)) {
			category = categoryManager.get(Long.valueOf(id));
			str = "select distinct o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.id = ?";
		} else if (StringUtils.isNotBlank(id)) {
			category = categoryManager.findByNaturalId(id);
			str = "select distinct o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.name = ?";
		} else {
			str = "select o from Order o where o.orderDate between ? and ?";
		}
		final String hql = str;
		orders = (List<Order>) orderManager
				.executeFind(new HibernateCallback<List<Order>>() {
					@Override
					public List<Order> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql.toString());
						q.setParameter(0, DateUtils.beginOfDay(getFrom()));
						q.setParameter(1, DateUtils.endOfDay(getTo()));
						if (StringUtils.isNotBlank(id))
							q.setParameter(2, StringUtils.isNumeric(id) ? Long
									.valueOf(id) : id);
						return q.list();
					}
				});
		title = (category != null ? category.getName() : "") + "销量根据销售方式统计";
		chart = new Chart(title + "(" + getDateRange() + ")",
				"font-size: 15px;");
		chart.setY_legend(new Text("销量", "{font-size: 12px; color: #778877}"));
		chart
				.setX_legend(new Text("销售方式",
						"{font-size: 12px; color: #778877}"));

		if (category != null) {
			Map<String, BigDecimal> sales = new HashMap<String, BigDecimal>();
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {
					if (!item.getProduct().getCategory().equals(category))
						continue;
					String saletype = order.getSaleType().getDisplayName();
					BigDecimal total = sales.get(saletype);
					if (total == null) {
						sales.put(saletype, item.getSubtotal());
					} else {
						sales.put(saletype, total.add(item.getSubtotal()));
					}
				}
			}
			Iterator<String> it = labels.iterator();
			while (it.hasNext()) {
				String label = it.next();
				BigDecimal total = sales.get(label);
				if (total == null || total.doubleValue() == 0)
					it.remove();
			}
			Double[] values = new Double[labels.size()];
			double max = 0;
			for (int i = 0; i < labels.size(); i++) {
				BigDecimal total = sales.get(labels.get(i));
				if (total == null)
					total = new BigDecimal(0.00);
				if (max == 0)
					max = total.doubleValue();
				else if (max < total.doubleValue())
					max = total.doubleValue();
				values[i] = total.doubleValue();
			}

			XAxis x = new XAxis();
			YAxis y = new YAxis();
			XAxisLabels xAxisLabels = new XAxisLabels(labels);
			xAxisLabels.setSize(12);
			x.setXAxisLabels(xAxisLabels);
			y.setSteps(ChartUtils.caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.addValues(values);
			chart.addElements(element);
		} else {
			Map<String, BigDecimal> sales = new HashMap<String, BigDecimal>();
			Map<String, Map<String, BigDecimal>> salesByCate = new HashMap<String, Map<String, BigDecimal>>();
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {
					String saletype = order.getSaleType().getDisplayName();
					BigDecimal total = sales.get(saletype);
					if (total == null) {
						sales.put(saletype, item.getSubtotal());
					} else {
						sales.put(saletype, total.add(item.getSubtotal()));
					}

					String cate = item.getProduct().getCategory().getName();
					Map<String, BigDecimal> map = salesByCate.get(cate);
					if (map == null) {
						map = new HashMap<String, BigDecimal>();
						salesByCate.put(cate, map);
					}
					total = map.get(saletype);
					if (total == null) {
						map.put(saletype, item.getSubtotal());
					} else {
						map.put(saletype, total.add(item.getSubtotal()));
					}
				}
			}
			Iterator<String> it = labels.iterator();
			while (it.hasNext()) {
				String label = it.next();
				BigDecimal total = sales.get(label);
				if (total == null || total.doubleValue() == 0)
					it.remove();
			}
			Double[] values = new Double[labels.size()];
			double max = 0;
			for (int i = 0; i < labels.size(); i++) {
				BigDecimal total = sales.get(labels.get(i));
				if (total == null)
					total = new BigDecimal(0.00);
				if (max == 0)
					max = total.doubleValue();
				else if (max < total.doubleValue())
					max = total.doubleValue();
				values[i] = total.doubleValue();
			}

			XAxis x = new XAxis();
			YAxis y = new YAxis();
			XAxisLabels xAxisLabels = new XAxisLabels(labels);
			xAxisLabels.setSize(12);
			x.setXAxisLabels(xAxisLabels);
			y.setSteps(ChartUtils.caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.setText("总销量");
			element.setTip("总销量 #val#");
			element.addValues(values);
			chart.addElements(element);
			int colorSeed = 0;
			for (Category cate : cates) {
				Map<String, BigDecimal> map = salesByCate.get(cate.getName());
				if (map == null)
					continue;
				values = new Double[labels.size()];
				for (int i = 0; i < labels.size(); i++) {
					BigDecimal total = map.get(labels.get(i));
					if (total == null)
						total = new BigDecimal(0.00);
					values[i] = total.doubleValue();
				}
				element = new BarChart();
				element.setColour(ChartUtils.caculateColor(colorSeed++));
				element.setText(cate.getName());
				element.setTip(cate.getName() + "销量 #val#");
				element.addValues(values);
				chart.addElements(element);
			}
		}

	}

	public void region() {
		List<Category> cates = categoryManager
				.findAll(org.hibernate.criterion.Order.asc("displayOrder"));
		List<String> labels = new ArrayList<String>();
		Region region = null;
		if (StringUtils.isNumeric(location))
			region = regionTreeControl.getRegionTree().getDescendantOrSelfById(
					Long.valueOf(location));
		else
			region = regionTreeControl.parseByAddress(location);
		if (region == null)
			region = regionTreeControl.getRegionTree();
		for (Region r : region.getChildren())
			labels.add(r.getName());
		labels.add(region.getName() + "未知地区");
		labels.add("其它地区");

		List<Order> orders;
		Category category = null;
		final String id = getUid();
		String str;
		if (StringUtils.isNumeric(id)) {
			category = categoryManager.get(Long.valueOf(id));
			str = "select distinct o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.id = ?";
		} else if (StringUtils.isNotBlank(id)) {
			category = categoryManager.findByNaturalId(id);
			str = "select distinct o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.name = ?";
		} else {
			str = "select distinct o from Order o join o.items item join item.product where o.orderDate between ? and ?";
		}
		final String hql = str;
		orders = (List<Order>) orderManager
				.executeFind(new HibernateCallback<List<Order>>() {
					@Override
					public List<Order> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql.toString());
						q.setParameter(0, DateUtils.beginOfDay(getFrom()));
						q.setParameter(1, DateUtils.endOfDay(getTo()));
						if (StringUtils.isNotBlank(id))
							q.setParameter(2, StringUtils.isNumeric(id) ? Long
									.valueOf(id) : id);
						return q.list();
					}
				});
		title = (category != null ? category.getName() : "") + "销量根据地区统计";
		chart = new Chart(title + "(" + getDateRange() + ")",
				"font-size: 15px;");
		chart.setY_legend(new Text("销量", "{font-size: 12px; color: #778877}"));
		chart.setX_legend(new Text("地区", "{font-size: 12px; color: #778877}"));

		if (category != null) {
			Map<String, BigDecimal> sales = new HashMap<String, BigDecimal>();
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {
					if (!item.getProduct().getCategory().equals(category))
						continue;
					String regionName = "其它地区";
					Region r = order.getCustomer().getRegion();
					if (r != null)
						r = regionTreeControl.getRegionTree()
								.getDescendantOrSelfById(r.getId());
					if (r != null) {
						if (r.isDescendantOrSelfOf(region)) {
							if (r.getId().equals(region.getId()))
								regionName = region.getName() + "未知地区";
							else
								regionName = r.getAncestorName(region
										.getLevel() + 1);
						} else {
							System.out.println(region);
						}
					}
					BigDecimal total = sales.get(regionName);
					if (total == null) {
						sales.put(regionName, item.getSubtotal());
					} else {
						sales.put(regionName, total.add(item.getSubtotal()));
					}
				}
			}
			Iterator<String> it = labels.iterator();
			while (it.hasNext()) {
				String label = it.next();
				BigDecimal total = sales.get(label);
				if (total == null || total.doubleValue() == 0)
					it.remove();
			}
			Double[] values = new Double[labels.size()];
			double max = 0;
			for (int i = 0; i < labels.size(); i++) {
				BigDecimal total = sales.get(labels.get(i));
				if (total == null)
					total = new BigDecimal(0.00);
				if (max == 0)
					max = total.doubleValue();
				else if (max < total.doubleValue())
					max = total.doubleValue();
				values[i] = total.doubleValue();
			}

			XAxis x = new XAxis();
			YAxis y = new YAxis();
			XAxisLabels xAxisLabels = new XAxisLabels(labels);
			xAxisLabels.setSize(12);
			x.setXAxisLabels(xAxisLabels);
			y.setSteps(ChartUtils.caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.addValues(values);
			chart.addElements(element);
		} else {
			Map<String, BigDecimal> sales = new HashMap<String, BigDecimal>();
			Map<String, Map<String, BigDecimal>> salesByCate = new HashMap<String, Map<String, BigDecimal>>();
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {
					String regionName = "其它地区";
					Region r = order.getCustomer().getRegion();
					if (r != null)
						r = regionTreeControl.getRegionTree()
								.getDescendantOrSelfById(r.getId());
					if (r != null) {
						if (r.isDescendantOrSelfOf(region))
							if (r.getId().equals(region.getId()))
								regionName = region.getName() + "未知地区";
							else
								regionName = r.getAncestorName(region
										.getLevel() + 1);
					}
					BigDecimal total = sales.get(regionName);
					if (total == null) {
						sales.put(regionName, item.getSubtotal());
					} else {
						sales.put(regionName, total.add(item.getSubtotal()));
					}

					String cate = item.getProduct().getCategory().getName();
					Map<String, BigDecimal> map = salesByCate.get(cate);
					if (map == null) {
						map = new HashMap<String, BigDecimal>();
						salesByCate.put(cate, map);
					}
					total = map.get(regionName);
					if (total == null) {
						map.put(regionName, item.getSubtotal());
					} else {
						map.put(regionName, total.add(item.getSubtotal()));
					}
				}
			}
			Iterator<String> it = labels.iterator();
			while (it.hasNext()) {
				String label = it.next();
				BigDecimal total = sales.get(label);
				if (total == null || total.doubleValue() == 0)
					it.remove();
			}
			Double[] values = new Double[labels.size()];
			double max = 0;
			for (int i = 0; i < labels.size(); i++) {
				BigDecimal total = sales.get(labels.get(i));
				if (total == null)
					total = new BigDecimal(0.00);
				if (max == 0)
					max = total.doubleValue();
				else if (max < total.doubleValue())
					max = total.doubleValue();
				values[i] = total.doubleValue();
			}

			XAxis x = new XAxis();
			YAxis y = new YAxis();
			XAxisLabels xAxisLabels = new XAxisLabels(labels);
			xAxisLabels.setSize(12);
			x.setXAxisLabels(xAxisLabels);
			y.setSteps(ChartUtils.caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.setText("总销量");
			element.setTip("总销量 #val#");
			element.addValues(values);
			chart.addElements(element);
			int colorSeed = 0;
			for (Category cate : cates) {
				Map<String, BigDecimal> map = salesByCate.get(cate.getName());
				if (map == null)
					continue;
				values = new Double[labels.size()];
				for (int i = 0; i < labels.size(); i++) {
					BigDecimal total = map.get(labels.get(i));
					if (total == null)
						total = new BigDecimal(0.00);
					values[i] = total.doubleValue();
				}
				element = new BarChart();
				element.setColour(ChartUtils.caculateColor(colorSeed++));
				element.setText(cate.getName());
				element.setTip(cate.getName() + "销量 #val#");
				element.addValues(values);
				chart.addElements(element);
			}
		}

	}

	public void product() {
		final String hql = "select distinct o from Order o join o.items item join item.product p where (o.orderDate between ? and ?) order by o.orderDate";
		List<Order> orders = (List<Order>) orderManager
				.executeFind(new HibernateCallback<List<Order>>() {
					@Override
					public List<Order> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql.toString());
						q.setParameter(0, DateUtils.beginOfDay(getFrom()));
						q.setParameter(1, DateUtils.endOfDay(getTo()));
						return q.list();
					}
				});

		title = "产品价格走势图";
		chart = new Chart(title + "(" + getDateRange() + ")",
				"font-size: 15px;");
		chart.setY_legend(new Text("价格", "{font-size: 12px; color: #778877}"));
		chart.setX_legend(new Text("时间", "{font-size: 12px; color: #778877}"));
		XAxis x = new XAxis();
		XAxisLabels xAxisLabels = new XAxisLabels();
		xAxisLabels.setText("#date:y-m-d#");
		xAxisLabels.setSteps(86400);
		xAxisLabels.setVisible_steps(1);
		xAxisLabels.setRotate(Rotate.VERTICAL);
		xAxisLabels.setSize(12);
		x.setXAxisLabels(xAxisLabels);
		x.setSteps(86400);
		x.setRange((long) (DateUtils.beginOfDay(getFrom()).getTime() / 1000),
				(long) (DateUtils.endOfDay(getTo()).getTime() / 1000));
		chart.setX_axis(x);
		Double max = 0.0;

		String[] ids = getId();
		for (int index = 0; index < ids.length; index++) {
			final Long id = Long.valueOf(ids[index]);
			Product product = productManager.get(id);
			List<OrderItem> list = new ArrayList<OrderItem>();
			for (Order o : orders)
				for (OrderItem oi : o.getItems())
					if (oi.getProduct().getId().equals(id)) {
						list.add(oi);
						continue;
					}
			List<Point> points = new ArrayList<Point>();
			if (list.size() == 1) {
				OrderItem item = list.get(0);
				points.add(new Point((long) (item.getOrder().getOrderDate()
						.getTime() / 1000), item.getPrice()));
			} else {
				Date date = null;
				BigDecimal amount = new BigDecimal(0.00);
				int quantity = 0;
				for (int i = 0; i < list.size(); i++) {
					OrderItem item = list.get(i);
					if (i == list.size() - 1) {
						if ((date != null && !DateUtils.isSameDay(item
								.getOrder().getOrderDate(), date))) {
							BigDecimal price = amount.divide(new BigDecimal(
									(double) quantity),
									BigDecimal.ROUND_CEILING);
							if (max < price.doubleValue())
								max = price.doubleValue();
							points.add(new Point(
									(long) (date.getTime() / 1000), price));
							date = item.getOrder().getOrderDate();
							amount = new BigDecimal(0.00);
							quantity = 0;

						}
						amount = amount.add(item.getSubtotal());
						quantity += item.getQuantity();
						BigDecimal price = amount.divide(new BigDecimal(
								(double) quantity), BigDecimal.ROUND_CEILING);
						if (max < price.doubleValue())
							max = price.doubleValue();
						points.add(new Point((long) (item.getOrder()
								.getOrderDate().getTime() / 1000), price));
					} else {
						if ((date != null && !DateUtils.isSameDay(item
								.getOrder().getOrderDate(), date))) {
							BigDecimal price = amount.divide(new BigDecimal(
									(double) quantity),
									BigDecimal.ROUND_CEILING);
							if (max < price.doubleValue())
								max = price.doubleValue();
							points.add(new Point(
									(long) (date.getTime() / 1000), price));
							date = item.getOrder().getOrderDate();
							amount = item.getSubtotal();
							quantity = item.getQuantity();
						} else {
							if (date == null)
								date = item.getOrder().getOrderDate();
							amount = amount.add(item.getSubtotal());
							quantity += item.getQuantity();
						}
					}

				}
			}
			LineChart element = new LineChart();
			element.setText(product.getFullname());
			element.setFontSize(12);
			element.setColour(ChartUtils.caculateColor(index));
			element.setValues(points);
			chart.addElements(element);
		}

		YAxis y = new YAxis();
		y.setSteps(ChartUtils.caculateSteps(max));
		y.setMax(max);
		chart.setY_axis(y);
	}

	public void stuff() {
		baseManager.setEntityClass(Stuffflow.class);
		DetachedCriteria dc = baseManager.detachedCriteria();
		dc.add(Restrictions.isNotNull("amount"));
		dc.add(Restrictions.between("date", DateUtils.beginOfDay(getFrom()),
				DateUtils.endOfDay(getTo())));
		dc.addOrder(org.hibernate.criterion.Order.asc("date"));
		List<Stuffflow> stuffflows = baseManager.findListByCriteria(dc);

		title = "原料价格走势图";
		chart = new Chart(title + "(" + getDateRange() + ")",
				"font-size: 15px;");
		chart.setY_legend(new Text("价格", "{font-size: 12px; color: #778877}"));
		chart.setX_legend(new Text("时间", "{font-size: 12px; color: #778877}"));
		XAxis x = new XAxis();
		XAxisLabels xAxisLabels = new XAxisLabels();
		xAxisLabels.setText("#date:y-m-d#");
		xAxisLabels.setSteps(86400);
		xAxisLabels.setVisible_steps(1);
		xAxisLabels.setRotate(Rotate.VERTICAL);
		xAxisLabels.setSize(12);
		x.setXAxisLabels(xAxisLabels);
		x.setSteps(86400);
		x.setRange((long) (DateUtils.beginOfDay(getFrom()).getTime() / 1000),
				(long) (DateUtils.endOfDay(getTo()).getTime() / 1000));
		chart.setX_axis(x);
		Double max = 0.0;
		String[] ids = getId();
		for (int index = 0; index < ids.length; index++) {
			Long id = Long.valueOf(ids[index]);
			Stuff stuff = stuffManager.get(id);
			List<Stuffflow> list = new ArrayList<Stuffflow>();
			for (Stuffflow sf : stuffflows)
				if (sf.getStuff().getId().equals(id) && sf.getAmount() != null
						&& sf.getAmount().doubleValue() > 0)
					list.add(sf);

			List<Point> points = new ArrayList<Point>();
			if (list.size() == 1) {
				Stuffflow sf = list.get(0);
				points.add(new Point((long) (sf.getDate().getTime() / 1000), sf
						.getAmount().divide(new BigDecimal(sf.getQuantity()))));
			} else {
				Date date = null;
				BigDecimal amount = new BigDecimal(0.00);
				int quantity = 0;
				for (int i = 0; i < list.size(); i++) {
					Stuffflow sf = list.get(i);
					if (i == list.size() - 1) {
						if ((date != null && !DateUtils.isSameDay(sf.getDate(),
								date))) {
							BigDecimal price = amount.divide(new BigDecimal(
									(double) quantity),
									BigDecimal.ROUND_CEILING);
							if (max < price.doubleValue())
								max = price.doubleValue();
							points.add(new Point(
									(long) (date.getTime() / 1000), price));
							date = sf.getDate();
							amount = new BigDecimal(0.00);
							quantity = 0;

						}
						amount = amount.add(sf.getAmount());
						quantity += sf.getQuantity();
						BigDecimal price = amount.divide(new BigDecimal(
								(double) quantity), BigDecimal.ROUND_CEILING);
						if (max < price.doubleValue())
							max = price.doubleValue();
						points.add(new Point(
								(long) (sf.getDate().getTime() / 1000), price));
					} else {
						if ((date != null && !DateUtils.isSameDay(sf.getDate(),
								date))) {
							BigDecimal price = amount.divide(new BigDecimal(
									(double) quantity),
									BigDecimal.ROUND_CEILING);
							if (max < price.doubleValue())
								max = price.doubleValue();
							points.add(new Point(
									(long) (date.getTime() / 1000), price));
							date = sf.getDate();
							amount = sf.getAmount();
							quantity = sf.getQuantity();
						} else {
							if (date == null)
								date = sf.getDate();
							amount = amount.add(sf.getAmount());
							quantity += sf.getQuantity();

						}
					}

				}
			}
			LineChart element = new LineChart();
			element.setText(stuff.getName());
			element.setFontSize(12);
			element.setColour(ChartUtils.caculateColor(index));
			element.setValues(points);
			chart.addElements(element);
		}

		YAxis y = new YAxis();
		y.setSteps(ChartUtils.caculateSteps(max));
		y.setMax(max);
		chart.setY_axis(y);
	}
}
