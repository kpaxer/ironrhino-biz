<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if returning.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('returning')}</title>
</head>
<body>
<@s.form action="${getUrl(actionBaseUrl+'/save')}" method="post" cssClass="ajax form-horizontal reset">
	<#if !returning.new>
		<@s.hidden name="returning.id" />
	</#if>
	<div class="control-group">
		<label class="control-label" for="customerName">${action.getText('customer')}${action.getText('name')}</label>
		<div class="controls">
			<@s.textfield id="customerName" theme="simple" name="customer.name" cssClass="required customerName"/>
			<span class="info" style="font-style:italic;margin-left:20px;"></span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="orderItems">${action.getText('orderItems')}</label>
		<div id="orderItems" class="controls">
		<table class="table table-condensed middle atleastone" style="table-layout:fixed;">
			<thead>
				<tr>
					<td style="width:47%;">${action.getText('product')}</td>
					<td>${action.getText('quantity')}</td>
					<td>${action.getText('price')}</td>
					<td>${action.getText('subtotal')}</td>
					<td class="manipulate"></td>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="3">${action.getText('amount')}</td>
					<td id="amount" style="text-align:right;">${returning.amount!}</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="3">${action.getText('freight')}</td>
					<td style="text-align:right;"><@s.textfield id="freight" name="returning.freight" theme="simple" cssClass="double positive add" cssStyle="text-align:right;width:60px;" tabindex="10"/></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="3">${action.getText('grandTotal')}</td>
					<td id="grandTotal" style="font-weight:bold;text-align:right;">${returning.grandTotal!}</td>
					<td></td>
				</tr>
			</tfoot>
			<tbody>
			<#assign size = 0>
			<#if productId?? && productId?size gt 0>
				<#assign size = productId?size-1>
			</#if>
			<#list 0..size as index>
				<tr>
					<td>
						<@s.select theme="simple" id="" name="productId" value="${(productId[index])!}" cssClass="required fetchprice chosen" cssStyle="width:260px;" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue=""/>
						<span class="info" style="font-style:italic;margin-left:5px;"></span>
					</td>
					<td><@s.textfield theme="simple" name="returning.items[${index}].quantity" cssClass="required integer positive quantity" cssStyle="width:40px;"/></td>
					<td><@s.textfield theme="simple" name="returning.items[${index}].price" cssClass="required double positive price" cssStyle="width:60px;"/></td>
					<td style="text-align:right;"><span class="info">${(returning.items[index].subtotal)!}</span></td>
					<td class="manipulate"></td>
				</tr>
			</#list>
			</tbody>
		</table>
		</div>
	</div>
	<@s.textfield label="%{getText('returnDate')}" name="returning.returnDate" cssClass="date required"/>
	<@s.select label="%{getText('station')}" name="stationId" list="stationList" listKey="id" listValue="name" headerKey="" headerValue=""/>
	<@s.select label="%{getText('salesman')}" name="salesman.id" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue=""/>
	<@s.textarea label="%{getText('memo')}" name="returning.memo" cssStyle="width:80%;height:50px;"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>