<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('reward')}</title>
</head>
<body>
<#assign config={"employee":{},"amount":{},"rewardDate":{"template":r"${value?string('yyyy年MM月dd日')}"}}>
<@richtable entityName="reward" config=config celleditable=false/>
</body>
</html></#escape>