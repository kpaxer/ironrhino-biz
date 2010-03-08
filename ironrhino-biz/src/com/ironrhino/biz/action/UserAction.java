package com.ironrhino.biz.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.BeanUtils;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.User;
import com.ironrhino.biz.service.UserManager;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Authorize(ifAnyGranted = Constants.ROLE_SUPERVISOR)
public class UserAction extends BaseAction {

	private static final long serialVersionUID = -79191921685741502L;

	private User user;

	private String[] roleId;

	private String rolesAsString;

	private ResultPage<User> resultPage;

	private String password;

	private String confirmPassword;

	@Inject
	private transient UserManager userManager;

	public String[] getRoleId() {
		return roleId;
	}

	public void setRoleId(String[] roleId) {
		this.roleId = roleId;
	}

	public String getRolesAsString() {
		return rolesAsString;
	}

	public void setRolesAsString(String rolesAsString) {
		this.rolesAsString = rolesAsString;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ResultPage<User> getResultPage() {
		return resultPage;
	}

	public void setResultPage(ResultPage<User> resultPage) {
		this.resultPage = resultPage;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public String execute() {
		DetachedCriteria dc = userManager.detachedCriteria();
		if (resultPage == null)
			resultPage = new ResultPage<User>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.asc("username"));
		resultPage = userManager.findByResultPage(resultPage);
		return LIST;
	}

	@Override
	public String input() {
		user = userManager.get(getUid());
		if (user == null)
			user = new User();
		return INPUT;
	}

	@Validations(requiredStrings = {
			@RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "user.username", trim = true, key = "username.required"),
			@RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "user.name", trim = true, key = "user.name.required") }, regexFields = { @RegexFieldValidator(type = ValidatorType.FIELD, fieldName = "user.username", expression = "^\\w{2,20}$", key = "user.username.invalid") }, fieldExpressions = { @FieldExpressionValidator(expression = "password == confirmPassword", fieldName = "confirmPassword", key = "confirmPassword.error") })
	public String save2() {
		if (user.isNew()) {
			user.setUsername(user.getUsername().toLowerCase());
			if (userManager.findByNaturalId("username", user.getUsername()) != null) {
				addFieldError("user.username",
						getText("validation.already.exists"));
				return INPUT;
			}
			user.setLegiblePassword(password);
		} else {
			User temp = user;
			user = userManager.get(temp.getId());
			BeanUtils.copyProperties(temp, user);
		}
		userManager.save(user);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}

	@Override
	public String save() {
		if (user != null && user.getId() != null) {
			user = userManager.get(user.getId());
			if (user != null) {
				if (StringUtils.isNotBlank(password)
						&& !password.equals("********"))
					user.setLegiblePassword(password);
				if (rolesAsString != null)
					user.setRolesAsString(rolesAsString);
				userManager.save(user);
				addActionMessage(getText("save.success"));
			}
		}
		return SUCCESS;
	}

	@Override
	public String delete() {
		String[] id = getId();
		if (id != null) {
			List<User> list;
			if (id.length == 1) {
				list = new ArrayList<User>(1);
				list.add(userManager.get(id[0]));
			} else {
				DetachedCriteria dc = userManager.detachedCriteria();
				dc.add(Restrictions.in("id", id));
				list = userManager.findListByCriteria(dc);
			}
			if (list.size() > 0) {
				for (User user : list)
					userManager.delete(user);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

}
