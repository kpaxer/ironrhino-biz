<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if order.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('order')}</title>
</head>
<body>
<@s.form action="${actionBaseUrl}/save" method="post" cssClass="ajax form-horizontal reset">
	<#if !order.new>
		<@s.hidden name="order.id" />
	</#if>
	<div class="row-fluid">
		<div class="span5" style="min-height:60px;"><@s.textfield id="customerName" label="%{getText('customer')}" name="customer.name" cssClass="required customerName"><@s.param name="after"><span class="info" style="font-style:italic;margin-left:15px;"></span></@s.param></@s.textfield></div>
		<div class="span3"><@s.textfield label="${action.getText('date')}" name="order.orderDate" cssClass="date required"/></div>
		<div class="span4"><@s.radio theme="simple" name="order.saleType" cssClass="custom" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" /></div>
	</div>
    
	<div>
		<div id="orderItems">
		<table class="table table-bordered middle" style="table-layout:fixed;">
			<thead>
				<tr>
					<td style="width:47%;">${action.getText('product')}</td>
					<td>${action.getText('quantity')}</td>
					<td>${action.getText('price')}</td>
					<td style="width:50px;">${action.getText('freegift')}</td>
					<td>${action.getText('subtotal')}</td>
					<td class="manipulate"></td>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="4" style="text-align:right;">${action.getText('amount')}</td>
					<td id="amount" style="text-align:right;">${order.amount!}</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="4" style="text-align:right;">${action.getText('discount')}</td>
					<td style="text-align:right;">－<@s.textfield id="discount" name="order.discount" theme="simple" cssClass="double positive" cssStyle="text-align:right;width:40px;" tabindex="10"/></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="4" style="text-align:right;">${action.getText('grandTotal')}</td>
					<td id="grandTotal" style="font-weight:bold;text-align:right;">${order.grandTotal!}</td>
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
						<@s.select id="" theme="simple" name="productId" value="${(productId[index])!}" cssClass="required fetchprice chosen decrease" cssStyle="width:260px;" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue=""/>
						<span class="info" style="font-style:italic;margin-left:5px;"></span>
					</td>
					<td><@s.textfield theme="simple" name="order.items[${index}].quantity" cssClass="required integer positive quantity" cssStyle="width:40px;"/></td>
					<td><@s.textfield theme="simple" name="order.items[${index}].price" cssClass="required double positive price" cssStyle="width:60px;"/></td>
					<td style="text-align:center;"><input type="checkbox" class="freegift"<#if order.items[index]?? && order.items[index].price == 0> checked</#if>></td>
					<td style="text-align:right;"><span class="info">${(order.items[index].subtotal)!}</span></td>
					<td class="manipulate"></td>
				</tr>
			</#list>
			</tbody>
		</table>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span3"><@s.select label="%{getText('salesman')}" name="salesman.id" cssClass="input-medium" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue=""/></div>
		<div class="span5">
			<div class="control-group">
				<label class="control-label">${action.getText('pay')}</label>
				<div class="controls">
					<label class="checkbox inline" style="margin-right:20px;"><@s.checkbox id="paid" theme="simple" name="order.paid" cssClass="custom"/></label>
					<span class="toggle"<#if !order.paid> style="display:none;"</#if>>
						<span style="margin:5px;">${action.getText('payDate')}</span><@s.textfield theme="simple" name="order.payDate"  cssClass="date"/>
					</span>
				</div>
			</div>
		</div>
		<div class="span4"><span style="margin:5px;">${action.getText('freight')}</span>－<@s.textfield id="freight"  theme="simple" name="order.freight" cssClass="double positive"/></div>
	</div>
	<div class="row-fluid">
		<div class="span3"><@s.select label="%{getText('deliveryman')}" name="deliveryman.id" cssClass="input-medium" list="deliverymanList" listKey="id" listValue="name" headerKey="" headerValue=""/></div>
		<div class="span5">
			<div class="control-gropu">
				<label class="control-label">${action.getText('ship')}</label>
				<div class="controls">
					<label class="checkbox inline" style="margin-right:20px;"><@s.checkbox id="shipped" theme="simple" name="order.shipped" cssClass="custom"/></label>
					<span class="toggle"<#if !order.shipped> style="display:none;"</#if>>
						<span style="margin:5px;">${action.getText('shipDate')}</span><@s.textfield theme="simple" name="order.shipDate" cssClass="date"/>
					</span>
				</div>
			</div>
		</div>
		<div class="span4"><span style="margin:5px;">${action.getText('station')}</span><@s.select theme="simple" name="stationId" cssClass="chosen" list="stationList" listKey="id" listValue="name" headerKey="" headerValue=""/></div>
	</div>
	<@s.textarea label="%{getText('memo')}" name="order.memo" cssStyle="width:95%;height:40px;"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>