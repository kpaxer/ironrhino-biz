<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if order.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('order')}</title>
</head>
<body>
<@s.form action="${getUrl('/order/save')}" method="post" cssClass="ajax">
	<#if !order.new>
		<@s.hidden name="order.id" />
		<div>
		<label class="field" for="customerName">${action.getText('customer')}${action.getText('name')}</label>
		<div>
			${customer.name}
		</div>
		</div>
		<table border="0" width="100%">
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
					<td align="right">
					${action.getText('subtotal')}
					</td>
				</tr>
			</thead>
			<tfoot align="right">
				<tr>
					<td colspan="3">${action.getText('amount')}</td>
					<td>${order.amount}</td>
				</tr>
				<#if order.discount??>
				<tr>
					<td colspan="3">${action.getText('discount')}</td>
					<td>-${order.discount}</td>
				</tr>
				</#if>
				<#if order.freight??>
				<tr>
					<td colspan="3">${action.getText('freight')}</td>
					<td>-${order.freight}</td>
				</tr>
				</#if>
				<tr>
					<td colspan="3">${action.getText('grandTotal')}</td>
					<td style="font-weight:bold;">${order.grandTotal}</td>
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
			<td align="right">
			${item.subtotal}
			</td>
			</tr>
			</#list>
			</tbody>
		</table>
	<#else>
	<div>
		<label class="field" for="customerName">${action.getText('customer')}${action.getText('name')}</label>
		<div>
			<@s.textfield id="customerName" theme="simple" name="customer.name" cssClass="required ajax"/>
			<span class="info" style="font-style:italic;margin-left:20px;"></span>
		</div>
	</div>
	<div>
		<label class="field" for="orderItems">${action.getText('orderItems')}</label>
		<div id="orderItems">
		<table border="0" width="88%">
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
					<td id="amount"></td>
				</tr>
				<tr>
					<td colspan="3">${action.getText('discount')}</td>
					<td>－<input type="text" id="discount" name="order.discount" class="double positive" style="text-align:right;width:60px;" tabindex="10"/></td>
				</tr>
				<tr>
					<td colspan="3">${action.getText('grandTotal')}</td>
					<td id="grandTotal" style="font-weight:bold;"></td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<td width="47%">
						<input type="text" size="5" class="filterselect" style="margin-right:3px;"/>
						<@s.select theme="simple" name="productId" cssClass="required fetchprice" cssStyle="width:230px;" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/>
						<span class="info" style="font-style:italic;margin-left:5px;"></span>
					</td>
					<td width="15%"><input type="text" name="order.items[0].quantity" class="required integer positive quantity"/></td>
					<td width="15%"><input type="text" name="order.items[0].price" class="required double positive price"/></td>
					<td width="14%" align="right"></td>
					<td><@button text="+" class="add"/><@button text="-" class="remove"/></td>
				</tr>
			</tbody>
		</table>
		</div>
	</div>
	</#if>
	<@s.textfield label="%{getText('orderDate')}" name="order.orderDate" cssClass="date required"/>
	<@s.select label="%{getText('salesman')}" name="employee.id" list="employeeList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
	<#if order.new>
	<@s.radio label="%{getText('saleType')}" name="order.saleType" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" />
	<!--
	<div>
		<label class="field">${action.getText('pay')}</label>
		<div>
			<span style="margin-right:5px;">${action.getText('paid')}</span><@s.checkbox theme="simple" name="order.paid" cssStyle="margin-right:20px;"/>
			<span style="margin:5px;">${action.getText('shipped')}</span><@s.checkbox id="shipped" theme="simple" name="order.shipped"/>
			<span style="display:none;">
			<span style="margin:5px;">${action.getText('freight')}</span>－<@s.textfield id="freight"  theme="simple" name="order.freight" cssClass="double positive"/>
		</span>
		</div>
	</div>
	-->
	<#else>
		<#if order.shipped>
			<@s.textfield id="freight" label="%{getText('freight')}" name="order.freight" cssClass="double positive"/>
		</#if>
	</#if>
	<@s.textarea label="%{getText('memo')}" name="order.memo" cssStyle="width:80%;" rows="3"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>