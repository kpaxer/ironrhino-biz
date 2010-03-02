<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('order')}</title>
</head>
<body>
<#assign config={"code":{},"customer":{},"orderDate":{"template":r"${value?string('yyyy年MM月dd日')}"},"paid":{},"shipped":{},"cancelled":{}}>
<@richtable entityName="order" config=config celleditable=false deleteable=false/>
<form action="<@url value="order"/>" method="post" class="ajax view" replacement="order_form">
<@s.textfield theme="simple" name="q" size="20"/><@s.submit theme="simple" value="%{getText('search')}" />
</form>
</body>
</html></#escape>