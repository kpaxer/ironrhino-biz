<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
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
				<div class="ajaxpanel" data-url="order/uninputed" style="float:left;width:150px;"></div>
				<div class="ajaxpanel" data-url="reward/uninputed" style="float:left;width:150px;"></div>
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
				<form method="get" action="customer" class="inline" target="_blank">
				<div class="control-group"><label for="threshold">天数:</label><input type="text" id="threshold" name="threshold" value="60" size="5" cssClass="required integer positive"/></div>
				<div class="control-group"><@s.submit theme="simple" value="确定"/></div>
				</form>
			</div>
		</div>
	</div>
	

	
</div>

</body>
</html></#escape>