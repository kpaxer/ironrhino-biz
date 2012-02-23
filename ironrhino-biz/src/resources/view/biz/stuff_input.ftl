<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if stuff.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('stuff')}</title>
</head>
<body>
<@s.form action="${getUrl(actionBaseUrl+'/save')}" method="post" cssClass="ajax">
	<#if !stuff.new>
		<@s.hidden name="stuff.id" />
	</#if>
	<@s.textfield label="%{getText('name')}" name="stuff.name" cssClass="required"/>
	<@s.textfield label="%{getText('weight')}" name="stuff.weight" cssClass="double positive"/>
	<@s.textfield label="%{getText('stock')}" name="stuff.stock" cssClass="integer"/>
	<@s.textfield label="%{getText('displayOrder')}" name="stuff.displayOrder" cssClass="integer"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>


