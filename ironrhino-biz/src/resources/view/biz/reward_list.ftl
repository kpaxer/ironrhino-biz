<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('reward')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"employee":{},"amount":{},"type":{},"rewardDate":{"template":r"${(entity.rewardDate?string('yyyy年MM月dd日'))!}"}}>
<@richtable entityName="reward" columns=columns celleditable=false searchable=true/>
</body>
</html></#escape>