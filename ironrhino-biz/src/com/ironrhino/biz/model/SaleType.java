package com.ironrhino.biz.model;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

public enum SaleType {
	FACTORY, SHOP, PUSH;
	public String getName() {
		return name();
	}

	public String getDisplayName() {
		return LocalizedTextUtil.findText(getClass(), name(), ActionContext
				.getContext().getLocale(), name(), null);
	}
}
