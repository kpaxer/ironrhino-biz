<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>List Customers</title>
</head>
<body>
<#assign config={"id":{"width":"80px"},"name":{"width":"300px","cellEdit":"click"},"address":{"width":"300px","class":"include_if_edited","template":r'<#if entity.region??><a title="点击查看${entity.region.name}所有客户" href="customer?regionId=${entity.region.id}" style="text-decoration:none;">${entity.region.fullname}${entity.address!}</a><#else>${entity.address!}</#if>'},"linkman":{"cellEdit":"click"},"phone":{"cellEdit":"click"}}>
<#assign actionColumnButtons=btn(action.getText('save'),null,'save')+btn(action.getText('edit'),null,'input')+btn(action.getText('view'),r"Richtable.open(Richtable.getUrl('view','${rowid}'),true)")+btn(action.getText('delete'),null,'del')>
<@richtable entityName="customer" config=config actionColumnWidth="180px" actionColumnButtons=actionColumnButtons/>
</body>
</html></#escape>
