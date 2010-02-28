package com.ironrhino.biz.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ironrhino.core.metadata.CheckCache;
import org.ironrhino.core.metadata.FlushCache;
import org.ironrhino.core.model.SimpleElement;
import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.User;

@Singleton
@Named("userManager")
public class UserManagerImpl extends BaseManagerImpl<User> implements
		UserManager {

	@Override
	@Transactional
	@FlushCache(key = "user_${args[0].username}")
	public void save(User user) {
		super.save(user);
	}

	@Transactional(readOnly = true)
	@CheckCache(key = "user_${args[0]}")
	public User loadUserByUsername(String username) {
		User user = getUserByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException("No such Username");
		populateAuthorities(user);
		return user;
	}

	public User getUserByUsername(String username) {
		return findByNaturalId(true, "username", username);
	}

	private void populateAuthorities(User user) {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		auths.add(new GrantedAuthorityImpl(Constants.ROLE_BUILTIN_ANONYMOUS));
		auths.add(new GrantedAuthorityImpl(Constants.ROLE_BUILTIN_USER));
		for (SimpleElement sce : user.getRoles())
			auths.add(new GrantedAuthorityImpl(sce.getValue()));
		user.setAuthorities(auths);
	}

}
