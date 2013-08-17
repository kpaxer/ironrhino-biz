<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('plan')}${action.getText('list')}</title>
</head>
<body>

<#assign columns={"product":{},"quantity":{},"planDate":{"template":r"${(entity.planDate?string('yyyy-MM-dd'))!}"},"completeDate":{"template":r"${(entity.completeDate?string('yyyy-MM-dd'))!}"}}>
<#assign actionColumnButtons=r'
<#if !entity.completed>
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button>
</#if>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input">${action.getText("create")}</button>
<button type="button" class="btn confirm" data-action="save">${action.getText("save")}</button>
<button type="button" class="btn" data-action="delete" data-shown="selected" data-filterselector=".uncompleted">${action.getText("delete")}</button>
<button type="button" class="btn" data-action="complete" data-shown="selected" data-filterselector=".uncompleted">${action.getText("complete")}</button>
<button type="button" class="btn reload">${action.getText("reload")}</button>
'>
<@richtable entityName="plan" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false searchable=true rowDynamicAttributes=r"<#if !entity.completed>{'class':'uncompleted'}</#if>"/>
</body>
</html></#escape>