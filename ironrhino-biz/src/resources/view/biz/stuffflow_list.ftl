<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('stuffflow')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"stuff":{},"quantity":{},"amount":{},"date":{"template":r"${(entity.date?string('yyyy-MM-dd'))!}"}}>
<@richtable entityName="stuffflow" columns=columns readonly=true searchable=true/>
</body>
</html></#escape>
