package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.model.Stuff;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class StuffAction extends BaseAction {

	private static final long serialVersionUID = -3091345003472881248L;

	protected static final Log log = LogFactory.getLog(StuffAction.class);

	private String vendorId;

	private Stuff stuff;

	private ResultPage<Stuff> resultPage;

	private transient BaseManager baseManager;

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
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

	@Override
	public String execute() {
		baseManager.setEntityClass(Stuff.class);
		DetachedCriteria dc = baseManager.detachedCriteria();
		if (resultPage == null)
			resultPage = new ResultPage<Stuff>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.asc("name"));
		resultPage = baseManager.findByResultPage(resultPage);
		return LIST;
	}

	@Override
	public String input() {
		baseManager.setEntityClass(Stuff.class);
		stuff = (Stuff) baseManager.get(getUid());
		if (stuff == null) {
			stuff = new Stuff();
		}
		return INPUT;
	}

	@Override
	@InputConfig(methodName = "input")
	@Validations(requiredFields = { @RequiredFieldValidator(type = ValidatorType.FIELD, fieldName = "stuff.name", key = "stuff.name.required") })
	public String save() {
		if (stuff.isNew()) {
			baseManager.setEntityClass(Stuff.class);
			if (baseManager.findByNaturalId(stuff.getName()) != null) {
				addActionError(getText("validation.already.exists"));
				return INPUT;
			}
		} else {
			Stuff temp = stuff;
			baseManager.setEntityClass(Stuff.class);
			stuff = (Stuff) baseManager.get(temp.getId());
			if (!stuff.getName().equals(temp.getName()))
				if (baseManager.findByNaturalId(temp.getName()) != null) {
					addActionError(getText("validation.already.exists"));
					return INPUT;
				}
			BeanUtils.copyProperties(temp, stuff);
		}
		baseManager.save(stuff);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String delete() {
		baseManager.setEntityClass(Stuff.class);
		String[] id = getId();
		if (id != null) {
			List<Stuff> list;
			if (id.length == 1) {
				list = new ArrayList<Stuff>(1);
				list.add((Stuff) baseManager.get(id[0]));
			} else {
				DetachedCriteria dc = baseManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = baseManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				boolean deletable = true;
				for (Stuff temp : list) {
					if (!baseManager.canDelete(temp)) {
						addActionError(temp.getName() + "不能删除");
						deletable = false;
						break;
					}
				}
				if (deletable) {
					for (Stuff temp : list)
						baseManager.delete(temp);
					addActionMessage(getText("delete.success"));
				}
			}
		}
		return SUCCESS;
	}

}
