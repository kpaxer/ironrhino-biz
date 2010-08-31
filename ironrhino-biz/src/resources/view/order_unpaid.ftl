<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>未付款的订单</title>
</head>
<body>
<#if unpaidOrders.size() gt 0>
<table border="0" width="95%" style="margin: 10px;">
			<thead>
				<tr>
					<td>${action.getText('code')}</td>
					<td>${action.getText('customer')}</td>
					<td>${action.getText('orderDate')}</td>
					<td>${action.getText('grandTotal')}</td>
					<td width="20%">${action.getText('station')}</td>
					<td width="10%"></td>
				</tr>
			</thead>
			<tbody>
			<#list unpaidOrders as var>
				<tr>
					<td><a target="_blank" href="<@url value="/order/view/${var.id}"/>">${var.code}</a></td>
					<td><a target="_blank" href="<@url value="/order?customer.id=${var.customer.id}"/>">${var.customer!}</a></td>
					<td>${var.orderDate?string("yyyy年MM月dd日")}</td>
					<td>${var.grandTotal}</td>
					<td><#if var.station??><a target="_blank" href="<@url value="/order?stationId=${var.station.id}"/>" <#if var.cashable> title="今天可结账" style="color:red;"</#if>>${var.station!}</a></#if></td>
					<td><@button text="${action.getText('pay')}" type="link" href="${getUrl('/order/pay/'+var.id)}" class="ajax" onsuccess="$(this).closest('tr').remove()"/></td>
				</tr>
			</#list>
			</tbody>
		</table>
</#if>
</body>
</html></#escape>
