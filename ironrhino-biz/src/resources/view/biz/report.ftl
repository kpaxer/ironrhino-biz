<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('report')}</title>
</head>
<body>
<div class="row">
	<div id="report_format" class="btn-group btn-switch span2 offset5" style="margin-bottom:10px;">
	  <button class="btn active" data-format="PDF">PDF</button>
	  <button class="btn" data-format="XLS">XLS</button>
	</div>
</div>

<div class="portal">
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">${action.getText('order')}</div>
			<div class="portlet-content">
				<h3>${action.getText('order')}${action.getText('detail')}</h3>
				<div style="clear:left;">${action.getText('date')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.textfield id="" name="date" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('customer')} ${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<input id="customerId" type="hidden" name="customer"/>
					<span class="listpick" data-options="{'url':'<@url value="/biz/customer/pick?columns=name,fullAddress"/>','name':'this','id':'#customerId'}">${action.getText('pick')}...</span>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('salesman')} ${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.select theme="simple" cssClass="required" cssStyle="width:80px;" name="salesman" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('deliveryman')} ${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.select theme="simple" cssClass="required" cssStyle="width:80px;" name="deliveryman" list="deliverymanList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('saleType')} ${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:80px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">${action.getText('product')}${action.getText('sales')}</h3>
				<div>${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="productsales"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('customer')} ${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="productsales"/>
					<input id="customerId2" type="hidden" name="customer"/>
					<span class="listpick" data-options="{'url':'<@url value="/biz/customer/pick?columns=name,fullAddress"/>','name':'this','id':'#customerId2'}">${action.getText('pick')}...</span>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('salesman')} ${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="productsales"/>
					<@s.select theme="simple" cssClass="required" cssStyle="width:80px;" name="salesman" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('saleType')} ${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="productsales"/>
					<@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:80px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">${action.getText('sales')}</h3>
				<div>${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="dailysales"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
	</div>
	 
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">${action.getText('reward')}</div>
			<div class="portlet-content">
				<h3>${action.getText('all')}${action.getText('reward')}${action.getText('detail')}</h3>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="privatereward"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<select name="negative" style="width:70px;"><option value="">${action.getText('all')}</option><option value="false">${action.getText('income')}</option><option value="true">${action.getText('outlay')}</option></select>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">${action.getText('single')}${action.getText('reward')}${action.getText('detail')}</h3>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="privatereward"/>
					<input id="employeeId" type="hidden" name="id"/>
					<span class="listpick" data-options="{'url':'<@url value="/biz/employee/pick?columns=name"/>','name':'this','id':'#employeeId'}">${action.getText('pick')}...</span>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<select name="negative" style="width:70px;"><option value="">${action.getText('all')}</option><option value="false">${action.getText('income')}</option><option value="true">${action.getText('outlay')}</option></select>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">${action.getText('reward')}${action.getText('list')}</h3>
				<div>${action.getText('date')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="reward"/>
					<@s.textfield id="" name="date" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('range')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="reward"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">${action.getText('all')}${action.getText('reward')}${action.getText('statistics')}</h3>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="aggregationreward"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<select name="negative" style="width:70px;"><option value="">${action.getText('all')}</option><option value="false">${action.getText('income')}</option><option value="true">${action.getText('outlay')}</option></select>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
	</div>
	 
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">${action.getText('stuff')}</div>
			<div class="portlet-content">
				<h3>${action.getText('stock')}${action.getText('list')}</h3>
				<form>
					<a class="btn report" href="report/jasper?type=product">${action.getText('stock')}</a>
					<a class="btn report" href="report/jasper?type=product&negative=true">-${action.getText('stock')}</a>
					<a class="btn report" href="report/jasper?type=stuff">${action.getText('stuff')}${action.getText('stock')}</a>
				</form>
				<h3>${action.getText('stuffflow')}${action.getText('statistics')}</h3>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="stuffflow"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>	
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
		<div class="portlet">
			<div class="portlet-header">${action.getText('customer')}</div>
			<div class="portlet-content">
				<div>${action.getText('date')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="customer"/>
					<@s.textfield id="" name="date" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('region')}</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="customer"/>
					<input id="regionId" type="hidden" name="id"/>
					<span class="treeselect" data-options="{'url':'<@url value="/region/children"/>','name':'this','id':'#regionId'}">${action.getText('pick')}...</span>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
	</div>
</div>

</body>
</html></#escape>
