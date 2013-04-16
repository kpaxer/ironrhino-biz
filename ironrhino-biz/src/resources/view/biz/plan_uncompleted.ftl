<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>未完成的计划</title>
</head>
<body>
<#if uncompletedPlans.size() gt 0>
<table class="table table-striped table-condensed middle">
	<thead>
		<tr>
			<td>${action.getText('product')}</td>
			<td>${action.getText('planDate')}</td>
			<td>${action.getText('quantity')}</td>
			<td style="width:13%;"></td>
		</tr>
	</thead>
	<tbody>
	<#list uncompletedPlans as var>
		<tr>
			<td>${var.product?string}</td>
			<td>${var.planDate?string("yyyy-MM-dd")}</td>
			<td>${var.quantity}</td>
			<td><a class="btn ajax" href="${getUrl(actionBaseUrl+'/complete/'+var.id)}" onsuccess="$(this).closest('tr').remove()">${action.getText('complete')}</a></td>
		</tr>
	</#list>
	</tbody>
</table>
</#if>
</body>
</html></#escape>
