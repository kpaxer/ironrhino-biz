<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('order')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"code":{"width":"100px"},"customer":{"template":r"<span class='poped' data-popurl='${getUrl('/biz/customer/view/'+value.id+'?type=card')}'>${value?string}</span>"},"grandTotal":{"width":"80px"},"orderDate":{"template":r"${(entity.orderDate?string('yyyy年MM月dd日'))!}","width":"120px"},"saleType":{"width":"100px"},"paid":{"width":"80px"},"shipped":{"width":"80px"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="view">${action.getText("view")}</button>
<button type="button" class="btn" data-view="input" '
+'data-windowoptions="{\'width\':\'950px\'}">${action.getText("edit")}</button>'
+r'<#if !entity.paid>
<button type="button" class="btn" data-action="pay">${action.getText("pay")}</button>
</#if>
<#if !entity.shipped>
<button type="button" class="btn" data-action="ship">${action.getText("ship")}</button>
</#if>
<#if !(entity.paid||entity.shipped)>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button>
</#if>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input" data-windowoptions="{\'width\':\'950px\'}">${action.getText("create")}</button>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button>
<button type="button" class="btn" data-action="pay">${action.getText("pay")}</button>
<button type="button" class="btn" data-action="ship">${action.getText("ship")}</button>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button>
'>
<@richtable entityName="order" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false deleteable=false searchable=true/>
</body>
</html></#escape>