package com.ironrhino.biz.model;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

public enum SaleType {
	FACTORY, SHOP, PUSH;
	public String getName() {
		return name();
	}

	public String getDisplayName() {
		try {
			return LocalizedTextUtil.findText(getClass(), name(), ActionContext
					.getContext().getLocale(), name(), null);
		} catch (Exception e) {
			return name();
		}
	}

	public static SaleType parse(String name) {
		if (name != null)
			for (SaleType en : values())
				if (name.equals(en.name()) || name.equals(en.getDisplayName()))
					return en;
		return null;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
}
