<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('view')}${action.getText('order')}</title>
</head>
<body>
<div id="info">
<div>
<span>${action.getText('code')}:</span><span style="margin-left:10px;">${order.code}</span>
<span style="margin-left:40px;">${action.getText('customer')}:</span><span style="margin-left:10px;">${order.customer}</span>
<span style="margin-left:40px;">${action.getText('orderDate')}:</span><span style="margin-left:10px;">${order.orderDate?string('yyyy年MM月dd日')}</span>
</div>

<table border="0" class="sortable" width="100%">
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
			<td>
			${action.getText('subtotal')}
			</td>
		</tr>
	</thead>
	<tfoot>
		<#if order.discount??>
		<tr>
			<td colspan="4" align="right">
			${action.getText('discount')}:<span style="margin-left:10px;">${order.discount}</span>
			</td>
		</tr>
		</#if>
		<tr>
			<td colspan="4" align="right">
			${action.getText('grandTotal')}:<span style="font-weight:bold;margin-left:10px;">${order.grandTotal}</span>
			</td>
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
	<td>
	${item.subtotal}
	</td>
	</tr>
	</#list>
	</tbody>
</table>

<#if order.memo?has_content>
<div>
<span>${action.getText('memo')}:</span><span style="margin-left:20px;">${order.memo!}</span>
</div>
</#if>
</div>
</body>
</html></#escape>
