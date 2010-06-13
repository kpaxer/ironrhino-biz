<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${title!'chart'}</title>
</head>
<body>
<form id="daterange" action="${getUrl('/chart/view')}" method="get" class="ajax view line" replacement="c"  style="margin-left:10px;">
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
	<div class="field"><@s.textfield theme="simple" name="from" cssClass="date required"/></div>
	<div class="field"><@s.textfield theme="simple" name="to" cssClass="date required"/></div>
	<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
</form>
<#assign dataurl='/chart/data'/>
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
