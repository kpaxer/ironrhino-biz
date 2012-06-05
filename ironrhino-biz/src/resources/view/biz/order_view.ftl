<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('view')}${action.getText('order')}</title>
</head>
<body>
<div id="info">
<div>
<span style="margin-left:5px;">${action.getText('code')}:</span><span style="margin-left:5px;">${order.code}</span>
<span style="margin-left:5px;">${action.getText('customer')}:</span><span>${order.customer}</span>
<span style="margin-left:5px;">${action.getText('phone')}:</span><span>${order.customer.phone!}&nbsp;${order.customer.mobile!}</span>
<span style="margin-left:5px;">${action.getText('address')}:</span><span>${order.customer.fullAddress!}</span>
<#if order.customer.tags?size gt 0>
<span style="margin-left:5px;">${action.getText('tag')}:</span><span>${order.customer.tagsAsString!}</span>
</#if>
</div>
<div>
<span style="margin-left:5px;">${action.getText('orderDate')}:</span><span>${order.orderDate?string('yyyy年MM月dd日')}</span>
<span style="margin-left:5px;">${action.getText('saleType')}:</span><span>${order.saleType.displayName}</span>
<#if order.salesman??>
<span style="margin-left:5px;">${action.getText('salesman')}:</span><span>${order.salesman.name}</span>
</#if>
<#if order.deliveryman??>
<span style="margin-left:5px;">${action.getText('deliveryman')}:</span><span>${order.deliveryman.name}</span>
</#if>
</div>
<div>
<#if order.paid&&order.payDate??>
<span style="margin-left:5px;">${action.getText('payDate')}:</span><span>${order.payDate?string('yyyy年MM月dd日')}</span>
</#if>

<#if order.shipped>
<#if order.shipDate??>
<span style="margin-left:5px;">${action.getText('shipDate')}:</span><span>${order.shipDate?string('yyyy年MM月dd日')}</span>
</#if>
<#if order.station??>
<span style="margin-left:5px;">${action.getText('station')}:</span><span>${order.station.name}</span>
</#if>
</#if>
</div>
<div>
<#if order.createUser??>
<span style="margin-left:5px;">${action.getText('createUser')}:</span><span>${order.createUser.name}</span>
<span style="margin-left:5px;">${action.getText('createDate')}:</span><span>${order.createDate?string('yyyy-MM-dd HH:mm:ss')}</span>
</#if>
<#if order.modifyUser??>
<span style="margin-left:5px;">${action.getText('modifyUser')}:</span><span>${order.modifyUser.name}</span>
<span style="margin-left:5px;">${action.getText('modifyDate')}:</span><span>${order.modifyDate?string('yyyy-MM-dd HH:mm:ss')}</span>
</#if>
</div>
<table class="table table-condensed middle">
	<thead>
		<tr>
			<td>
			${action.getText('product')}
			</td>
			<td>
			${action.getText('quantity')}
			</td>
			<td>
			${action.getText('price')}
			</td>
			<td style="text-align:right;">
			${action.getText('subtotal')}
			</td>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<td colspan="3">${action.getText('amount')}</td>
			<td style="text-align:right;">${order.amount}</td>
		</tr>
		<#if order.discount??>
		<tr>
			<td colspan="3">${action.getText('discount')}</td>
			<td style="text-align:right;">-${order.discount}</td>
		</tr>
		</#if>
		<#if order.freight??>
		<tr>
			<td colspan="3">${action.getText('freight')}</td>
			<td style="text-align:right;">-${order.freight}</td>
		</tr>
		</#if>
		<tr>
			<td colspan="3">${action.getText('grandTotal')}</td>
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
	${item.price}
	</td>
	<td style="text-align:right;">
	${item.subtotal}
	</td>
	</tr>
	</#list>
	</tbody>
</table>
<#if order.memo?has_content>
<div>
<pre style="margin-left:20px;">${order.memo!}</pre>
</div>
</#if>
</div>
</body>
</html></#escape>
