package com.ironrhino.biz.action;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.chart.openflashchart.Chart;
import org.ironrhino.core.chart.openflashchart.axis.XAxis;
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

	@Inject
	private transient OrderManager orderManager;

	@Inject
	private transient StuffManager stuffManager;

	@Inject
	private transient ProductManager productManager;

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

	@Override
	public String execute() {
		return SUCCESS;
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
			}
		}
		return JSON;
	}

	public void brand() {

		// 时间区间 默认一年内 柱图 接受可选参数品种缩小范围
		baseManager.setEntityClass(Brand.class);
		List<Brand> brands = baseManager.findAll(org.hibernate.criterion.Order
				.asc("displayOrder"));
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
		chart = new Chart((category != null ? category.getName() : "") + "销量统计");
		XAxis x = new XAxis();
		YAxis y = new YAxis();
		chart.setX_axis(x);
		chart.setY_axis(y);
		if (category != null) {
			Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {
					if (!item.getProduct().getCategory().equals(category))
						continue;
					String brand = item.getProduct().getBrand().getName();
					BigDecimal total = map.get(brand);
					if (total == null) {
						map.put(brand, item.getSubtotal());
					} else {
						map.put(brand, total.add(item.getSubtotal()));
					}
				}
			}
			Double[] values = new Double[labels.size()];
			double max = 0;
			for (int i = 0; i < labels.size(); i++) {
				BigDecimal total = map.get(labels.get(i));
				if (total == null)
					total = new BigDecimal(0.00);
				if (max == 0)
					max = total.doubleValue();
				else if (max < total.doubleValue())
					max = total.doubleValue();
				values[i] = total.doubleValue();
			}
			BarChart element = new BarChart();
			x.setLabels(labels);
			y.setMax(max);
			element.addValues(values);
			chart.addElements(element);
		} else {
			for (Order order : orders) {
				for (OrderItem item : order.getItems()) {

					// TODO group by brand,category
					// LinkedHashMap<String,LinkedHashMap<String,Double>>
				}
			}
		}

	}

	public void category() {
		chart = new Chart("品种");
		// 时间区间 默认一年内 柱图
	}

	public void region() {
		chart = new Chart("地区");
		// 时间区间 默认一年内 湖南地级市级别 和 其他(外地和未知地区客户) 柱图 接受可选参数品种缩小范围
	}

	public void month() {
		// 时间区间 默认一年内 按月统计 销量和价格 线图 接受必选参数产品id 可以多个 做成treeTable选择对比
	}

	public void stuff() {
		// 时间区间 默认一年内 按月统计 进货量 和价格 线图 接受必选参数原料id 可以多个 做成treeTable选择对比
	}

}
