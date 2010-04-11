<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if employee.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('employee')}</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<@s.if test="%{!employee.isNew()}">
		<@s.hidden name="employee.id" />
	</@s.if>
	<@s.textfield label="%{getText('name')}" name="employee.name" cssClass="required"/>
	<@s.select label="%{getText('type')}" name="employee.type" list="@com.ironrhino.biz.model.EmployeeType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
	<@s.textfield label="%{getText('phone')}" name="employee.phone" />
	<@s.checkbox label="%{getText('dimission')}" name="employee.dimission" />
	<@s.textarea label="%{getText('memo')}" name="employee.memo" cols="50" rows="10"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>