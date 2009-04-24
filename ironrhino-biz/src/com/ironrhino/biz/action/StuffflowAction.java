package com.ironrhino.biz.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.QueryForm;
import org.ironrhino.common.model.ResultPage;
import org.ironrhino.common.model.Status;
import org.ironrhino.common.util.AuthzUtils;
import org.ironrhino.core.annotation.Redirect;
import org.ironrhino.core.ext.struts.BaseAction;
import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.model.Stuff;
import com.ironrhino.biz.model.Stuffflow;
import com.ironrhino.biz.model.User;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class StuffflowAction extends BaseAction {

	protected static final Log log = LogFactory.getLog(StuffflowAction.class);

	private String stuffId;

	private List<Stuff> stuffList;

	private Stuffflow stuffflow;

	private QueryForm queryForm;

	private ResultPage<Stuffflow> resultPage;

	private BaseManager baseManager;

	public QueryForm getQueryForm() {
		return queryForm;
	}

	public void setQueryForm(QueryForm queryForm) {
		this.queryForm = queryForm;
	}

	public String getStuffId() {
		return stuffId;
	}

	public void setStuffId(String stuffId) {
		this.stuffId = stuffId;
	}

	public List<Stuff> getStuffList() {
		return stuffList;
	}

	public Stuffflow getStuffflow() {
		return stuffflow;
	}

	public void setStuffflow(Stuffflow stuffflow) {
		this.stuffflow = stuffflow;
	}

	public ResultPage<Stuffflow> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Stuffflow> resultPage) {
		this.resultPage = resultPage;
	}

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;

	}

	// @Authorize(ifAnyGranted = Constants.ROLE_CEO)
	public String execute() {
		baseManager.setEntityClass(Stuffflow.class);
		DetachedCriteria dc = baseManager.detachedCriteria();
		dc.add(Restrictions.eq("status", Status.REQUESTED));
		if (resultPage == null)
			resultPage = new ResultPage<Stuffflow>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.desc("requestDate"));
		resultPage = baseManager.getResultPage(resultPage);
		return LIST;
	}

	public String inputIn() {
		baseManager.setEntityClass(Stuff.class);
		stuffList = baseManager.getAll();
		return "in";
	}

	@Redirect
	@InputConfig(methodName = "inputIn")
	@Validations(requiredFields = { @RequiredFieldValidator(type = ValidatorType.FIELD, fieldName = "stuffflow.quantity", key = "stuffflow.quantity.required") }, intRangeFields = { @IntRangeFieldValidator(type = ValidatorType.FIELD, fieldName = "stuffflow.quantity", key = "integer.required", min = "1") })
	// @Authorize(ifAnyGranted = Constants.ROLE_WAREHOUSEMAN)
	public String in() {
		baseManager.setEntityClass(Stuff.class);
		Stuff stuff;
		if (StringUtils.isBlank(stuffId)
				|| (stuff = (Stuff) baseManager.get(stuffId)) == null) {
			addFieldError("stuffId", getText("stuff.not.selected"));
			return inputIn();
		}
		stuffflow.setStuff(stuff);
		stuffflow.setRequestUser(AuthzUtils.getUserDetails(User.class));
		baseManager.save(stuffflow);
		addActionMessage(getText("save.success"));
		targetUrl = "/";
		return REDIRECT;
	}

	public String inputOut() {
		baseManager.setEntityClass(Stuff.class);
		stuffList = baseManager.getAll();
		return "out";
	}

	@Redirect
	@InputConfig(methodName = "inputOut")
	@Validations(requiredFields = { @RequiredFieldValidator(type = ValidatorType.FIELD, fieldName = "stuffflow.quantity", key = "stuffflow.quantity.required") }, intRangeFields = { @IntRangeFieldValidator(type = ValidatorType.FIELD, fieldName = "stuffflow.quantity", key = "integer.required", min = "1") })
	// @Authorize(ifAnyGranted = Constants.ROLE_WAREHOUSEMAN)
	public String out() {
		baseManager.setEntityClass(Stuff.class);
		Stuff stuff;
		if (StringUtils.isBlank(stuffId)
				|| (stuff = (Stuff) baseManager.get(stuffId)) == null) {
			addFieldError("stuffId", getText("stuff.not.selected"));
			return inputOut();
		}
		stuffflow.setQuantity(-stuffflow.getQuantity());
		if (stuffflow.getQuantity() + stuff.getStock() < 0) {
			addActionError(getText("overflow"));
			return "out";
		}
		stuffflow.setStuff(stuff);
		stuffflow.setRequestUser(AuthzUtils.getUserDetails(User.class));
		stuffflow.setRequestDate(new Date());
		baseManager.save(stuffflow);
		addActionMessage(getText("save.success"));
		targetUrl = "/";
		return REDIRECT;
	}

	public String confirm() {
		baseManager.setEntityClass(Stuffflow.class);
		stuffflow = (Stuffflow) baseManager.get(getUid());
		if (stuffflow == null)
			return ERROR;
		Stuff stuff = stuffflow.getStuff();
		if (stuffflow.getQuantity() + stuff.getStock() < 0) {
			addActionError(getText("overflow"));
			return ERROR;
		}
		stuff.setStock(stuffflow.getQuantity() + stuff.getStock());
		stuffflow.setStatus(Status.CONFIRMED);
		stuffflow.setAuditDate(new Date());
		stuffflow.setAuditUser(AuthzUtils.getUserDetails(User.class));
		baseManager.save(stuffflow);
		return SUCCESS;
	}

	public String cancel() {
		baseManager.setEntityClass(Stuffflow.class);
		stuffflow = (Stuffflow) baseManager.get(getUid());
		if (stuffflow == null)
			return ERROR;
		stuffflow.setStatus(Status.CANCELLED);
		stuffflow.setAuditDate(new Date());
		stuffflow.setAuditUser(AuthzUtils.getUserDetails(User.class));
		baseManager.save(stuffflow);
		return SUCCESS;
	}

	public String history() {
		baseManager.setEntityClass(Stuffflow.class);
		DetachedCriteria dc = baseManager.detachedCriteria();
		if (queryForm != null)
			queryForm.fill(dc, "requestDate");
		if (resultPage == null)
			resultPage = new ResultPage<Stuffflow>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.desc("requestDate"));
		resultPage = baseManager.getResultPage(resultPage);
		return "history";
	}

	public String report() {
		return SUCCESS;
	}

}
