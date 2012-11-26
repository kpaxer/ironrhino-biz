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
					<td>${action.getText('code')}</td>
					<td>${action.getText('customer')}</td>
					<td>${action.getText('orderDate')}</td>
					<td>${action.getText('grandTotal')}</td>
					<td style="width:13%;"></td>
				</tr>
			</thead>
			<tbody>
			<#list unshippedOrders as var>
				<tr>
					<td><a target="_blank" href="order/view/${var.id}">${var.code}</a></td>
					<td><a target="_blank" href="order?customer.id=${var.customer.id}">${var.customer!}</a></td>
					<td>${var.orderDate?string("yyyy年MM月dd日")}</td>
					<td>${var.grandTotal}</td>
					<td><a class="btn ajax" href="${getUrl(actionBaseUrl+'/ship/'+var.id)}" onsuccess="$(this).closest('tr').remove()">${action.getText('ship')}</a></td>
				</tr>
			</#list>
			</tbody>
		</table>
</#if>
</body>
</html></#escape>
