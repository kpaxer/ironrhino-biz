<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title></title>
</head>
<body>
<@s.form action="${getUrl('/stuffflow/save')}" method="post" cssClass="ajax">
	<@s.hidden name="stuff.id" />
	<@s.hidden name="out" />
	<@s.textfield label="%{getText('quantity')}" name="stuffflow.quantity" cssClass="required integer positive"/>
	<#if !out>
	<@s.textfield label="%{getText('amount')}" name="stuffflow.amount" cssClass="double positive"/>
	</#if>
	<@s.textfield label="%{getText('date')}" name="stuffflow.date" cssClass="required date"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>

