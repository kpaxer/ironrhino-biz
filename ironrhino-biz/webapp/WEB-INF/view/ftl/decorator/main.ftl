<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#compress><#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#noescape>${title}</#noescape></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="chrome=1" />
<meta name="context_path" content="${request.contextPath}" />
<link rel="shortcut icon" href="<@url value="/assets/images/favicon.ico"/>" />
<link href="<@url value="/assets/styles/all-min.css"/>" media="screen" rel="stylesheet" type="text/css" />
<!--[if IE]>
	<link href="<@url value="/assets/styles/ie.css"/>" media="all" rel="stylesheet" type="text/css" />
<![endif]-->
<link rel="alternate" href="<@url value="/product/feed"/>" title="ironrhino products" type="application/atom+xml" />
<script src="<@url value="/assets/scripts/all-min.js"/>" type="text/javascript"></script>
<#noescape>${head}</#noescape>
</head>

<body>
<div id="wrapper">
<div id="header">
<div id="menu">
<ul class="nav">
	<li><a href="<@url value="/index"/>">首页</a></li>
	<@authorize ifAnyGranted="ROLE_SUPERVISOR">
		<li><a>基础数据</a>
		<ul>
			<li><a href="<@url value="/brand"/>">商标管理</a></li>
			<li><a href="<@url value="/category"/>">品种管理</a></li>
			<li><a href="<@url value="/common/region"/>">区域管理</a></li>
			<li><a href="<@url value="/user"/>">用户管理</a></li>
		</ul>
		</li>
	</@authorize>
	<@authorize ifAnyGranted="ROLE_SUPERVISOR,ROLE_SALESMAN">
	<li><a href="<@url value="/product"/>">产品管理</a></li>
	<li><a href="<@url value="/plan"/>">生产计划</a></li>
	<li><a href="<@url value="/customer"/>">客户管理</a></li>
	<li><a href="<@url value="/order"/>">订单管理</a></li>
	<li><a href="<@url value="/employee"/>">员工管理</a></li>
	<li><a href="<@url value="/reward"/>">工资管理</a></li>
	</@authorize>
	<li><a href="<@url value="/report"/>">报表</a></li>
	<!--
	<@authorize ifAnyGranted="ROLE_SUPERVISOR,ROLE_WAREHOUSEMAN">
		<li><a>仓库管理</a>
		<ul>
			<@authorize ifAnyGranted="系统管理员">
				<li><a href="<@url value="/vendor"/>">供货商管理</a></li>
				<li><a href="<@url value="/stuff"/>">原料管理</a></li>
				<li><a href="<@url value="/stuffflow"/>">出入库审核</a></li>
				<li><a href="<@url value="/stuffflow/history"/>">审核记录</a></li>
			</@authorize>
			<li><a href="<@url value="/stuffflow/in"/>">入库</a></li>
			<li><a href="<@url value="/stuffflow/out"/>">出库</a></li>

		</ul>
		</li>
	</@authorize>
	-->
	<li><a href="<@url value="/changePassword"/>">修改密码</a></li>
	<li><a href="<@url value="/logout"/>">注销</a></li>
</ul>
</div>
</div>

<div id="main">
<div id="content" style="margin: 20px 20px; padding: 10px;">
<div id="message">
<@s.actionerror cssClass="action_error" />
<@s.actionmessage cssClass="action_message" />
</div>
<#noescape>${body}</#noescape>
</div>
</div>
</div>
</body>
</html></#escape></#compress>
