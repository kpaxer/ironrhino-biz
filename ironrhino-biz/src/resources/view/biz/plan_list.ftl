<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('plan')}${action.getText('list')}</title>
</head>
<body>

<#assign columns={"product":{},"quantity":{},"planDate":{"template":r"${(entity.planDate?string('yyyy年MM月dd日'))!}"},"completeDate":{"template":r"${(entity.completeDate?string('yyyy年MM月dd日'))!}"}}>
<#assign actionColumnButtons=r'
<#if !entity.completed>
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button><#t>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button><#t>
<button type="button" class="btn" data-action="complete">${action.getText("complete")}</button><#t>
</#if>
'>
<#assign bottomButtons=r'
<button type="button" class="btn" data-view="input">${action.getText("create")}</button><#t>
<button type="button" class="btn" data-action="save">${action.getText("save")}</button><#t>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button><#t>
<button type="button" class="btn" data-action="complete">${action.getText("complete")}</button><#t>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button><#t>
'>
<@richtable entityName="plan" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false searchable=true/>
</body>
</html></#escape>