<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('product')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"brand":{"excludeIfNotEdited",true},"category":{"excludeIfNotEdited",true},"name":{"width":"200px","cellEdit":"click"},"stock":{"cellEdit":"click"},"shopStock":{"cellEdit":"click"},"weight":{"cellEdit":"click"},"price":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button><#t>
<a class="btn" href="chart/view?type=product&id=${entity.id}" rel="richtable" '
+'data-windowoptions="{\'width\':\'1200px\',\'reloadonclose\':false}">${action.getText("price")+action.getText("trend")}</a><#t>'
+r'<a class="btn ajax view" href="plan?product.id=${entity.id}">${action.getText("plan")}</a><#t>
<a class="btn" href="plan/input?product.id=${entity.id}" rel="richtable">${action.getText("create")+action.getText("plan")}</a><#t>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input">${action.getText("create")}</button><#t>
<button type="button" class="btn" data-action="save">${action.getText("save")}</button><#t>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button><#t>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button><#t>
<a class="btn" href="${getUrl("/biz/chart/view?type=product")}" rel="richtable" data-windowoptions="{\'width\':\'1200px\',\'reloadonclose\':false}">${action.getText("compare")+action.getText("price")+action.getText("trend")}</a>
'>
<@richtable entityName="product" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons columns=columns searchable=true/>
</body>
</html></#escape>