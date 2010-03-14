<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${title!'chart'}</title>
</head>
<body>

<form id="daterange" action="${getUrl('/chart/view/'+uid!)}" method="get" class="ajax view line" replacement="c">
	<#if uid??>
		<input type="hidden" name="id" value="${uid}" />
	</#if>
	<#list Parameters?keys as name>
	<#if name!='from'&&name!='to'>
	<input type="hidden" name="${name}" value="${Parameters[name]}" />
	</#if>
	</#list>
	<div><@s.textfield theme="simple" name="from" cssClass="date required"/></div>
	<div><@s.textfield theme="simple" name="to" cssClass="date required"/></div>
	<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
</form>

<#assign dataurl='/chart/data'/>
<#if uid??>
<#assign dataurl=dataurl+'/'+uid>
</#if>
<#if request.queryString??>
<#assign dataurl=dataurl+'?'+request.queryString>
</#if>
<div id="c">
<div id="chart" class="chart" data="<@url value="${dataurl}"/>" style="width:1200px; height:400px;"></div>
</div>
</body>
</html></#escape>
