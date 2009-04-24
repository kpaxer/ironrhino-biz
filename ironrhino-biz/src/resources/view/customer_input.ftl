<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>Create/Edit Customer</title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<@s.if test="%{!customer.isNew()}">
		<@s.hidden name="customer.id" />
		<@s.textfield label="%{getText('customer.code')}" name="customer.code"
			readonly="true" />
	</@s.if>
	<@s.else>
		<@s.hidden name="regionId" />
		<@s.textfield label="%{getText('customer.code')}" name="customer.code" />
	</@s.else>
	<@s.textfield label="%{getText('customer.name')}" name="customer.name" />
	<@s.textfield label="%{getText('customer.address')}" id="address" name="customer.address" onfocus="Region.select('address')"/>
	<@s.textfield label="%{getText('customer.postCode')}" name="customer.postCode" />
	<@s.textfield label="%{getText('customer.phone')}" name="customer.phone" />
	<@s.textfield label="%{getText('customer.mobile')}" name="customer.mobile" />
	<@s.textarea label="%{getText('customer.description')}"
		name="customer.description" />
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html>


