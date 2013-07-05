<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('view')}${action.getText('order')}</title>
</head>
<body>
<table id="details" class="table<#if !Parameters.printpage??> table-bordered</#if> middle">
	<tbody>
		<tr>
			<td class="fieldlabel">${action.getText('code')}</td><td>${order.code!}</td>
			<td class="fieldlabel">${action.getText('orderDate')}</td><td>${order.orderDate!?string('yyyy-MM-dd')}</td>
			<td class="fieldlabel">${action.getText('saleType')}</td><td>${order.saleType.displayName!}</td>
		</tr>
		<tr>
			<td class="fieldlabel">${action.getText('customer')}</td><td><#if !Parameters.printpage??><a href="${actionBaseUrl}?customer.id=${order.customer.id}" target="_blank"></#if>${order.customer}<#if !Parameters.printpage??></a></#if></td>
			<td class="fieldlabel">${action.getText('phone')}</td><td>${order.customer.phone!}&nbsp;${order.customer.mobile!}</td>
			<td class="fieldlabel">${action.getText('address')}</td><td>${order.customer.fullAddress!}</td>
		</tr>
		<tr>
			<td class="fieldlabel">${action.getText('payDate')}</td><td>${(order.payDate?string('yyyy-MM-dd'))!}</td>
			<td class="fieldlabel">${action.getText('shipDate')}</td><td>${(order.shipDate?string('yyyy-MM-dd'))!}</td>
			<td class="fieldlabel">${action.getText('station')}</td><td>${(order.station.name)!}</td>
		</tr>
		<tr>
			<td class="fieldlabel">${action.getText('salesman')}</td><td>${(order.salesman.name)!}</td>
			<td class="fieldlabel">${action.getText('deliveryman')}</td><td>${(order.deliveryman.name)!}</td>
			<td class="fieldlabel">${action.getText('createUser')}</td><td>${order.createUser!}</td>
		</tr>
		<tr>
			<td class="fieldlabel">${action.getText('createDate')}</td><td>${order.createDate!}</td>
			<td class="fieldlabel">${action.getText('modifyUser')}</td><td>${order.modifyUser!}</td>
			<td class="fieldlabel">${action.getText('modifyDate')}</td><td>${order.modifyDate!}</td>
		</tr>
	</tbody>
</table>
<table id="items" class="table<#if !Parameters.printpage??> table-bordered</#if> middle">
	<thead>
		<tr>
			<td style="font-weight:bold;">
			${action.getText('product')}
			</td>
			<td style="font-weight:bold;">
			${action.getText('quantity')}
			</td>
			<td style="font-weight:bold;">
			${action.getText('price')}
			</td>
			<td style="font-weight:bold;text-align:right;">
			${action.getText('subtotal')}
			</td>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<td colspan="3" style="font-weight:bold;text-align:right;">${action.getText('amount')}</td>
			<td style="text-align:right;">${order.amount}</td>
		</tr>
		<#if order.discount??>
		<tr>
			<td colspan="3" style="font-weight:bold;text-align:right;">${action.getText('discount')}</td>
			<td style="text-align:right;">-${order.discount}</td>
		</tr>
		</#if>
		<#if order.freight??>
		<tr>
			<td colspan="3" style="font-weight:bold;text-align:right;">${action.getText('freight')}</td>
			<td style="text-align:right;">-${order.freight}</td>
		</tr>
		</#if>
		<tr>
			<td colspan="3" style="font-weight:bold;text-align:right;">${action.getText('grandTotal')}</td>
			<td style="font-weight:bold;text-align:right;">${order.grandTotal}</td>
		</tr>
	</tfoot>
	<tbody>
	<#list order.items as item>
	<tr>
	<td>
	${item.product}
	</td>
	<td>
	${item.quantity}
	</td>
	<td>
	${item.price}<#if item.price==0>(${action.getText('freegift')})</#if>
	</td>
	<td style="text-align:right;">
	${item.subtotal}
	</td>
	</tr>
	</#list>
	</tbody>
</table>
<#if order.memo?has_content>
<pre id="memo">${order.memo!}</pre>
</#if>
<#if !Parameters.printpage??>
<div style="text-align:center;">
	<a href="${actionBaseUrl+'/view/'+order.id}?decorator=simple&printpage=true" target="_blank" class="btn">${action.getText('print')}</a>
</div>
</#if>
</body>
</html></#escape>
