<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText(name)}</title>
</head>
<body>
<@includePage path="/index/banner"/>
<div class="clearfix">
	<div class="icon ${name}">
	</div>
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
<#assign selected=column?? && column==var/>
<li<#if selected> class="selected"</#if>><#if selected><span><#else><a href="<@url value="/${name}/list/${var}"/>"></#if>${var}<#if selected></span><#else></a></#if></li>
</#list>
</ul>
<#if column??>
<div id="_list" class="list">
<dl>
	<#list resultPage.result as page>
	<#assign pageurl="/${name}/p${page.path}?column=${column}"/>
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
</#if>
</div>
</body>
</html></#escape>
