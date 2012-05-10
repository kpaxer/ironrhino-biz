<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('customer')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"id":{"width":"60px"},"name":{"width":"120px","cellEdit":"click"},"address":{"template":r'<#if entity.region??><#if !Parameters.regionId??><a class="region" title="点击查看${entity.region.fullname}所有客户" href="customer?regionId=${entity.region.id}"></#if>${entity.region.fullname}<#if !Parameters.regionId??></a></#if></#if>${value!}'},"activeDate":{"width":"105px","template":r"${(entity.activeDate?string('yyyy年MM月dd日'))!}"},"linkman":{"cellEdit":"click","width":"80px"},"phone":{"cellEdit":"click","width":"100px"},"mobile":{"cellEdit":"click","width":"100px"},"fax":{"cellEdit":"click","width":"100px"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="view">${action.getText("view")}</button><#t>
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button><#t>
<a class="btn ajax view" href="order?customer.id=${entity.id}">${action.getText("order")}</a><#t>
<a class="btn" href="order/input?customer.id=${entity.id}" rel="richtable"
'+'
 data-windowoptions="{\'width\':\'950px\',\'reloadonclose\':false}">${action.getText("create")}${action.getText("order")}</a>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input">${action.getText("create")}</button><#t>
<button type="button" class="btn" data-action="save">${action.getText("save")}</button><#t>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button><#t>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button><#t>
<button type="button" class="btn" onclick="$(\'#merge\').toggle()">${action.getText("merge")}</button><#t>
'>
<#assign searchButtons=r'<a class="btn" href="chart/geo" target="_blank">按区域检索</a>'/>
<@richtable entityName="customer" columns=columns actionColumnWidth="195px" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons searchable=true searchButtons=searchButtons/>

<form id="merge" action="customer/merge" method="post" class="ajax reset" style="display:none;" onprepare="return confirm('此操作不能恢复,确定要合并?');" onsuccess="Richtable.reload($('#customer_form'))">
<div style="padding-top:10px;text-align:center;">
	<span style="margin:3px;">将</span><input type="text" name="id" class="required"/>
	<span style="margin:3px;">合并到</span><input type="text" name="id" class="required"/>
	<@s.submit theme="simple" value="%{getText('confirm')}" />
	</div>
</form>
</body>
</html></#escape>
