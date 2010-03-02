<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>Create/Edit Product</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<@s.hidden name="product.id" />
	<@s.hidden name="categoryId" />
	<@s.if test="%{product.isNew()}">
	<@s.textfield label="%{getText('name')}" name="product.name" cssClass="required"/>
	<@s.select label="%{getText('spec')}" name="specId" cssClass="required" list="specList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
	</@s.if>
	<@s.else>
	<@s.hidden name="product.name" />
	<@s.hidden name="specId" />
	</@s.else>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>


