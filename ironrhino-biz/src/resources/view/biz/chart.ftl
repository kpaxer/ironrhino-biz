<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('chart')}</title>
</head>
<body>
<div id="c" style="margin-bottom:10px;"></div>
<div class="portal">
	<div class="portal-column half">
		<div class="portlet">
			<div class="portlet-header">按商标统计</div>
			<div class="portlet-content">
				<div>所有品种</div>
				<form action="chart/view" method="get" class="form-inline ajax view" replacement="c">
					<input type="hidden" name="type" value="brand"/>
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/> 
				</form>
				<div style="clear:left;">指定品种</div>
				<form action="chart/view" method="get" class="form-inline ajax view" replacement="c">
					<input type="hidden" name="type" value="brand"/>
					<@s.select theme="simple" cssClass="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
		
		<div class="portlet">
			<div class="portlet-header">按销售方式</div>
			<div class="portlet-content">
				<div>所有品种</div>
				<form action="chart/view" method="get" class="form-inline ajax view" replacement="c">
					<input type="hidden" name="type" value="saletype"/>
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">指定品种</div>
				<form action="chart/view" method="get" class="form-inline ajax view" replacement="c">
					<input type="hidden" name="type" value="saletype"/>
					<@s.select theme="simple" cssClass="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
		
		<div class="portlet">
			<div class="portlet-header">全国销量分布图</div>
			<div class="portlet-content">
				<div>所有品种</div>
				<form action="chart/chinamap" method="get" class="form-inline clearfix" target="_blank" replacement="c">
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">指定品种</div>
				<form action="chart/chinamap" method="get" class="form-inline clearfix" target="_blank" replacement="c">
					<@s.select theme="simple" cssClass="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
		
	</div>
	
	
	<div class="portal-column half">
		<div class="portlet">
			<div class="portlet-header">按品种统计</div>
			<div class="portlet-content">
				<div>所有商标</div>
				<form action="chart/view" method="get" class="form-inline ajax view" replacement="c">
					<input type="hidden" name="type" value="category"/>
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">指定商标</div>
				<form action="chart/view" method="get" class="form-inline ajax view" replacement="c">
					<input type="hidden" name="type" value="category"/>
					<@s.select theme="simple" cssClass="required" name="id" list="brandList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
		
		<div class="portlet">
			<div class="portlet-header">按地区统计</div>
			<div class="portlet-content">
				<div>所有品种</div>
				<form action="chart/view" method="get" class="form-inline ajax view" replacement="c">
					<input type="hidden" name="type" value="region"/>
					<div class="control-group" style="margin-right:5px;"><input id="location" type="hidden" name="location"/><span id="region" class="treeselect" data-options="{'url':'<@url value="/region/children"/>','name':'region','id':'location'}">请点击选择地区</span></div>
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">指定品种</div>
				<form action="chart/view" method="get" class="form-inline ajax view" replacement="c">
					<input type="hidden" name="type" value="region"/>
					<div class="control-group" style="margin-right:5px;"><input id="location2" type="hidden" name="location"/><span id="region2" class="treeselect"  data-options="{'url':'<@url value="/region/children"/>','name':'region2','id':'location2'}">请点击选择地区</span></div>
					<@s.select theme="simple" cssClass="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
					<@s.textfield theme="simple" id="" name="from" cssClass="date required"/>
					<@s.textfield theme="simple" id="" name="to" cssClass="date required"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
	</div>
</div>


</body>
</html></#escape>