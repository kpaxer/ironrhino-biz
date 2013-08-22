<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('order')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"code":{"width":"100px"},"customer":{"template":r"<a href='${getUrl('/biz/customer/input/'+value.id)}' class='noid' rel='richtable'><span class='poped' data-popurl='${getUrl('/biz/customer/view/'+value.id+'?type=card')}'>${value?string}</span></a>"},"grandTotal":{"width":"100px"},"orderDate":{"template":r"${(entity.orderDate?string('yyyy-MM-dd'))!}","width":"100px"},"saleType":{"width":"100px"},"paid":{"width":"80px"},"shipped":{"width":"80px"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="view" '
+'data-windowoptions="{\'width\':\'950px\'}">${action.getText("view")}</button>
<button type="button" class="btn" data-view="input" '
+'data-windowoptions="{\'width\':\'950px\'}">${action.getText("edit")}</button>'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input" data-windowoptions="{\'width\':\'950px\'}">${action.getText("create")}</button>
<button type="button" class="btn" data-action="delete" data-shown="selected" data-filterselector=":not(.paid):not(.shipped)">${action.getText("delete")}</button>
<button type="button" class="btn" data-action="pay" data-shown="selected" data-filterselector=":not(.paid)">${action.getText("pay")}</button>
<button type="button" class="btn" data-action="ship" data-shown="selected" data-filterselector=":not(.shipped)">${action.getText("ship")}</button>
<button type="button" class="btn reload">${action.getText("reload")}</button>
<button type="button" class="btn filter">${action.getText("filter")}</button>
<button type="button" class="btn more">${action.getText("more")}</button>
'>
<@richtable entityName="order" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false deletable=false searchable=true rowDynamicAttributes=r"{'class':'<#if entity.paid> paid</#if><#if entity.shipped> shipped</#if>'}"/>
</body>
</html></#escape>