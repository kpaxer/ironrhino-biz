<!DOCTYPE html>
<#escape x as x?html><html>
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
		<ul class="thumbnails">
		<#list resultPage.result as page>
			<#if column??>
			<#assign pageurl="/${name}/p${page.pagepath}?column=${column}"/>
			<#else>
			<#assign pageurl="/${name}/p${page.pagepath}"/>
			</#if>
			<li class="span4">
			<div class="thumbnail">
			<a href="<@url value="${pageurl}"/>" class="ajax view">
				<img src="${page.content}" alt="${page.title!}"/>
			</a>
			<h5 style="text-align:center;"><a href="<@url value="${pageurl}"/>" class="ajax view">${page.title!}</a></h5>
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
