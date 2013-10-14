<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('uninputed')}${action.getText('order')}</title>
</head>
<body>
<#if uninputedDates.size() gt 0>
<h5>${action.getText('uninputed')}${action.getText('order')}</h5>
<ul>
	<#list uninputedDates as var>
	<li>${var?string('yyyy-MM-dd')}</li>
	</#list>
</ul>
</#if>
</body>
</html></#escape>
