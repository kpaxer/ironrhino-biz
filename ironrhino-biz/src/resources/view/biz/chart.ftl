<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
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
				<form action="chart/view" method="get" class="line clearfix ajax view" replacement="c">
					<input type="hidden" name="type" value="brand"/>
					<div class="field"><@s.textfield theme="simple" id="date1" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date2" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">指定品种</div>
				<form action="chart/view" method="get" class="line clearfix ajax view" replacement="c">
					<input type="hidden" name="type" value="brand"/>
					<div class="field"><@s.select theme="simple" cssClass="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield theme="simple" id="date3" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date4" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
		
		<div class="portlet">
			<div class="portlet-header">按销售方式</div>
			<div class="portlet-content">
				<div>所有品种</div>
				<form action="chart/view" method="get" class="line clearfix ajax view" replacement="c">
					<input type="hidden" name="type" value="saletype"/>
					<div class="field"><@s.textfield theme="simple" id="date9" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date10" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">指定品种</div>
				<form action="chart/view" method="get" class="line clearfix ajax view" replacement="c">
					<input type="hidden" name="type" value="saletype"/>
					<div class="field"><@s.select theme="simple" cssClass="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield theme="simple" id="date11" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date12" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
		
		<div class="portlet">
			<div class="portlet-header">全国销量分布图</div>
			<div class="portlet-content">
				<div>所有品种</div>
				<form action="chart/ammap" method="get" class="line clearfix" target="_blank" replacement="c">
					<div class="field"><@s.textfield theme="simple" id="date17" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date18" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">指定品种</div>
				<form action="chart/ammap" method="get" class="line clearfix" target="_blank" replacement="c">
					<div class="field"><@s.select theme="simple" cssClass="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield theme="simple" id="date19" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date20" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
		
	</div>
	
	
	<div class="portal-column half">
		<div class="portlet">
			<div class="portlet-header">按品种统计</div>
			<div class="portlet-content">
				<div>所有商标</div>
				<form action="chart/view" method="get" class="line clearfix ajax view" replacement="c">
					<input type="hidden" name="type" value="category"/>
					<div class="field"><@s.textfield theme="simple" id="date5" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date6" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">指定商标</div>
				<form action="chart/view" method="get" class="line clearfix ajax view" replacement="c">
					<input type="hidden" name="type" value="category"/>
					<div class="field"><@s.select theme="simple" cssClass="required" name="id" list="brandList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield theme="simple" id="date7" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date8" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
		
		<div class="portlet">
			<div class="portlet-header">按地区统计</div>
			<div class="portlet-content">
				<div>所有品种</div>
				<form action="chart/view" method="get" class="line clearfix ajax view" replacement="c">
					<input type="hidden" name="type" value="region"/>
					<div class="field" style="margin-right:5px;"><input id="location" type="hidden" name="location"/><span id="region" class="treeselect" treeoptions="{'url':'<@url value="/region/children"/>','name':'region','id':'location'}">请点击选择地区</span></div>
					<div class="field"><@s.textfield theme="simple" id="date13" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date14" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">指定品种</div>
				<form action="chart/view" method="get" class="line clearfix ajax view" replacement="c">
					<input type="hidden" name="type" value="region"/>
					<div class="field" style="margin-right:5px;"><input id="location2" type="hidden" name="location"/><span id="region2" class="treeselect"  treeoptions="{'url':'<@url value="/region/children"/>','name':'region2','id':'location2'}">请点击选择地区</span></div>
					<div class="field"><@s.select theme="simple" cssClass="required" name="id" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield theme="simple" id="date15" name="from" cssClass="date required"/></div>
					<div class="field"><@s.textfield theme="simple" id="date16" name="to" cssClass="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
	</div>
</div>


</body>
</html></#escape>