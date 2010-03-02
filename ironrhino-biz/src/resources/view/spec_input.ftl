<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if spec.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('spec')}</title>
</head>
<body>
<@s.form  action="save" method="post" cssClass="ajax">
	<@s.if test="%{!spec.isNew()}">
		<@s.hidden name="spec.id" />
	</@s.if>
	<p>
	<label for="save_spec_basicQuantity">${action.getText('basicQuantity')}</label>
	<@s.textfield name="spec.basicQuantity" cssClass="double" theme="simple"/>
	&nbsp;<@s.select name="spec.basicMeasure" list="{'g','kg','l','ml'}" theme="simple"/>
	</p>
	<p>
	<label for="save_spec_basicQuantity">${action.getText('baleQuantity')}</label>
	<@s.textfield name="spec.baleQuantity" cssClass="integer" theme="simple"/>
	/<@s.select name="spec.basicPackName" list="{'包','瓶'}" theme="simple"/>
	</p>
	<@s.select label="%{getText('balePackName')}" name="spec.balePackName" list="{'箱','件'}" headerKey="" headerValue="请选择"/>
	<@s.textfield label="%{getText('displayOrder')}" name="spec.displayOrder"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>


