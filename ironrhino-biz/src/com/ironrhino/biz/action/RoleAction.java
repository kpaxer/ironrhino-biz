package com.ironrhino.biz.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Role;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class RoleAction extends BaseAction {

	private static final long serialVersionUID = -3030909718541990696L;

	protected static final Log log = LogFactory.getLog(RoleAction.class);

	private Role role;

	private ResultPage<Role> resultPage;

	private transient BaseManager<Role> baseManager;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public ResultPage<Role> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<Role> resultPage) {
		this.resultPage = resultPage;
	}

	public void setBaseManager(BaseManager<Role> baseManager) {
		this.baseManager = baseManager;
		this.baseManager.setEntityClass(Role.class);
	}

	public String execute() {
		DetachedCriteria dc = baseManager.detachedCriteria();
		if (role != null) {
			if (StringUtils.isNotBlank(role.getName()))
				dc.add(Restrictions.ilike("name", role.getName(),
						MatchMode.ANYWHERE));
		}
		if (resultPage == null)
			resultPage = new ResultPage<Role>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.asc("name"));
		resultPage = baseManager.getResultPage(resultPage);
		return LIST;
	}

	public String input() {
		role = baseManager.get(getUid());
		if (role == null)
			role = new Role();
		return INPUT;
	}

	@Validations(requiredStrings = { @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "role.name", trim = true, key = "validation.required") })
	public String save() {
		if (role.isNew()) {
			role.setName(role.getName().toUpperCase());
			if (role.getName().startsWith("ROLE_BUILTIN_")
					|| baseManager.getByNaturalId("name", role.getName()) != null) {
				addFieldError("role.name", getText("validation.already.exists"));
				return INPUT;
			}
		} else {
			Role temp = role;
			role = baseManager.get(temp.getId());
			if (!temp.getName().equals(role.getName())
					&& baseManager.getByNaturalId("name", temp.getName()) != null) {
				addFieldError("role.name", getText("validation.already.exists"));
				return INPUT;
			}
			BeanUtils.copyProperties(temp, role);
		}
		baseManager.save(role);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	public String delete() {
		String[] id = getId();
		if (id != null) {
			DetachedCriteria dc = baseManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<Role> list = baseManager.getListByCriteria(dc);
			if (list.size() > 0) {
				for (Role role : list)
					baseManager.delete(role);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

}
