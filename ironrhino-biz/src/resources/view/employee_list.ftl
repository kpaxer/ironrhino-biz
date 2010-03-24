<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('employee')}${action.getText('list')}</title>
<style>
a.region:link,a.region:hover,a.region:visited,a.region:active {
color:#000;
text-decoration:none;
}
</style>
</head>
<body>
<#assign config={"id":{},"name":{"cellEdit":"click"},"phone":{"cellEdit":"click"},"dimission":{"cellEdit":"click,select_template_boolean"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'order\')}' type='link' href='${getUrl(\'/order?employee.id=\'+entity.id)}'/>
<@button text='${action.getText(\'reward\')}' type='link' href='${getUrl(\'/reward?employee.id=\'+entity.id)}'/>
<@button text='${action.getText(\'create\')+action.getText(\'reward\')}' type='link' href='${getUrl(\'/reward/input?employee.id=\'+entity.id)}' rel='richtable'/>
">
<@richtable entityName="employee" config=config actionColumnWidth="270px" actionColumnButtons=actionColumnButtons/>
</body>
</html></#escape>
