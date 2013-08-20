<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('category')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"name":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click","width":"100px"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button>
<a class="btn" href="${getUrl("/common/schema/input/category:"+entity.id+"?brief=true")}" rel="richtable">${action.getText("schema")}</a>
'>
<@richtable entityName="category" actionColumnButtons=actionColumnButtons columns=columns searchable=true/>
</body>
</html></#escape>