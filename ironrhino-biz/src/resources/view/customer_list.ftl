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
<#assign config={"id":{},"name":{"width":"180px","cellEdit":"click"},"address":{"width":"300px","template":r'<#if entity.region??><a class="region" title="点击查看${entity.region.fullname}所有客户" href="customer?regionId=${entity.region.id}">${entity.region.fullname}</a></#if>${value!}'},"linkman":{"cellEdit":"click"},"phone":{"cellEdit":"click"},"fax":{"cellEdit":"click"}}>
<#assign actionColumnButtons=btn(action.getText('save'),null,'save')
+btn(action.getText('edit'),null,'input')
+btn(action.getText('view'),null,'view')
+btn(action.getText('order'),'','','link','',r'order?customer.id=${rowid}')
+btn(action.getText('delete'),null,'del')
>
<#assign bottomButtons=btn(action.getText('create'),null,'input')
+btn(action.getText('save'),null,'save')
+btn(action.getText('delete'),null,'del')
+btn('合并客户','$(\'#merge\').show()')
+btn(action.getText('reload'),null,'reload')
>

<#assign searchButtons=btn('按区域检索','','','link','',r'${getUrl("/customer/region")}')/>
<@richtable entityName="customer" config=config actionColumnWidth="210px" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons searchable=true searchButtons=searchButtons/>

<form id="merge" action="<@url value="/customer/merge"/>" method="post" class="ajax reset" style="display:none;" onsuccess="Richtable.reload()">
<p>
	<span style="margin:3px;">将</span><input type="text" name="id" class="required"/>
	<span style="margin:3px;">合并到</span><input type="text" name="id" class="required"/>
	<@s.submit theme="simple" value="%{getText('confirm')}" />
	</p>
</form>
</body>
</html></#escape>
