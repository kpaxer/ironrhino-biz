package com.ironrhino.biz.action;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Spec;
import com.ironrhino.biz.service.ProductManager;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class SpecAction extends BaseAction {

	private static final long serialVersionUID = -5776482217589240704L;

	protected static final Log log = LogFactory.getLog(SpecAction.class);

	private Spec spec;

	private ResultPage<Spec> resultPage;

	private transient BaseManager<Spec> baseManager;

	@Inject
	private transient ProductManager productManager;

	public Spec getSpec() {
		return spec;
	}

	public void setSpec(Spec spec) {
		this.spec = spec;
	}

	public ResultPage<Spec> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Spec> resultPage) {
		this.resultPage = resultPage;
	}

	public void setBaseManager(BaseManager<Spec> baseManager) {
		this.baseManager = baseManager;
		this.baseManager.setEntityClass(Spec.class);
	}

	@Override
	public String execute() {
		DetachedCriteria dc = baseManager.detachedCriteria();
		if (resultPage == null)
			resultPage = new ResultPage<Spec>();
		resultPage.setDetachedCriteria(dc);
		dc.addOrder(Order.asc("displayOrder"));
		resultPage = baseManager.findByResultPage(resultPage);
		return LIST;
	}

	@Override
	public String input() {
		String id = getUid();
		if (StringUtils.isNumeric(id))
			spec = baseManager.get(Long.valueOf(id));
		if (spec == null)
			spec = new Spec();
		return INPUT;
	}

	@Override
	public String save() {
		if (spec.getBasicQuantity() == null) {
			Spec temp = spec;
			spec = baseManager.get(temp.getId());
			spec.setDisplayOrder(temp.getDisplayOrder());
		}
		baseManager.save(spec);
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
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Spec> list = baseManager.findListByCriteria(dc);
			if (list.size() > 0) {
				boolean deletable = true;
				for (Spec spec : list) {
					dc = productManager.detachedCriteria();
					dc.createAlias("spec", "s").add(
							Restrictions.eq("s.id", spec.getId()));
					int count = productManager.countByCriteria(dc);
					if (count > 0) {
						deletable = false;
						addActionError(spec.getName() + "下面有产品,不能删除");
					}
				}
				if (deletable) {
					for (Spec spec : list)
						baseManager.delete(spec);
					addActionMessage(getText("delete.success"));
				}
			}
		}
		return SUCCESS;
	}

}
