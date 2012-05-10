<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('returning')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"customer":{"template":r"<span class='tiped' data-tipurl='${getUrl('/biz/customer/view/'+value.id+'?type=tip')}'>${value?string}</span>"},"grandTotal":{"width":"80px"},"returnDate":{"template":r"${(entity.returnDate?string('yyyy年MM月dd日'))!}","width":"120px"}}>
<#assign actionColumnButtons='
<button type="button" class="btn" data-view="view">${action.getText("view")}</button><#t>
<button type="button" class="btn" data-view="input" data-windowoptions="{\'width\':\'950px\'}">${action.getText("edit")}</button><#t>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input" data-windowoptions="{\'width\':\'950px\'}">${action.getText("create")}</button><#t>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button><#t>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button><#t>
'>
<@richtable entityName="returning" columns=columns actionColumnWidth="90px" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false deleteable=false searchable=true/>
</body>
</html></#escape>