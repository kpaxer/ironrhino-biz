<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
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
		<table class="table atleastone" style="table-layout:fixed;">
			<thead>
				<tr>
					<td width="47%">${action.getText('product')}</td>
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
						<input type="text" class="filterselect" style="margin-right:3px;width:50px;"/>
						<@s.select theme="simple" name="productId" value="${(productId[index])!}" cssClass="required fetchprice" cssStyle="width:230px;" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/>
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
	<@s.select label="%{getText('station')}" name="stationId" cssStyle="width:200px;" list="stationList" listKey="id" listValue="name" headerKey="" headerValue=""/>
	<@s.select label="%{getText('salesman')}" name="salesman.id" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue=""/>
	<@s.textarea label="%{getText('memo')}" name="returning.memo" cssStyle="width:80%;" rows="3"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>