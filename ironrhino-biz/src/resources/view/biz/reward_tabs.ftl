<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('reward')}${action.getText('list')}</title>
</head>
<body>
<div class="tabs">
	<ul>
		<li><a href="#tab1">全部</a></li>
		<li><a href="#tab2">收入</a></li>
		<li><a href="#tab3">支出</a></li>
	</ul>
<#assign dataurl=getUrl("/biz/reward?1")/>
<#if request.queryString??>
<#assign dataurl=dataurl+'&'+request.queryString>
</#if>
	<div id="tab1" class="ajaxpanel" url="<@url value=dataurl/>">
	</div>
	<div id="tab2" class="ajaxpanel manual" url="<@url value=dataurl+'&negative=false'/>">
	</div>
	<div id="tab3" class="ajaxpanel manual" url="<@url value=dataurl+'&negative=true'/>">
	</div>
</div>
</body>
</html></#escape>