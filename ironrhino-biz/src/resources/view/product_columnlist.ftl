<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText(name)}</title>
</head>
<body>
<@includePage path="/index/banner"/>
<ul class="breadcrumb">
	<li>
    	<a href="<@url value="/"/>">${action.getText('index')}</a> <span class="divider">/</span>
	</li>
<#if !column??>
	<li class="active">${action.getText(name)}</li>
<#else>
	<li>
    	<a href="<@url value="/${name}"/>">${action.getText(name)}</a> <span class="divider">/</span>
	</li>
	<li class="active">${column!}</li>
</#if>
</ul>

<div class="container-fluid column ${name}">
  <div class="row-fluid">
    <div class="span2">
		<ul class="nav nav-list">
			<li class="nav-header">${name}</li>
			<#list columns as var>
			<#assign active=column?? && column==var/>
			<li<#if active> class="active"</#if>><a href="<@url value="/${name}/list/${var}"/>" class="ajax view history">${var}</a></li>
			</#list>
		</ul>
    </div>
    <div id="list" class="span10">
		<ul class="unstyled row-fluid">
		<#list resultPage.result as page>
			<#if column??>
			<#assign pageurl="/${name}/p${page.path}?column=${column}"/>
			<#else>
			<#assign pageurl="/${name}/p${page.path}"/>
			</#if>
			<li class="span4">
			<div class="pic">
			<a href="<@url value="${pageurl}"/>" class="ajax view">
				<img src="${page.content}" alt="${page.title!}" style="width:100%" />
			</a>	
			</div>
			<div class="title">
				<a href="<@url value="${pageurl}"/>" class="ajax view">${page.title!}</a>
			</div>
			</li>
		</#list>
		</ul>
		<@pagination class="ajax view history" replacement="list" cache="true"/>
    </div>
  </div>
</div>
</body>
</html></#escape>
