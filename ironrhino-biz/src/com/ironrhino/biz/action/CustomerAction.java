package com.ironrhino.biz.action;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.ResultPage;
import org.ironrhino.core.annotation.Authorize;
import org.ironrhino.core.ext.struts.BaseAction;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.model.Region;
import com.ironrhino.biz.service.CustomerManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class CustomerAction extends BaseAction {

	private Customer customer;

	private ResultPage<Customer> resultPage;

	private Integer regionId;

	private BaseManager<Region> baseManager;

	private CustomerManager customerManager;

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

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public void setBaseManager(BaseManager<Region> baseManager) {
		this.baseManager = baseManager;
		baseManager.setEntityClass(Region.class);
	}

	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}

	public String execute() {
		DetachedCriteria dc = customerManager.detachedCriteria();
		Region region = null;
		if (regionId != null) {
			region = baseManager.get(regionId);
			if (region != null)
				dc.add(Restrictions.eq("region", region));
		}
		if (resultPage == null)
			resultPage = new ResultPage<Customer>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.asc("code"));
		resultPage = customerManager.getResultPage(resultPage);
		return LIST;
	}

	public String input() {
		customer = customerManager.get(getUid());
		if (customer == null)
			customer = new Customer();
		return INPUT;
	}

	public String save() {
		if (customer == null)
			return INPUT;
		if (customer.isNew()) {
			if (customerManager.getByNaturalId("code", customer.getCode()) != null) {
				addFieldError("customer.code", getText("customer.code.exists"));
				return INPUT;
			}
			if (regionId != null) {
				Region region = baseManager.get(regionId);
				customer.setRegion(region);
			}
		} else {
			Customer temp = customer;
			customer = customerManager.get(temp.getId());
			BeanUtils.copyProperties(temp, customer);
		}
		customerManager.save(customer);
		addActionMessage(getText("save.success", "save {0} successfully",
				new String[] { customer.getCode() }));
		return SUCCESS;
	}

	public String view() {
		customer = customerManager.get(getUid());
		return VIEW;
	}

	public String delete() {
		String[] id = getId();
		if (id != null) {
			DetachedCriteria dc = customerManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Customer> list = customerManager.getListByCriteria(dc);
			if (list.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				for (Customer customer : list) {
					customerManager.delete(customer);
					sb.append(customer.getCode() + ",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append(")");
				addActionMessage(getText("delete.success",
						"delete {0} successfully",
						new String[] { sb.toString() }));
			}
		}
		return SUCCESS;
	}

	public String region() {
		customer = customerManager.get(getUid());
		if (customer != null && regionId != null) {
			customer.setRegion(baseManager.get(regionId));
			customerManager.save(customer);
			addActionMessage(getText("change.region.success",
					"change region to {0} successfully",
					new String[] { customer.getRegion().getName() }));
		}
		return "region";
	}

}
