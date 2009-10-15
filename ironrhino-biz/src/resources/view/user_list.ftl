<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('user')}</title>
</head>
<body>
<#assign config={"username":{},"name":{},"password":{"value":"********","trimPrefix":true,"cellEdit":"input,ec_edit_template_password","class":"include_if_edited"}}>
<#assign actionColumnButtons=btn(action.getText('save'),r"Richtable.save('${rowid}')")+btn(action.getText('edit'),r"Richtable.input('${rowid}')")+btn(action.getText('role'),r"Richtable.open(Richtable.getUrl('role','${rowid}'))")+btn(action.getText('delete'),r"Richtable.del('${rowid}')")>
<@richtable entityName="user" config=config actionColumnWidth="180px" actionColumnButtons=actionColumnButtons/>
<div style="display: none;">
<textarea id="ec_edit_template_password">
	<input type="password" class="inputtext" value=""
	onblur="Richtable.updatePasswordCell(this)" style="width: 100%;" name="" />
</textarea></div>
</body>
</html></#escape>