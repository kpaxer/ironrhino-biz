<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if stuffflow.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('stuffflow')}</title>
</head>
<body>
<@s.form action="${actionBaseUrl}/save" method="post" cssClass="ajax form-horizontal">
	<@s.hidden name="stuff.id" />
	<@s.hidden name="out" />
	<@s.textfield label="%{getText('quantity')}" name="stuffflow.quantity" cssClass="required integer positive"/>
	<#if !out>
	<@s.textfield label="%{getText('amount')}" name="stuffflow.amount" cssClass="double positive"/>
	</#if>
	<@s.textfield label="%{getText('date')}" name="stuffflow.date" cssClass="required date"/>
	<@s.textarea label="%{getText('memo')}" name="plan.memo" cssClass="input-xxlarge"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>


