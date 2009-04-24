<#include "richtable-macro.ftl"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>List Customers</title>
</head>
<body>
<#assign config={"code":{},"name":{"cellEdit":"input"},"address":{"cellEdit":"input"},"postCode":{"cellEdit":"input"},"phone":{"cellEdit":"input"},"mobile":{"cellEdit":"input"},"description":{"cellEdit":"input"}}>
<@richtable entityName="customer" config=config actionColumnWidth="260px" actionColumnButtons='<button type="button" onclick="Richtable.save(\'#id\')">保存</button><button type="button" onclick="Richtable.input(\'#id\')">编辑</button><button type="button" onclick="Richtable.open(Richtable.getUrl(\'region\',\'#id\'),true)">地区</button><button type="button" onclick="Richtable.del(\'#id\')">删除</button>'/>
</body>
</html>
