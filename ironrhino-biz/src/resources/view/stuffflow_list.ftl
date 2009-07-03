<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>出入库审核</title>
</head>
<body>
<#assign config={"stuff":{},"quantity":{},"amount":{},"requestUser":{},"requestDate":{}}>
<#assign actionColumnButtons=btn("Richtable.execute('confirm','#id')",action.getText('confirm'))+btn("Richtable.execute('cancel','#id')",action.getText('cancel'))>
<@richtable 
entityName="stuffflow" 
config=config 
actionColumnWidth="100px" 
actionColumnButtons=actionColumnButtons 
bottomButtons=btn("Richtable.reload()",action.getText('reload'))
/>
</body>
</html>
