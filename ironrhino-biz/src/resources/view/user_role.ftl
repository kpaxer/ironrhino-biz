<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('edit')}${action.getText('user')}</title>
</head>
<body>
<@s.form action="/user/role" method="post" cssClass="ajax">
	<@s.hidden name="id" />
	<@s.checkboxlist name="roleId" list="roleList" listKey="id" listValue="name"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>


