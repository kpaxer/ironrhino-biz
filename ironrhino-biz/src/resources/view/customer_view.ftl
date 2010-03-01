<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${customer.name}</title>
</head>
<body>
<div id="info">
<div>
<span>${action.getText('id')}:&nbsp;&nbsp;</span><span style="font-weight:bold;">${customer.id}</span>
&nbsp;&nbsp;&nbsp;&nbsp;<span>${action.getText('name')}:&nbsp;&nbsp;</span><span style="font-weight:bold;">${customer.name!}</span>
&nbsp;&nbsp;&nbsp;&nbsp;<span>${action.getText('address')}:&nbsp;&nbsp;</span><span style="font-weight:bold;"><#if customer.region??>${customer.region.fullname}</#if>${customer.address!}</span>
</div>
<div>
<span>${action.getText('linkman')}:&nbsp;&nbsp;</span><span style="font-weight:bold;">${customer.linkman!}</span>
&nbsp;&nbsp;&nbsp;&nbsp;<span>${action.getText('phone')}:&nbsp;&nbsp;</span>&nbsp;&nbsp;<span style="font-weight:bold;">${customer.phone!}</span>
</div>
<#if customer.memo?has_content>
<div>
<span>${action.getText('memo')}:&nbsp;&nbsp;</span><span>${customer.memo!}</span>
</div>
</#if>
</div>
</body>
</html></#escape>
