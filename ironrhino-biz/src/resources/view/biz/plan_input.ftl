<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if plan.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('plan')}</title>
</head>
<body>
<@s.form action="${actionBaseUrl}/save" method="post" cssClass="ajax form-horizontal">
	<#if !plan.new>
		<@s.hidden name="plan.id" />
	</#if>
	<#if !plan.completed>
		<div class="control-group">
		<label class="control-label" for="productId">${action.getText('product')}</label>
		<div class="controls">
		<@s.select id="productId" theme="simple" name="product.id" cssClass="required chosen" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue=""/>
		</div>
		</div>
		<@s.textfield label="%{getText('quantity')}" name="plan.quantity" cssClass="integer positive required"/>
		<@s.textfield label="%{getText('planDate')}" name="plan.planDate" cssClass="date required"/>
	</#if>
	<@s.textarea label="%{getText('memo')}" name="plan.memo" cssStyle="width:400px;height:200px;"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>