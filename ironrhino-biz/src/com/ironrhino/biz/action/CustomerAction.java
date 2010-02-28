package com.ironrhino.biz.action;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.Region;
import org.ironrhino.common.support.RegionTreeControl;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.service.CustomerManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class CustomerAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Customer customer;

	private ResultPage<Customer> resultPage;

	private Long regionId;

	private String q;

	private int pn;

	private int ps = 20;

	private transient CompassSearchResults searchResults;

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

	public CompassSearchResults getSearchResults() {
		return searchResults;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getPn() {
		return pn;
	}

	public void setPn(int pn) {
		this.pn = pn;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public Region getRegionTree() {
		return regionTreeControl.getRegionTree();
	}

	@Override
	public String execute() {
		DetachedCriteria dc = customerManager.detachedCriteria();
		Region region = null;
		if (regionId != null) {
			region = regionTreeControl.getRegionTree().getDescendantOrSelfById(
					regionId);
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
			BeanUtils.copyProperties(temp, customer);
		}
		customerManager.save(customer);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String view() {
		String id = getUid();
		if (StringUtils.isNumeric(id))
			customer = customerManager.get(Long.valueOf(id));
		return VIEW;
	}

	@Override
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

	public String search() {
		if (StringUtils.isNotBlank(q)) {
			String query = q.trim();
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "customer" });
			if (pn > 0)
				cc.setPageNo(pn);
			if (ps > 0)
				cc.setPageSize(ps);
			if (ps > 100)
				cc.setPageSize(100);
			searchResults = compassSearchService.search(cc);
			if (searchResults.getTotalHits() == 1) {
				customer = (Customer) searchResults.getHits()[0].getData();
				targetUrl = "/customer/view/" + customer.getId();
				return REDIRECT;
			}
		}
		return "search";
	}

	public String region() {
		return "region";
	}

}
