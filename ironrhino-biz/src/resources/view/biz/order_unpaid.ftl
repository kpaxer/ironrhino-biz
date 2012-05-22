<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>未付款的订单</title>
</head>
<body>
<#if unpaidOrders.size() gt 0>
<table class="table table-striped unpaid_order">
			<thead>
				<tr>
					<td>${action.getText('code')}</td>
					<td width="22%">${action.getText('customer')}</td>
					<td width="25%">${action.getText('orderDate')}</td>
					<td>${action.getText('grandTotal')}</td>
					<td width="18%">${action.getText('station')}</td>
					<td width="12%"></td>
				</tr>
			</thead>
			<tbody>
			<#assign grandTotal=0>
			<#list unpaidOrders as var>
				<#assign grandTotal=grandTotal+var.grandTotal>
				<tr>
					<td><a target="_blank" href="order/view/${var.id}">${var.code}</a></td>
					<td><a target="_blank" href="order?customer.id=${var.customer.id}">${var.customer!}</a></td>
					<td>${var.orderDate?string("yyyy年MM月dd日")}</td>
					<td>${var.grandTotal}</td>
					<td><#if var.station??><a target="_blank" href="order?stationId=${var.station.id}" <#if var.cashable> title="今天可结账" style="color:red;"</#if>>${var.station!}</a></#if></td>
					<td><a class="btn ajax pay" href="${getUrl('/biz/order/pay/'+var.id)}">${action.getText('pay')}</a></td>
				</tr>
			</#list>
			</tbody>
			<tfoot>
			<tr>
					<td></td>
					<td></td>
					<td width="22%">合计</td>
					<td>${grandTotal}</td>
					<td width="20%"></td>
					<td width="10%"></td>
				</tr>
			</tfoot>
		</table>
</#if>
</body>
</html></#escape>
