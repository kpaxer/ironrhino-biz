<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if employee.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('employee')}</title>
</head>
<body>
<@s.form action="${actionBaseUrl}/save" method="post" cssClass="ajax form-horizontal">
	<#if !employee.new>
		<@s.hidden name="employee.id" />
	</#if>
	<@s.textfield label="%{getText('name')}" name="employee.name" cssClass="required checkavailable"/>
	<@s.select label="%{getText('type')}" name="employee.type" list="@com.ironrhino.biz.model.EmployeeType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
	<@s.textfield label="%{getText('phone')}" name="employee.phone" />
	<@s.textfield label="%{getText('address')}" name="employee.address" cssStyle="width:400px;"/>
	<@s.checkbox label="%{getText('dimission')}" name="employee.dimission" cssClass="custom" />
	<@s.textarea label="%{getText('memo')}" name="employee.memo" cssStyle="width:400px;height:100px;"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>