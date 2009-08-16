package com.ironrhino.biz.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.ext.struts.BaseAction;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.ui.savedrequest.SavedRequest;

@AutoConfig
public class LoginAction extends BaseAction {

	private String error;

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@SkipValidation
	public String execute() {
		if (StringUtils.isNotBlank(error))
			addActionError(getText(error));
		HttpServletRequest request = ServletActionContext.getRequest();
		SavedRequest savedRequest = (SavedRequest) request
				.getSession()
				.getAttribute(
						AbstractProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY);
		if (savedRequest != null) {
			targetUrl = savedRequest.getFullRequestUrl();
			if (isUseJson())
				ServletActionContext
						.getRequest()
						.getSession()
						.removeAttribute(
								AbstractProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY);
		}
		if (StringUtils.isBlank(targetUrl))
			targetUrl = request.getHeader("Referer");
		return SUCCESS;
	}
}
