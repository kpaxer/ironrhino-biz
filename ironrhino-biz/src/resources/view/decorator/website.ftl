<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#compress><#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#noescape>${title}</#noescape>-<@printSetting key="company.name"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="chrome=1" />
<meta name="context_path" content="${request.contextPath}" />
<link rel="shortcut icon" href="<@url value="/assets/images/favicon.ico"/>" />
<link href="<@url value="/assets/styles/ironrhino-min.css"/>" media="screen" rel="stylesheet" type="text/css" />
<link href="<@url value="/assets/styles/app.css"/>" media="screen" rel="stylesheet" type="text/css" />
<#assign ua = request.getAttribute('userAgent')/>
<#if ua?? && ua.name=='msie' && ua.majorVersion lt 9>
<!--[if IE]>
	<link href="<@url value="/assets/styles/ie.css"/>" media="all" rel="stylesheet" type="text/css" />
<![endif]-->
</#if>
<script src="<@url value="/assets/scripts/ironrhino-min.js"/>" type="text/javascript"></script>
<script src="<@url value="/assets/scripts/app-min.js"/>" type="text/javascript"></script>
<#noescape>${head}</#noescape>
</head>

<body>
<div id="wrapper" style="width:980px;">
<div id="header">
<a href="<@url value="/"/>" title="${action.getText('index')}"><img src="/assets/images/logo.jpg" alt="${action.getText('index')}"/></a>
<div class="topright">
<@includePage path="/topright"/>
</div>
	<ul class="menu rounded" corner="top 8px">
		<li><a href="<@url value="/product"/>">${action.getText('product')}</a></li>
		<li><a href="<@url value="/joinus"/>">${action.getText('joinus')}</a></li>
		<li><a href="<@url value="/aboutus"/>">${action.getText('aboutus')}</a></li>
		<li><a href="<@url value="${ssoServerBase!}/login"/>">${action.getText('login')}</a></li>
	</ul>
	<form method="get" action="<@url value="/search"/>" class="search">
		<div><input type="text" name="q" value="${q!}"/><button type="submit">${action.getText('search')}</button></div>
	</form>
</div>

<div id="content" class="rounded" corner="bottom 8px">
<div id="message">
<@s.actionerror cssClass="action_error" />
<@s.actionmessage cssClass="action_message" />
</div>
<#noescape>${body}</#noescape>
</div>

<div id="footer">
	<#assign pages=statics['org.ironrhino.core.util.ApplicationContextUtils'].getBean('pageManager').findListByTag('INDEX_FOOTER_MENU')>
	<#if pages?size gt 0>
	<ul>
	<#list pages as page>
		<li><a href="<@url value="/p${page.path}"/>">${page.title}</a>|</li>
	</#list>
	</ul>
	</#if>
	<p><@printSetting key="company.contact" default="contact:111-1111111"/></p>
	<p class="copyright">&copy;<@printSetting key="company.name" default="XXX Company."/><@printSetting key="copyright.reserved" default=".2010.Copyright reserved."/></p>
</div>

</div>
</body>
</html></#escape></#compress>
