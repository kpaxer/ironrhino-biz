<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>List Customers</title>
</head>
<body>
<#assign config={"id":{"width":"80px"},"name":{"width":"300px","cellEdit":"click"},"address":{"width":"300px","class":"include_if_edited","template":r"<#if entity.region??>${entity.region.fullname!}</#if>${entity.address!}"},"linkman":{"cellEdit":"click"},"phone":{"cellEdit":"click"}}>
<@richtable entityName="customer" config=config/>
</body>
</html></#escape>
