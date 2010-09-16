<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#compress><#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#noescape>${title}</#noescape></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="chrome=1" />
<meta name="context_path" content="${request.contextPath}" />
<link href="<@url value="/assets/styles/ironrhino-min.css"/>" media="screen" rel="stylesheet" type="text/css" />
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
<div id="content">
<div id="message">
<@s.actionerror cssClass="action_error" />
<@s.actionmessage cssClass="action_message" />
</div>
<#noescape>${body}</#noescape>
</div>
</body>
</html></#escape></#compress>
