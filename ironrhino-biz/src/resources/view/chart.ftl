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
	float: left;
	display: inline;
	width: 390px;
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
	<h1 class="rounded">订单报表</h1>
	<div>
		<h3>订单详情</h3>
		<span>按天</span>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="order"/>
			<div><@s.textfield name="date" cssClass="date required" theme="simple"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="order"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按客户和区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="order"/>
			<div><input type="text" name="id" class="required" style="width:150px;"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">产品销量统计</h3>
		<div class="clear">按区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="productsales"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按客户和区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="productsales"/>
			<div><input type="text" name="id" class="required" style="width:150px;"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
	</div>
</div>

<div class="block">
	<h1 class="rounded">工资报表</h1>
	<div>
		<h3>工资详单</h3>
		<div class="clear">按天</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="reward"/>
			<div><@s.textfield name="date" cssClass="date required" theme="simple"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="reward"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">个人工资详单</h3>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="privatereward"/>
			<div><input type="text" name="id" class="required" style="width:80px;"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><span>包括支出</span><input type="checkbox" name="includePaid" value="true"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">所有员工工资统计</h3>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="aggregationreward"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><span>包括支出</span><input type="checkbox" name="includePaid" value="true"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
	</div>
</div>

<div class="block">
<h1 class="rounded">产品和原料库存报表</h1>
<div>
	<h3 class="clear">库存清单</h3>
	<@button text="库存单" type="link" href="${getUrl('/report/jasper?type=product')}" class="report"/>
	<@button text="欠货单" type="link" href="${getUrl('/report/jasper?type=product&negative=true')}" class="report"/>
	<@button text="原料库存单" type="link" href="${getUrl('/report/jasper?type=stuff')}" class="report"/>
	<h3 class="clear">出入库统计</h3>
	<form action="${getUrl('/report/jasper')}" method="post" class="line">
		<input type="hidden" name="type" value="stuffflow"/>
		<div><input type="text" name="from" class="date required"/></div>
		<div><input type="text" name="to" class="date required"/></div>
		<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
	</form>
</div>
</div>

<div class="block">
	<h1 class="rounded">客户报表</h1>
	<div>
		<h3>客户资料</h3>
		<div class="clear">按天</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="customer"/>
			<div><@s.textfield name="date" cssClass="date required" theme="simple"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="line">
			<input type="hidden" name="type" value="customer"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/><div/>
		</form>
	</div>
</div>

</body>
</html></#escape>