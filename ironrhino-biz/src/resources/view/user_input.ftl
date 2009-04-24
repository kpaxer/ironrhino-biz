<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if user.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('user')}</title>
</head>
<body>
<@s.form action="save2" method="post" cssClass="ajax">
	<@s.if test="%{!user.isNew()}">
		<@s.hidden name="user.id" />
		<@s.textfield label="%{getText('username')}" name="user.username"
			required="true" readonly="true"/>
	</@s.if>
	<@s.else>
		<@s.textfield label="%{getText('username')}" name="user.username"
			required="true" cssClass="required"/>
		<@s.password label="%{getText('password')}" name="password"
			required="true" cssClass="required"/>
		<@s.password label="%{getText('confirmPassword')}"
			name="confirmPassword" required="true" cssClass="required"/>
	</@s.else>
	<@s.textfield label="%{getText('user.name')}" name="user.name"
		required="true" cssClass="required"/>
	<@s.hidden id="regionId" name="regionId"/>
	<@s.textfield id="address" label="%{getText('address')}" name="user.address"/>
	<@s.textfield label="%{getText('postCode')}" name="user.postCode"/>
	<@s.textfield label="%{getText('phone')}" name="user.phone"/>
	<@s.textfield label="%{getText('mobile')}" name="user.mobile"/>
	<@s.checkbox label="%{getText('enabled')}" name="user.enabled" />
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html>


