package com.ironrhino.common.action;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.AuthzUtils;
import org.ironrhino.security.model.User;
import org.ironrhino.security.model.UserRole;

@AutoConfig
public class IndexAction extends BaseAction {

	private static final long serialVersionUID = 29792886600858873L;

	@Override
	public String execute() {
		User user = AuthzUtils.getUserDetails(User.class);
		if (user != null
				&& user.getRoles().contains(UserRole.ROLE_ADMINISTRATOR)) {
			targetUrl = "/biz/index";
			return REDIRECT;
		}
		return SUCCESS;
	}

}
