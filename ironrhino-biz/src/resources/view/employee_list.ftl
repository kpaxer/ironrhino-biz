<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('employee')}</title>
<style>
a.region:link,a.region:hover,a.region:visited,a.region:active {
color:#000;
text-decoration:none;
}
</style>
</head>
<body>
<#assign config={"id":{},"name":{"cellEdit":"click"},"phone":{"cellEdit":"click"},"disabled":{"cellEdit":"click,select_template_boolean"}}>
<#assign actionColumnButtons=btn(action.getText('save'),null,'save')
+btn(action.getText('edit'),null,'input')
+btn(action.getText('reward'),'','','link','',r'reward?employee.id=${rowid}')
+btn(action.getText('delete'),null,'del')>

<@richtable entityName="employee" config=config actionColumnWidth="210px" actionColumnButtons=actionColumnButtons/>
</body>
</html></#escape>
