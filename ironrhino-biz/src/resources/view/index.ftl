<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>首页</title>
<style>
table {
margin: 20px;
}
thead td {
font-weight:bold;
}
</style>
</head>
<body>
<div id="unpaidOrders">
<#if unpaidOrders.size() gt 0>
<h3 class="rounded" style="background-color:#adadad;">未付款的订单</h3>
<table border="0" width="88%">
			<thead>
				<tr>
					<td>${action.getText('code')}</td>
					<td>${action.getText('customer')}</td>
					<td>${action.getText('orderDate')}</td>
					<td>${action.getText('grandTotal')}</td>
					<td width="10%"></td>
				</tr>
			</thead>
			<tbody>
			<#list unpaidOrders as var>
				<tr>
					<td>${var.code}</td>
					<td>${var.customer?string}</td>
					<td>${var.orderDate?string("yyyy年MM月dd日")}</td>
					<td>${var.grandTotal}</td>
					<td><@button text="${action.getText('pay')}" type="link" href="${getUrl('/order/pay/'+var.id)}" class="ajax view" replacement="unpaidOrders"/></td>
				</tr>
			</#list>
			</tbody>
		</table>
</#if>
</div>
<div id="unshippedOrders">
<#if unshippedOrders.size() gt 0>
<h3 class="rounded" style="background-color:#adadad;">未发货的订单</h3>
<table border="0" width="88%">
			<thead>
				<tr>
					<td>${action.getText('code')}</td>
					<td>${action.getText('customer')}</td>
					<td>${action.getText('orderDate')}</td>
					<td>${action.getText('grandTotal')}</td>
					<td width="10%"></td>
				</tr>
			</thead>
			<tbody>
			<#list unshippedOrders as var>
				<tr>
					<td>${var.code}</td>
					<td>${var.customer?string}</td>
					<td>${var.orderDate?string("yyyy年MM月dd日")}</td>
					<td>${var.grandTotal}</td>
					<td><@button text="${action.getText('ship')}" type="link" href="${getUrl('/order/ship/'+var.id)}" class="ajax view" replacement="unshippedOrders"/></td>
				</tr>
			</#list>
			</tbody>
		</table>
</#if>
</div>
<div id="uncompletedPlans">
<#if uncompletedPlans.size() gt 0>
<h3 class="rounded" style="background-color:#adadad;">未完成的计划</h3>
<table border="0" width="88%">
			<thead>
				<tr>
					<td>${action.getText('product')}</td>
					<td>${action.getText('quantity')}</td>
					<td>${action.getText('planDate')}</td>
					<td width="10%"></td>
				</tr>
			</thead>
			<tbody>
			<#list uncompletedPlans as var>
				<tr>
					<td>${var.product?string}</td>
					<td>${var.quantity}</td>
					<td>${var.planDate?string("yyyy年MM月dd日")}</td>
					<td><@button text="${action.getText('complete')}" type="link" href="${getUrl('/plan/complete/'+var.id)}" class="ajax view" replacement="uncompletedPlans"/></td>
				</tr>
			</#list>
			</tbody>
		</table>
</#if>
</div>
</body>
</html></#escape>
