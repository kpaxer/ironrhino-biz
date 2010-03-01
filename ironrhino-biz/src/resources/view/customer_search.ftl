<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>搜索客户</title>
</head>
<body>
<form id="search_form" action="<@url value="/customer/search"/>" method="get">
<span><input id="q" type="text" name="q" size="20" maxlength="256" class="autocomplete_off" value="${Parameters.q!}" /></span>
<span><@s.submit value="搜索" theme="simple" /></span>
</form>
<div id="search_result">
<#if searchResults??>
 <#if searchResults.hits??>
		<#list searchResults.hits as var>
			<p><a href="<@url value="/customer/view/${var.data().id}"/>">${var.data().name}</a></p>
		</#list>
		<#if searchResults.pages??>
			<p>
			<#assign index=0>
			<#list searchResults.pages as var>
				<#assign index=index+1>
				<#if var.selected>
					<span>${var.from}-${var.to}</span>
					<#else>
					<a href="<@url value="search?q=${q}&amp;pn=${index}&amp;ps=${ps}"/>">${var.from}-${var.to}</a>
				</#if>
			</#list></p>
		</#if>
	<#else>
	没有找到结果,<a href="<@url value="/customer/region"/>">按区域检索?</a>
	</#if>
</#if>
</div>
</body>
</html></#escape>
