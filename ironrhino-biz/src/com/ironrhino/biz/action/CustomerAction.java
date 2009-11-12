package com.ironrhino.biz.action;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.Region;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Customer;
import com.ironrhino.biz.service.CustomerManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class CustomerAction extends BaseAction {

	private static final long serialVersionUID = 4331302727890834065L;

	private Customer customer;

	private ResultPage<Customer> resultPage;

	private Integer regionId;

	private transient BaseManager<Region> baseManager;

	@Autowired
	private transient CustomerManager customerManager;

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
				addFieldError("customer.code",
						getText("validation.already.exists"));
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
		addActionMessage(getText("save.success"));
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
				for (Customer customer : list)
					customerManager.delete(customer);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

	public String region() {
		customer = customerManager.get(getUid());
		if (customer != null && regionId != null) {
			customer.setRegion(baseManager.get(regionId));
			customerManager.save(customer);
			addActionMessage(getText("operation.success"));
		}
		return "region";
	}

}
