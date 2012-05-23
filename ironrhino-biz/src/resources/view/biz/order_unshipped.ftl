<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
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
					<td width="12%"></td>
				</tr>
			</thead>
			<tbody>
			<#list unshippedOrders as var>
				<tr>
					<td><a target="_blank" href="order/view/${var.id}">${var.code}</a></td>
					<td><a target="_blank" href="order?customer.id=${var.customer.id}">${var.customer!}</a></td>
					<td>${var.orderDate?string("yyyy年MM月dd日")}</td>
					<td>${var.grandTotal}</td>
					<td><a class="btn ajax" href="${getUrl('/biz/order/ship/'+var.id)}" onsuccess="$(this).closest('tr').remove()">${action.getText('ship')}</a></td>
				</tr>
			</#list>
			</tbody>
		</table>
</#if>
</body>
</html></#escape>
