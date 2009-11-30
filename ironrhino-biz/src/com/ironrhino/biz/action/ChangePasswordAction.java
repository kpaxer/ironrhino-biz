package com.ironrhino.biz.action;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.AuthzUtils;

import com.ironrhino.biz.model.User;
import com.ironrhino.biz.service.UserManager;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@AutoConfig
public class ChangePasswordAction extends BaseAction {

	private static final long serialVersionUID = 6152147578696153533L;

	protected static Log log = LogFactory.getLog(ChangePasswordAction.class);

	private String currentPassword;

	private String password;

	private String confirmPassword;

	@Inject
	private transient UserManager userManager;

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	@InputConfig
	@Validations(stringLengthFields = { @StringLengthFieldValidator(type = ValidatorType.FIELD, trim = true, minLength = "6", maxLength = "20", fieldName = "password", key = "validation.required") }, expressions = { @ExpressionValidator(expression = "password == confirmPassword", key = "confirmPassword.error") })
	public String execute() {
		User user = AuthzUtils.getUserDetails(User.class);
		if (user == null || !user.isPasswordValid(currentPassword)) {
			addFieldError("currentPassword", getText("currentPassword.error"));
			return INPUT;
		}
		user.setLegiblePassword(password);
		log.info("'" + user.getUsername() + "' changed password");
		userManager.save(user);
		addActionMessage(getText("save.success"));
		return INPUT;
	}
}
