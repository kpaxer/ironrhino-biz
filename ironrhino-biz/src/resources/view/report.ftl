<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>报表</title>
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

<@button id="xls" text="当前是PDF,切换到EXCEL"/><@button id="pdf" text="当前是EXCEL,切换到PDF" style="display:none;"/>

<div>客户报表</div>
<div>
<@button text="当日结单" type="link" href="${getUrl('/report/jasper?type=customer')}" class="report"/>
<form action="${getUrl('/report/jasper')}" method="post" class="report">
<input type="hidden" name="type" value="customer"/>
<input type="text" name="date" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post" class="report">
<input type="hidden" name="type" value="customer"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
</div>

<div>订单报表</div>
<div>
<@button text="当日订单" type="link" href="${getUrl('/report/jasper?type=order')}" class="report"/>
<form action="${getUrl('/report/jasper')}" method="post" class="report">
<input type="hidden" name="type" value="order"/>
<input type="text" name="date" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post" class="report">
<input type="hidden" name="type" value="order"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
</div>

<div>工资报表</div>
<div>
<@button text="当日结单" type="link" href="${getUrl('/report/jasper?type=reward')}" class="report"/>
<form action="${getUrl('/report/jasper')}" method="post" class="report">
<input type="hidden" name="type" value="reward"/>
<input type="text" name="date" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post" class="report">
<input type="hidden" name="type" value="reward"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post" class="report">
<input type="hidden" name="type" value="privatereward"/>
<@s.select theme="simple" name="id" list="employeeList" cssStyle="width:80px;" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<span>包括支出</span><input type="checkbox" name="includePaid" value="true"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post" class="report">
<input type="hidden" name="type" value="aggregationreward"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<span>包括支出</span><input type="checkbox" name="includePaid" value="true"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
</div>

<div>产品库存报表</div>
<div>
<@button text="库存" type="link" href="${getUrl('/report/jasper?type=product')}" class="report"/>
<@button text="欠货" type="link" href="${getUrl('/report/jasper?type=product&negative=true')}" class="report"/>
</div>

</body>
</html></#escape>
