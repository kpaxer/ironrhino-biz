<#assign html5 = false/>
<#assign ua = request.getAttribute('userAgent')/>
<#if ua?? && (ua.name!='msie' || ua.majorVersion gt 8)>
<#assign html5 = true/>
</#if>
<#if html5>
<!DOCTYPE html>
<html>
<#else>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
</#if>
<#compress><#escape x as x?html>
<head>
<title><#noescape>${title}</#noescape>-<@printSetting key="company.name" /></title>
<#if html5>
<meta charset="utf-8">
<#else>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</#if>
<#assign metaKeywords=getSetting('meta.keywords')/><#if metaKeywords!=''><meta name="keywords" content="${metaKeywords}"/></#if>
<#assign metaDescription=getSetting('meta.description')/><#if metaDescription!=''><meta name="description" content="${metaDescription}"/></#if>
<#if request.contextPath!=''>
<meta name="context_path" content="${request.contextPath}" />
</#if>
<link rel="shortcut icon" href="<@url value="/assets/website/images/favicon.ico"/>" />
<link href="<@url value="/assets/styles/ironrhino-lite-min.css"/>" media="all" rel="stylesheet" type="text/css"/>
<link href="<@url value="/assets/website/style-min.css"/>" rel="stylesheet" type="text/css"/>
<#noescape>${head}</#noescape>
</head>
<body>
	<div id="wrapper">
		
		<div id="header">
	      <div id="logo"><a href="<@url value="/"/>"><img src="<@url value="/assets/website/images/logo.gif"/>" alt="${action.getText('index')}"/></a></div>
	      <div id="menu">
			<#assign href=request.requestURI?substring(request.contextPath?length)/>
			<ul>
				<li<#if href=='/'||href=='/index'> class="selected"</#if>><a href="<@url value="/"/>">${action.getText('index')}</a></li>
				<li<#if href=='/product'||href?starts_with('/product/')> class="selected"</#if>><a href="<@url value="/product"/>">${action.getText('product')}</a></li>
				<#assign pages=statics['org.ironrhino.core.util.ApplicationContextUtils'].getBean('pageManager').findListByTag('HEADER_LINK')>
				<#list pages as page>
				<li<#if href=='/p'+page.path> class="selected"</#if>><a href="<@url value="${'/p'+page.path}"/>">${page.title}</a></li>
				</#list>
			</ul>
	      </div>
		  <form id="search" action="<@url value="/search"/>" method="get">
			<input type="search" class="text" name="q" value="${q!}" size="20"/>
			<input type="submit" class="button" value="${action.getText('search')}"/>
		  </form>
	    </div>
	
		<div id="main" class="clearfix">
			<div id="content">
				<#if action.hasActionErrors()||action.hasActionMessages()>
				<div id="message">
				<@s.actionerror cssClass="action_error" />
				<@s.actionmessage cssClass="action_message" />
				</div>
				</#if>
				<#noescape>${body}</#noescape>
			</div>
		</div>
		
		<div id="footer">
	      <div class="copyright">&copy;<@printSetting key="company.name" default="XXX Company LTD."/><@printSetting key="copyright.reserved" default=".2010.All rights reserved."/></div>
		  <div class="contact"><@printSetting key="company.contact" default="1350000000"/></div>
	      <div class="links">
			<#assign pages=statics['org.ironrhino.core.util.ApplicationContextUtils'].getBean('pageManager').findListByTag('FOOTER_LINK')>
			<#list pages as page>
			<a href="<@url value="${'/p'+page.path}"/>">${page.title}</a>
			</#list>
	      </div>
	    </div>
    
	</div>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
	<script src="<@url value="/assets/scripts/ironrhino-lite-njq-min.js"/>" type="text/javascript"></script>
</body>
</html></#escape></#compress>