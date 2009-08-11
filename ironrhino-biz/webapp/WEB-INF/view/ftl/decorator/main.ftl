<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#noescape>${title}</#noescape></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="context_path" content="${request.contextPath}" />
<link rel="shortcut icon" href="${base}/images/favicon.ico" />
<link href="${base}/styles/all-min.css" media="screen" rel="stylesheet" type="text/css" />
<!--[if IE]>
	<link href="${base}/styles/ie.css" media="all" rel="stylesheet" type="text/css" />
<![endif]-->
<link rel="alternate" href="${base}/product/feed" title="ironrhino products" type="application/atom+xml" />
<script src="${base}/scripts/all-min.js" type="text/javascript"></script>
<script type="text/javascript" src="${base}/dwr/engine.js"></script>
<script type="text/javascript" src="${base}/dwr/interface/ApplicationContextConsole.js"></script>
<#noescape>${head}</#noescape>
</head>

<body>
<div id="wrapper">
<div id="header" style="height: 40px">
<div id="menu">
<ul class="nav">
	<li><a>日常工作</a>
	<ul>

	</ul>
	</li>
	<@authorize ifAnyGranted="系统管理员">
		<li><a>系统配置</a>
		<ul>
			<li><a href="${base}/controlPanel">控制面板</a></li>
			<li><a href="${base}/securityConfig">安全配置</a></li>
			<li><a href="${base}/common/setting">参数设置</a></li>
			<li><a href="${base}/common/customizeEntity">属性定制</a></li>
		</ul>
		</li>
		<li><a>基础数据</a>
		<ul>
			<li><a href="${base}/spec">规格管理</a></li>
			<li><a href="${base}/region">区域管理</a></li>
		</ul>
		</li>
		<li><a>用户管理</a>
		<ul>
			<li><a href="${base}/user">用户管理</a></li>
			<li><a href="${base}/role">角色管理</a></li>
		</ul>
		</li>
	</@authorize>
	<@authorize ifAnyGranted="系统管理员,仓管员">
		<li><a>仓库管理</a>
		<ul>
			<@authorize ifAnyGranted="系统管理员">
				<li><a href="${base}/vendor">供货商管理</a></li>
				<li><a href="${base}/stuff">原料管理</a></li>
				<li><a href="${base}/stuffflow">出入库审核</a></li>
				<li><a href="${base}/stuffflow/history">审核记录</a></li>
			</@authorize>
			<li><a href="${base}/stuffflow/in">入库</a></li>
			<li><a href="${base}/stuffflow/out">出库</a></li>

		</ul>
		</li>
	</@authorize>
	<li><a>产品管理</a>
	<ul>
		<li><a href="${base}/category">目录管理</a></li>
		<li><a href="${base}/product">产品管理</a></li>
	</ul>
	</li>
	<li><a href="${base}/customer">客户管理</a></li>
	<li><a href="${base}/order">订单管理</a></li>

	<li><a href="${base}/changePassword">修改密码</a></li>
	<li><a href="${base}/logout">注销</a></li>
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
<div id="footer"></div>
</div>
</body>
</html></#escape>
