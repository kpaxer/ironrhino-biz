<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('view')}${action.getText('returning')}</title>
</head>
<body>
<div id="info">
<div>
<span style="margin-left:5px;">${action.getText('customer')}:</span><span>${returning.customer}</span>
<span style="margin-left:5px;">${action.getText('phone')}:</span><span>${returning.customer.phone!}&nbsp;${returning.customer.mobile!}</span>
<span style="margin-left:5px;">${action.getText('address')}:</span><span>${returning.customer.fullAddress!}</span>
<#if returning.customer.tags?size gt 0>
<span style="margin-left:5px;">${action.getText('tag')}:</span><span>${returning.customer.tagsAsString!}</span>
</#if>
</div>
<div>
<span style="margin-left:5px;">${action.getText('returnDate')}:</span><span>${returning.returnDate?string('yyyy年MM月dd日')}</span>
<#if returning.salesman??>
<span style="margin-left:5px;">${action.getText('salesman')}:</span><span>${returning.salesman.name}</span>
</#if>
</div>
<div>
<#if returning.station??>
<span style="margin-left:5px;">${action.getText('station')}:</span><span>${returning.station.name}</span>
</#if>
</div>
<div>
<#if returning.createUser??>
<span style="margin-left:5px;">${action.getText('createUser')}:</span><span>${returning.createUser.name}</span>
<span style="margin-left:5px;">${action.getText('createDate')}:</span><span>${returning.createDate?string('yyyy-MM-dd HH:mm:ss')}</span>
</#if>
<#if returning.modifyUser??>
<span style="margin-left:5px;">${action.getText('modifyUser')}:</span><span>${returning.modifyUser.name}</span>
<span style="margin-left:5px;">${action.getText('modifyDate')}:</span><span>${returning.modifyDate?string('yyyy-MM-dd HH:mm:ss')}</span>
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
	<tfoot align="right">
		<tr>
			<td colspan="3">${action.getText('amount')}</td>
			<td style="text-align:right;">${returning.amount}</td>
		</tr>
		<#if returning.freight??>
		<tr>
			<td colspan="3">${action.getText('freight')}</td>
			<td style="text-align:right;">${returning.freight}</td>
		</tr>
		</#if>
		<tr>
			<td colspan="3">${action.getText('grandTotal')}</td>
			<td style="font-weight:bold;text-align:right;">${returning.grandTotal}</td>
		</tr>
	</tfoot>
	<tbody>
	<#list returning.items as item>
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
<#if returning.memo?has_content>
<div class="well">${returning.memo!}</div>
</#if>
</div>
</body>
</html></#escape>
