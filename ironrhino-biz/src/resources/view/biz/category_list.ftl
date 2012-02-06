<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('category')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"name":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'schema\')}' type='link' href='${getUrl(\'/common/schema/input/category:\'+entity.id+\'?brief=true\')}' rel='richtable'/>
">

<@richtable entityName="category" actionColumnWidth="120px" actionColumnButtons=actionColumnButtons columns=columns searchable=true/>
</body>
</html></#escape>