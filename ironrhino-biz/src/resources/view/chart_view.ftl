<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${title!'chart'}</title>
</head>
<body>
<#assign dataurl='/chart/data'/>
<#if uid??>
<#assign dataurl=dataurl+'/'+uid>
</#if>
<#if request.queryString??>
<#assign dataurl=dataurl+'?'+request.queryString>
</#if>
<div id="chart" class="chart" data="<@url value="${dataurl}"/>" style="width:1200px; height:400px;">
</body>
</html></#escape>
