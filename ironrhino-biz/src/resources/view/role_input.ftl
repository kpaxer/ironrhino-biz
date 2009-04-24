<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if role.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('role')}</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<@s.hidden name="role.id" />
	<@s.textfield label="%{getText('role.name')}" name="role.name" cssClass="required"/>
	<@s.submit value="%{getText('save')}"/>
</@s.form>
</body>
</html>