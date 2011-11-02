package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.compass.core.CompassHit;
import org.compass.core.support.search.CompassSearchResults;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.CompassCriteria;
import org.ironrhino.core.search.CompassSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.StuffManager;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR)
public class StuffAction extends BaseAction {

	private static final long serialVersionUID = -3091345003472881248L;

	private String vendorId;

	private Stuff stuff;

	private ResultPage<Stuff> resultPage;

	@Inject
	private transient StuffManager stuffManager;

	@Autowired(required = false)
	private transient CompassSearchService compassSearchService;

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
		if (StringUtils.isBlank(keyword) || compassSearchService == null) {
			DetachedCriteria dc = stuffManager.detachedCriteria();
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "name"));
			dc.addOrder(Order.asc("displayOrder"));
			if (resultPage == null)
				resultPage = new ResultPage<Stuff>();
			resultPage.setDetachedCriteria(dc);
			resultPage = stuffManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			CompassCriteria cc = new CompassCriteria();
			cc.setQuery(query);
			cc.setAliases(new String[] { "stuff" });
			cc.addSort("displayOrder", "INT", false);
			if (resultPage == null)
				resultPage = new ResultPage<Stuff>();
			cc.setPageNo(resultPage.getPageNo());
			cc.setPageSize(resultPage.getPageSize());
			CompassSearchResults searchResults = compassSearchService
					.search(cc);
			int totalHits = searchResults.getTotalHits();
			CompassHit[] hits = searchResults.getHits();
			if (hits != null) {
				List<Stuff> list = new ArrayList<Stuff>(hits.length);
				for (CompassHit ch : searchResults.getHits()) {
					Stuff s = (Stuff) ch.getData();
					list.add(s);
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
			stuff = stuffManager.get(Long.valueOf(id));
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
			stuff = stuffManager.get(temp.getId());
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
				list.add(stuffManager.get(id[0]));
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
