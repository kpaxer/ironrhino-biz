<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${title!'chart'}</title>
</head>
<body>
<form id="daterange" action="<@url value="/biz/chart/view"/>" method="get" class="ajax view form-inline nodirty" replacement="c"  style="margin-left:10px;">
	<#if id??>
	<#list id as var>
	<input type="hidden" name="id" value="${var}" />
	</#list>
	</#if>
	<#list Parameters?keys as name>
	<#if name!='id'&&name!='from'&&name!='to'>
	<input type="hidden" name="${name}" value="${Parameters[name]}" />
	</#if>
	</#list>
	<@s.textfield theme="simple" name="from" cssClass="date required"/>
	<@s.textfield theme="simple" name="to" cssClass="date required"/>
	<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<#assign dataurl=getUrl("/biz/chart/data")/>
<#if request.queryString??>
<#assign dataurl=dataurl+'?'+request.queryString>
</#if>
<div id="c" style="clear: both;">
<div id="chart" class="chart" data="<@url value="${dataurl}"/>" style="width:1150px; height:400px;">
<span style="color:red;">请先安装flash插件</span>
</div>
</div>
</body>
</html></#escape>
