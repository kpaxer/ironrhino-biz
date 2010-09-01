<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#compress><#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#noescape>${title}</#noescape></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="chrome=1" />
<meta name="context_path" content="${request.contextPath}" />
<link rel="shortcut icon" href="<@url value="/assets/images/favicon.ico"/>" />
<link href="<@url value="/assets/styles/ironrhino-min.css"/>" media="screen" rel="stylesheet" type="text/css" />
<link href="<@url value="/assets/styles/app-min.css"/>" media="screen" rel="stylesheet" type="text/css" />
<!--[if IE]>
	<link href="<@url value="/assets/styles/ie.css"/>" media="all" rel="stylesheet" type="text/css" />
<![endif]-->
<script src="<@url value="/assets/scripts/ironrhino-min.js"/>" type="text/javascript"></script>
<script src="<@url value="/assets/scripts/app-min.js"/>" type="text/javascript"></script>
<#noescape>${head}</#noescape>
</head>

<body>
<div id="wrapper">
<div id="header">
<@authorize ifAnyGranted="ROLE_BUILTIN_USER">
<ul class="menu rounded" corner="top 8px">
	<li><a href="<@url value="/index"/>">首页</a></li>
	<li><a href="<@url value="/customer"/>">客户</a></li>
	<li><a href="<@url value="/order"/>">订单</a></li>
	<li><a href="<@url value="/plan"/>">计划</a></li>
	<li><a href="<@url value="/stuff"/>">原料</a></li>
	<li><a href="<@url value="/reward"/>">工资</a></li>
	<li><a href="<@url value="/product"/>">产品</a></li>
	<li><a href="<@url value="/employee"/>">员工</a></li>
	<li><a href="<@url value="/brand"/>">商标</a></li>
	<li><a href="<@url value="/category"/>">品种</a></li>
	<li><a href="<@url value="/station"/>">货运站</a></li>
	<li><a href="<@url value="/report"/>">报表</a></li>
	<li><a href="<@url value="/chart"/>">图表</a></li>
	<li><a href="<@url value="/user/password"/>">修改密码</a></li>
	<li><a href="<@url value="${ssoServerBase!}${ssoServerBase!}/logout"/>">注销</a></li>
</ul>
</@authorize>
<@authorize ifNotGranted="ROLE_BUILTIN_USER">
<div class="menu rounded" corner="top 8px" style="text-align:center;font-size:1.2em;font-weight:bold;">
${title}
</div>
</@authorize>
</div>

<div id="content" class="rounded" corner="bottom 8px">
<div id="message">
<@s.actionerror cssClass="action_error" />
<@s.actionmessage cssClass="action_message" />
</div>
<#noescape>${body}</#noescape>
</div>

<!--
<div id="footer">
	<ul>
		<li><a href="<@url value="/index"/>">首页</a></li>
	</ul>
	<p class="copyright">Copyright 2010, 版权所有</p>
</div>
-->

</div>
</body>
</html></#escape></#compress>
