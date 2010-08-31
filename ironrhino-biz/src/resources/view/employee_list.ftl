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
<#assign columns={"id":{},"name":{"cellEdit":"click"},"type":{"cellEdit":"click,rt_select_template_type"},"phone":{"cellEdit":"click"},"dimission":{"cellEdit":"click,rt_select_template_boolean"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'edit\')}' view='input'/>
<@button text='${action.getText(\'reward\')}' type='link' href='${getUrl(\'/reward?employee.id=\'+entity.id)}'/>
<@button text='支工资' type='link' href='${getUrl(\'/reward/input?negative=true&employee.id=\'+entity.id)}' rel='richtable' windowoptions='{\'reloadonclose\':false}'/>
<@button text='发工资' type='link' href='${getUrl(\'/reward/input?employee.id=\'+entity.id)}' rel='richtable' windowoptions='{\'reloadonclose\':false}'/>
<#if entity.type??&&entity.type.name()=='SALESMAN'>
<@button text='${action.getText(\'order\')}' type='link' href='${getUrl(\'/order?employee.id=\'+entity.id)}'/>
</#if>
">
<@richtable entityName="employee" columns=columns actionColumnWidth="230px" actionColumnButtons=actionColumnButtons searchable=true/>
<div style="display: none;">
<textarea id="rt_select_template_type">
<@s.select theme="simple" cssStyle="width: 100%;" onblur="Richtable.updateCell(this)" list="@com.ironrhino.biz.model.EmployeeType@values()" listKey="name" listValue="displayName" headerKey="" headerValue=""/>
</textarea>
</div>
</body>
</html></#escape>
