<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('plan')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"product":{},"quantity":{},"planDate":{"template":r"${value?string('yyyy年MM月dd日')}"},"completeDate":{"template":r"<#if entity.completeDate??>${value?string('yyyy年MM月dd日')}</#if>"}}>
<#assign actionColumnButtons=r"
<#if !entity.completed>
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'complete\')}' action='complete'/>
</#if>
">
<#assign bottomButtons=r"
<@button text='${action.getText(\'create\')}' view='input'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'complete\')}' action='complete'/>
<@button text='${action.getText(\'reload\')}' action='reload'/>
">
<@richtable entityName="plan" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false searchable=true/>
</body>
</html></#escape>