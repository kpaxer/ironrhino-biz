package com.ironrhino.biz.service;

import org.ironrhino.core.service.BaseManager;
import org.springframework.security.userdetails.UserDetailsService;

import com.ironrhino.biz.model.User;

public interface UserManager extends BaseManager<User>, UserDetailsService {

	public void save(User user);

	public User getUserByUsername(String username);
}
