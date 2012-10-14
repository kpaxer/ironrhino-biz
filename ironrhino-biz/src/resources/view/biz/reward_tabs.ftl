<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('reward')}${action.getText('list')}</title>
</head>
<body>

<#assign dataurl=getUrl("/biz/reward?1=1")/>
<#if request.queryString??>
<#assign dataurl=dataurl+'&'+request.queryString?replace('view=tabs&','')>
</#if>
<ul class="nav nav-tabs">
	<li class="active"><a href="#all_reward" data-toggle="tab">全部</a></li>
	<li><a href="#positive_reward" data-toggle="tab">收入</a></li>
	<li><a href="#negative_reward" data-toggle="tab">支出</a></li>
</ul>
<div class="tab-content">
	<div id="all_reward" class="tab-pane ajaxpanel active" data-url="<@url value=dataurl/>">
	</div>
	<div id="positive_reward" class="tab-pane ajaxpanel manual" data-url="<@url value=dataurl+'&negative=false'/>">
	</div>
	<div id="negative_reward" class="tab-pane ajaxpanel manual" data-url="<@url value=dataurl+'&negative=true'/>">
	</div>
</div>
</body>
</html></#escape>