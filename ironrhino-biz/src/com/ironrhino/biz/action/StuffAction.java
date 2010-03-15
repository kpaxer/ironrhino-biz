package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.service.StuffManager;
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

	@Inject
	private transient StuffManager stuffManager;

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

	@Override
	public String execute() {
		DetachedCriteria dc = stuffManager.detachedCriteria();
		if (resultPage == null)
			resultPage = new ResultPage<Stuff>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.asc("name"));
		resultPage = stuffManager.findByResultPage(resultPage);
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNumeric(id))
			stuff = (Stuff) stuffManager.get(Long.valueOf(id));
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
			if (stuffManager.findByNaturalId(stuff.getName()) != null) {
				addActionError(getText("validation.already.exists"));
				return INPUT;
			}
		} else {
			Stuff temp = stuff;
			stuff = (Stuff) stuffManager.get(temp.getId());
			if (!stuff.getName().equals(temp.getName()))
				if (stuffManager.findByNaturalId(temp.getName()) != null) {
					addActionError(getText("validation.already.exists"));
					return INPUT;
				}
			BeanUtils.copyProperties(temp, stuff);
		}
		stuffManager.save(stuff);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String delete() {
		String[] _id = getId();
		if (_id != null) {
			Long[] id = new Long[_id.length];
			for (int i = 0; i < _id.length; i++)
				id[i] = Long.valueOf(_id[i]);
			List<Stuff> list;
			if (id.length == 1) {
				list = new ArrayList<Stuff>(1);
				list.add((Stuff) stuffManager.get(id[0]));
			} else {
				DetachedCriteria dc = stuffManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = stuffManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				boolean deletable = true;
				for (Stuff temp : list) {
					if (!stuffManager.canDelete(temp)) {
						addActionError(temp.getName() + "不能删除");
						deletable = false;
						break;
					}
				}
				if (deletable) {
					for (Stuff temp : list)
						stuffManager.delete(temp);
					addActionMessage(getText("delete.success"));
				}
			}
		}
		return SUCCESS;
	}

}
