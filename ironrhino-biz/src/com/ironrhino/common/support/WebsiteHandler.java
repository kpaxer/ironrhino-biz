package com.ironrhino.common.support;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.ironrhino.common.support.SettingControl;
import org.ironrhino.core.servlet.AccessHandler;

@Named
@Singleton
public class WebsiteHandler implements AccessHandler {

	public static final String REQUEST_ATTR_NAME_WEBSITE = "website";

	@Inject
	private SettingControl settingControl;

	@Override
	public String getPattern() {
		return null;
	}

	@Override
	public boolean handle(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			boolean website = false;
			URL url = null;
			url = new URL(request.getRequestURL().toString());
			String host = url.getHost();
			String[] websiteDomain = settingControl
					.getStringArray("website.domain");
			for (String domain : websiteDomain) {
				if ((host.equalsIgnoreCase(domain) || host
						.equalsIgnoreCase("www." + domain))) {
					website = true;
					break;
				}
			}
			if (website) {
				request.setAttribute(REQUEST_ATTR_NAME_WEBSITE, website);
				request.setAttribute("decorator", REQUEST_ATTR_NAME_WEBSITE);
				String uri = request.getRequestURI();
				uri = uri.substring(request.getContextPath().length());
				if (uri.startsWith("/biz/")) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public boolean isWebsite() {
		Boolean b = (Boolean) ServletActionContext.getRequest().getAttribute(
				REQUEST_ATTR_NAME_WEBSITE);
		return b != null ? b : false;
	}

}
