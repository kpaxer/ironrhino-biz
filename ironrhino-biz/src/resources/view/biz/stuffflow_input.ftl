<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if stuffflow.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('stuffflow')}</title>
</head>
<body>
<@s.form action="${actionBaseUrl}/save" method="post" class="ajax form-horizontal">
	<@s.hidden name="stuff.id" />
	<@s.hidden name="out" />
	<@s.textfield label="%{getText('quantity')}" name="stuffflow.quantity" class="required integer positive"/>
	<#if !out>
	<@s.textfield label="%{getText('amount')}" name="stuffflow.amount" class="double positive"/>
	</#if>
	<@s.textfield label="%{getText('date')}" name="stuffflow.date" class="required date"/>
	<@s.textarea label="%{getText('memo')}" name="plan.memo" class="input-xxlarge"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>


