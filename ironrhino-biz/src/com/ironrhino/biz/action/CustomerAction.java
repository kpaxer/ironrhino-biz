package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.Region;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.metadata.JsonConfig;
import org.ironrhino.core.model.LabelValue;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.ironrhino.core.util.DateUtils;
import org.ironrhino.core.util.JsonUtils;

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Employee;
import com.ironrhino.biz.model.Order;
import com.ironrhino.biz.model.Station;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.CustomerManager;
import com.ironrhino.biz.service.OrderManager;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR)
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

	@Inject
	private transient CompassSearchService compassSearchService;

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
		if (StringUtils.isBlank(keyword)) {
			DetachedCriteria dc = customerManager.detachedCriteria();
			Region region = null;
			if (regionId != null) {
				region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(regionId);
				if (region != null && !region.isRoot()) {
					if (region.isLeaf()) {
						dc.add(Restrictions.eq("region", region));
					} else {
						dc.add(Restrictions.in("region", region
								.getDescendantsAndSelf()));
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
			resultPage.setDetachedCriteria(dc);
			resultPage = customerManager.findByResultPage(resultPage);
			for (Customer c : resultPage.getResult())
				if (c.getRegion() != null)
					c.setRegion(regionTreeControl.getRegionTree()
							.getDescendantOrSelfById(c.getRegion().getId()));
		} else {
			String query = keyword.trim();
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "customer" });
			if (resultPage == null)
				resultPage = new ResultPage<Customer>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			CompassSearchResults searchResults = compassSearchService
					.search(cc);
			int totalHits = searchResults.getTotalHits();
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Customer> list = new ArrayList<Customer>(hits.length);
				for (CompassHit ch : searchResults.getHits()) {
					Customer c = (Customer) ch.getData();
					c = customerManager.get(c.getId());
					if (c != null) {
						if (c.getRegion() != null)
							c.setRegion(regionTreeControl.getRegionTree()
									.getDescendantOrSelfById(
											c.getRegion().getId()));
						list.add(c);
					} else {
						totalHits--;
					}
				}
				resultPage.setResult(list);
			} else {
				resultPage.setResult(Collections.EMPTY_LIST);
			}
			resultPage.setTotalRecord(totalHits);
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (org.ironrhino.core.util.StringUtils.isNumericOnly(id))
			customer = customerManager.get(Long.valueOf(id));
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
	public String save() {
		if (customer == null)
			return INPUT;
		if (customer.isNew()) {
			if (customerManager.findByNaturalId(customer.getName()) != null) {
				addFieldError("customer.name",
						getText("validation.already.exists"));
				return INPUT;
			}
			if (regionId != null) {
				Region region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(regionId);
				customer.setRegion(region);
			}
		} else {
			Customer temp = customer;
			customer = customerManager.get(temp.getId());
			if (!customer.getName().equals(temp.getName())) {
				if (customerManager.findByNaturalId(temp.getName()) != null) {
					addFieldError("customer.name",
							getText("validation.already.exists"));
					return INPUT;
				}
			}
			if (regionId != null) {
				Region r = customer.getRegion();
				if (r == null || !r.getId().equals(regionId)) {
					Region region = regionTreeControl.getRegionTree()
							.getDescendantOrSelfById(regionId);
					customer.setRegion(region);
				}
			}
			if (temp.getAddress() == null)
				temp.setAddress(customer.getAddress());
			if (temp.getMemo() == null)
				temp.setMemo(customer.getMemo());
			BeanUtils.copyProperties(temp, customer);
		}
		customerManager.save(customer);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String view() {
		String id = getUid();
		if (org.ironrhino.core.util.StringUtils.isNumericOnly(id)) {
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
		String[] _id = getId();
		if (_id != null) {
			Long[] id = new Long[_id.length];
			for (int i = 0; i < _id.length; i++)
				id[i] = Long.valueOf(_id[i]);
			List<Customer> list;
			if (id.length == 1) {
				list = new ArrayList<Customer>(1);
				list.add(customerManager.get(id[0]));
			} else {
				DetachedCriteria dc = customerManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = customerManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				boolean deletable = true;
				for (Customer c : list) {
					if (!customerManager.canDelete(c)) {
						deletable = false;
						addActionError(c.getName() + "有订单,不能删除只能合并到其他客户");
						break;
					}
				}
				if (deletable) {
					for (Customer customer : list)
						customerManager.delete(customer);
					addActionMessage(getText("delete.success"));
				}
			}
		}
		return SUCCESS;
	}

	public String merge() {
		String[] id = getId();
		if (id != null && id.length == 2) {
			Customer source ;
			if(org.ironrhino.core.util.StringUtils.isNumericOnly(id[0])){
				source = customerManager.get(Long.valueOf(id[0]));
			}else{
				source = customerManager.findByNaturalId(id[0].trim());		
			}
			if(source == null){
				addActionError("被合并的客户不能为空");
				return SUCCESS;
			}
			Customer target;
			if(org.ironrhino.core.util.StringUtils.isNumericOnly(id[1])){
				target = customerManager.get(Long.valueOf(id[1]));
			}else{
				target = customerManager.findByNaturalId(id[1].trim());		
			}
			if(target == null){
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
		keyword = ServletActionContext.getRequest().getParameter("term");
		if (StringUtils.isBlank(keyword))
			return NONE;
		CompassCriteria cc = new CompassCriteria();
		cc.setPageSize(1000);
		cc.setQuery(keyword);
		cc.setAliases(new String[] { "customer" });
		CompassSearchResults searchResults = compassSearchService.search(cc);
		if (searchResults.getTotalHits() > 0) {
			suggestions = new ArrayList<LabelValue>(searchResults
					.getTotalHits());
			for (CompassHit ch : searchResults.getHits()) {
				Customer c = (Customer) ch.getData();
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
		String id = getUid().trim();
		if (org.ironrhino.core.util.StringUtils.isNumericOnly(id))
			customer = customerManager.get(Long.valueOf(id));
		else if (StringUtils.isNotBlank(id))
			customer = customerManager.findByNaturalId(id);
		if (customer != null) {
			DetachedCriteria dc = orderManager.detachedCriteria();
			dc.add(Restrictions.eq("customer", customer));
			dc.addOrder(org.hibernate.criterion.Order.desc("orderDate"));
			dc.addOrder(org.hibernate.criterion.Order.desc("code"));
			Order lastOrder = orderManager.findByCriteria(dc);
			if (lastOrder != null) {
				Map<String, String> map = new HashMap<String, String>();
				Employee salesman = lastOrder.getSalesman();
				if (salesman != null)
					map.put("salesman", String.valueOf(salesman.getId()));
				Station station = lastOrder.getStation();
				if (station != null)
					map.put("station", String.valueOf(station.getId()));
				if (!map.isEmpty())
					customer.setMemo(JsonUtils.toJson(map));
			}
		}
		return JSON;
	}

}
