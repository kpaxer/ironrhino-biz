<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if stuff.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('stuff')}</title>
</head>
<body>
<@s.form action="${getUrl(actionBaseUrl+'/save')}" method="post" cssClass="ajax form-horizontal">
	<#if !stuff.new>
		<@s.hidden name="stuff.id" />
	</#if>
	<@s.textfield label="%{getText('name')}" name="stuff.name" cssClass="required"/>
	<@s.textfield label="%{getText('weight')}" name="stuff.weight" type="number" cssClass="double positive"/>
	<@s.textfield label="%{getText('stock')}" name="stuff.stock" type="number" cssClass="integer"/>
	<@s.textfield label="%{getText('displayOrder')}" name="stuff.displayOrder" type="number" cssClass="integer"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>


