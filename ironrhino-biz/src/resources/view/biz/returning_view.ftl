<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('view')}${action.getText('returning')}</title>
</head>
<body>
<table id="details" class="table<#if !Parameters.printpage??> table-bordered</#if> middle">
	<tbody>
		<tr>
			<td class="fieldlabel">${action.getText('returnDate')}</td><td style="width:24%;">${(returning.returnDate?string('yyyy年MM月dd日'))!}</td>
			<td class="fieldlabel">${action.getText('customer')}</td><td><a href="${getUrl("/biz/order")}?customer.id=${returning.customer.id}" target="_blank">${returning.customer}</a></td>
			<td class="fieldlabel">${action.getText('phone')}</td><td style="width:24%;">${returning.customer.phone!}&nbsp;${returning.customer.mobile!}</td>
		</tr>
		<tr>
			<td class="fieldlabel">${action.getText('station')}</td><td>${(returning.station.name)!}</td>
			<td class="fieldlabel">${action.getText('salesman')}</td><td>${(returning.salesman.name)!}</td>
			<td class="fieldlabel">${action.getText('createUser')}</td><td>${returning.createUser!}</td>
		</tr>
		<tr>
			<td class="fieldlabel">${action.getText('createDate')}</td><td>${returning.createDate!}</td>
			<td class="fieldlabel">${action.getText('modifyUser')}</td><td>${returning.modifyUser!}</td>
			<td class="fieldlabel">${action.getText('modifyDate')}</td><td>${returning.modifyDate!}</td>
		</tr>
	</tbody>
</table>
<table id="items" class="table<#if !Parameters.printpage??> table-bordered</#if> middle">
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
			<td colspan="3" style="text-align:right;">${action.getText('amount')}</td>
			<td style="text-align:right;">${returning.amount}</td>
		</tr>
		<#if returning.freight??>
		<tr>
			<td colspan="3" style="text-align:right;">${action.getText('freight')}</td>
			<td style="text-align:right;">${returning.freight}</td>
		</tr>
		</#if>
		<tr>
			<td colspan="3" style="text-align:right;">${action.getText('grandTotal')}</td>
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
<pre id="memo">${returning.memo!}</pre>
</#if>
<#if !Parameters.printpage??>
<div style="text-align:center;">
	<a href="${getUrl(actionBaseUrl+'/view/'+returning.id+'?decorator=simple&printpage=true')}" target="_blank" class="btn">${action.getText('print')}</a>
</div>
</#if>
</body>
</html></#escape>
