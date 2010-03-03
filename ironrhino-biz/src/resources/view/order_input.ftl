<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if order.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('order')}</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<@s.if test="%{!order.isNew()}">
		<@s.hidden name="order.id" />
	</@s.if>
		<p>
			<label for="customerId">${action.getText('customer')}</label>
			<div>
			<#if customer??>
				<@s.hidden name="customer.id" />${customer.name}
			<#else>
				<@s.textfield id="customerId" theme="simple" name="customer.id" size="8" cssClass="required ajax"/>
				&nbsp;&nbsp;<span></span>
			</#if>
			</div>
		</p>
	
	<@s.textfield label="%{getText('orderDate')}" name="order.orderDate" cssClass="date"/>
	<p>
		<label for="orderItems">${action.getText('orderItems')}</label>
		<div id="orderItems">
		<table border="0" width="80%">
			<thead>
				<tr>
					<td>${action.getText('product')}</td>
					<td>${action.getText('quantity')}</td>
					<td>${action.getText('price')}</td>
					<td>${action.getText('subtotal')}</td>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="2">
					${action.getText('discount')}:&nbsp;<@s.textfield id="discount" theme="simple" name="order.discount" cssClass="double"/>
					</td>
					<td colspan="2" align="right">
					${action.getText('grandTotal')}:&nbsp;<span id="grandTotal" style="font-weight:bold;"></span>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<td><@s.select theme="simple" name="productId" cssClass="required" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/></td>
					<td><@s.textfield theme="simple" name="order.items[0].quantity" cssClass="required integer"/></td>
					<td><@s.textfield theme="simple" name="order.items[0].price" cssClass="required double"/></td>
					<td></td>
				</tr>
			</tbody>
		</table>
		</div>
	</p>
	
	<@s.textarea label="%{getText('memo')}" name="order.memo" cssStyle="width:80%;" rows="3"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>