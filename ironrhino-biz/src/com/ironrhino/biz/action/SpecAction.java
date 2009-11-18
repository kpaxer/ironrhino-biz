package com.ironrhino.biz.action;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Spec;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class SpecAction extends BaseAction {

	private static final long serialVersionUID = -5776482217589240704L;

	protected static final Log log = LogFactory.getLog(SpecAction.class);

	private Spec spec;

	private ResultPage<Spec> resultPage;

	private transient BaseManager<Spec> baseManager;

	public Spec getSpec() {
		return spec;
	}

	public void setSpec(Spec spec) {
		this.spec = spec;
	}

	public ResultPage<Spec> findByResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Spec> resultPage) {
		this.resultPage = resultPage;
	}

	public void setBaseManager(BaseManager<Spec> baseManager) {
		this.baseManager = baseManager;
		this.baseManager.setEntityClass(Spec.class);
	}

	public String execute() {
		DetachedCriteria dc = baseManager.detachedCriteria();
		if (resultPage == null)
			resultPage = new ResultPage<Spec>();
		resultPage.setDetachedCriteria(dc);
		resultPage = baseManager.findByResultPage(resultPage);
		return LIST;
	}

	public String input() {
		spec = baseManager.get(getUid());
		if (spec == null)
			spec = new Spec();
		return INPUT;
	}

	public String save() {
		baseManager.save(spec);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	public String delete() {
		String[] id = getId();
		if (id != null) {
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Spec> list = baseManager.findListByCriteria(dc);
			if (list.size() > 0) {
				for (Spec spec : list) 
					// TODO check if can delete
					baseManager.delete(spec);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

}
