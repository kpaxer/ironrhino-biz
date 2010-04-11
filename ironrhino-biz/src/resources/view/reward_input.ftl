<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if reward.new><#if negative>支<#else>发</#if><#else>${action.getText('edit')}</#if>${action.getText('reward')}</title>
</head>
<body>
<@s.form action="${getUrl('/reward/save')}" method="post" cssClass="ajax">
	<@s.hidden name="negative" />
	<#if !reward.new>
		<@s.hidden name="reward.id" />
	<#else>
		<@s.select label="%{getText('employee')}" name="employee.id" cssClass="required" list="employeeList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
	</#if>
	<@s.textfield label="%{(negative?'支':'发')+getText('amount')}" name="reward.amount" cssClass="required double positive"/>
	<#if !negative>
		<@s.select label="%{getText('type')}" name="reward.type" list="@com.ironrhino.biz.model.RewardType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
	</#if>
	<@s.textfield label="%{getText('rewardDate')}" name="reward.rewardDate" cssClass="required date"/>
	<@s.textarea label="%{getText('memo')}" name="reward.memo" cols="50" rows="10"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>