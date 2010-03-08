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
		<div>
			<label class="field" for="customerName">${action.getText('customer')}${action.getText('name')}</label>
			<div>
				<@s.textfield id="customerName" theme="simple" name="customer.name" cssClass="required ajax"/>
				<span class="info" style="font-style:italic;margin-left:20px;"></span>
			</div>
		</div>
	
	<@s.if test="%{order.isNew()}">
	<div>
		<label class="field" for="orderItems">${action.getText('orderItems')}</label>
		<div id="orderItems">
		<table border="0" width="90%">
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
					${action.getText('discount')}:<input type="text" id="discount" name="order.discount" class="double positive" style="margin-left:10px;" tabindex="10"/>
					</td>
					<td colspan="2" align="right">
					${action.getText('grandTotal')}:<span id="grandTotal" style="font-weight:bold;margin-left:10px;"></span>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<td width="50%">
						<input type="text" size="5" class="filterselect" style="margin-right:3px;"/>
						<@s.select theme="simple" name="productId" cssClass="required fetchprice" cssStyle="width:100px;" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/>
						<span class="info" style="font-style:italic;margin-left:5px;"></span>
					</td>
					<td width="8%"><input type="text" name="order.items[0].quantity" class="required integer positive quantity"/></td>
					<td width="8%"><input type="text" name="order.items[0].price" class="required double positive price"/></td>
					<td align="right"></td>
					<td><@button text="+" class="add"/><@button text="-" class="remove" style="margin-left:2px;"/></td>
				</tr>
			</tbody>
		</table>
		</div>
	</div>
	</@s.if>
	<@s.radio label="%{getText('saleType')}" name="order.saleType" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" />
	<@s.textfield label="%{getText('orderDate')}" name="order.orderDate" cssClass="date required"/>
	<#if order.new>
		<@s.checkbox label="%{getText('paid')}" name="order.paid"/>
		<@s.checkbox label="%{getText('shipped')}" name="order.shipped"/>
	</#if>
	<@s.textarea label="%{getText('memo')}" name="order.memo" cssStyle="width:80%;" rows="3"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>