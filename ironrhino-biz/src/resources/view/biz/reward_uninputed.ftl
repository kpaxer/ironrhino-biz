<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>未输工资的日期</title>
</head>
<body>
<#if uninputedDates.size() gt 0>
<h4>未输工资的日期</h4>
<ul>
	<#list uninputedDates as var>
	<li>${var?string('yyyy年MM月dd日')}</li>
	</#list>
</ul>
</#if>
</body>
</html></#escape>
