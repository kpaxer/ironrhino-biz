<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>List Products</title>
</head>
<body>
<#assign config={"name":{},"stock":{},"criticalStock":{},"spec":{},"category":{}}>
<#assign actionColumnButtons=btn("Richtable.input('#id')",action.getText('edit'))+btn("Richtable.open(Richtable.getUrl('category','#id'),true,true)","目录")+btn("Richtable.del('#id')",action.getText('delete'))>
<@richtable entityName="product" config=config celleditable=false actionColumnButtons=actionColumnButtons/>
</body>
</html></#escape>