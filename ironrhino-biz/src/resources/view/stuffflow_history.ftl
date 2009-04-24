<#include "richtable-macro.ftl"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>出入库审核历史记录</title>
<link rel="stylesheet" href="${base}/themes/ui.datepicker.css" type="text/css" media="screen" />
<script type="text/javascript" src="${base}/scripts/ui.datepicker.js"></script>
<script type="text/javascript" src="${base}/scripts/ui.datepicker-zh-CN.js"></script>
</head>
<body>

<#assign config={"stuff":{},"quantity":{},"amount":{},"status":{"renderLink":true},"requestUser":{"renderLink":true},"requestDate":{},"auditUser":{"renderLink":true},"auditDate":{}}>
<@richtable entityName="stuffflow" action="history" config=config readonly=true/>

<form action="history" method="post" class="ajax view" replacement="richtable_main_content">
	<@s.select theme="simple" name="queryForm.status" list="@org.ironrhino.common.model.Status@values()" listKey="name" listValue="displayName" headerKey="" headerValue="${action.getText('all')}"/>
	<@s.textfield theme="simple" name="queryForm.startDate" size="10" cssClass="date"/>---<@s.textfield theme="simple" name="queryForm.endDate" size="10" cssClass="date"/>
	<@s.submit theme="simple" value="%{getText('search')}" />
<form>

</body>
</html>