<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if order.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('order')}</title>
</head>
<body>
<@s.form action="${getUrl('/order/save')}" method="post" cssClass="ajax reset">
	<#if !order.new>
		<@s.hidden name="order.id" />
	</#if>
	<div class="field">
		<label class="field" for="customerName">${action.getText('customer')}${action.getText('name')}</label>
		<div>
			<@s.textfield id="customerName" theme="simple" name="customer.name" cssClass="required customerName"/>
			<span class="info" style="font-style:italic;margin-left:20px;"></span>
		</div>
	</div>
	<div class="field">
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
			<tfoot align="right">
				<tr>
					<td colspan="3">${action.getText('amount')}</td>
					<td id="amount"></td>
				</tr>
				<tr>
					<td colspan="3">${action.getText('discount')}</td>
					<td>－<@s.textfield id="discount" name="order.discount" theme="simple" cssClass="double positive" cssStyle="text-align:right;width:60px;" tabindex="10"/></td>
				</tr>
				<tr>
					<td colspan="3">${action.getText('grandTotal')}</td>
					<td id="grandTotal" style="font-weight:bold;">${order.grandTotal!}</td>
				</tr>
			</tfoot>
			<tbody>
			<#if !order.new>
				<#list 0..productId?size-1 as index>
				<tr>
					<td width="47%">
						<input type="text" size="5" class="filterselect" style="margin-right:3px;"/>
						<@s.select theme="simple" name="productId" value="${productId[index]}" cssClass="required fetchprice" cssStyle="width:230px;" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/>
						<span class="info" style="font-style:italic;margin-left:5px;"></span>
					</td>
					<td width="15%"><@s.textfield name="order.items[${index}].quantity" cssClass="required integer positive quantity"/></td>
					<td width="15%"><@s.textfield name="order.items[${index}].price" cssClass="required double positive price"/></td>
					<td width="14%" align="right"><span class="info">${order.items[index].subtotal}</span></td>
					<td><@button text="+" class="add"/><@button text="-" class="remove"/></td>
				</tr>
				</#list>
			<#else>
				<tr>
					<td width="47%">
						<input type="text" size="5" class="filterselect" style="margin-right:3px;"/>
						<@s.select theme="simple" name="productId" cssClass="required fetchprice" cssStyle="width:230px;" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/>
						<span class="info" style="font-style:italic;margin-left:5px;"></span>
					</td>
					<td width="15%"><input type="text" name="order.items[0].quantity" class="required integer positive quantity"/></td>
					<td width="15%"><input type="text" name="order.items[0].price" class="required double positive price"/></td>
					<td width="14%" align="right"><span class="info"></span></td>
					<td><@button text="+" class="add"/><@button text="-" class="remove"/></td>
				</tr>
			</#if>
			</tbody>
		</table>
		</div>
	</div>
	<@s.textfield label="%{getText('orderDate')}" name="order.orderDate" cssClass="date required"/>
	<@s.select label="%{getText('salesman')}" name="salesman.id" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue=""/>
	<@s.radio label="%{getText('saleType')}" name="order.saleType" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" />
	<@s.select label="%{getText('deliveryman')}" name="deliveryman.id" list="deliverymanList" listKey="id" listValue="name" headerKey="" headerValue=""/>
	<div class="field">
		<label class="field">${action.getText('pay')}</label>
		<div>
			<span>${action.getText('paid')}</span><@s.checkbox id="paid" theme="simple" name="order.paid" cssStyle="margin-right:20px;"/>
			<span<#if !order.paid> style="display:none;"</#if>>
				<span style="margin:5px;">${action.getText('payDate')}</span><@s.textfield theme="simple" name="order.payDate"  cssClass="date"/>
			</span>
		</div>
	</div>
	<div class="field">
		<label class="field">${action.getText('ship')}</label>
		<div>
			<span>${action.getText('shipped')}</span><@s.checkbox id="shipped" theme="simple" name="order.shipped"/>
			<span<#if !order.shipped> style="display:none;"</#if>>
				<span style="margin:5px;">${action.getText('shipDate')}</span><@s.textfield theme="simple" name="order.shipDate" cssClass="date"/>
			</span>
			<span style="margin:5px;">${action.getText('freight')}</span>－<@s.textfield id="freight"  theme="simple" name="order.freight" cssClass="double positive"/>
			<span style="margin:5px;">${action.getText('station')}</span>
			<@s.select theme="simple" name="stationId" cssStyle="width:200px;" list="stationList" listKey="id" listValue="name" headerKey="" headerValue=""/>
		</div>
	</div>
	<@s.textarea label="%{getText('memo')}" name="order.memo" cssStyle="width:80%;" rows="3"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>