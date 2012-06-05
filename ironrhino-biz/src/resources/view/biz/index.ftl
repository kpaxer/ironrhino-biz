<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>首页</title>
</head>
<body>

<div class="portal savable">

	<div class="portal-column full">
		<div id="inputorder" class="portlet">
			<div class="portlet-header">输入订单</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="order/input"></div>
			</div>
		</div>
	</div>

	<div class="portal-column half">
		<div id="unpaid" class="portlet">
			<div class="portlet-header">未输单的日期</div>
			<div class="portlet-content">
				<div class="row">			
				<div class="ajaxpanel span6" data-url="order/uninputed" style="width:45%;"></div>
				<div class="ajaxpanel span6" data-url="reward/uninputed" style="width:45%;"></div>
				</div>
			</div>
		</div>
		<div id="unpaid" class="portlet">
			<div class="portlet-header">未付款的订单</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="order/unpaid"></div>
			</div>
		</div>
	</div>
	
	<div class="portal-column half">
		<div id="inputreward" class="portlet">
			<div class="portlet-header">录入工资</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="reward/input"></div>
			</div>
		</div>
		<div id="unshipped" class="portlet">
			<div class="portlet-header">未发货的订单</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="order/unshipped"></div>
			</div>
		</div>
		<div id="uncompleted" class="portlet">
			<div class="portlet-header">未完成的计划</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="plan/uncompleted"></div>
			</div>
		</div>
		<div id="inactive" class="portlet">
			<div class="portlet-header">长时间没下单的客户</div>
			<div class="portlet-content">
				<form method="get" action="customer" class="form-inline" target="_blank">
				<label for="threshold">天数:</label><input type="text" id="threshold" name="threshold" value="60" style="width:50px;" cssClass="required integer positive"/>
				<@s.submit theme="simple" value="确定"/>
				</form>
			</div>
		</div>
	</div>
	

	
</div>

</body>
</html></#escape>