<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('product')}s</title>
</head>
<body>
<#assign config={"name":{"cellEdit":"click"},"category":{"class":"include_if_edited"},"brand":{"class":"include_if_edited"},"spec":{"class":"include_if_edited"},"displayOrder":{"cellEdit":"click"}}>
<@richtable entityName="product" config=config/>
</body>
</html></#escape>