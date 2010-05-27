<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if customer.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('customer')}</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<@s.if test="%{!customer.isNew()}">
		<@s.hidden name="customer.id" />
	</@s.if>
	<@s.textfield label="%{getText('name')}" name="customer.name" cssClass="required"/>
	<div class="selectregion" regionname="region" full="true" regionid="regionId">
	<@s.hidden id="regionId" name="regionId" />
	<label class="field" for="region"><span style="cursor:pointer;">请选择地区</span></label>
	<div id="region"><#if customer.region??>${customer.region.fullname}<#else>...</#if></div>
	</div>
	<@s.textfield label="%{getText('address')}" id="address" name="customer.address"/>
	<@s.textfield label="%{getText('linkman')}" name="customer.linkman" />
	<@s.textfield label="%{getText('phone')}" name="customer.phone" />
	<@s.textfield label="%{getText('mobile')}" name="customer.mobile" />
	<@s.textfield label="%{getText('fax')}" name="customer.fax" />
	<@s.textarea label="%{getText('memo')}" name="customer.memo" cols="50" rows="10"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>