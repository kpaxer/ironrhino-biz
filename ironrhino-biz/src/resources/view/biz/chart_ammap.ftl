<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${title!}</title>
<script>
Observation.ammap = function(container){
swfobject.embedSWF("<@url value='/assets/components/ammap/ammap.swf'/>","chart_ammap","1000px","700px","9.0.0","<@url value='/assets/images/expressInstall.swf'/>",{
		'path' : '<@url value='/assets/components/ammap/'/>',
		'data_file' : $('#chart_ammap').attr('data_file'),
		'settings_file' : '<@url value='/assets/components/ammap/ammap_settings.xml'/>',
		'preloader_color' : '#999999'
		},{
			wmode : "transparent"
		});
};
</script>
</head>
<body>
<form id="daterange" action="<@url value='/biz/chart/ammap'/>" method="get" class="ajax view form-inline" replacement="c" style="margin-left:10px;">
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
<#assign data_file="${getUrl('/biz/chart/ammapdata')}"/>
<#if request.queryString??>
<#assign data_file=data_file+'?'+request.queryString>
</#if>
<div id="c" style="clear: both;">
	<div id="chart_ammap" data_file="${data_file}">
	</div>
</div>
</body>
</html>

