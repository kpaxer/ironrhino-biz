<!DOCTYPE html>
<#escape x as x?html><html>
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
