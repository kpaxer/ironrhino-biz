<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('product')}${action.getText('list')}</title>
</head>
<body>
<#assign config={"brand":{},"category":{},"name":{"width":"200px","cellEdit":"click"},"stock":{"cellEdit":"click"},"weight":{"cellEdit":"click"},"price":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'price\')+action.getText(\'trend\')}' type='link' href='${getUrl(\'/chart/view?type=product&id=\'+entity.id)}' rel='richtable' windowoptions='{\'width\':\'1200px\'}'/>
<@button text='${action.getText(\'plan\')}' type='link' href='${getUrl(\'/plan?product.id=\'+entity.id)}'/>
<@button text='${action.getText(\'create\')+action.getText(\'plan\')}' type='link' href='${getUrl(\'/plan/input?product.id=\'+entity.id)}' rel='richtable'/>
">
<@richtable entityName="product" actionColumnWidth="300px" actionColumnButtons=actionColumnButtons config=config searchable=true/>
</body>
</html></#escape>