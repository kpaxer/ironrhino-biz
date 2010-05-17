<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if reward.new><#if negative>支<#else>发</#if><#else>${action.getText('edit')}</#if>${action.getText('reward')}</title>
</head>
<body>
<@s.form action="${getUrl('/reward/save')}" method="post" cssClass="ajax">
	<@s.hidden name="negative" />
	<@s.textfield label="%{getText('rewardDate')}" name="reward.rewardDate" cssClass="required date"/>
	<#if !reward.new>
		<@s.hidden name="reward.id" />
	<#else>
		<@s.select label="%{getText('employee')}" name="employee.id" cssClass="required" list="employeeList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
		<#if !negative>
		<table border="0" width="100%" class="datagrid">
		<thead>
			<tr>
				<td>
				${action.getText('amount')}
				</td>
				<td>
				${action.getText('type')}
				</td>
				<td>
				${action.getText('memo')}
				</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="text" name="rewardList[0].amount" class="required double positive"/></td>
				<td>
					<@s.select theme="simple" name="rewardList[0].type" list="@com.ironrhino.biz.model.RewardType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
				</td>
				<td><input type="text" name="rewardList[0].memo"/></td>
				<td><@button text="+" class="add"/><@button text="-" class="remove"/></td>
			</tr>
		</tbody>
		</table>
		</#if>
	</#if>
	<#if negative||!reward.new>
	<@s.textfield label="%{(negative?'支':'发')+getText('amount')}" name="reward.amount" cssClass="required double positive"/>
	<#if !negative>
		<@s.select label="%{getText('type')}" name="reward.type" list="@com.ironrhino.biz.model.RewardType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
	</#if>
	<@s.textarea label="%{getText('memo')}" name="reward.memo" cols="50" rows="10"/>
	</#if>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>