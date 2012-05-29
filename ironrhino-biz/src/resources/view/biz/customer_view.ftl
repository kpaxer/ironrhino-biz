<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${customer.name}</title>
<#if Parameters.type?default('')=='card'>
<meta name="decorator" content="none"/>
</#if>
</head>
<body>
<#if Parameters.type?default('')=='card'>
	<div style="text-align:left;">
	<div><span>${action.getText('id')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.id}<span></div>
	<div><span>${action.getText('name')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.name!}<span></div>
	<div><span>${action.getText('address')}:</span><span style="font-weight:bold;margin-left:20px;"><#if customer.region??>${customer.region.fullname}</#if>${customer.address!}<span></div>
	<div><span>${action.getText('linkman')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.linkman!}<span></div>
	<div><span>${action.getText('phone')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.phone!}<span></div>
	<div><span>${action.getText('mobile')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.mobile!}<span></div>
	<div><span>${action.getText('fax')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.fax!}<span></div>
	<#if customer.tags?size gt 0><div><span>${action.getText('tag')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.tagsAsString!}</span></div></#if>
	<div><span>${action.getText('activeDate')}:</span><span>${customer.activeDate?string('yyyy年MM月dd日')}</span></div>
	<div><span>${action.getText('createDate')}:</span><span>${customer.createDate?string('yyyy年MM月dd日')}</span></div>
	<#if customer.memo?has_content><div><span>${action.getText('memo')}:</span><span style="margin-left:20px;">${customer.memo!}</span></div></#if>
	</div>
<#else>
	<div id="info">
		<div>
		<span>${action.getText('id')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.id}</span>
		<span style="margin-left:40px;">${action.getText('name')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.name!}</span>
		<span style="margin-left:40px;">${action.getText('address')}:</span><span style="font-weight:bold;margin-left:20px;"><#if customer.region??>${customer.region.fullname}</#if>${customer.address!}</span>
		</div>
		<div>
		<span>${action.getText('linkman')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.linkman!}</span>
		<span style="margin-left:20px;">${action.getText('phone')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.phone!}</span>
		<span style="margin-left:20px;">${action.getText('mobile')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.mobile!}</span>
		<span style="margin-left:20px;">${action.getText('fax')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.fax!}</span>
		</div>
		<#if customer.tags?size gt 0>
		<div>
		<span>${action.getText('tag')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.tagsAsString!}</span>
		</div>
		</#if>
		<div>
		<span>${action.getText('activeDate')}:</span><span>${customer.activeDate?string('yyyy年MM月dd日')}</span>
		<span style="margin-left:5px;">${action.getText('createDate')}:</span><span>${customer.createDate?string('yyyy年MM月dd日')}</span>
		</div>
		<#if customer.memo?has_content>
		<div>
		<span>${action.getText('memo')}:</span><span style="margin-left:20px;">${customer.memo!}</span>
		</div>
		</#if>
	</div>
</#if>
</body>
</html></#escape>
