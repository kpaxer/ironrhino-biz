<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if reward.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('reward')}</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<#if !reward.new>
		<@s.hidden name="reward.id" />
	<#else>
		<@s.select label="%{getText('employee')}" name="employee.id" cssClass="required" list="employeeList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
	</#if>
	<@s.textfield label="%{getText('amount')}" name="reward.amount" cssClass="double required"/>
	<@s.textfield label="%{getText('rewardDate')}" name="reward.rewardDate" cssClass="date required"/>
	<@s.textarea label="%{getText('memo')}" name="reward.memo" cols="50" rows="10"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>