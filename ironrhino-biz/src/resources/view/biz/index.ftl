<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('index')}</title>
</head>
<body>

<div class="portal savable">

	<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_SALESMAN">
	<div class="portal-column full">
		<div id="inputorder" class="portlet">
			<div class="portlet-header">${action.getText('create')}${action.getText('order')}</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="order/input"></div>
			</div>
		</div>
	</div>
	</@authorize>

	<div class="portal-column half">
		
		<div id="uninputed" class="portlet">
			<div class="portlet-header">${action.getText('uninputed')}${action.getText('date')}</div>
			<div class="portlet-content">
				<div class="row">
				<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_SALESMAN">
				<div class="ajaxpanel span6" data-url="order/uninputed" style="width:40%;"></div>
				</@authorize>
				<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_HR">
				<div class="ajaxpanel span6" data-url="reward/uninputed" style="width:40%;"></div>
				</@authorize>
				</div>
			</div>
		</div>
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_SALESMAN">
		<div id="unpaid" class="portlet">
			<div class="portlet-header">${action.getText('unpaid')}${action.getText('order')}</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="order/unpaid"></div>
			</div>
		</div>
		</@authorize>
	</div>
	
	<div class="portal-column half">
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_HR">
		<div id="inputreward" class="portlet">
			<div class="portlet-header">${action.getText('create')}${action.getText('reward')}</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="reward/input"></div>
			</div>
		</div>
		</@authorize>
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_SALESMAN">
		<div id="unshipped" class="portlet">
			<div class="portlet-header">${action.getText('unshipped')}${action.getText('order')}</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="order/unshipped"></div>
			</div>
		</div>
		</@authorize>
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_PLANMANAGER">
		<div id="uncompleted" class="portlet">
			<div class="portlet-header">${action.getText('uncompleted')}${action.getText('plan')}</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="plan/uncompleted"></div>
			</div>
		</div>
		</@authorize>
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_CUSTOMERMANAGER">
		<div id="inactive" class="portlet">
			<div class="portlet-header">${action.getText('inactive')}${action.getText('customer')}</div>
			<div class="portlet-content">
				<form method="get" action="customer" class="form-inline" target="_blank">
				<label for="threshold">${action.getText('days')}:</label><input type="text" id="threshold" name="threshold" value="60" style="width:50px;" cssClass="required integer positive"/>
				<@s.submit theme="simple"/>
				</form>
			</div>
		</div>
		</@authorize>
	</div>
	

	
</div>

</body>
</html></#escape>