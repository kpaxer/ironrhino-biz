<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>报表</title>
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
<script>
$(function(){
	$('#pdf,#xls').click(function(){
		var btn = $(this);
		var format = btn.attr('id');
		if(format=='xls'){
			$('#xls').hide();
			$('#pdf').show();
		}else{
			$('#pdf').hide();
			$('#xls').show();
		}
		$('a.report').attr('href',function(i,href){
			var i = href.indexOf('format=');
			if(i>0)
				href = href.substring(0,i-1);
			return href+='&format='+format;
		});
		$('form.report').each(function(){
			var hidden = $('input[name="format"]',this);
			if(hidden.length){
				hidden.val(format);
			}else{
				$(this).prepend('<input type="hidden" name="format" value="'+format+'"/>');
			}
		});
	});
});
</script>
</head>
<body>
<div style="clear:both;text-align:center;">
<@button id="xls" text="当前是PDF,切换到EXCEL"/><@button id="pdf" text="当前是EXCEL,切换到PDF" style="display:none;"/>
</div>


<div class="block">
	<h1 class="rounded">订单报表</h1>
	<div>
		<h3>订单详情</h3>
		<span>按天</span>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="order"/>
			<div><@s.textfield id="date1" name="date" cssClass="date required" theme="simple"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="order"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按客户和区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="order"/>
			<div><input type="text" name="customer" class="required" style="width:120px;"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按业务员和区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="order"/>
			<div><input type="text" name="salesman" class="required" style="width:120px;"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按销售方式和区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="order"/>
			<div><@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:120px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="请选择"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">产品销量统计</h3>
		<div class="clear">按区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="productsales"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按客户和区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="productsales"/>
			<div><input type="text" name="customer" class="required" style="width:120px;"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按业务员和区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="productsales"/>
			<div><input type="text" name="salesman" class="required" style="width:120px;"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按销售方式和区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="productsales"/>
			<div><@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:120px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="请选择"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">按天统计销量报表</h3>
		<div class="clear">区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="dailysales"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
	</div>
</div>

<div class="block">
	<h1 class="rounded">工资报表</h1>
	<div>
		<h3 class="clear">所有人工资详单</h3>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="privatereward"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><span>包括支出</span><input type="checkbox" name="includePaid" value="true"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">个人工资详单</h3>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="privatereward"/>
			<div><input type="text" name="id" class="required" style="width:80px;"/></div>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><span>包括支出</span><input type="checkbox" name="includePaid" value="true"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">工资清单</h3>
		<div class="clear">按天</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="reward"/>
			<div><@s.textfield id="date2" name="date" cssClass="date required" theme="simple"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按区间</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="reward"/>
			<div><input type="text" name="from" class="date required"/></div>
			<div><input type="text" name="to" class="date required"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<h3 class="clear">所有员工工资统计</h3>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
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
	<form action="${getUrl('/report/jasper')}" method="post" class="report line">
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
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="customer"/>
			<div><@s.textfield id="date3" name="date" cssClass="date required" theme="simple"/></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
		</form>
		<div class="clear">按地区</div>
		<form action="${getUrl('/report/jasper')}" method="post" class="report line">
			<input type="hidden" name="type" value="customer"/>
			<input id="regionId" type="hidden" name="id"/>
			<div><span id="region" onclick="Region.select('region',true,'regionId')" style="cursor:pointer;">请选择地区</span></div>
			<div><@s.submit theme="simple" value="%{getText('confirm')}"/><div/>
		</form>
	</div>
</div>

</body>
</html></#escape>
