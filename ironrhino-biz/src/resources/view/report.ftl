<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>报表</title>
<style>
h1 {
font-size: 15px;
background-color:#adadad;
}

div.block {
	float: left;
	display: inline;
	width: 390px;
	margin-top: 10px;
	margin-bottom: 10px;
	
}
div.block form {
	margin:0px;
	padding:0px;
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
	<h1 class="rounded">客户报表</h1>
	<div>
		<h3>客户资料</h3>
		<span>按天</span>
		<form action="${getUrl('/report/jasper')}" method="post" class="report">
			<input type="hidden" name="type" value="customer"/>
			<@s.textfield name="date" cssClass="date" theme="simple"/>
			<@s.submit theme="simple" value="%{getText('confirm')}"/>
		</form>
		<span>按区间</span>
		<form action="${getUrl('/report/jasper')}" method="post" class="report">
			<input type="hidden" name="type" value="customer"/>
			<input type="text" name="from" class="date"/>
			<input type="text" name="to" class="date"/>
			<@s.submit theme="simple" value="%{getText('confirm')}"/>
		</form>
	</div>
</div>

<div class="block">
	<h1 class="rounded">订单报表</h1>
	<div>
		<h3>订单详情</h3>
		<span>按天</span>
		<form action="${getUrl('/report/jasper')}" method="post" class="report">
			<input type="hidden" name="type" value="order"/>
			<@s.textfield name="date" cssClass="date" theme="simple"/>
			<@s.submit theme="simple" value="%{getText('confirm')}"/>
		</form>
		<span>按区间</span>
		<form action="${getUrl('/report/jasper')}" method="post" class="report">
			<input type="hidden" name="type" value="order"/>
			<input type="text" name="from" class="date"/>
			<input type="text" name="to" class="date"/>
			<@s.submit theme="simple" value="%{getText('confirm')}"/>
		</form>
		<h3>产品销量统计</h3>
		<form action="${getUrl('/report/jasper')}" method="post" class="report">
			<input type="hidden" name="type" value="productsales"/>
			<input type="text" name="from" class="date"/>
			<input type="text" name="to" class="date"/>
			<@s.submit theme="simple" value="%{getText('confirm')}"/>
		</form>
	</div>
</div>

<div class="block">
	<h1 class="rounded">工资报表</h1>
	<div>
		<h3>工资详单</h3>
		<span>按天</span>
		<form action="${getUrl('/report/jasper')}" method="post" class="report">
			<input type="hidden" name="type" value="reward"/>
			<@s.textfield name="date" cssClass="date" theme="simple"/>
			<@s.submit theme="simple" value="%{getText('confirm')}"/>
		</form>
		<span>按区间</span>
		<form action="${getUrl('/report/jasper')}" method="post" class="report">
			<input type="hidden" name="type" value="reward"/>
			<input type="text" name="from" class="date"/>
			<input type="text" name="to" class="date"/>
			<@s.submit theme="simple" value="%{getText('confirm')}"/>
		</form>
		<h3>个人工资详单</h3>
		<form action="${getUrl('/report/jasper')}" method="post" class="report">
			<input type="hidden" name="type" value="privatereward"/>
			<@s.select theme="simple" name="id" list="employeeList" cssStyle="width:80px;" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
			<input type="text" name="from" class="date"/>
			<input type="text" name="to" class="date"/>
			<span>包括支出</span><input type="checkbox" name="includePaid" value="true"/>
			<@s.submit theme="simple" value="%{getText('confirm')}"/>
		</form>
		<h3>所有员工工资统计</h3>
		<form action="${getUrl('/report/jasper')}" method="post" class="report">
			<input type="hidden" name="type" value="aggregationreward"/>
			<input type="text" name="from" class="date"/>
			<input type="text" name="to" class="date"/>
			<span>包括支出</span><input type="checkbox" name="includePaid" value="true"/>
			<@s.submit theme="simple" value="%{getText('confirm')}"/>
		</form>
	</div>
</div>

<div class="block">
<h1 class="rounded">产品库存报表</h1>
<div>
	<@button text="库存单" type="link" href="${getUrl('/report/jasper?type=product')}" class="report"/>
	<@button text="欠货单" type="link" href="${getUrl('/report/jasper?type=product&negative=true')}" class="report"/>
</div>
</div>

</body>
</html></#escape>
