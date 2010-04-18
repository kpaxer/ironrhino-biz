package com.ironrhino.biz.model;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

public enum EmployeeType {
	PACKER, DELIVERYMAN, WAREHOUSEMAN , SALESMAN, MANAGER, SERVANT;
	public String getName() {
		return name();
	}

	public String getDisplayName() {
		return LocalizedTextUtil.findText(getClass(), name(), ActionContext
				.getContext().getLocale(), name(), null);
	}

	public static EmployeeType parse(String name) {
		if (name != null)
			for (EmployeeType en : values())
				if (name.equals(en.name()) || name.equals(en.getDisplayName()))
					return en;
		return null;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
}
