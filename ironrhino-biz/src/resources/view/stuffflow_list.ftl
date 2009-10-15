<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>出入库审核</title>
</head>
<body>
<#assign config={"stuff":{},"quantity":{},"amount":{},"requestUser":{},"requestDate":{}}>
<#assign actionColumnButtons=btn(action.getText('confirm'),r"Richtable.execute('confirm','${rowid}')")+btn(action.getText('cancel'),r"Richtable.execute('cancel','${rowid}')")>
<@richtable 
entityName="stuffflow" 
config=config 
actionColumnWidth="100px" 
actionColumnButtons=actionColumnButtons 
bottomButtons=btn(action.getText('reload'),"Richtable.reload()")
/>
</body>
</html></#escape>
