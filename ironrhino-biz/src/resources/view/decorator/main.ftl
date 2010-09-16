<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#assign website=true/><@authorize ifAnyGranted="ROLE_ADMINISTRATOR"><#assign website=false/></@authorize><#assign website=website&&request.requestURI!='/login'/>
<#compress><#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#noescape>${title}</#noescape><#if website>-<@printSetting key="company.name"/></#if></title>
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
<script src="<@url value="/assets/scripts/ironrhino.js"/>" type="text/javascript"></script>
<script src="<@url value="/assets/scripts/app-min.js"/>" type="text/javascript"></script>
<#noescape>${head}</#noescape>
</head>

<body>
<div id="wrapper"<#if website> style="width:980px;"</#if>>
<div id="header">
<#if website>
<a href="<@url value="/"/>" title="${action.getText('index')}"><img src="/assets/images/logo.jpg" alt="${action.getText('index')}"/></a>
<div class="topright">
<@includePage path="/topright"/>
</div>
</#if>
<#if request.requestURI=='/login'>
		<div class="menu rounded" corner="top 8px" style="text-align:center;font-size:1.2em;font-weight:bold;">
		${title}
		</div>
<#else>
	<#if !website>
		<ul class="menu rounded" corner="top 8px">
			<li><a href="<@url value="/biz/index"/>">${action.getText('index')}</a></li>
			<li><a href="<@url value="/biz/customer"/>">${action.getText('customer')}</a></li>
			<li><a href="<@url value="/biz/order"/>">${action.getText('order')}</a></li>
			<li><a href="<@url value="/biz/plan"/>">${action.getText('plan')}</a></li>
			<li><a href="<@url value="/biz/stuff"/>">${action.getText('stuff')}</a></li>
			<li><a href="<@url value="/biz/reward"/>">${action.getText('reward')}</a></li>
			<li><a href="<@url value="/biz/product"/>">${action.getText('product')}</a></li>
			<li><a href="<@url value="/biz/employee"/>">${action.getText('employee')}</a></li>
			<li><a href="<@url value="/biz/brand"/>">${action.getText('brand')}</a></li>
			<li><a href="<@url value="/biz/category"/>">${action.getText('category')}</a></li>
			<li><a href="<@url value="/biz/station"/>">${action.getText('station')}</a></li>
			<li><a href="<@url value="/biz/report"/>">${action.getText('report')}</a></li>
			<li><a href="<@url value="/biz/chart"/>">${action.getText('chart')}</a></li>
			<li><a href="<@url value="${ssoServerBase!}/user/password"/>">${action.getText('change')}${action.getText('password')}</a></li>
			<li><a href="<@url value="${ssoServerBase!}/logout"/>">${action.getText('logout')}</a></li>
		</ul>
	<#else>
		<ul class="menu rounded" corner="top 8px">
			<li><a href="<@url value="/product"/>">${action.getText('product')}</a></li>
			<li><a href="<@url value="/joinus"/>">${action.getText('joinus')}</a></li>
			<li><a href="<@url value="/aboutus"/>">${action.getText('aboutus')}</a></li>
			<li><a href="<@url value="${ssoServerBase!}/login"/>">${action.getText('login')}</a></li>
		</ul>
		<form method="get" action="<@url value="/search"/>" class="search">
			<div><input type="text" name="q" value="${q!}"/><button type="submit">${action.getText('search')}</button></div>
		</form>
	</#if>
</#if>
</div>

<div id="content" class="rounded" corner="bottom 8px">
<div id="message">
<@s.actionerror cssClass="action_error" />
<@s.actionmessage cssClass="action_message" />
</div>
<#noescape>${body}</#noescape>
</div>

<#if website>
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
</#if>

</div>
</body>
</html></#escape></#compress>
