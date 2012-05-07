<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('order')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"code":{"width":"100px"},"customer":{"template":r"<span class='tiped' tipurl='${getUrl('/biz/customer/view/'+value.id+'?type=tip')}'>${value?string}</span>"},"grandTotal":{"width":"80px"},"orderDate":{"template":r"${(entity.orderDate?string('yyyy年MM月dd日'))!}","width":"120px"},"saleType":{"width":"100px"},"paid":{"width":"80px"},"shipped":{"width":"80px"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'view\')}' view='view'/>
<@button text='${action.getText(\'edit\')}' view='input' windowoptions='{\'width\':\'900px\'}'/>
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
<@button text='${action.getText(\'create\')}' view='input' windowoptions='{\'width\':\'950px\'}'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'pay\')}' action='pay'/>
<@button text='${action.getText(\'ship\')}' action='ship'/>
<@button text='${action.getText(\'reload\')}' action='reload'/>
">
<@richtable entityName="order" columns=columns actionColumnWidth="230px" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false deleteable=false searchable=true/>
</body>
</html></#escape>