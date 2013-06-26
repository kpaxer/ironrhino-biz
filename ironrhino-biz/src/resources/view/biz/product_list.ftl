<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('product')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"brand":{"excludeIfNotEdited",true},"category":{"excludeIfNotEdited",true},"name":{"width":"150px","cellEdit":"click"},"stock":{"width":"80px","cellEdit":"click"},"shopStock":{"width":"90px","cellEdit":"click"},"weight":{"width":"70px","cellEdit":"click"},"price":{"width":"70px","cellEdit":"click"},"displayOrder":{"width":"120px","cellEdit":"click"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button>
<a class="btn hidden-tablet hidden-phone hidden-pad" href="chart/view?type=product&id=${entity.id}" rel="richtable" '
+'data-windowoptions="{\'width\':\'1200px\',\'reloadonclose\':false}">${action.getText("price")+action.getText("trend")}</a> '
+r'<a class="btn ajax view" href="plan?product.id=${entity.id}">${action.getText("plan")}</a>
<a class="btn" href="plan/input?product.id=${entity.id}" rel="richtable">${action.getText("create")+action.getText("plan")}</a>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input">${action.getText("create")}</button>
<button type="button" class="btn confirm" data-action="save">${action.getText("save")}</button>
<button type="button" class="btn" data-action="delete" data-shown="selected">${action.getText("delete")}</button>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button>
<a class="btn hidden-tablet hidden-phone hidden-pad" data-shown="selected" href="${getUrl("/biz/chart/view?type=product")}" rel="richtable" data-windowoptions="{\'width\':\'1200px\',\'reloadonclose\':false}">${action.getText("compare")}</a>
'>
<@richtable entityName="product" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons columns=columns searchable=true/>
</body>
</html></#escape>