<!DOCTYPE html>
<#escape x as x?html><html>
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
	<div class="list">
		<dl>
			<#list resultPage.result as page>
			<#assign pageurl="/product/p${page.pagepath}"/>
			<dd>
				<div style="float:left;width:192px;">
					<div style="padding:10px;">
						<a href="<@url value="${pageurl}"/>">
							<img src="${page.content}" alt="${page.title!}" style="width:172px;height:150px;" />
						</a>
					</div>
					<div style="text-align:center;">
						<a href="<@url value="${pageurl}"/>">${page.title!}</a>
					</div>
				</div>
			</dd>
			</#list>
		</dl>
		<@pagination class="ajax view"/>
	</div>
</div>
</body>
</html></#escape>