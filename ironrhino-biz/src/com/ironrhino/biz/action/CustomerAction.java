package com.ironrhino.biz.action;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.Region;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.model.LabelValue;
import org.ironrhino.core.model.Persistable;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.SearchService.Mapper;
import org.ironrhino.core.search.elasticsearch.ElasticSearchCriteria;
import org.ironrhino.core.search.elasticsearch.ElasticSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.ironrhino.core.util.DateUtils;
import org.ironrhino.core.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Category;
import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.Station;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.CustomerManager;
import com.ironrhino.biz.service.OrderManager;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + ","
		+ UserRole.ROLE_CUSTOMERMANAGER)
public class CustomerAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Customer customer;

	private ResultPage<Customer> resultPage;

	private List<LabelValue> suggestions;

	private Long regionId;

	private int threshold;

	@Inject
	private transient CustomerManager customerManager;

	@Inject
	private transient OrderManager orderManager;

	@Inject
	private transient RegionTreeControl regionTreeControl;

	@Autowired(required = false)
	private transient ElasticSearchService elasticSearchService;

	public Class<? extends Persistable<?>> getEntityClass() {
		return Customer.class;
	}

	public ResultPage<Customer> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Customer> resultPage) {
		this.resultPage = resultPage;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public List<LabelValue> getSuggestions() {
		return suggestions;
	}

	@Override
	public String execute() {
		if (StringUtils.isBlank(keyword) || elasticSearchService == null) {
			DetachedCriteria dc = customerManager.detachedCriteria();
			CriterionUtils.filter(dc, getEntityClass());
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "name",
						"phone", "mobile", "address"));
			Region region = null;
			if (regionId != null) {
				region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(regionId);
				if (region != null && !region.isRoot()) {
					if (region.isLeaf()) {
						dc.add(Restrictions.eq("region", region));
					} else {
						dc.add(Restrictions.in("region",
								region.getDescendantsAndSelf()));
					}
				}
			}
			if (threshold > 0) {
				dc.add(Restrictions.le("activeDate", DateUtils.addDays(
						DateUtils.beginOfDay(new Date()), -threshold)));
				dc.addOrder(org.hibernate.criterion.Order.asc("activeDate"));
			}
			dc.addOrder(org.hibernate.criterion.Order.asc("id"));
			if (resultPage == null)
				resultPage = new ResultPage<Customer>();
			resultPage.setCriteria(dc);
			resultPage = customerManager.findByResultPage(resultPage);
			for (Customer c : resultPage.getResult())
				if (c.getRegion() != null)
					c.setRegion(regionTreeControl.getRegionTree()
							.getDescendantOrSelfById(c.getRegion().getId()));
		} else {
			String query = keyword.trim();
			ElasticSearchCriteria criteria = new ElasticSearchCriteria();
			criteria.setQuery(query);
			criteria.setTypes(new String[] { "customer" });
			if (resultPage == null)
				resultPage = new ResultPage<Customer>();
			resultPage.setCriteria(criteria);
			resultPage = elasticSearchService.search(resultPage,
					new Mapper<Customer>() {
						@Override
						public Customer map(Customer source) {
							Customer c = source;
							// c = customerManager.get(c.getId());
							if (c.getRegion() != null)
								c.setRegion(regionTreeControl.getRegionTree()
										.getDescendantOrSelfById(
												c.getRegion().getId()));
							return c;
						}
					});
		}
		return LIST;
	}

	public String vcard() throws Exception {
		if (resultPage == null)
			resultPage = new ResultPage<Customer>();
		resultPage.setPageNo(1);
		ResultPage.MAX_PAGESIZE.set(10000);
		resultPage.setPageSize(ResultPage.MAX_PAGESIZE.get());
		String filename = "customers.vcf";
		String[] id = getId();
		if (id != null) {
			List<Customer> result = new ArrayList<Customer>();
			resultPage.setResult(result);
			for (String uid : id)
				result.add(customerManager.get(Long.valueOf(uid)));
		} else {
			execute();
			if (StringUtils.isNotBlank(keyword)) {
				filename = URLEncoder.encode(keyword, "utf-8") + ".vcf";
			}
		}
		if (resultPage.getResult().size() == 0)
			return NONE;
		ServletActionContext.getResponse().setContentType("text/x-vcard");
		ServletActionContext.getResponse().setHeader("Content-Disposition",
				"attachment; filename=\"" + filename + "\"");
		return "vcard";
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNotBlank(id))
			if (StringUtils.isNumeric(id))
				customer = customerManager.get(Long.valueOf(id));
			else
				customer = customerManager.findByNaturalId(id);
		if (customer == null)
			customer = new Customer();
		else {
			Region region = customer.getRegion();
			if (region != null) {
				region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(region.getId());
				if (region != null) {
					regionId = region.getId();
					customer.setRegion(region);
				}
			}
		}
		return INPUT;
	}

	@Override
	@Validations(requiredStrings = { @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "customer.name", trim = true, key = "validation.required") })
	public String save() {
		if (!makeEntityValid())
			return INPUT;
		customerManager.save(customer);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	public String checkavailable() {
		return makeEntityValid() ? NONE : INPUT;
	}

	private boolean makeEntityValid() {
		if (customer == null) {
			addActionError(getText("access.denied"));
			return false;
		}
		if (customer.isNew()) {
			if (StringUtils.isNotBlank(customer.getName())) {
				if (customerManager.findByNaturalId(customer.getName()) != null) {
					addFieldError("customer.name",
							getText("validation.already.exists"));
					return false;
				}
			}
			if (regionId != null) {
				Region region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(regionId);
				customer.setRegion(region);
			}
		} else {
			Customer temp = customer;
			customer = customerManager.get(temp.getId());
			if (StringUtils.isNotBlank(temp.getName())
					&& !customer.getName().equals(temp.getName())) {
				if (customerManager.findByNaturalId(temp.getName()) != null) {
					addFieldError("customer.name",
							getText("validation.already.exists"));
					return false;
				}
			}
			if (regionId != null) {
				Region r = customer.getRegion();
				if (r == null || !r.getId().equals(regionId)) {
					Region region = regionTreeControl.getRegionTree()
							.getDescendantOrSelfById(regionId);
					customer.setRegion(region);
				}
			} else if (ServletActionContext.getRequest().getParameter(
					"regionId") != null)
				customer.setRegion(null);
			if (temp.getAddress() == null)
				temp.setAddress(customer.getAddress());
			if (temp.getMemo() == null)
				temp.setMemo(customer.getMemo());
			BeanUtils.copyProperties(temp, customer);
			customerManager.evict(customer);
		}
		return true;
	}

	@Override
	public String view() {
		String id = getUid();
		if (StringUtils.isNumeric(id)) {
			customer = customerManager.get(Long.valueOf(id));
			if (customer.getRegion() != null)
				customer.setRegion(regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(customer.getRegion().getId()));
		} else {
			return ACCESSDENIED;
		}
		return VIEW;
	}

	@Override
	public String delete() {
		String[] id = getId();
		if (id != null) {
			customerManager.delete((Serializable[]) id);
			addActionMessage(getText("delete.success"));
		}
		return SUCCESS;
	}

	public String merge() {
		String[] id = getId();
		if (id != null && id.length == 2) {
			Customer source;
			if (StringUtils.isNumeric(id[0])) {
				source = customerManager.get(Long.valueOf(id[0]));
			} else {
				source = customerManager.findByNaturalId(id[0].trim());
			}
			if (source == null) {
				addActionError("被合并的客户不能为空");
				return SUCCESS;
			}
			Customer target;
			if (StringUtils.isNumeric(id[1])) {
				target = customerManager.get(Long.valueOf(id[1]));
			} else {
				target = customerManager.findByNaturalId(id[1].trim());
			}
			if (target == null) {
				addActionError("客户不能为空");
				return SUCCESS;
			}
			customerManager.merge(source, target);
			addActionMessage(getText("operate.success"));
		}
		return SUCCESS;
	}

	@JsonConfig(root = "suggestions")
	public String suggest() {
		if (elasticSearchService == null)
			return NONE;
		if (StringUtils.isBlank(keyword))
			return NONE;
		ElasticSearchCriteria cc = new ElasticSearchCriteria();
		cc.setQuery(keyword);
		cc.setTypes(new String[] { "customer" });
		List<Customer> list = elasticSearchService.search(cc);
		if (list.size() > 0) {
			suggestions = new ArrayList<LabelValue>(list.size());
			for (Customer c : list) {
				if (c.getRegion() != null)
					c.setRegion(regionTreeControl.getRegionTree()
							.getDescendantOrSelfById(c.getRegion().getId()));
				LabelValue lv = new LabelValue();
				lv.setValue(c.getName());
				String address = c.getFullAddress();
				if (StringUtils.isNotBlank(address))
					lv.setLabel(new StringBuilder(c.getName()).append("(")
							.append(c.getFullAddress()).append(")").toString());
				else
					lv.setLabel(c.getName());
				suggestions.add(lv);
			}
		}
		return JSON;
	}

	@JsonConfig(root = "customer")
	public String json() {
		String id = getUid();
		if (id != null)
			id = id.trim();
		if (StringUtils.isNumeric(id))
			customer = customerManager.get(Long.valueOf(id));
		else if (StringUtils.isNotBlank(id))
			customer = customerManager.findByNaturalId(id);
		if (customer != null) {
			DetachedCriteria dc = orderManager.detachedCriteria();
			dc.add(Restrictions.eq("customer", customer));
			dc.addOrder(org.hibernate.criterion.Order.desc("orderDate"));
			dc.addOrder(org.hibernate.criterion.Order.desc("code"));
			Order lastOrder = orderManager.findByCriteria(dc);
			Map<String, String> extra = new HashMap<String, String>();
			if (lastOrder != null) {
				Employee salesman = lastOrder.getSalesman();
				if (salesman != null)
					extra.put("salesman", String.valueOf(salesman.getId()));
				Station station = lastOrder.getStation();
				if (station != null)
					extra.put("station", String.valueOf(station.getId()));
			}
			customer.setMemo(JsonUtils.toJson(extra));
		}
		return JSON;
	}

	@JsonConfig(root = "suggestions")
	public String tag() {
		if (elasticSearchService == null)
			return NONE;
		ElasticSearchCriteria criteria = new ElasticSearchCriteria();
		if (StringUtils.isNotBlank(keyword))
			criteria.setQuery("*" + keyword + "*");
		else
			criteria.setQuery("*");
		criteria.setTypes(new String[] { "category" });
		List<Category> list = elasticSearchService.search(criteria);
		if (list.size() > 0) {
			suggestions = new ArrayList<LabelValue>(list.size());
			for (Category cat : list) {
				LabelValue lv = new LabelValue();
				lv.setValue(cat.getName());
				suggestions.add(lv);
			}
		}
		return JSON;
	}

}
