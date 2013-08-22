<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('customer')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"name":{"width":"120px","cellEdit":"click"},"address":{"cssClass":"excludeIfNotEdited hidden-pad","template":r'<#if entity.region??><#if !Parameters.regionId??><a class="region" title="点击查看${entity.region.fullname}所有客户" href="customer?regionId=${entity.region.id}"></#if>${entity.region.fullname}<#if !Parameters.regionId??></a></#if></#if>${value!}'},"linkman":{"cellEdit":"click","width":"80px"},"phone":{"cellEdit":"click","width":"100px"},"mobile":{"cellEdit":"click","width":"100px"},"fax":{"cellEdit":"click","width":"100px"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="view">${action.getText("view")}</button>
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button>
<a class="btn ajax view" href="order?customer.id=${entity.id}">${action.getText("order")}</a>
<a class="btn" href="order/input?customer.id=${entity.id}" rel="richtable"
'+'
 data-windowoptions="{\'width\':\'950px\',\'reloadonclose\':false}">${action.getText("create")}${action.getText("order")}</a>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input">${action.getText("create")}</button>
<button type="button" class="btn confirm" data-action="save">${action.getText("save")}</button>
<button type="button" class="btn" data-action="delete" data-shown="selected">${action.getText("delete")}</button>
<button type="button" class="btn reload">${action.getText("reload")}</button>
<button type="button" class="btn filter">${action.getText("filter")}</button>
<button type="button" class="btn" onclick="$(\'#merge\').toggle()">${action.getText("merge")}</button>
<button type="submit" class="btn noajax" formaction="${actionBaseUrl}/vcard">${action.getText("vcard")}</button>
'>
<#assign searchButtons=r'<a href="chart/geo" class="hidden-tablet hidden-phone hidden-pad" target="_blank" title="按区域检索" style="line-height:30px;margin-left:2px;"><i class="icon-filter"></i></a>'/>
<@richtable entityName="customer" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons searchable=true/>

<form id="merge" action="customer/merge" method="post" class="ajax reset form-inline" style="display:none;" onprepare="return confirm('此操作不能恢复,确定要合并?');" onsuccess="$('#customer_form').submit()">
<div style="text-align:center;">
	<span style="margin:3px;">将</span><input type="text" name="id" class="required"/>
	<span style="margin:3px;">合并到</span><input type="text" name="id" class="required"/>
	<@s.submit theme="simple" value="%{getText('confirm')}" />
	</div>
</form>
</body>
</html></#escape>
