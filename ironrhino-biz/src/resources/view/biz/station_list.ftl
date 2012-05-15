<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('station')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"id":{"width":"60px"},"name":{"width":"120px","cellEdit":"click"},"address":{"width":"220px","template":r'<#if entity.region??><a class="region" title="点击查看${entity.region.fullname}所有客户" href="station?regionId=${entity.region.id}">${entity.region.fullname}</a></#if>${value!}'},"destination":{"width":"100px","cellEdit":"click"},"linkman":{"cellEdit":"click","width":"80px"},"phone":{"cellEdit":"click","width":"200px"},"mobile":{"cellEdit":"click","width":"165px"},"fax":{"cellEdit":"click","width":"100px"}}>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input">${action.getText("create")}</button><#t>
<button type="button" class="btn" data-action="save">${action.getText("save")}</button><#t>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button><#t>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button><#t>
<button type="button" class="btn" onclick="$(\'#merge\').toggle()">${action.getText("merge")}</button><#t>
'>
<@richtable entityName="station" columns=columns bottomButtons=bottomButtons searchable=true/>

<form id="merge" action="station/merge" method="post" class="ajax reset" style="display:none;" onprepare="return confirm('此操作不能恢复,确定要合并?');" onsuccess="Richtable.reload($('#station_form'))">
<div style="padding-top:10px;text-align:center;">
	<span style="margin:3px;">将</span><input type="text" name="id" class="required"/>
	<span style="margin:3px;">合并到</span><input type="text" name="id" class="required"/>
	<@s.submit theme="simple" value="%{getText('confirm')}" />
	</div>
</form>
</body>
</html></#escape>
