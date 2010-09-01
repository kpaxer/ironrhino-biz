<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${customer.name}</title>
</head>
<body>
<div id="info">
<div>
<span>${action.getText('id')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.id}</span>
<span style="margin-left:40px;">${action.getText('name')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.name!}</span>
<span style="margin-left:40px;">${action.getText('address')}:</span><span style="font-weight:bold;margin-left:20px;"><#if customer.region??>${customer.region.fullname}</#if>${customer.address!}</span>
</div>
<div>
<span>${action.getText('linkman')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.linkman!}</span>
<span style="margin-left:40px;">${action.getText('phone')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.phone!}</span>
<span style="margin-left:40px;">${action.getText('mobile')}:</span><span style="font-weight:bold;margin-left:20px;">${customer.mobile!}</span>
</div>
<#if customer.memo?has_content>
<div>
<span>${action.getText('memo')}:</span><span style="margin-left:20px;">${customer.memo!}</span>
</div>
</#if>
</div>
</body>
</html></#escape>
