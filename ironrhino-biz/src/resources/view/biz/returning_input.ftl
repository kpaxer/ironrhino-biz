<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if returning.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('returning')}</title>
</head>
<body>
<@s.form action="${getUrl(actionBaseUrl+'/save')}" method="post" cssClass="ajax reset">
	<#if !returning.new>
		<@s.hidden name="returning.id" />
	</#if>
	<div class="field clearfix">
		<label class="field" for="customerName">${action.getText('customer')}${action.getText('name')}</label>
		<div>
			<@s.textfield id="customerName" theme="simple" name="customer.name" cssClass="required customerName"/>
			<span class="info" style="font-style:italic;margin-left:20px;"></span>
		</div>
	</div>
	<div class="field clearfix">
		<label class="field" for="orderItems">${action.getText('orderItems')}</label>
		<div id="orderItems">
		<table border="0" width="90%" class="atleastone">
			<thead>
				<tr>
					<td>${action.getText('product')}</td>
					<td>${action.getText('quantity')}</td>
					<td>${action.getText('price')}</td>
					<td>${action.getText('subtotal')}</td>
				</tr>
			</thead>
			<tfoot align="right">
				<tr>
					<td colspan="3">${action.getText('amount')}</td>
					<td id="amount">${returning.amount!}</td>
				</tr>
				<tr>
					<td colspan="3">${action.getText('freight')}</td>
					<td><@s.textfield id="freight" name="returning.freight" theme="simple" cssClass="double positive add" cssStyle="text-align:right;width:60px;" tabindex="10"/></td>
				</tr>
				<tr>
					<td colspan="3">${action.getText('grandTotal')}</td>
					<td id="grandTotal" style="font-weight:bold;">${returning.grandTotal!}</td>
				</tr>
			</tfoot>
			<tbody>
			<#assign size = 0>
			<#if productId?? && productId?size gt 0>
				<#assign size = productId?size-1>
			</#if>
			<#list 0..size as index>
				<tr>
					<td width="47%">
						<input type="text" size="5" class="filterselect" style="margin-right:3px;"/>
						<@s.select theme="simple" name="productId" value="${(productId[index])!}" cssClass="required fetchprice" cssStyle="width:230px;" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/>
						<span class="info" style="font-style:italic;margin-left:5px;"></span>
					</td>
					<td width="13%"><@s.textfield name="returning.items[${index}].quantity" cssClass="required integer positive quantity"/></td>
					<td width="13%"><@s.textfield name="returning.items[${index}].price" cssClass="required double positive price"/></td>
					<td width="15%" align="right"><span class="info">${(returning.items[index].subtotal)!}</span></td>
					<td><button type="button" class="btn add">+</button><button type="button" class="btn remove">-</button><button type="button" class="btn moveup">↑</button><button type="button" class="btn movedown">↓</button></td>
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