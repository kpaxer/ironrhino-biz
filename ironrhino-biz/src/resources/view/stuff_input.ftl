<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title></title>
</head>
<body>
<@s.form action="save" method="post" cssClass="ajax">
	<@s.hidden name="stuff.id" />
	<@s.if test="%{stuff.isNew()}">
	<@s.textfield label="%{getText('name')}" name="stuff.name" cssClass="required"/>
	<@s.select label="%{getText('spec')}" name="specId" cssClass="required" list="specList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
   	<@s.select label="%{getText('vendor')}" name="vendorId" list="vendorList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
	</@s.if>
	<@s.else>
	<@s.hidden name="stuff.name" />
	<@s.hidden name="specId" />
	<@s.hidden name="vendorId" />
	</@s.else>
	<@s.textfield label="%{getText('criticalStock')}" name="stuff.criticalStock" cssClass="integer"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html>


