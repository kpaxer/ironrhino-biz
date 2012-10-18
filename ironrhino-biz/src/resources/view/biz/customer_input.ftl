<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if customer.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('customer')}</title>
</head>
<body>
<@s.form action="${getUrl(actionBaseUrl+'/save')}" method="post" cssClass="ajax form-horizontal">
	<#if !customer.new>
		<@s.hidden name="customer.id" />
	</#if>
	<@s.textfield label="%{getText('name')}" name="customer.name" cssClass="required checkavailable" cssStyle="width:350px;"/>
	<div class="control-group treeselect" data-options="{'url':'<@url value="/region/children"/>','name':'region','id':'regionId','cache':false}">
	<@s.hidden id="regionId" name="regionId" />
	<label class="control-label" for="region">地区</label>
	<div class="controls">
	<span id="region"><#if customer.region??>${customer.region.fullname}<a class="remove" href="#">&times;</a></span><#else>...</#if></span>
	</div>
	</div>
	<@s.textfield label="%{getText('address')}" name="customer.address" cssStyle="width:350px;"/>
	<@s.textfield label="%{getText('linkman')}" name="customer.linkman" cssStyle="width:350px;" />
	<@s.textfield label="%{getText('phone')}" name="customer.phone" cssStyle="width:350px;" />
	<@s.textfield label="%{getText('mobile')}" name="customer.mobile" cssStyle="width:350px;" />
	<@s.textfield label="%{getText('fax')}" name="customer.fax" cssStyle="width:350px;" />
	<@s.textfield label="%{getText('tag')}" name="customer.tagsAsString" cssStyle="width:350px;" cssClass="tags" source="${getUrl('/biz/customer/tag')}"/>
	<@s.textarea label="%{getText('memo')}" name="customer.memo" cssStyle="width:350px;height:40px;"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>