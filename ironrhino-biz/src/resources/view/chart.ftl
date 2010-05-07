<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>图表</title>
<style>
h1 {
font-size: 15px;
background-color:#adadad;
}

.clear {
	clear: both;
}

div.block {
	margin-top: 10px;
	margin-bottom: 10px;
	
}

div.block > div {
	padding:0px 10px 0px 10px;
}

</style>
</head>
<body>

<div class="block">
	<h1 class="rounded">销量统计</h1>
	<div>
		<h3 class="clear">按商标统计</h3>
		<div class="clear">所有品种</div>
		<form action="${getUrl('/chart/view')}" method="get" class="line">
			<input type="hidden" name="type" value="brand"/>
			<div><@s.textfield theme="simple" id="date1" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date2" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">指定品种</div>
		<form action="${getUrl('/chart/view')}" method="get" class="line">
			<input type="hidden" name="type" value="brand"/>
			<div><span>品种:</span><input type="text" name="id" class="required" style="width:150px;"/></div>
			<div><@s.textfield theme="simple" id="date3" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date4" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">按品种统计</h3>
		<div class="clear">所有商标</div>
		<form action="${getUrl('/chart/view')}" method="get" class="line">
			<input type="hidden" name="type" value="category"/>
			<div><@s.textfield theme="simple" id="date5" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date6" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">指定商标</div>
		<form action="${getUrl('/chart/view')}" method="get" class="line">
			<input type="hidden" name="type" value="category"/>
			<div><span>商标:</span><input type="text" name="id" class="required" style="width:150px;"/></div>
			<div><@s.textfield theme="simple" id="date7" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date8" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">按销售方式统计</h3>
		<div class="clear">所有品种</div>
		<form action="${getUrl('/chart/view')}" method="get" class="line">
			<input type="hidden" name="type" value="saletype"/>
			<div><@s.textfield theme="simple" id="date9" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date10" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">指定品种</div>
		<form action="${getUrl('/chart/view')}" method="get" class="line">
			<input type="hidden" name="type" value="saletype"/>
			<div><span>品种:</span><input type="text" name="id" class="required" style="width:150px;"/></div>
			<div><@s.textfield theme="simple" id="date11" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date12" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">按地区统计</h3>
		<div class="clear">所有品种</div>
		<form action="${getUrl('/chart/view')}" method="get" class="line">
			<input type="hidden" name="type" value="region"/>
			<div style="margin-right:5px;"><input id="location" type="hidden" name="location"/><span id="region" class="region" regionname="region" full="true" regionid="location">请点击选择地区</span></div>
			<div><@s.textfield theme="simple" id="date13" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date14" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">指定品种</div>
		<form action="${getUrl('/chart/view')}" method="get" class="line">
			<input type="hidden" name="type" value="category"/>
			<div><span>品种:</span><input type="text" name="id" class="required" style="width:150px;"/></div>
			<div><span>地区:</span><input type="text" name="location" class="required" style="width:150px;"/></div>
			<div><@s.textfield theme="simple" id="date15" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date16" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">全国销量分布图</h3>
		<div class="clear">所有品种</div>
		<form action="${getUrl('/chart/ammap')}" method="get" class="line">
			<div><@s.textfield theme="simple" id="date17" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date18" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">指定品种</div>
		<form action="${getUrl('/chart/ammap')}" method="get" class="line">
			<div><input type="text" name="id" class="required" style="width:150px;"/></div>
			<div><@s.textfield theme="simple" id="date19" name="from" cssClass="date required"/></div>
			<div><@s.textfield theme="simple" id="date20" name="to" cssClass="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
	</div>
</div>



</body>
</html></#escape>