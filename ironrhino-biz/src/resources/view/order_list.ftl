<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('order')}</title>
<style>
form p.submit{
text-align:center;
}
</style>
</head>
<body>
<#assign config={"code":{},"customer":{},"grandTotal":{},"orderDate":{"template":r"${value?string('yyyy年MM月dd日')}"},"saleType":{},"paid":{},"shipped":{},"cancelled":{}}>
<#assign actionColumnButtons=btn(action.getText('view'),null,'view')
+btn(action.getText('edit'),null,'input')
+btn(action.getText('pay'),null,'pay')
+btn(action.getText('ship'),null,'pay')
+btn(action.getText('cancel'),null,'pay')
+btn(action.getText('delete'),null,'del')>

<@richtable entityName="order" config=config actionColumnWidth="250px" actionColumnButtons=actionColumnButtons celleditable=false deleteable=false searchable=true/>
</body>
</html></#escape>