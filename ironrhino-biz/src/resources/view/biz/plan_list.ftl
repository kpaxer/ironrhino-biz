<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('plan')}${action.getText('list')}</title>
</head>
<body>

<#assign columns={"product":{},"quantity":{},"planDate":{"template":r"${(entity.planDate?string('yyyy年MM月dd日'))!}"},"completeDate":{"template":r"${(entity.completeDate?string('yyyy年MM月dd日'))!}"}}>
<#assign actionColumnButtons=r'
<#if !entity.completed>
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button>
<button type="button" class="btn" data-action="complete">${action.getText("complete")}</button>
</#if>
'>
<#assign bottomButtons=r'
<button type="button" class="btn" data-view="input">${action.getText("create")}</button>
<button type="button" class="btn" data-action="save">${action.getText("save")}</button>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button>
<button type="button" class="btn" data-action="complete">${action.getText("complete")}</button>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button>
'>
<@richtable entityName="plan" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false searchable=true/>
</body>
</html></#escape>