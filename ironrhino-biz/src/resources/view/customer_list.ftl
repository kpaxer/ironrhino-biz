<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('customer')}</title>
<style>
a.region:link,a.region:hover,a.region:visited,a.region:active {
color:#000;
text-decoration:none;
}
</style>
</head>
<body>
<#assign config={"id":{"width":"80px"},"name":{"width":"300px","cellEdit":"click"},"address":{"width":"300px","template":r'<#if entity.region??><a class="region" title="点击查看${entity.region.fullname}所有客户" href="customer?regionId=${entity.region.id}">${entity.region.fullname}</a></#if>${entity.address!}'},"linkman":{"cellEdit":"click"},"phone":{"cellEdit":"click"}}>
<#assign actionColumnButtons=btn(action.getText('save'),null,'save')+btn(action.getText('edit'),null,'input')+btn(action.getText('view'),r"Richtable.open(Richtable.getUrl('view','${rowid}'),true)")+btn(action.getText('delete'),null,'del')>
<@richtable entityName="customer" config=config actionColumnWidth="180px" actionColumnButtons=actionColumnButtons/>
<form action="<@url value="customer"/>" method="post" class="ajax view" replacement="customer_form">
<@s.textfield theme="simple" name="q" size="20"/><@s.submit theme="simple" value="%{getText('search')}" /><@button type="link" text="按区域检索" href="${getUrl('/customer/region')}"/>
</form>
</body>
</html></#escape>
