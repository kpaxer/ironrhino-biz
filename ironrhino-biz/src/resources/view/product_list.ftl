<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('product')}</title>
</head>
<body>
<#assign config={"brand":{},"category":{},"name":{"cellEdit":"click"},"spec":{},"stock":{"cellEdit":"click"},"displayOrder":{"cellEdit":"click"}}>
<@richtable entityName="product" config=config/>
</body>
</html></#escape>