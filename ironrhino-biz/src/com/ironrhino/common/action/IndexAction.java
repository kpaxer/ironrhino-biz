package com.ironrhino.common.action;

import javax.inject.Inject;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.struts.BaseAction;

import com.ironrhino.common.support.WebsiteHandler;

@AutoConfig
public class IndexAction extends BaseAction {

	private static final long serialVersionUID = 29792886600858873L;

	@Inject
	private WebsiteHandler websiteHandler;

	@Override
	public String execute() {
		boolean website = websiteHandler.isWebsite();
		if (!website) {
			targetUrl = "/biz/index";
			return REDIRECT;
		} else {
			return SUCCESS;
		}
	}
}
