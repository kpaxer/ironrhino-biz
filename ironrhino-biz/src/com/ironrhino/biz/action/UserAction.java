package com.ironrhino.biz.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.ResultPage;
import org.ironrhino.common.util.BeanUtils;
import org.ironrhino.core.metadata.Authorize;
import org.ironrhino.core.ext.struts.BaseAction;
import org.ironrhino.core.service.BaseManager;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Role;
import com.ironrhino.biz.model.User;
import com.ironrhino.biz.service.UserManager;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
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

	private List<Role> roleList;

	private ResultPage<User> resultPage;

	private String password;

	private String confirmPassword;

	private transient UserManager userManager;

	private transient BaseManager<Role> baseManager;

	public String[] getRoleId() {
		return roleId;
	}

	public void setRoleId(String[] roleId) {
		this.roleId = roleId;
	}

	public List<Role> getRoleList() {
		return roleList;
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

	public void setBaseManager(BaseManager<Role> baseManager) {
		this.baseManager = baseManager;
		this.baseManager.setEntityClass(Role.class);
	}

	public String execute() {
		DetachedCriteria dc = userManager.detachedCriteria();
		if (resultPage == null)
			resultPage = new ResultPage<User>();
		resultPage.setDetachedCriteria(dc);
		resultPage.addOrder(Order.asc("username"));
		resultPage = userManager.getResultPage(resultPage);
		return LIST;
	}

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
			if (userManager.getByNaturalId("username", user.getUsername()) != null) {
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

	public String save() {
		if (user != null && user.getId() != null) {
			user = userManager.get(user.getId());
			if (user != null) {
				if (StringUtils.isNotBlank(password)
						&& !password.equals("********"))
					user.setLegiblePassword(password);
				userManager.save(user);
				addActionMessage(getText("save.success"));
			}
		}
		return SUCCESS;
	}

	public String delete() {
		String[] id = getId();
		if (id != null) {
			DetachedCriteria dc = userManager.detachedCriteria();
			dc.add(Restrictions.in("id", id));
			List<User> list = userManager.getListByCriteria(dc);
			if (list.size() > 0) {
				for (User user : list)
					userManager.delete(user);
				addActionMessage(getText("delete.success"));
			}
		}
		return SUCCESS;
	}

	public String inputRole() {
		user = userManager.get(getUid());
		if (user == null)
			return ERROR;
		roleId = new String[user.getRoles().size()];
		int i = -1;
		for (Role r : user.getRoles()) {
			i++;
			roleId[i] = r.getId();
		}
		roleList = baseManager.getAll();
		return "role";
	}

	@InputConfig(methodName = "inputRole")
	public String role() {
		user = userManager.get(getUid());
		if (user == null)
			return ERROR;
		user.getRoles().clear();
		userManager.save(user);
		for (String id : roleId) {
			Role r = baseManager.get(id);
			if (r != null)
				user.getRoles().add(r);
		}
		userManager.save(user);
		addActionMessage(getText("save.success"));
		return SUCCESS;
	}
}
