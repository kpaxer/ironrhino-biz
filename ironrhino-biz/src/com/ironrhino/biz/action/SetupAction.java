package com.ironrhino.biz.action;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.service.BaseManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.security.model.User;

import com.ironrhino.biz.model.UserRole;

@AutoConfig
@SuppressWarnings("unchecked")
public class SetupAction extends BaseAction {

	private static final long serialVersionUID = 7038201018786069091L;

	private transient BaseManager baseManager;

	boolean region;

	public void setRegion(boolean region) {
		this.region = region;
	}

	public void setBaseManager(BaseManager baseManager) {
		this.baseManager = baseManager;
	}

	@Override
	public String execute() {
		baseManager.setEntityClass(User.class);
		long cnt = baseManager.countAll();
		if (cnt == 0) {
			initUser();
		}
		targetUrl = "/";
		return REDIRECT;
	}

	private void initUser() {

		// init user
		User admin = new User();
		admin.setUsername("admin");
		admin.setLegiblePassword("password");
		admin.setName("管理员");
		admin.getRoles().add(UserRole.ROLE_ADMINISTRATOR);
		baseManager.save(admin);
	}

}
