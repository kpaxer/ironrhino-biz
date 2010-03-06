<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>报表</title>
</head>
<body>
<div>客户报表</div>
<div>
<@button text="当日结单" type="link" href="${getUrl('/report/jasper?type=customer')}" target="_blank"/>
<form action="${getUrl('/report/jasper')}" method="post">
<input type="hidden" name="type" value="customer"/>
<input type="text" name="date" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post">
<input type="hidden" name="type" value="customer"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
</div>
<div>订单报表</div>
<div>
<@button text="当日订单" type="link" href="${getUrl('/report/jasper?type=order')}" target="_blank"/>
<form action="${getUrl('/report/jasper')}" method="post">
<input type="hidden" name="type" value="order"/>
<input type="text" name="date" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post">
<input type="hidden" name="type" value="order"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
</div>
<div>工资报表</div>
<div>
<@button text="当日结单" type="link" href="${getUrl('/report/jasper?type=reward')}"/>
<form action="${getUrl('/report/jasper')}" method="post">
<input type="hidden" name="type" value="reward"/>
<input type="text" name="date" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post">
<input type="hidden" name="type" value="reward"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post">
<input type="hidden" name="type" value="personalreward"/>
<@s.select theme="simple" name="id" list="employeeList" cssStyle="width:80px;" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<span>包括支出</span><input type="checkbox" name="includePaid" value="true"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
<form action="${getUrl('/report/jasper')}" method="post">
<input type="hidden" name="type" value="aggregationreward"/>
<input type="text" name="from" class="date"/>
<input type="text" name="to" class="date"/>
<span>包括支出</span><input type="checkbox" name="includePaid" value="true"/>
<@s.submit theme="simple" value="%{getText('confirm')}"/>
</form>
</div>
</body>
</html></#escape>
