package com.ironrhino.biz.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.common.util.CodecUtils;
import org.ironrhino.core.annotation.AutoConfig;
import org.ironrhino.core.annotation.NaturalId;
import org.ironrhino.core.annotation.NotInCopy;
import org.ironrhino.core.annotation.Recordable;
import org.ironrhino.core.model.BaseEntity;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

@Recordable
@AutoConfig
@Searchable(alias = "user")
public class User extends BaseEntity implements UserDetails {
	@NaturalId
	@SearchableProperty
	private String username;

	@NotInCopy
	private String password;

	@SearchableProperty
	private String name;

	@SearchableProperty
	private String address;

	@SearchableProperty
	private String postCode;

	@SearchableProperty
	private String phone;

	@SearchableProperty
	private String mobile;

	private GrantedAuthority[] authorities;

	private boolean enabled;

	@NotInCopy
	private Date createDate;

	@NotInCopy
	private Set<Role> roles = new HashSet<Role>(0);

	public User() {
		createDate = new Date();
		enabled = true;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		this.authorities = authorities;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public GrantedAuthority[] getAuthorities() {
		return this.authorities;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setLegiblePassword(String legiblePassword) {
		this.password = CodecUtils.digest(legiblePassword);
	}

	public boolean isPasswordValid(String legiblePassword) {
		return this.password.equals(CodecUtils.digest(legiblePassword));
	}
	
	public String toString(){
		return this.name;
	}
}
