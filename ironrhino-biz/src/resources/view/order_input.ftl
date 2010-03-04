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
			<label for="customerName">${action.getText('customer')}${action.getText('name')}</label>
			<div>
				<@s.textfield id="customerName" theme="simple" name="customer.name" cssClass="required ajax autocomplete_off"/>
				<span class="info" style="margin-left:20px;"></span>
			</div>
		</p>
	
	<@s.textfield label="%{getText('orderDate')}" name="order.orderDate" cssClass="date"/>
	<@s.if test="%{order.isNew()}">
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
					${action.getText('discount')}:<@s.textfield id="discount" theme="simple" name="order.discount" cssClass="double autocomplete_off" cssStyle="margin-left:10px;"/>
					</td>
					<td colspan="2" align="right">
					${action.getText('grandTotal')}:<span id="grandTotal" style="font-weight:bold;margin-left:10px;"></span>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<td><@s.select theme="simple" name="productId" cssClass="required" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/></td>
					<td><@s.textfield theme="simple" name="order.items[0].quantity" cssClass="required integer positive autocomplete_off"/></td>
					<td><@s.textfield theme="simple" name="order.items[0].price" cssClass="required double positive autocomplete_off"/></td>
					<td></td>
					<td><@button text="+" class="add"/><@button text="-" class="remove" style="margin-left:2px;"/></td>
				</tr>
			</tbody>
		</table>
		</div>
	</p>
	</@s.if>
	<@s.checkbox label="%{getText('paid')}" name="order.paid"/>
	<@s.checkbox label="%{getText('shipped')}" name="order.shipped"/>
	<@s.if test="%{!order.isNew()}">
		<@s.checkbox label="%{getText('cancelled')}" name="order.cancelled"/>
	</@s.if>
	<@s.textarea label="%{getText('memo')}" name="order.memo" cssStyle="width:80%;" rows="3"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>