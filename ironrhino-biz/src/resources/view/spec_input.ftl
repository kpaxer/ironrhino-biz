<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if spec.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('role')}</title>
</head>
<body>
<@s.form  action="save" method="post" cssClass="ajax">
	<@s.hidden name="spec.id" />
	<@s.select label="%{getText('basicPackName')}" name="spec.basicPackName" list="{'包','瓶'}"/>
	<@s.textfield label="%{getText('basicQuantity')}" name="spec.basicQuantity" cssClass="double"/>
	<@s.select label="%{getText('basicMeasure')}" name="spec.basicMeasure" list="{'g','kg','l','ml'}"/>
	<@s.textfield label="%{getText('baleQuantity')}" name="spec.baleQuantity" cssClass="integer"/>
	<@s.select label="%{getText('balePackName')}" name="spec.balePackName" list="{'箱','件'}" headerKey="" headerValue="请选择"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html>


