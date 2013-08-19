package com.ironrhino.biz.model;

import org.ironrhino.core.model.Displayable;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

public enum RewardType implements Displayable {
	SALARY, BONUS, PACKING, DELIVERY, PORTAGE, TRIP, PETROL, MOBILE, DINNER;
	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDisplayName() {
		try {
			return LocalizedTextUtil.findText(getClass(), name(), ActionContext
					.getContext().getLocale(), name(), null);
		} catch (Exception e) {
			return name();
		}
	}

	public static RewardType parse(String name) {
		if (name != null)
			for (RewardType en : values())
				if (name.equals(en.name()) || name.equals(en.getDisplayName()))
					return en;
		return null;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
}
