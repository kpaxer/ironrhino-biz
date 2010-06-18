<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>首页</title>
<style>
.portal-column { 
	width: 50%;
	}
</style>
</head>
<body>


<div class="portal savable">

	<div class="portal-column" style="width:100%;">
		<div id="inputorder" class="portlet">
			<div class="portlet-header">输入订单</div>
			<div class="portlet-content">
				<div class="ajaxpanel" url="<@url value="/order/input"/>"></div>
			</div>
		</div>
	</div>

	<div class="portal-column">
		<div id="unpaid" class="portlet">
			<div class="portlet-header">未付款的订单</div>
			<div class="portlet-content">
				<div class="ajaxpanel" url="<@url value="/order/unpaid"/>"></div>
			</div>
		</div>
	</div>
	
	<div class="portal-column">
		<div id="inputreward" class="portlet">
			<div class="portlet-header">录入工资</div>
			<div class="portlet-content">
				<div class="ajaxpanel" url="<@url value="/reward/input"/>"></div>
			</div>
		</div>
		<div id="unshipped" class="portlet">
			<div class="portlet-header">未发货的订单</div>
			<div class="portlet-content">
				<div class="ajaxpanel" url="<@url value="/order/unshipped"/>"></div>
			</div>
		</div>
		<div id="uncompleted" class="portlet">
			<div class="portlet-header">未完成的计划</div>
			<div class="portlet-content">
				<div class="ajaxpanel" url="<@url value="/plan/uncompleted"/>"></div>
			</div>
		</div>
		<div id="inactive" class="portlet">
			<div class="portlet-header">长时间没下单的客户</div>
			<div class="portlet-content">
				<form method="get" action="<@url value="/customer"/>" class="line" target="_blank">
				<div class="field"><label for="threshold">天数:</label><input id="threshold" name="threshold" value="60" size="5" cssClass="required integer positive"/></div>
				<div class="field"><@s.submit theme="simple" value="确定"/></div>
				</form>
			</div>
		</div>
	</div>
	

	
</div>

</body>
</html></#escape>