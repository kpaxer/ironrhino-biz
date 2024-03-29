﻿<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if customer.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('customer')}</title>
</head>
<body>
<@s.form action="${actionBaseUrl}/save" method="post" class="ajax form-horizontal sequential_create">
	<#if !customer.new>
		<@s.hidden name="customer.id" />
	</#if>
	<@s.textfield label="%{getText('name')}" name="customer.name" class="required checkavailable" cssStyle="width:350px;"/>
	<div class="control-group treeselect" data-options="{'url':'<@url value="/region/children"/>','cache':false}">
	<@s.hidden class="treeselect-id" name="regionId" />
	<label class="control-label" for="region">地区</label>
	<div class="controls">
	<span class="treeselect-name"><#if customer.region??>${customer.region.fullname}</#if></span>
	</div>
	</div>
	<@s.textfield label="%{getText('address')}" name="customer.address" cssStyle="width:350px;"/>
	<@s.textfield label="%{getText('linkman')}" name="customer.linkman" cssStyle="width:350px;" />
	<@s.textfield label="%{getText('phone')}" name="customer.phone" cssStyle="width:350px;" />
	<@s.textfield label="%{getText('mobile')}" name="customer.mobile" cssStyle="width:350px;" />
	<@s.textfield label="%{getText('fax')}" name="customer.fax" cssStyle="width:350px;" />
	<@s.textfield label="%{getText('tag')}" name="customer.tagsAsString" cssStyle="width:350px;" class="tags" dynamicAttributes={"data-source":"${actionBaseUrl}/tag"}/>
	<@s.textarea label="%{getText('memo')}" name="customer.memo" cssStyle="width:350px;height:40px;"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>