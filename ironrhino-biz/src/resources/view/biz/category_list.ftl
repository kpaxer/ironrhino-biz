<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('category')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"name":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button>
<a class="btn" href="${getUrl("/common/schema/input/category:"+entity.id+"?brief=true")}" rel="richtable">${action.getText("schema")}</a>
'>
<@richtable entityName="category" actionColumnButtons=actionColumnButtons columns=columns searchable=true/>
</body>
</html></#escape>