package com.ironrhino.common.action;

import javax.inject.Inject;

import org.apache.struts2.ServletActionContext;
import org.ironrhino.common.support.SettingControl;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.util.AuthzUtils;
import org.ironrhino.security.model.User;
import org.ironrhino.security.model.UserRole;

@AutoConfig
public class IndexAction extends BaseAction {

	private static final long serialVersionUID = 29792886600858873L;

	@Inject
	private SettingControl settingControl;

	@Override
	public String execute() {
		User user = AuthzUtils.getUserDetails(User.class);
		boolean website = true;
		if (user != null
				&& user.getRoles().contains(UserRole.ROLE_ADMINISTRATOR)) {
			website = false;
		}
		String host = ServletActionContext.getRequest().getServerName();
		String websiteDomain = settingControl.getStringValue("website.domain",
				"");
		if (!host.equals(websiteDomain))
			website = false;
		if (!website) {
			targetUrl = "/biz/index";
			return REDIRECT;
		} else {
			return SUCCESS;
		}
	}
}
