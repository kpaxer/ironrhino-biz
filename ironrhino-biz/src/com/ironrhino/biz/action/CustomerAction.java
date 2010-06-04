package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.CustomerManager;

@Authorize(ifAnyGranted = org.ironrhino.security.model.UserRole.ROLE_ADMINISTRATOR)
public class CustomerAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Customer customer;

	private ResultPage<Customer> resultPage;

	private List<String> suggestions;

	private Long regionId;

	@Inject
	private transient CustomerManager customerManager;

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

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public List<String> getSuggestions() {
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
			if (resultPage == null)
				resultPage = new ResultPage<Customer>();
			resultPage.setDetachedCriteria(dc);
			resultPage.addOrder(org.hibernate.criterion.Order.asc("id"));
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
		if (StringUtils.isNumeric(id))
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
			Customer source = customerManager.findByNaturalId(id[0].trim());
			Customer target = customerManager.findByNaturalId(id[1].trim());
			customerManager.merge(source, target);
			addActionMessage(getText("operate.success"));
		}
		return SUCCESS;
	}

	public String suggest() {
		CompassCriteria cc = new CompassCriteria();
		cc.setPageSize(1000);
		cc.setQuery(ServletActionContext.getRequest().getParameter("q"));
		cc.setAliases(new String[] { "customer" });
		CompassSearchResults searchResults = compassSearchService.search(cc);
		if (searchResults.getTotalHits() > 0) {
			suggestions = new ArrayList<String>();
			for (CompassHit ch : searchResults.getHits())
				suggestions.add(((Customer) ch.getData()).getName());
			return "suggest";
		}
		return NONE;
	}

	@JsonConfig(root = "customer")
	public String json() {
		String id = getUid().trim();
		if (StringUtils.isNumeric(id))
			customer = customerManager.get(Long.valueOf(id));
		else if (StringUtils.isNotBlank(id))
			customer = customerManager.findByNaturalId(id);
		return JSON;
	}

}
