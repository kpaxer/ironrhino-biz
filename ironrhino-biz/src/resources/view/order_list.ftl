<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('order')}${action.getText('list')}</title>
<style>
form p.submit{
text-align:center;
}
</style>
</head>
<body>
<#assign config={"code":{},"customer":{},"grandTotal":{},"orderDate":{"template":r"${value?string('yyyy年MM月dd日')}"},"saleType":{},"paid":{},"shipped":{}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'view\')}' view='view'/>
<@button text='${action.getText(\'edit\')}' view='input'/>
<#if !entity.paid>
<@button text='${action.getText(\'pay\')}' action='pay'/>
</#if>
<#if !entity.shipped>
<@button text='${action.getText(\'ship\')}' action='ship'/>
</#if>
<#if !(entity.paid||entity.shipped)>
<@button text='${action.getText(\'delete\')}' action='delete'/>
</#if>
">

<@richtable entityName="order" config=config actionColumnWidth="230px" actionColumnButtons=actionColumnButtons celleditable=false deleteable=false searchable=true/>
</body>
</html></#escape>