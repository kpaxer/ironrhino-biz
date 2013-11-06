package com.ironrhino.biz.action;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.ironrhino.core.hibernate.CriteriaState;
import org.ironrhino.core.hibernate.CriterionUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.Persistable;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.elasticsearch.ElasticSearchCriteria;
import org.ironrhino.core.search.elasticsearch.ElasticSearchService;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.UserRole;
import com.ironrhino.biz.service.StuffManager;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Authorize(ifAnyGranted = UserRole.ROLE_ADMINISTRATOR + ","
		+ UserRole.ROLE_WAREHOUSEMAN)
public class StuffAction extends BaseAction {

	private static final long serialVersionUID = -3091345003472881248L;

	private String vendorId;

	private Stuff stuff;

	private ResultPage<Stuff> resultPage;

	@Autowired
	private transient StuffManager stuffManager;

	@Autowired(required = false)
	private transient ElasticSearchService<Stuff> elasticSearchService;

	public Class<? extends Persistable<?>> getEntityClass() {
		return Stuff.class;
	}

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
		if (StringUtils.isBlank(keyword) || elasticSearchService == null) {
			DetachedCriteria dc = stuffManager.detachedCriteria();
			CriteriaState criteriaState = CriterionUtils.filter(dc,
					getEntityClass());
			if (StringUtils.isNotBlank(keyword))
				dc.add(CriterionUtils.like(keyword, MatchMode.ANYWHERE, "name"));
			dc.addOrder(Order.asc("displayOrder"));
			if (resultPage == null)
				resultPage = new ResultPage<Stuff>();
			resultPage.setCriteria(dc);
			if (criteriaState.getOrderings().isEmpty())
				dc.addOrder(Order.asc("name"));
			resultPage = stuffManager.findByResultPage(resultPage);
		} else {
			String query = keyword.trim();
			ElasticSearchCriteria criteria = new ElasticSearchCriteria();
			criteria.setQuery(query);
			criteria.setTypes(new String[] { "stuff" });
			criteria.addSort("displayOrder", false);
			if (resultPage == null)
				resultPage = new ResultPage<Stuff>();
			resultPage.setCriteria(criteria);
			resultPage = elasticSearchService.search(resultPage);
		}
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNumeric(id))
			stuff = stuffManager.get(Long.valueOf(id));
		if (stuff == null) {
			stuff = new Stuff();
		}
		return INPUT;
	}

	@Override
	@InputConfig(methodName = "input")
	@Validations(requiredStrings = { @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "stuff.name", trim = true, key = "stuff.name.required") })
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
		String[] id = getId();
		if (id != null) {
			stuffManager.delete((Serializable[]) id);
			addActionMessage(getText("delete.success"));
		}
		return SUCCESS;
	}

}
