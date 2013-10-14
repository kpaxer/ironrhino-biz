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
			<th>${action.getText('product')}</th>
			<th>${action.getText('planDate')}</th>
			<th>${action.getText('quantity')}</th>
			<th style="min-width:52px;"></th>
		</tr>
	</thead>
	<tbody>
	<#list uncompletedPlans as var>
		<tr>
			<td>${var.product?string}</td>
			<td>${var.planDate?string("yyyy-MM-dd")}</td>
			<td>${var.quantity}</td>
			<td><a class="btn ajax" href="${actionBaseUrl+'/complete/'+var.id}" onsuccess="$(this).closest('tr').remove()">${action.getText('complete')}</a></td>
		</tr>
	</#list>
	</tbody>
</table>
</#if>
</body>
</html></#escape>
