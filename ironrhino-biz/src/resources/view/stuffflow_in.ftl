<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>入库</title>
</head>
<body>
<@s.form action="in" method="post" cssClass="ajax">
    <@s.select label="%{getText('stuff')}" name="stuffId" list="stuffList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/>
	<@s.textfield label="%{getText('quantity')}" name="stuffflow.quantity" cssClass="required integer"/>
	<@s.textfield label="%{getText('amount')}" name="stuffflow.amount"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html>


