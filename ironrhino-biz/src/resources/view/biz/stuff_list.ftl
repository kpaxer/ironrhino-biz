<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('stuff')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"name":{"cellEdit":"click"},"weight":{"cellEdit":"click"},"stock":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button>
<a class="btn" href="chart/view?type=stuff&id=${entity.id}" rel="richtable" '
+'data-windowoptions="{\'width\':\'1200px\',\'reloadonclose\':false}">${action.getText("price")+action.getText("trend")}</a> '
+r'<a class="btn ajax view" href="stuffflow?stuff.id=${entity.id}">${action.getText("stuffflow")+action.getText("record")}</a>
<a class="btn" href="stuffflow/input?stuff.id=${entity.id}" rel="richtable">${action.getText("stuffflow.in")}</a>
<a class="btn" href="stuffflow/input?out=true&stuff.id=${entity.id}" rel="richtable">${action.getText("stuffflow.out")}</a>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input">${action.getText("create")}</button>
<button type="button" class="btn confirm" data-action="save">${action.getText("save")}</button>
<button type="button" class="btn confirm" data-action="delete" data-shown="selected">${action.getText("delete")}</button>
<button type="button" class="btn reload">${action.getText("reload")}</button>
<button type="button" class="btn filter">${action.getText("filter")}</button>
<a class="btn" href="chart/view?type=stuff" rel="richtable" data-windowoptions="{\'width\':\'1200px\',\'reloadonclose\':false}">${action.getText("compare")}</a>
'>
<@richtable entityName="stuff" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons searchable=true/>
</body>
</html></#escape>