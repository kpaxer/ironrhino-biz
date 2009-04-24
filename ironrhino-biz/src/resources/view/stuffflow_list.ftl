<#include "richtable-macro.ftl"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>出入库审核</title>
</head>
<body>
<#assign config={"stuff":{},"quantity":{},"amount":{},"requestUser":{},"requestDate":{}}>
<@richtable 
entityName="stuffflow" 
config=config 
actionColumnWidth="150px" 
actionColumnButtons='<button type="button" onclick="Richtable.execute(\'confirm\',\'#id\')">确认</button><button type="button" onclick="Richtable.execute(\'cancel\',\'#id\')">退回</button>' 
bottomButtons='<button type="button" onclick="Richtable.reload()">刷新</button>'
/>
</body>
</html>
