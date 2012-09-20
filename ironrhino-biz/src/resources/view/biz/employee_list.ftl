<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('employee')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"id":{"width":"70px"},"name":{"width":"120px","cellEdit":"click"},"type":{"width":"80px","cellEdit":"click,select,rt_select_template_type"},"phone":{"width":"200px","cellEdit":"click"},"address":{"cellEdit":"click"},"dimission":{"width":"80px","cellEdit":"click,boolean"}}>
<#assign actionColumnButtons=r'
<button type="button" class="btn" data-view="input">${action.getText("edit")}</button>
<a class="btn ajax view" href="${getUrl("/biz/reward/tabs?employee.id="+entity.id)}">${action.getText("reward")}</a>
<a class="btn" href="${getUrl("/biz/reward/input?negative=true&employee.id="+entity.id)}" rel="richtable" '
+'data-windowoptions="{\'reloadonclose\':false}">支工资</a> '
+r'<a class="btn" href="${getUrl("/biz/reward/input?employee.id="+entity.id)}" rel="richtable" '
+'data-windowoptions="{\'reloadonclose\':false}">发工资</a> '
+r'<#if entity.type??&&entity.type.name()=="SALESMAN"><a class="btn ajax view" href="${getUrl("/biz/order?salesman.id="+entity.id)}">${action.getText("salesman")}${action.getText("order")}</a>
</#if>
<#if entity.type??&&entity.type.name()=="DELIVERYMAN"><a class="btn ajax view" href="${getUrl("/biz/order?deliveryman.id="+entity.id)}">${action.getText("deliveryman")}${action.getText("order")}</a>
</#if>
'>
<@richtable entityName="employee" columns=columns actionColumnButtons=actionColumnButtons searchable=true/>
<div style="display: none;">
<textarea id="rt_select_template_type">
<@s.select theme="simple" list="@com.ironrhino.biz.model.EmployeeType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
</textarea>
</div>
</body>
</html></#escape>
