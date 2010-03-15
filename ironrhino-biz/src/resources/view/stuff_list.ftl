<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('stuff')}${action.getText('list')}</title>
</head>
<body>
<#assign config={"name":{"cellEdit":"click"},"weight":{"cellEdit":"click"},"stock":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'price\')+action.getText(\'trend\')}' type='link' href='${getUrl(\'/chart/view?type=stuff&id=\'+entity.id)}' rel='richtable' windowoptions='{\'width\':\'1200px\'}'/>
<@button text='${action.getText(\'stuffflow\')+action.getText(\'record\')}' type='link' href='${getUrl(\'/stuffflow?stuff.id=\'+entity.id)}'/>
<@button text='入库' type='link' href='${getUrl(\'/stuffflow/input?stuff.id=\'+entity.id)}' rel='richtable'/>
<@button text='出库' type='link' href='${getUrl(\'/stuffflow/input?out=true&stuff.id=\'+entity.id)}' rel='richtable'/>
">
<#assign bottomButtons=r"
<@button text='${action.getText(\'create\')}' view='input'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'reload\')}' action='reload'/>
<@button text='${action.getText(\'compare\')+action.getText(\'price\')+action.getText(\'trend\')}' type='link' href='${getUrl(\'/chart/view?type=stuff\')}' rel='richtable' windowoptions='{\'width\':\'1200px\'}'/>
">
<@richtable entityName="stuff" config=config actionColumnWidth="350px" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons/>
</body>
</html></#escape>