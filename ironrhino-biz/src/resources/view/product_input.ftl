<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if product.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('product')}</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<#if !product.new>
		<@s.hidden name="product.id" />
	</#if>
	<@s.select label="%{getText('category')}" name="categoryId" cssClass="required" list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
	<@s.select label="%{getText('brand')}" name="brandId" cssClass="required" list="brandList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
	<@s.textfield label="%{getText('name')}" name="product.name" cssClass="required"/>
	<@s.textfield label="%{getText('stock')}" name="product.stock" cssClass="integer"/>
	<@s.textfield label="%{getText('weight')}" name="product.weight" cssClass="double positive"/>
	<@s.textfield label="%{getText('price')}" name="product.price" cssClass="double positive"/>
	<@s.textfield label="%{getText('displayOrder')}" name="product.displayOrder" cssClass="integer"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>


