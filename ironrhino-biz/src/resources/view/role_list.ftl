<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('list')}${action.getText('role')}</title>
</head>
<body>
<#assign config={"name":{"cellEdit":"input"}}>
<@richtable entityName="role" config=config/></div>
</body>
</html>
