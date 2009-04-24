<#include "richtable-macro.ftl"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>List Products</title>
</head>
<body>
<#assign config={"name":{},"stock":{},"criticalStock":{},"spec":{},"category":{}}>
<@richtable entityName="product" config=config  celleditable=false actionColumnWidth="200px" actionColumnButtons='<button type="button" onclick="Richtable.input(\'#id\')">编辑</button><button type="button" onclick="Richtable.open(Richtable.getUrl(\'category\',\'#id\'),true)">目录</button><button type="button" onclick="Richtable.del(\'#id\')">删除</button>'/>
</body>
</html>