<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('stuff')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"name":{"cellEdit":"click"},"weight":{"cellEdit":"click"},"stock":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button><#t>
<a class="btn" href="chart/view?type=stuff&id=${entity.id}" rel="richtable" '
+'data-windowoptions="{\'width\':\'1200px\',\'reloadonclose\':false}">${action.getText("price")+action.getText("trend")}</a><#t>'
+r'<a class="btn ajax view" href="stuffflow?stuff.id=${entity.id}">${action.getText("stuffflow")+action.getText("record")}</a><#t>
<a class="btn" href="stuffflow/input?stuff.id=${entity.id}" rel="richtable">入库</a><#t>
<a class="btn" href="stuffflow/input?out=true&stuff.id=${entity.id}" rel="richtable">出库</a><#t>
'>
<#assign bottomButtons='
<button type="button" class="btn" data-view="input">${action.getText("create")}</button><#t>
<button type="button" class="btn" data-action="save">${action.getText("save")}</button><#t>
<button type="button" class="btn" data-action="delete">${action.getText("delete")}</button><#t>
<button type="button" class="btn" data-action="reload">${action.getText("reload")}</button><#t>
<a class="btn" href="chart/view?type=stuff" rel="richtable" data-windowoptions="{\'width\':\'1200px\',\'reloadonclose\':false}">${action.getText("compare")+action.getText("price")+action.getText("trend")}</a><#t>
'>
<@richtable entityName="stuff" columns=columns actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons searchable=true/>
</body>
</html></#escape>