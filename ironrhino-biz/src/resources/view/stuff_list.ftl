<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>原料管理</title>
</head>
<body>
<#assign config={"name":{"cellEdit":"click"},"weight":{"cellEdit":"click"},"stock":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'save\')}' action='save'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'记录\')}' type='link' href='${getUrl(\'/stuffflow?sutff.id=\'+entity.id)}'/>
<@button text='入库' type='link' href='${getUrl(\'/stuffflow/input?stuff.id=\'+entity.id)}' rel='richtable'/>
<@button text='出库' type='link' href='${getUrl(\'/stuffflow/input?out=true&stuff.id=\'+entity.id)}' rel='richtable'/>
">
<@richtable entityName="stuff" config=config actionColumnWidth="270px" actionColumnButtons=actionColumnButtons/>
</body>
</html></#escape>