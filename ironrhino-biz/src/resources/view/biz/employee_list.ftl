<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('employee')}${action.getText('list')}</title>
<style>
a.region:link,a.region:hover,a.region:visited,a.region:active {
color:#000;
text-decoration:none;
}
</style>
</head>
<body>
<#assign columns={"id":{"width":"70px"},"name":{"width":"120px","cellEdit":"click"},"type":{"width":"80px","cellEdit":"click,select,rt_select_template_type"},"phone":{"width":"200px","cellEdit":"click"},"address":{"cellEdit":"click"},"dimission":{"width":"80px","cellEdit":"click,boolean"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'reward\')}' type='link' href=getUrl('/biz/reward/tabs?employee.id='+entity.id) class='ajax view'/>
<@button text='支工资' type='link' href=getUrl('/biz/reward/input?negative=true&employee.id='+entity.id) rel='richtable' windowoptions='{\'reloadonclose\':false}'/>
<@button text='发工资' type='link' href=getUrl('/biz/reward/input?employee.id='+entity.id) rel='richtable' windowoptions='{\'reloadonclose\':false}'/>
<#if entity.type??&&entity.type.name()=='SALESMAN'>
<@button text='${action.getText(\'salesman\')}${action.getText(\'order\')}' type='link' href=getUrl('/biz/order?salesman.id='+entity.id) class='ajax view'/>
</#if>
<#if entity.type??&&entity.type.name()=='DELIVERYMAN'>
<@button text='${action.getText(\'deliveryman\')}${action.getText(\'order\')}' type='link' href=getUrl('/biz/order?deliveryman.id='+entity.id) class='ajax view'/>
</#if>
">
<@richtable entityName="employee" columns=columns actionColumnWidth="270px" actionColumnButtons=actionColumnButtons searchable=true/>
<div style="display: none;">
<textarea id="rt_select_template_type">
<@s.select theme="simple" cssStyle="width: 100%;" onblur="Richtable.updateCell(this)" list="@com.ironrhino.biz.model.EmployeeType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
</textarea>
</div>
</body>
</html></#escape>
