<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#compress><#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#noescape>${title}</#noescape>-<@printSetting key="company.name" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<#assign metaKeywords=getSetting('meta.keywords')/><#if metaKeywords!=''><meta name="keywords" content="${metaKeywords}"/></#if>
<#assign metaDescription=getSetting('meta.description')/><#if metaDescription!=''><meta name="description" content="${metaDescription}"/></#if>
<meta name="context_path" content="${request.contextPath}" />
<link rel="shortcut icon" href="<@url value="/assets/upload/website/favicon.ico"/>" />
<link href="<@url value="/assets/styles/ironrhino-lite-min.css"/>" media="all" rel="stylesheet" type="text/css"/>
<link href="<@url value="/assets/styles/website.css"/>" rel="stylesheet" type="text/css"/>
<#noescape>${head}</#noescape>
</head>
<body>
	<div id="wrapper">
	
		<div id="header" class="clearfix">
			<div class="left">
				<div id="logo"><a href="<@url value="/"/>"><img src="<@url value="/assets/upload/website/logo.jpg"/>" alt="${action.getText('index')}"/></a></div>
			</div>
			<div class="right">
				<div class="pic">
					<img src="<@url value="/assets/upload/website/pic1.gif"/>"/>
					<img src="<@url value="/assets/upload/website/pic2.gif"/>"/>
					<img src="<@url value="/assets/upload/website/pic3.gif"/>"/>
				</div>
				<div class="text">
					<@includePage path="/topright"/>
				</div>
			</div>
		</div>
	
		<div id="main" class="clearfix">
			<div id="sidebar">
				<#assign href=request.requestURI?substring(request.contextPath?length)/>
				<ul>
					<li<#if href=='/'||href=='/index'> class="selected"</#if>><a href="<@url value="/"/>">${action.getText('index')}</a></li>
					<li<#if href=='/product'||href?starts_with('/product/')> class="selected"</#if>><a href="<@url value="/"/>"><a href="<@url value="/product"/>">${action.getText('product')}</a></li>
					<#assign pages=statics['org.ironrhino.core.util.ApplicationContextUtils'].getBean('pageManager').findListByTag('INDEX_MENU')>
					<#list pages as page>
					<li<#if href=='/p'+page.path> class="selected"</#if>><a href="<@url value="${'/p'+page.path}"/>">${page.title}</a></li>
					</#list>
				</ul>
				<div class="search">
					<form action="<@url value="/search"/>" method="get">
						<input type="search" name="q" value="${q!}" size="20"/><@button type="submit" text="${action.getText('search')}"/>
					</form>
				</div>
				<div class="text">
					<@includePage path="/sidebarbottom"/>
				</div>
			</div>
			<div id="content">
				<div id="message">
				<@s.actionerror cssClass="action_error" />
				<@s.actionmessage cssClass="action_message" />
				</div>
				<#noescape>${body}</#noescape>
			</div>
		</div>
	
		<div id="footer" class="clearfix">
			<div class="logo"><a href="<@url value="/"/>"><img src="<@url value="/assets/upload/website/logo.jpg"/>" alt="${action.getText('index')}"/></a></div>
			<div class="contact"><@printSetting key="company.contact" default="1350000000"/></div>
			<div class="copyright">&copy;<@printSetting key="company.name" default="XXX Company LTD."/><@printSetting key="copyright.reserved" default=".2010.All rights reserved."/></div>
		</div>
	</div>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js" type="text/javascript"></script>
	<script src="<@url value="/assets/scripts/ironrhino-lite-njq-min.js"/>" type="text/javascript"></script>
</body>
</html></#escape></#compress>