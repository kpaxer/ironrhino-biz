<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('view')}${action.getText('order')}</title>
</head>
<body>
<div id="info">
<div>
<span>${action.getText('code')}:&nbsp;&nbsp;</span><span>${order.code}</span>
&nbsp;&nbsp;&nbsp;&nbsp;<span>${action.getText('customer')}:&nbsp;&nbsp;</span><span>${order.customer}</span>
&nbsp;&nbsp;&nbsp;&nbsp;<span>${action.getText('orderDate')}:&nbsp;&nbsp;</span><span>${order.orderDate?string('yyyy年MM月dd日')}</span>
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
			${action.getText('discount')}:&nbsp;<span>${order.discount}</span>
			</td>
		</tr>
		</#if>
		<tr>
			<td colspan="4" align="right">
			${action.getText('grandTotal')}:&nbsp;<span style="font-weight:bold;">${order.grandTotal}</span>
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
<span>${action.getText('memo')}:&nbsp;&nbsp;</span><span>${order.memo!}</span>
</div>
</#if>
</div>
</body>
</html></#escape>
