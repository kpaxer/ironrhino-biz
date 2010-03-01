<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>ironrhino</title>
</head>
<body>
<div id="customer_search">
<form id="search_form" action="<@url value="/customer/search"/>" method="get">
<span><input id="q" type="text" name="q" size="20" maxlength="256" class="autocomplete_off" value="${Parameters.q!}" /></span>
<span><@s.submit value="搜索" theme="simple" /></span>
<span><@button type="link" text="按区域检索" href="${getUrl('/customer/region')}"/></span>
</form>
</div>
</body>
</html></#escape>
