<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('product')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"brand":{"excludeIfNotEdited",true},"category":{"excludeIfNotEdited",true},"name":{"width":"200px","cellEdit":"click"},"stock":{"cellEdit":"click"},"shopStock":{"cellEdit":"click"},"weight":{"cellEdit":"click"},"price":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'price\')+action.getText(\'trend\')}' type='link' href='chart/view?type=product&id=${entity.id}' rel='richtable' windowoptions='{\'width\':\'1200px\',\'reloadonclose\':false}'/>
<@button text='${action.getText(\'plan\')}' type='link' href='plan?product.id=${entity.id}' class='ajax view'/>
<@button text='${action.getText(\'create\')+action.getText(\'plan\')}' type='link' href='plan/input?product.id=${entity.id}' rel='richtable'/>
">
<#assign bottomButtons=r"
<@button text='${action.getText(\'create\')}' view='input'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'reload\')}' action='reload'/>
<@button text='${action.getText(\'compare\')+action.getText(\'price\')+action.getText(\'trend\')}' type='link' href='chart/view?type=product' rel='richtable' windowoptions='{\'width\':\'1200px\',\'reloadonclose\':false}'/>
">
<@richtable entityName="product" actionColumnWidth="230px" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons columns=columns searchable=true/>
</body>
</html></#escape>