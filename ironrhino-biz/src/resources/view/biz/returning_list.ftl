<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('returning')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"customer":{"template":r"<span class='poped' data-popurl='${getUrl('/biz/customer/view/'+value.id+'?type=card')}'>${value?string}</span>"},"grandTotal":{"width":"120px"},"returnDate":{"template":r"${(entity.returnDate?string('yyyy-MM-dd'))!}","width":"120px"}}>
<#assign actionColumnButtons='
<button type="button" class="btn" data-view="view" data-windowoptions="{\'width\':\'950px\'}">${action.getText("view")}</button>
<button type="button" class="btn" data-view="input" data-windowoptions="{\'width\':\'950px\'}">${action.getText("edit")}</button>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input" data-windowoptions="{\'width\':\'950px\'}">${action.getText("create")}</button>
<button type="button" class="btn confirm" data-action="delete" data-shown="selected">${action.getText("delete")}</button>
<button type="button" class="btn reload">${action.getText("reload")}</button>
<button type="button" class="btn filter">${action.getText("filter")}</button>
'>
<@richtable entityName="returning" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false deletable=false searchable=true/>
</body>
</html></#escape>