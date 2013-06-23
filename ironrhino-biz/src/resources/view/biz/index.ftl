<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('index')}</title>
</head>
<body>

<div class="portal savable">

	<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_SALESMAN">
	<div>
		<ul class="portal-column">
			<li id="inputorder" class="portlet">
				<div class="portlet-header">${action.getText('create')}${action.getText('order')}</div>
				<div class="portlet-content">
					<div class="ajaxpanel" data-url="order/input"></div>
				</div>
			</li>
		</ul>
	</div>
	</@authorize>

	<ul class="portal-column">
		<li id="uninputed" class="portlet">
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
		</li>
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_SALESMAN">
		<li id="unpaid" class="portlet">
			<div class="portlet-header">${action.getText('unpaid')}${action.getText('order')}</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="order/unpaid"></div>
			</div>
		</li>
		</@authorize>
	</ul>
	
	<ul class="portal-column">
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_HR">
		<li id="inputreward" class="portlet">
			<div class="portlet-header">${action.getText('create')}${action.getText('reward')}</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="reward/input"></div>
			</div>
		</li>
		</@authorize>
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_SALESMAN">
		<li id="unshipped" class="portlet">
			<div class="portlet-header">${action.getText('unshipped')}${action.getText('order')}</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="order/unshipped"></div>
			</div>
		</li>
		</@authorize>
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_PLANMANAGER">
		<li id="uncompleted" class="portlet">
			<div class="portlet-header">${action.getText('uncompleted')}${action.getText('plan')}</div>
			<div class="portlet-content">
				<div class="ajaxpanel" data-url="plan/uncompleted"></div>
			</div>
		</li>
		</@authorize>
		<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_CUSTOMERMANAGER">
		<li id="inactive" class="portlet">
			<div class="portlet-header">${action.getText('inactive')}${action.getText('customer')}</div>
			<div class="portlet-content">
				<form method="get" action="customer" class="form-inline" target="_blank">
				<label for="threshold">${action.getText('days')}:</label><input type="text" id="threshold" name="threshold" value="60" style="width:50px;" cssClass="required integer positive"/>
				<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</li>
		</@authorize>
	</ul>

	
</div>

</body>
</html></#escape>