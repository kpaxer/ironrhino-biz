<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('station')}${action.getText('list')}</title>
<style>
a.region:link,a.region:hover,a.region:visited,a.region:active {
color:#000;
text-decoration:none;
}
</style>
</head>
<body>
<#assign columns={"id":{"width":"60px"},"name":{"width":"120px","cellEdit":"click"},"address":{"width":"220px","template":r'<#if entity.region??><a class="region" title="点击查看${entity.region.fullname}所有客户" href="station?regionId=${entity.region.id}">${entity.region.fullname}</a></#if>${value!}'},"destination":{"width":"180px","cellEdit":"click"},"linkman":{"cellEdit":"click","width":"80px"},"phone":{"cellEdit":"click","width":"100px"},"mobile":{"cellEdit":"click","width":"100px"},"fax":{"cellEdit":"click","width":"100px"}}>
<#assign bottomButtons=r"
<@button text='${action.getText(\'create\')}' view='input'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'reload\')}' action='reload'/>
<@button text='${action.getText(\'merge\')}' onclick='$(\'#merge\').toggle()'/>
">
<@richtable entityName="station" columns=columns actionColumnWidth="120px" bottomButtons=bottomButtons searchable=true/>

<form id="merge" action="<@url value="/station/merge"/>" method="post" class="ajax reset" style="display:none;" onsuccess="Richtable.reload()">
<div>
	<span style="margin:3px;">将</span><input type="text" name="id" class="required"/>
	<span style="margin:3px;">合并到</span><input type="text" name="id" class="required"/>
	<@s.submit theme="simple" value="%{getText('confirm')}" />
	</div>
</form>
</body>
</html></#escape>
