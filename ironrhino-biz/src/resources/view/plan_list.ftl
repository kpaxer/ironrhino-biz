<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('plan')}</title>
</head>
<body>
<#assign config={"product":{},"quantity":{},"planDate":{"template":r"${value?string('yyyy年MM月dd日')}"},"completed":{}}>
<#assign actionColumnButtons=btn(action.getText('complete'),null,'complete')
+btn(action.getText('edit'),null,'input')
+btn(action.getText('delete'),null,'del')>
<@richtable entityName="plan" config=config actionColumnButtons=actionColumnButtons celleditable=false searchable=true/>
</body>
</html></#escape>