<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#compress><#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#noescape>${title}</#noescape>-<@printSetting key="company.name" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="context_path" content="${request.contextPath}" />
<link rel="shortcut icon" href="<@url value="/assets/images/website/favicon.ico"/>" />
<link href="<@url value="/assets/styles/ironrhino-min.css"/>" media="all" rel="stylesheet" type="text/css"/>
<link href="<@url value="/assets/styles/website.css"/>" rel="stylesheet" type="text/css"/>
<script src="<@url value="/assets/scripts/ironrhino-min.js"/>" type="text/javascript"></script>
<script src="<@url value="/assets/scripts/website.js"/>" type="text/javascript"></script>
<#noescape>${head}</#noescape>
</head>
<body>
	<div id="wrapper">
	
		<div id="header" class="clearfix">
			<div class="left">
				<div id="logo"></div>
			</div>
			<div class="right">
				<div class="pic">
					<img src="<@url value="/assets/images/website/pic1.gif"/>"/>
					<img src="<@url value="/assets/images/website/pic2.gif"/>"/>
					<img src="<@url value="/assets/images/website/pic3.gif"/>"/>
				</div>
				<div class="text">
					<@includePage path="/topright"/>
				</div>
			</div>
		</div>
	
		<div id="main" class="clearfix">
			<div id="sidebar">
				<ul>
					<li><a href="/">${action.getText('index')}</a></li>
					<li><a href="/product">${action.getText('product')}</a></li>
					<li><a href="/aboutus">${action.getText('aboutus')}</a></li>
					<li><a href="/contact">${action.getText('contact')}</a></li>
				</ul>
				<div class="search">
					<@s.form theme="simple" action="search" method="get">
						<@s.textfield theme="simple" name="q" size="20"/><@button type="submit" text="${action.getText('search')}"/>
					</@s.form>
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
			<div class="logo"></div>
			<div class="contact"><@printSetting key="company.contact" default="1350000000"/></div>
			<div class="copyright">&copy;<@printSetting key="company.name" default="XXX Company LTD."/><@printSetting key="copyright.reserved" default=".2010.All rights reserved."/></div>
		</div>
	</div>
</body>
</html></#escape></#compress>