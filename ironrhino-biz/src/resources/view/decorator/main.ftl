<#assign requestURI=request.requestURI?substring(request.contextPath?length)/>
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
<html xmlns="http://www.w3.org/1999/xhtml">
</#if>
<#compress><#escape x as x?html>
<head>
<title><#noescape>${title}</#noescape></title>
<#if modernBrowser>
<meta charset="utf-8">
<#else>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</#if>
<#if request.contextPath!=''>
<meta name="context_path" content="${request.contextPath}" />
</#if>
<link rel="shortcut icon" href="<@url value="/assets/images/favicon.ico"/>" />
<link href="<@url value="/assets/styles/ironrhino${modernBrowser?string('-min','')}.css"/>" media="screen" rel="stylesheet" type="text/css" />
<#if !modernBrowser><link href="<@url value="/assets/styles/ie.css"/>" media="all" rel="stylesheet" type="text/css" /></#if>
<link href="<@url value="/assets/styles/app${modernBrowser?string('-min','')}.css"/>" media="screen" rel="stylesheet" type="text/css" />
<script src="<@url value="/assets/scripts/ironrhino${modernBrowser?string('-min','')}.js"/>" type="text/javascript"></script>
<script src="<@url value="/assets/scripts/app${modernBrowser?string('-min','')}.js"/>" type="text/javascript"></script>
<#noescape>${head}</#noescape>
</head>
<body>
<div id="wrapper">
<div id="header">
<#if requestURI=='/login'>
		<div class="menu rounded" corner="top 8px" style="text-align:center;font-size:1.2em;font-weight:bold;">
		${title}
		</div>
<#else>
		<ul class="menu rounded" corner="top 8px">
			<li><a class="ajax view" href="<@url value="/biz/index"/>">${action.getText('index')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/customer"/>">${action.getText('customer')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/order"/>">${action.getText('order')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/plan"/>">${action.getText('plan')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/stuff"/>">${action.getText('stuff')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/reward"/>">${action.getText('reward')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/product"/>">${action.getText('product')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/employee"/>">${action.getText('employee')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/brand"/>">${action.getText('brand')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/category"/>">${action.getText('category')}</a></li>
			<li><a class="ajax view" href="<@url value="/biz/station"/>">${action.getText('station')}</a></li>
			<li>
				<a class="ajax view" href="<@url value="/biz/report"/>">${action.getText('report')}</a>
				<ul>
					<li><a class="ajax view" href="<@url value="/biz/chart"/>">${action.getText('chart')}</a></li>
				</ul>
			</li>
			<li>
				<a class="ajax view" href="<@url value="${ssoServerBase!}/user/password"/>">${action.getText('change')}${action.getText('password')}</a>
				<ul>
					<li><a class="ajax view" href="<@url value="${ssoServerBase!}/user/profile"/>">${action.getText('profile')}</a></li>
				</ul>
			</li>
			<li><a href="<@url value="${ssoServerBase!}/logout"/>">${action.getText('logout')}</a></li>
		</ul>
</#if>
</div>

<div id="content" class="rounded" corner="bottom 8px">
<div id="message">
<@s.actionerror cssClass="action_error" />
<@s.actionmessage cssClass="action_message" />
</div>
<#noescape>${body}</#noescape>
</div>


</div>
</body>
</html></#escape></#compress>
