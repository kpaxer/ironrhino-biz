<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('reward')}${action.getText('list')}</title>
</head>
<body>

<#assign dataurl=actionBaseUrl/>
<#if request.queryString??>
<#assign dataurl=dataurl+'?'+request.queryString>
</#if>
<ul class="nav nav-tabs">
	<li class="active"><a href="#all_reward" data-toggle="tab">全部</a></li>
	<li><a href="#positive_reward" data-toggle="tab">收入</a></li>
	<li><a href="#negative_reward" data-toggle="tab">支出</a></li>
</ul>
<div class="tab-content">
	<div id="all_reward" class="tab-pane ajaxpanel active" data-url="${dataurl}">
	</div>
	<div id="positive_reward" class="tab-pane ajaxpanel manual" data-url="${dataurl+dataurl?contains('?')?string('&','?')}amount=0&amount-op=GT">
	</div>
	<div id="negative_reward" class="tab-pane ajaxpanel manual" data-url="${dataurl+dataurl?contains('?')?string('&','?')}amount=0&amount-op=LT">
	</div>
</div>
</body>
</html></#escape>