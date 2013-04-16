<ul class="nav">
	<li><a class="ajax view" href="<@url value="/biz/"/>">${action.getText('index')}</a></li>
	<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_CUSTOMERMANAGER">
	<li><a class="ajax view" href="<@url value="/biz/customer"/>">${action.getText('customer')}</a></li>
	<li><a class="ajax view" href="<@url value="/biz/station"/>">${action.getText('station')}</a></li>
	</@authorize>
	<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_SALESMAN">
	<li><a class="ajax view" href="<@url value="/biz/order"/>">${action.getText('order')}</a>
	<li><a class="ajax view" href="<@url value="/biz/returning"/>">${action.getText('returning')}</a></li>
	</@authorize>
	<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_PLANMANAGER">
	<li><a class="ajax view" href="<@url value="/biz/plan"/>">${action.getText('plan')}</a></li>
	</@authorize>
	<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_WAREHOUSEMAN">
	<li><a class="ajax view" href="<@url value="/biz/stuff"/>">${action.getText('stuff')}</a></li>
	</@authorize>
	<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_HR">
	<li><a class="ajax view" href="<@url value="/biz/reward"/>">${action.getText('reward')}</a></li>
	<li><a class="ajax view" href="<@url value="/biz/employee"/>">${action.getText('employee')}</a></li>
	</@authorize>
	<@authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_PRODUCTMANAGER">
	<li><a class="ajax view" href="<@url value="/biz/product"/>">${action.getText('product')}</a></li>
	<li><a class="ajax view" href="<@url value="/biz/brand"/>">${action.getText('brand')}</a></li>
	<li><a class="ajax view" href="<@url value="/biz/category"/>">${action.getText('category')}</a></li>
	</@authorize>
	<@authorize ifAnyGranted="ROLE_ADMINISTRATOR">
	<li><a class="ajax view" href="<@url value="/biz/report"/>">${action.getText('report')}</a></li>
    <li><a class="ajax view hidden-tablet hidden-phone hidden-pad" href="<@url value="/biz/chart"/>">${action.getText('chart')}</a></li>
    </@authorize>
</ul>