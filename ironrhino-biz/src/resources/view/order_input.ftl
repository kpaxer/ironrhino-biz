<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if order.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('order')}</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<@s.if test="%{!order.isNew()}">
		<@s.hidden name="order.id" />
	</@s.if>
	<#if customer??>
		<@s.hidden name="customer.id" />
	</#if>
	<@s.textfield label="%{getText('orderDate')}" name="order.orderDate" cssClass="date"/>
	<@s.textfield label="%{getText('discount')}" name="order.discount" cssClass="double"/>
	<@s.textarea label="%{getText('memo')}" name="customer.memo" cols="50" rows="10"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>