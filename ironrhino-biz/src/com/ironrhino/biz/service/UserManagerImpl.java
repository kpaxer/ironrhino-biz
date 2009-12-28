package com.ironrhino.biz.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.core.metadata.CheckCache;
import org.ironrhino.core.metadata.FlushCache;
import org.ironrhino.core.service.BaseManagerImpl;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.ironrhino.biz.Constants;
import com.ironrhino.biz.model.Role;
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
		Collection<Role> roles = new HashSet<Role>();
		List<String> names = new ArrayList<String>();
		for (Role r : user.getRoles())
			names.add(r.getName());
		if (names.size() > 0) {
			final DetachedCriteria dc = DetachedCriteria.forClass(Role.class);
			dc.add(Restrictions.in("name", names));
			Collection<Role> userRoles = (Collection<Role>) execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					return dc.getExecutableCriteria(session).list();
				}
			});
			roles.addAll(userRoles);
			names.clear();
		}

		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		auths.add(new GrantedAuthorityImpl(Constants.ROLE_BUILTIN_ANONYMOUS));
		auths.add(new GrantedAuthorityImpl(Constants.ROLE_BUILTIN_USER));
		for (Role role : roles)
			auths.add(new GrantedAuthorityImpl(role.getName()));
		user.setAuthorities(auths);
	}

}
