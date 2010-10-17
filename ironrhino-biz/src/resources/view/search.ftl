<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('search')}</title>
</head>
<body>
<@includePage path="/index/banner"/>
<div class="clearfix">
	<div class="crumbs"> 
	${action.getText('current.location')}:
	<a href="<@url value="/"/>">${action.getText('index')}</a><span>&gt;</span>
		${action.getText('search')}
	</div>
</div>
<div class="clearfix column search">
<div id="_list" class="list">
<dl>
	<#list resultPage.result as page>
	<#assign pageurl="/product/p${page.path}"/>
	<dd>
		<div style="float:left;width:192px;">
			<div style="padding:10px;">
				<a href="<@url value="${pageurl}"/>">
					<img src="${page.content}" alt="${page.title!}" width="172" height="150" />
				</a>
			</div>
			<div style="text-align:center;">
				<a href="<@url value="${pageurl}"/>">${page.title!}</a>
			</div>
		</div>
	</dd>
	</#list>
</dl>
<@pagination class="ajax view" replacement="_list" cache="true"/>
</div>
</div>
</body>
</html></#escape>