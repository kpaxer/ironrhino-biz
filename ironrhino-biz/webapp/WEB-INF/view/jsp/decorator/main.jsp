<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="authz"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><decorator:title default="ironrhino" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="context_path" content="${pageContext.request.contextPath}" />
<link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>" />
<link href="<c:url value="/styles/main.css"/>" media="all"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<c:url value="/themes/base/ui.all.css"/>"
	type="text/css" media="screen" />
<!--[if IE]>
	<link href="<c:url value="/styles/ie.css"/>" media="all"
		rel="stylesheet" type="text/css" />
	<![endif]-->
<script src="<c:url value="/scripts/jquery.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/ui.core.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/ui.dialog.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/ui.resizable.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/ui.draggable.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/ui.tabs.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/jquery.history.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/jquery.form.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/jquery.corner.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/jquery.dragable.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/sortabletable.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/scripts/application.js"/>"
	type="text/javascript"></script>
<script type="text/javascript" src="<c:url value="/scripts/richtable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/dwr/engine.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/dwr/interface/ApplicationContextConsole.js"/>"></script>

<decorator:head />
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
	<authz:authorize ifAnyGranted="系统管理员">
		<li><a>系统配置</a>
		<ul>
			<li><a href="<c:url value="/controlPanel"/>">控制面板</a></li>
			<li><a href="<c:url value="/securityConfig"/>">安全配置</a></li>
			<li><a href="<c:url value="/common/setting"/>">参数设置</a></li>
			<li><a href="<c:url value="/common/customizeEntity"/>">属性定制</a></li>
		</ul>
		</li>
		<li><a>基础数据</a>
		<ul>
			<li><a href="<c:url value="/spec"/>">规格管理</a></li>
			<li><a href="<c:url value="/region"/>">区域管理</a></li>
		</ul>
		</li>
		<li><a>用户管理</a>
		<ul>
			<li><a href="<c:url value="/user"/>">用户管理</a></li>
			<li><a href="<c:url value="/role"/>">角色管理</a></li>
		</ul>
		</li>
	</authz:authorize>
	<authz:authorize ifAnyGranted="系统管理员,仓管员">
		<li><a>仓库管理</a>
		<ul>
			<authz:authorize ifAnyGranted="系统管理员">
				<li><a href="<c:url value="/vendor"/>">供货商管理</a></li>
				<li><a href="<c:url value="/stuff"/>">原料管理</a></li>
				<li><a href="<c:url value="/stuffflow"/>">出入库审核</a></li>
				<li><a href="<c:url value="/stuffflow/history"/>">审核记录</a></li>
			</authz:authorize>
			<li><a href="<c:url value="/stuffflow/in"/>">入库</a></li>
			<li><a href="<c:url value="/stuffflow/out"/>">出库</a></li>

		</ul>
		</li>
	</authz:authorize>
	<li><a>产品管理</a>
	<ul>
		<li><a href="<c:url value="/category"/>">目录管理</a></li>
		<li><a href="<c:url value="/product"/>">产品管理</a></li>
	</ul>
	</li>
	<li><a href="<c:url value="/customer"/>">客户管理</a></li>
	<li><a href="<c:url value="/order"/>">订单管理</a></li>

	<li><a href="<c:url value="/changePassword"/>">修改密码</a></li>
	<li><a href="<c:url value="/logout"/>">注销</a></li>
</ul>
</div>
</div>

<div id="main">
<div id="content" style="margin: 20px 20px; padding: 10px;">
<div id="message"><s:actionerror cssClass="action_error" /><s:actionmessage
	cssClass="action_message" /></div>
<decorator:body /></div>
</div>

</div>
</body>
</html>
