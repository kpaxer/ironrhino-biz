<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('stuffflow')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"stuff":{},"quantity":{},"amount":{},"date":{"template":r"${value?string('yyyy年MM月dd日')}"}}>
<#assign actionColumnButtons=''>
<@richtable entityName="stuffflow" columns=columns readonly=true searchable=true/>
</body>
</html></#escape>
