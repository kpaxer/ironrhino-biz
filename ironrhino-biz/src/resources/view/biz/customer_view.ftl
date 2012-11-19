<!DOCTYPE html>
<#escape x as x?html><html>
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
<table id="details" class="table<#if !Parameters.printpage??> table-bordered</#if> middle">
	<tbody>
		<tr>
			<td class="fieldlabel">${action.getText('id')}</td><td>${customer.id!}</td>
			<td class="fieldlabel">${action.getText('name')}</td><td width="18%">${customer.name!}</td>
			<td class="fieldlabel">${action.getText('address')}</td><td width="25%">${customer.fullAddress!}</td>
		</tr>
		<tr>
			<td class="fieldlabel">${action.getText('phone')}</td><td>${customer.phone!}</td>
			<td class="fieldlabel">${action.getText('mobile')}</td><td>${customer.mobile!}</td>
			<td class="fieldlabel">${action.getText('fax')}</td><td>${customer.fax!}</td>
		</tr>
		<tr>
			<td class="fieldlabel">${action.getText('tag')}</td><td>${customer.tagsAsString!}</td>
			<td class="fieldlabel">${action.getText('createDate')}</td><td>${customer.createDate?string('yyyy年MM月dd日')!}</td>
			<td class="fieldlabel">${action.getText('activeDate')}</td><td>${customer.activeDate?string('yyyy年MM月dd日')!}</td>
		</tr>
	</tbody>
</table>
<#if customer.memo?has_content>
<pre>${customer.memo!}</pre>
</#if>
<#if !Parameters.printpage??>
<div style="text-align:center;">
	<a href="${getUrl(actionBaseUrl+'/view/'+customer.id+'?decorator=simple&printpage=true')}" target="_blank" class="btn">${action.getText('print')}</a>
</div>
</#if>
</#if>
</body>
</html></#escape>
