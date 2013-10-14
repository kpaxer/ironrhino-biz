<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>未发货的订单</title>
</head>
<body>
<#if unshippedOrders.size() gt 0>
<table class="table table-striped table-condensed middle">
			<thead>
				<tr>
					<th>${action.getText('code')}</th>
					<th>${action.getText('customer')}</th>
					<th>${action.getText('orderDate')}</th>
					<th>${action.getText('grandTotal')}</th>
					<th style="min-width:52px;"></th>
				</tr>
			</thead>
			<tbody>
			<#list unshippedOrders as var>
				<tr>
					<td><a target="_blank" href="order/view/${var.id}">${var.code}</a></td>
					<td><a target="_blank" href="order?customer.id=${var.customer.id}">${var.customer!}</a></td>
					<td>${var.orderDate?string("yyyy-MM-dd")}</td>
					<td>${var.grandTotal}</td>
					<td><a class="btn ajax" href="${actionBaseUrl+'/ship/'+var.id}" onsuccess="$(this).closest('tr').remove()">${action.getText('ship')}</a></td>
				</tr>
			</#list>
			</tbody>
		</table>
</#if>
</body>
</html></#escape>
