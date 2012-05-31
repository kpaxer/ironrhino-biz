<#assign modernBrowser = false/>
<#assign ua = request.getAttribute('userAgent')/>
<#if ua?? && (ua.name!='msie' || ua.majorVersion gt 8)>
<#assign modernBrowser = true/>
</#if>
<#if modernBrowser>
<!DOCTYPE html>
<html>
<#else>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
</#if>
<#compress><#escape x as x?html>
<head>
<title><#noescape>${title}</#noescape>-<@printSetting key="company.name" /></title>
<#if modernBrowser>
<meta charset="utf-8">
<#else>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</#if>
<#assign metaKeywords=getSetting('meta.keywords')/><#if metaKeywords!=''><meta name="keywords" content="${metaKeywords}"/></#if>
<#assign metaDescription=getSetting('meta.description')/><#if metaDescription!=''><meta name="description" content="${metaDescription}"/></#if>
<#if request.contextPath!=''>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="context_path" content="${request.contextPath}" />
</#if>
<link rel="shortcut icon" href="<@url value="/assets/website/images/favicon.ico"/>" />
<link href="http://twitter.github.com/bootstrap/assets/css/bootstrap.css" media="all" rel="stylesheet" type="text/css"/>
<link href="http://twitter.github.com/bootstrap/assets/css/bootstrap-responsive.css" media="all" rel="stylesheet" type="text/css"/>
<link href="<@url value="/assets/styles/ironrhino-lite${modernBrowser?string('-min','')}.css"/>" media="all" rel="stylesheet" type="text/css"/>
<link href="<@url value="/assets/website/style${modernBrowser?string('-min','')}.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
	<div id="wrapper">
		
		<div id="header">
	      <div id="logo"><a href="<@url value="/"/>"><img src="<@url value="/assets/website/images/logo.gif"/>" alt="${action.getText('index')}"/></a></div>
	      <div id="menu">
			<#assign href=request.requestURI?substring(request.contextPath?length)/>
			<ul class="unstyled">
				<li<#if href=='/'||href=='/index'> class="active"</#if>><a href="<@url value="/"/>">${action.getText('index')}</a></li>
				<li<#if href=='/product'||href?starts_with('/product/')> class="active"</#if>><a href="<@url value="/product"/>">${action.getText('product')}</a></li>
				<#assign pages=statics['org.ironrhino.core.util.ApplicationContextUtils'].getBean('pageManager').findListByTag('HEADER_LINK')>
				<#list pages as page>
				<li<#if href=='/p'+page.path> class="active"</#if>><a href="<@url value="${'/p'+page.path}"/>">${page.title}</a></li>
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
				<@s.actionerror />
				<@s.actionmessage />
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
	<script src="http://twitter.github.com/bootstrap/assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="<@url value="/assets/scripts/ironrhino-lite${modernBrowser?string('-min','')}.js"/>" type="text/javascript"></script>
	<#noescape>${head}</#noescape>
</body>
</html></#escape></#compress>