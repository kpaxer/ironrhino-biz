<#include "richtable-macro.ftl"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('user')}</title>
</head>
<body>
<#assign config={"username":{},"name":{},"password":{"value":"********","trimPrefix":true,"cellEdit":"input,ec_edit_template_password","class":"include_if_edited"}}>
<@richtable entityName="user" config=config actionColumnWidth="260px" actionColumnButtons='<button type="button" onclick="Richtable.save(\'#id\')">保存</button><button type="button" onclick="Richtable.input(\'#id\')">编辑</button><button type="button" onclick="Richtable.open(Richtable.getUrl(\'role\',\'#id\'))">角色</button><button type="button" onclick="Richtable.del(\'#id\')">删除</button>'/>
<div style="display: none;">
<textarea id="ec_edit_template_password">
	<input type="password" class="inputtext" value=""
	onblur="Richtable.updatePasswordCell(this)" style="width: 100%;" name="" />
</textarea></div>
</body>
</html>