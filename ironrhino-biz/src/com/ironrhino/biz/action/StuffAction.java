package com.ironrhino.biz.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.ResultPage;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.model.Spec;
import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.Vendor;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class StuffAction extends BaseAction {

	private static final long serialVersionUID = -3091345003472881248L;

	protected static final Log log = LogFactory.getLog(StuffAction.class);

	private String vendorId;

	private List<Vendor> vendorList;

	private String specId;

	private List<Spec> specList;

	private Stuff stuff;

	private ResultPage<Stuff> resultPage;

	private transient BaseManager baseManager;

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public List<Vendor> getVendorList() {
		return vendorList;
	}

	public List<Spec> getSpecList() {
		return specList;
	}

	public Stuff getStuff() {
		return stuff;
	}

	public void setStuff(Stuff stuff) {
		this.stuff = stuff;
	}

	public ResultPage<Stuff> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Stuff> resultPage) {
		this.resultPage = resultPage;
	}

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;

	}

	// @Authorize(ifAnyGranted = Constants.ROLE_CEO)
	public String execute() {
		baseManager.setEntityClass(Stuff.class);
		DetachedCriteria dc = baseManager.detachedCriteria();
		if (resultPage == null)
			resultPage = new ResultPage<Stuff>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.asc("name"));
		resultPage = baseManager.getResultPage(resultPage);
		return LIST;
	}

	public String input() {
		baseManager.setEntityClass(Vendor.class);
		vendorList = baseManager.getAll();
		baseManager.setEntityClass(Spec.class);
		specList = baseManager.getAll();
		baseManager.setEntityClass(Stuff.class);
		stuff = (Stuff) baseManager.get(getUid());
		if (stuff != null) {
			if (stuff.getVendor() != null)
				vendorId = stuff.getVendor().getId();
			if (stuff.getSpec() != null)
				specId = stuff.getSpec().getId();
		} else {
			stuff = new Stuff();
		}
		return INPUT;
	}

	@InputConfig(methodName = "input")
	@Validations(requiredFields = {
			@RequiredFieldValidator(type = ValidatorType.FIELD, fieldName = "stuff.name", key = "stuff.name.required"),
			@RequiredFieldValidator(type = ValidatorType.FIELD, fieldName = "specId", key = "specId.required") })
	public String save() {
		if (stuff.isNew()) {
			Vendor vendor = null;
			Spec spec = null;
			baseManager.setEntityClass(Spec.class);
			if ((spec = (Spec) baseManager.get(specId)) != null)
				stuff.setSpec(spec);
			baseManager.setEntityClass(Stuff.class);
			if (baseManager.getByNaturalId("name", stuff.getName(), "spec",
					stuff.getSpec()) != null) {
				addActionError(getText("validation.already.exists"));
				return input();
			}
			baseManager.setEntityClass(Vendor.class);
			if (StringUtils.isNotBlank(vendorId)
					&& (vendor = (Vendor) baseManager.get(vendorId)) != null)
				stuff.setVendor(vendor);

		} else {
			Stuff temp = stuff;
			baseManager.setEntityClass(Stuff.class);
			stuff = (Stuff) baseManager.get(temp.getId());
			if (stuff == null)
				return ERROR;
			stuff.setCriticalStock(temp.getCriticalStock());
		}
		baseManager.save(stuff);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	public String delete() {
		String[] id = getId();
		if (id != null) {
			baseManager.setEntityClass(Stuff.class);
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Stuff> list = baseManager.getListByCriteria(dc);
			if (list.size() > 0) {
				for (Stuff stuff : list)
					baseManager.delete(stuff);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

}
