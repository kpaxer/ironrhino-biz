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
import org.ironrhino.common.model.Region;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.chart.openflashchart.Chart;
import org.ironrhino.core.chart.openflashchart.Text;
import org.ironrhino.core.chart.openflashchart.axis.XAxis;
import org.ironrhino.core.chart.openflashchart.axis.XAxisLabels;
import org.ironrhino.core.chart.openflashchart.axis.YAxis;
import org.ironrhino.core.chart.openflashchart.elements.BarChart;
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
import com.ironrhino.biz.service.OrderManager;

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
	private transient OrderManager orderManager;

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
		if (from == null)
			from = getDate();
		return from;
	}

	public Date getTo() {
		if (to == null) {
			to = getFrom();
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

	@Override
	public String execute() {
		return SUCCESS;
	}

	@Override
	public String view() {
		return VIEW;
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

	private int caculateSteps(double max) {
		double d = max / 10;
		if (d < 1)
			return 1;
		int i = (int) d;
		if (d > i)
			i++;
		int digit = String.valueOf(i).length();

		int first = i % ((int) Math.pow(10, digit - 1));
		if (first < 5)
			first = 5;
		else
			first = 10;
		return first * ((int) Math.pow(10, digit - 1));
	}

	private String[] colors = new String[] { "#ee4400", "#94ee00", "#00eee6",
			"#ee00c7", "#9800ee" };

	private String caculateColor(int seed) {
		if (seed <= colors.length)
			return colors[seed - 1];
		boolean odd = seed % 2 != 0;
		seed = odd ? seed * 2 : 10 - seed;
		StringBuilder sb = new StringBuilder();
		sb.append('#');
		sb.append(seed);
		sb.append(10 - seed);
		sb.append(seed);
		sb.append(10 - seed);
		sb.append(seed);
		sb.append(10 - seed);
		return sb.toString();
	}

	public void brand() {
		baseManager.setEntityClass(Brand.class);
		List<Brand> brands = baseManager.findAll(org.hibernate.criterion.Order
				.asc("displayOrder"));
		baseManager.setEntityClass(Category.class);
		List<Category> cates = baseManager
				.findAll(org.hibernate.criterion.Order.asc("displayOrder"));
		List<String> labels = new ArrayList<String>();
		for (Brand b : brands)
			labels.add(b.getName());
		List<Order> orders;
		Category category = null;
		final String id = getUid();
		String str;
		baseManager.setEntityClass(Category.class);
		if (StringUtils.isNumeric(id)) {
			category = (Category) baseManager.get(Long.valueOf(id));
			str = "select o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.id = ?";
		} else if (StringUtils.isNotBlank(id)) {
			category = (Category) baseManager.findByNaturalId(id);
			str = "select o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.name = ?";
		} else {
			str = "select o from Order o join o.items item join item.product where o.orderDate between ? and ?";
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
		chart.setY_legend(new Text("销量", "font-size: 15px;"));
		chart.setX_legend(new Text("商标", "font-size: 15x;"));

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
			y.setSteps(caculateSteps(max));
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
			y.setSteps(caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.setText("总销量");
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
				element.setColour(caculateColor(++colorSeed));
				element.setText(cate.getName());
				element.addValues(values);
				chart.addElements(element);
			}
		}

	}

	public void category() {
		baseManager.setEntityClass(Brand.class);
		List<Brand> brands = baseManager.findAll(org.hibernate.criterion.Order
				.asc("displayOrder"));
		baseManager.setEntityClass(Category.class);
		List<Category> cates = baseManager
				.findAll(org.hibernate.criterion.Order.asc("displayOrder"));
		List<String> labels = new ArrayList<String>();
		for (Category c : cates)
			labels.add(c.getName());
		List<Order> orders;
		Brand brand = null;
		final String id = getUid();
		String str;
		baseManager.setEntityClass(Brand.class);
		if (StringUtils.isNumeric(id)) {
			brand = (Brand) baseManager.get(Long.valueOf(id));
			str = "select o from Order o join o.items item join item.product p join p.brand b where (o.orderDate between ? and ?) and b.id = ?";
		} else if (StringUtils.isNotBlank(id)) {
			brand = (Brand) baseManager.findByNaturalId(id);
			str = "select o from Order o join o.items item join item.product p join p.brand b where (o.orderDate between ? and ?) and b.name = ?";
		} else {
			str = "select o from Order o join o.items item join item.product where o.orderDate between ? and ?";
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
		chart.setY_legend(new Text("销量", "font-size: 15px;"));
		chart.setX_legend(new Text("品种", "font-size: 15x;"));

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
			y.setSteps(caculateSteps(max));
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
			y.setSteps(caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.setText("总销量");
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
				element.setColour(caculateColor(++colorSeed));
				element.setText(b.getName());
				element.addValues(values);
				chart.addElements(element);
			}
		}

	}

	public void region() {
		baseManager.setEntityClass(Category.class);
		List<Category> cates = baseManager
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
		baseManager.setEntityClass(Category.class);
		if (StringUtils.isNumeric(id)) {
			category = (Category) baseManager.get(Long.valueOf(id));
			str = "select o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.id = ?";
		} else if (StringUtils.isNotBlank(id)) {
			category = (Category) baseManager.findByNaturalId(id);
			str = "select o from Order o join o.items item join item.product p join p.category c where (o.orderDate between ? and ?) and c.name = ?";
		} else {
			str = "select o from Order o join o.items item join item.product where o.orderDate between ? and ?";
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
		chart.setY_legend(new Text("销量", "font-size: 15px;"));
		chart.setX_legend(new Text("地区", "font-size: 15x;"));

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
			y.setSteps(caculateSteps(max));
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
			y.setSteps(caculateSteps(max));
			y.setMax(max);
			chart.setX_axis(x);
			chart.setY_axis(y);
			BarChart element = new BarChart();
			element.setText("总销量");
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
				element.setColour(caculateColor(++colorSeed));
				element.setText(cate.getName());
				element.addValues(values);
				chart.addElements(element);
			}
		}

	}

	public void month() {
		// 时间区间 默认一年内 按月统计 销量和价格 线图 接受必选参数产品id 可以多个 做成treeTable选择对比
	}

	public void stuff() {
		// 时间区间 默认一年内 按月统计 进货量 和价格 线图 接受必选参数原料id 可以多个 做成treeTable选择对比
	}

}
