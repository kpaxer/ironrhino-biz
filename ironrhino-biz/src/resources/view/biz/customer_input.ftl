<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if customer.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('customer')}</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<#if !customer.new>
		<@s.hidden name="customer.id" />
	</#if>
	<@s.textfield label="%{getText('name')}" name="customer.name" cssClass="required"/>
	<div class="treeselect" treeoptions="{'url':'<@url value="/region/children"/>','name':'region','id':'regionId','cache':false}">
	<@s.hidden id="regionId" name="regionId" />
	<label class="field" for="region"><span style="cursor:pointer;">请选择地区</span></label>
	<#if customer.region??><span id="region">${customer.region.fullname}</span><a class="close">x</a><#else><span id="region">...</span></#if>
	</div>
	<@s.textfield label="%{getText('address')}" name="customer.address" size="50"/>
	<@s.textfield label="%{getText('linkman')}" name="customer.linkman" size="50" />
	<@s.textfield label="%{getText('phone')}" name="customer.phone" size="50" />
	<@s.textfield label="%{getText('mobile')}" name="customer.mobile" size="50" />
	<@s.textfield label="%{getText('fax')}" name="customer.fax" size="50" />
	<@s.textfield label="%{getText('tag')}" name="customer.tagsAsString" size="50" cssClass="tagbox multiautocomplete" source="${getUrl('/biz/customer/tag')}"/>
	<@s.textarea label="%{getText('memo')}" name="customer.memo" cols="50" rows="10"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>