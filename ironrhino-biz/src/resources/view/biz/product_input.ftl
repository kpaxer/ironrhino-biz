<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if product.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('product')}</title>
</head>
<body>
<@s.form action="${actionBaseUrl}/save" method="post" class="ajax form-horizontal">
	<#if !product.new>
		<@s.hidden name="product.id"/>
	</#if>
	<@s.select label="%{getText('brand')}" name="brandId" class="required" list="brandList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
	<@s.select label="%{getText('category')}" name="categoryId" class="required conjunct" dynamicAttributes={"data-replacement":"editAttributes"} list="categoryList" listKey="id" listValue="name" headerKey="" headerValue="%{getText('select')}"/>
	<@s.textfield label="%{getText('name')}" name="product.name" class="required"/>
	<@s.textfield label="%{getText('stock')}" name="product.stock" type="number" class="integer"/>
	<@s.textfield label="%{getText('shopStock')}" name="product.shopStock" type="number" class="integer positive"/>
	<@s.textfield label="%{getText('weight')}" name="product.weight" type="number" class="double positive"/>
	<@s.textfield label="%{getText('price')}" name="product.price" type="number" class="double positive"/>
	<@s.textfield label="%{getText('displayOrder')}" name="product.displayOrder" type="number" class="integer"/>
	<div id="editAttributes">
	<@editAttributes schemaName='category:'+categoryId! attributes=product.attributes parameterNamePrefix='product.'/>
	</div>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>


