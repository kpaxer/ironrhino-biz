package com.ironrhino.biz.action;

import javax.inject.Inject;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.service.EntityManager;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.security.model.User;

import com.ironrhino.biz.model.UserRole;

@AutoConfig
@SuppressWarnings("unchecked")
public class SetupAction extends BaseAction {

	private static final long serialVersionUID = 7038201018786069091L;

	@Inject
	private transient EntityManager entityManager;

	boolean region;

	public void setRegion(boolean region) {
		this.region = region;
	}

	@Override
	public String execute() {
		entityManager.setEntityClass(User.class);
		long cnt = entityManager.countAll();
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
		entityManager.save(admin);
	}

}
