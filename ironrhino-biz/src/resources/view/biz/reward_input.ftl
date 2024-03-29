<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if reward.new><#if negative??&&negative>支<#else>发</#if><#else>${action.getText('edit')}</#if>${action.getText('reward')}</title>
</head>
<body>
<@s.form action="${actionBaseUrl}/save" method="post" class="ajax reset form-horizontal">
	<@s.hidden name="negative" />
	<@s.textfield label="%{getText('rewardDate')}" name="reward.rewardDate" class="required date"/>
	<#if !reward.new>
		<@s.hidden name="reward.id" />
		<@s.textfield label="%{(negative?'支':'发')+getText('amount')}" name="reward.amount" class="required double positive"/>
		<@s.select label="%{getText('type')}" name="reward.type" list="@com.ironrhino.biz.model.RewardType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
		<@s.textarea label="%{getText('memo')}" name="reward.memo" class="input-xxlarge" cssStyle="height:50px;"/>
	<#else>
		<@s.select label="%{getText('employee')}" name="employee.id" class="required" list="employeeList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
		<table class="datagrid table table-bordered">
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
				<td class="manipulate"></td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="text" name="rewardList[0].amount" class="required double positive"/></td>
				<td>
					<@s.select theme="simple" name="rewardList[0].type" cssStyle="width:100px;" list="@com.ironrhino.biz.model.RewardType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
				</td>
				<td><input type="text" name="rewardList[0].memo" class="input-medium"/></td>
				<td class="manipulate"></td>
			</tr>
		</tbody>
		</table>
	</#if>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>