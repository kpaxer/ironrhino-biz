<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('order')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"code":{"width":"100px"},"customer":{"template":r"<span class='tiped' data-tipurl='${getUrl('/biz/customer/view/'+value.id+'?type=tip')}'>${value?string}</span>"},"grandTotal":{"width":"80px"},"orderDate":{"template":r"${(entity.orderDate?string('yyyy年MM月dd日'))!}","width":"120px"},"saleType":{"width":"100px"},"paid":{"width":"80px"},"shipped":{"width":"80px"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="view">${action.getText("view")}</button><#t>
<button type="button" class="btn" data-view="input" '
+'data-windowoptions="{\'width\':\'950px\'}">${action.getText("edit")}</button><#t>'
+r'<#if !entity.paid>
<button type="button" class="btn" data-action="pay">${action.getText("pay")}</button><#t>
</#if>
<#if !entity.shipped>
<button type="button" class="btn" data-action="ship">${action.getText("ship")}</button><#t>
</#if>
<#if !(entity.paid||entity.shipped)>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button><#t>
</#if>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input" data-windowoptions="{\'width\':\'950px\'}">${action.getText("create")}</button><#t>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button><#t>
<button type="button" class="btn" data-action="pay">${action.getText("pay")}</button><#t>
<button type="button" class="btn" data-action="ship">${action.getText("ship")}</button><#t>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button><#t>
'>
<@richtable entityName="order" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false deleteable=false searchable=true/>
</body>
</html></#escape>