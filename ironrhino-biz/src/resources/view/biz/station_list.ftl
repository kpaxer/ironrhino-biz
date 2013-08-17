<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('station')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"name":{"width":"120px","cellEdit":"click"},"address":{"width":"220px","cssClass":"excludeIfNotEdited","template":r'<#if entity.region??><a class="region" title="点击查看${entity.region.fullname}所有货运站" href="station?regionId=${entity.region.id}">${entity.region.fullname}</a></#if>${value!}'},"destination":{"width":"150px","cellEdit":"click"},"linkman":{"cellEdit":"click","width":"80px"},"phone":{"cellEdit":"click","width":"200px"},"mobile":{"cellEdit":"click","width":"200px"},"fax":{"cellEdit":"click","width":"80px"}}>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input">${action.getText("create")}</button>
<button type="button" class="btn confirm" data-action="save">${action.getText("save")}</button>
<button type="button" class="btn" data-action="delete" data-shown="selected">${action.getText("delete")}</button>
<button type="button" class="btn reload">${action.getText("reload")}</button>
<button type="button" class="btn" onclick="$(\'#merge\').toggle()">${action.getText("merge")}</button>
'>
<@richtable entityName="station" columns=columns bottomButtons=bottomButtons searchable=true/>

<form id="merge" action="station/merge" method="post" class="ajax reset" style="display:none;" onprepare="return confirm('此操作不能恢复,确定要合并?');" onsuccess="$('#station_form').submit()">
<div style="padding-top:10px;text-align:center;">
	<span style="margin:3px;">将</span><input type="text" name="id" class="required"/>
	<span style="margin:3px;">合并到</span><input type="text" name="id" class="required"/>
	<@s.submit theme="simple" value="%{getText('confirm')}" />
	</div>
</form>
</body>
</html></#escape>
