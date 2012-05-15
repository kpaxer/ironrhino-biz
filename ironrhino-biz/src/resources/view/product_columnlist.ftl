<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText(name)}</title>
</head>
<body>
<@includePage path="/index/banner"/>
<div class="clearfix">
	<div class="crumbs"> 
	${action.getText('current.location')}:
	<a href="<@url value="/"/>">${action.getText('index')}</a><span>&gt;</span>
	<#if !column??>
		${action.getText(name)}
	<#else>
		<a href="<@url value="/${name}"/>">${action.getText(name)}</a><span>&gt;</span>
		${column!}
	</#if>
	</div>
</div>
<div class="clearfix column ${name}">
<ul class="catalog">
<#list columns as var>
<#assign active=column?? && column==var/>
<li<#if active> class="active"</#if>><#if active><span><#else><a href="<@url value="/${name}/list/${var}"/>" class="ajax view"></#if>${var}<#if active></span><#else></a></#if></li>
</#list>
</ul>
<div class="list">
<dl class="clearfix">
	<#list resultPage.result as page>
	<#if column??>
	<#assign pageurl="/${name}/p${page.path}?column=${column}"/>
	<#else>
	<#assign pageurl="/${name}/p${page.path}"/>
	</#if>
	<dd>
		<div class="pic">
			<a href="<@url value="${pageurl}"/>" class="ajax view">
				<img src="${page.content}" alt="${page.title!}" width="172" height="150" />
			</a>	
		</div>
		<div class="title">
			<a href="<@url value="${pageurl}"/>" class="ajax view">${page.title!}</a>
		</div>
	</dd>
	</#list>
</dl>
<@pagination class="ajax view" cache="true"/>
</div>
</div>
</body>
</html></#escape>
