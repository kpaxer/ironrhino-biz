<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('chart')}</title>
</head>
<body>
<div id="c" style="margin-bottom:10px;"></div>
<div class="portal savable">
	<ul class="portal-column">
		<li id="portlet-brand" class="portlet">
			<div class="portlet-header">${action.getText('brand')}</div>
			<div class="portlet-content">
				<div>${action.getText('all')}${action.getText('category')}</div>
				<form action="chart/view" method="get" class="form-inline ajax view" data-replacement="c">
					<input type="hidden" name="type" value="brand"/>
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/> 
				</form>
				<div style="clear:left;">${action.getText('single')}${action.getText('category')}</div>
				<form action="chart/view" method="get" class="form-inline ajax view" data-replacement="c">
					<input type="hidden" name="type" value="brand"/>
					<@s.select theme="simple" class="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</li>
		
		<li id="portlet-saleType" class="portlet">
			<div class="portlet-header">${action.getText('saleType')}</div>
			<div class="portlet-content">
				<div>${action.getText('all')}${action.getText('category')}</div>
				<form action="chart/view" method="get" class="form-inline ajax view" data-replacement="c">
					<input type="hidden" name="type" value="saletype"/>
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('single')}${action.getText('category')}</div>
				<form action="chart/view" method="get" class="form-inline ajax view" data-replacement="c">
					<input type="hidden" name="type" value="saletype"/>
					<@s.select theme="simple" class="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</li>
		
		<li id="portlet-country" class="portlet">
			<div class="portlet-header">${action.getText('country')}</div>
			<div class="portlet-content">
				<div>${action.getText('all')}${action.getText('category')}</div>
				<form action="chart/chinamap" method="get" class="form-inline" target="_blank" data-replacement="c">
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('single')}${action.getText('category')}</div>
				<form action="chart/chinamap" method="get" class="form-inline" target="_blank" data-replacement="c">
					<@s.select theme="simple" class="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</li>
		
	</ul>
	
	
	<ul class="portal-column">
		<li id="portlet-category" class="portlet">
			<div class="portlet-header">${action.getText('category')}</div>
			<div class="portlet-content">
				<div>${action.getText('all')}${action.getText('brand')}</div>
				<form action="chart/view" method="get" class="form-inline ajax view" data-replacement="c">
					<input type="hidden" name="type" value="category"/>
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('single')}${action.getText('brand')}</div>
				<form action="chart/view" method="get" class="form-inline ajax view" data-replacement="c">
					<input type="hidden" name="type" value="category"/>
					<@s.select theme="simple" class="required" name="id" list="brandList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</li>
		
		<li id="portlet-region" class="portlet">
			<div class="portlet-header">${action.getText('region')}</div>
			<div class="portlet-content">
				<div>${action.getText('all')}${action.getText('category')}</div>
				<form action="chart/view" method="get" class="form-inline ajax view" data-replacement="c">
					<input type="hidden" name="type" value="region"/>
					<input id="location" type="hidden" name="location"/><span class="treeselect" data-options="{'url':'<@url value="/region/children"/>','name':'this','id':'#location'}"></span>
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">${action.getText('single')}${action.getText('category')}</div>
				<form action="chart/view" method="get" class="form-inline ajax view" data-replacement="c">
					<input type="hidden" name="type" value="region"/>
					<input id="location2" type="hidden" name="location"/><span class="treeselect"  data-options="{'url':'<@url value="/region/children"/>','name':'this','id':'#location2'}"></span>
					<@s.select theme="simple" class="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
					<@s.textfield theme="simple" id="" name="from" class="date required"/>
					<@s.textfield theme="simple" id="" name="to" class="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</li>
	</ul>
</div>


</body>
</html></#escape>