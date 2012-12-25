<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${title!}</title>
<script src="<@url value="/assets/components/raphael/raphael.js"/>" type="text/javascript"></script>
<script src="<@url value="/assets/components/raphael/chinamap.js"/>" type="text/javascript"></script>
<script>
Observation.chinamap = function(container){
	$('.chinamap',container).each(function(){
		var t = $(this);
		$.getJSON(t.data('url'),function(data){
			t.chinamap(data);
			if(data.states){
				var list = $('.list',container);
				$.each(data.states,function(k,v){
					$('<li>'+v.title+'</li>').appendTo(list);
				});
			}
		});
	});
};
</script>
</head>
<body>
<form id="daterange" action="<@url value='${actionBaseUrl}/chinamap'/>" method="get" class="ajax view form-inline" data-replacement="c" style="margin-left:10px;">
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
<#assign data_file="${getUrl(actionBaseUrl+'/chinamapdata')}"/>
<#if request.queryString??>
<#assign data_file=data_file+'?'+request.queryString>
</#if>
<div id="c" class="row" style="clear: both;">
	<div class="chinamap span8" data-url="${data_file}" style="height:550px;">
	</div>
	<div class="span4">
	<ol class="list">
	</ol>
	</div>
</div>
</body>
</html>

