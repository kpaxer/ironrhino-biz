package com.ironrhino.biz.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NaturalId;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.metadata.RecordAware;
import org.ironrhino.core.model.BaseEntity;
import org.ironrhino.core.model.SimpleElement;
import org.ironrhino.core.util.CodecUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RecordAware
@AutoConfig
@Searchable(alias = "user")
public class User extends BaseEntity implements UserDetails {

	private static final long serialVersionUID = -6135434863820342822L;

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
	private String phone;

	private Collection<GrantedAuthority> authorities;

	private boolean enabled;

	@NotInCopy
	private Date createDate;

	@NotInCopy
	@NotInJson
	private Set<SimpleElement> roles = new HashSet<SimpleElement>(0);

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<SimpleElement> getRoles() {
		return roles;
	}

	public void setRoles(Set<SimpleElement> roles) {
		this.roles = roles;
	}

	@NotInCopy
	public String getRolesAsString() {
		return StringUtils.join(roles.iterator(), ',');
	}

	public void setRolesAsString(String rolesAsString) {
		SimpleElement.fillCollectionWithString(roles, rolesAsString);
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

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
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

	@Override
	public String toString() {
		return this.name;
	}
}
