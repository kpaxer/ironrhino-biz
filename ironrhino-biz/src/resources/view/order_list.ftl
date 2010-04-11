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
<#assign config={"code":{"width":"100px"},"customer":{},"grandTotal":{"width":"80px"},"orderDate":{"template":r"${value?string('yyyy年MM月dd日')}","width":"120px"},"saleType":{"width":"100px"},"paid":{"width":"80px"},"shipped":{"width":"80px"}}>
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
<#assign bottomButtons=r"
<@button text='${action.getText(\'create\')}' view='input' windowoptions='{\'width\':\'900px\'}'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'pay\')}' action='pay'/>
<@button text='${action.getText(\'ship\')}' action='ship'/>
<@button text='${action.getText(\'reload\')}' action='reload'/>
">
<@richtable entityName="order" config=config actionColumnWidth="230px" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false deleteable=false searchable=true/>
</body>
</html></#escape>