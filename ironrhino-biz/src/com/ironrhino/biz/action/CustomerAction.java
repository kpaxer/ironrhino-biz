package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.service.CustomerManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR + ","
		+ Constants.ROLE_SALESMAN)
public class CustomerAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Customer customer;

	private ResultPage<Customer> resultPage;

	private Long regionId;

	private String keyword;

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Region getRegionTree() {
		return regionTreeControl.getRegionTree();
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
			resultPage.addOrder(Order.asc("id"));
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
			if (regionId != null) {
				Region region = regionTreeControl.getRegionTree()
						.getDescendantOrSelfById(regionId);
				customer.setRegion(region);
			}
		} else {
			Customer temp = customer;
			customer = customerManager.get(temp.getId());
			if (!customer.getName().equals(temp.getName())) {
				if (customerManager.findByNaturalId(true, temp.getName()) != null) {
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
	@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
	public String delete() {
		String[] _id = getId();
		if (_id != null) {
			Long[] id = new Long[_id.length];
			for (int i = 0; i < _id.length; i++)
				id[i] = Long.valueOf(_id[i]);
			DetachedCriteria dc = customerManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Customer> list = customerManager.findListByCriteria(dc);
			if (list.size() > 0) {
				for (Customer customer : list)
					customerManager.delete(customer);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

	public String region() {
		return "region";
	}

	@JsonConfig(root = "customer")
	public String json() {
		String id = getUid();
		if (StringUtils.isNumeric(id)) {
			customer = customerManager.get(Long.valueOf(id));
		} else if (StringUtils.isNotBlank(id)) {
			customer = customerManager.findByNaturalId(true, id);
			if (customer == null) {
				CompassCriteria cc = new CompassCriteria();
				cc.setQuery(id);
				cc.setAliases(new String[] { "customer" });
				cc.setPageNo(1);
				cc.setPageSize(10);
				CompassSearchResults searchResults = compassSearchService
						.search(cc);
				int hits = searchResults.getTotalHits();
				if (hits == 1) {
					customer = (Customer) searchResults.getHits()[0].getData();
				} else if (hits > 1) {
					StringBuilder sb = new StringBuilder();
					for (CompassHit ch : searchResults.getHits())
						sb.append(((Customer) ch.getData()).getName()).append(
								",");
					sb.deleteCharAt(sb.length() - 1);
					customer = new Customer();
					customer.setName(sb.toString());
				}
			}
		}
		if (customer == null)
			customer = new Customer();
		return JSON;
	}

}
