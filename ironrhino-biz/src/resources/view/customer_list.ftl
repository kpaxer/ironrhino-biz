<#include "richtable-macro.ftl"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>List Customers</title>
</head>
<body>
<#assign config={"code":{},"name":{"cellEdit":"input"},"address":{"cellEdit":"input"},"postCode":{"cellEdit":"input"},"phone":{"cellEdit":"input"},"mobile":{"cellEdit":"input"},"description":{"cellEdit":"input"}}>
<#assign actionColumnButtons=btn("Richtable.save('#id')",action.getText('save'))+btn("Richtable.input('#id')",action.getText('edit'))+btn("Richtable.open(Richtable.getUrl('region','#id'),true)","地区")+btn("Richtable.del('#id')",action.getText('delete'))>
<@richtable entityName="customer" config=config actionColumnWidth="180px" actionColumnButtons=actionColumnButtons/>
</body>
</html>
