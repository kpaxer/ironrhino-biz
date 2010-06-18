<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('customer')}${action.getText('list')}</title>
<style>
a.region:link,a.region:hover,a.region:visited,a.region:active {
color:#000;
text-decoration:none;
}
</style>
</head>
<body>
<#assign columns={"id":{"width":"60px"},"name":{"width":"120px","cellEdit":"click"},"address":{"template":r'<#if entity.region??><a class="region" title="点击查看${entity.region.fullname}所有客户" href="customer?regionId=${entity.region.id}">${entity.region.fullname}</a></#if>${value!}'},"activeDate":{"width":"105px","template":r"<#if entity.activeDate??>${value?string('yyyy年MM月dd日')}</#if>"},"linkman":{"cellEdit":"click","width":"80px"},"phone":{"cellEdit":"click","width":"100px"},"mobile":{"cellEdit":"click","width":"100px"},"fax":{"cellEdit":"click","width":"100px"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'view\')}' view='view'/>
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'order\')}' type='link' href='${getUrl(\'/order?customer.id=\'+entity.id)}'/>
<@button text='${action.getText(\'create\')+action.getText(\'order\')}' type='link' href='${getUrl(\'/order/input?customer.id=\'+entity.id)}' rel='richtable' windowoptions='{\'width\':\'900px\',\'reloadonclose\':false}'/>
">
<#assign bottomButtons=r"
<@button text='${action.getText(\'create\')}' view='input'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'reload\')}' action='reload'/>
<@button text='${action.getText(\'merge\')}' onclick='$(\'#merge\').toggle()'/>
">
<#assign searchButtons=r"<@button text='按区域检索' type='link' href='${getUrl(\'/chart/geo\')}'/>"/>
<@richtable entityName="customer" columns=columns actionColumnWidth="200px" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons searchable=true searchButtons=searchButtons/>

<form id="merge" action="<@url value="/customer/merge"/>" method="post" class="ajax reset" style="display:none;" onsuccess="Richtable.reload()">
<div>
	<span style="margin:3px;">将</span><input type="text" name="id" class="required"/>
	<span style="margin:3px;">合并到</span><input type="text" name="id" class="required"/>
	<@s.submit theme="simple" value="%{getText('confirm')}" />
	</div>
</form>
</body>
</html></#escape>
