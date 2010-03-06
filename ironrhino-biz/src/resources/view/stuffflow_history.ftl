<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>出入库审核历史记录</title>
</head>
<body>
<#assign config={"stuff":{},"quantity":{},"amount":{},"status":{"renderLink":true},"requestUser":{"renderLink":true},"requestDate":{},"auditUser":{"renderLink":true},"auditDate":{}}>
<@richtable entityName="stuffflow" action="history" config=config readonly=true/>
</body>
</html></#escape>