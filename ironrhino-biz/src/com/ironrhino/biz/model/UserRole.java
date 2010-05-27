package com.ironrhino.biz.model;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

public class UserRole {

	public static final String ROLE_BUILTIN_ANONYMOUS = "ROLE_BUILTIN_ANONYMOUS";
	public static final String ROLE_BUILTIN_USER = "ROLE_BUILTIN_USER";
	public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";
	public static final String ROLE_WAREHOUSEMAN = "ROLE_WAREHOUSEMAN";
	public static final String ROLE_SALESMAN = "ROLE_SALESMAN";
	public static final String ROLE_HR = "ROLE_HR";

	public static Map<String, String> getRoles() {
		Map<String, String> roles = new LinkedHashMap<String, String>();
		Class c = UserRole.class;
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			if (f.getName().startsWith("ROLE_BUILTIN_"))
				continue;
			roles.put(f.getName(), LocalizedTextUtil.findText(c, f.getName(),
					ActionContext.getContext().getLocale(), f.getName(), null));
		}
		return roles;
	}

}
