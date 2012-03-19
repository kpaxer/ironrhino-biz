<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('returning')}${action.getText('list')}</title>
</head>
<body>
<#assign columns={"customer":{"template":r"<span class='tiped' tipurl='${getUrl('/biz/customer/view/'+value.id+'?type=tip')}'>${value?string}</span>"},"grandTotal":{"width":"80px"},"returnDate":{"template":r"${(entity.returnDate?string('yyyy年MM月dd日'))!}","width":"120px"}}>
<#assign actionColumnButtons=r"
<@button text='${action.getText(\'view\')}' view='view'/>
<@button text='${action.getText(\'edit\')}' view='input' windowoptions='{\'width\':\'900px\'}'/>
">
<#assign bottomButtons=r"
<@button text='${action.getText(\'create\')}' view='input' windowoptions='{\'width\':\'900px\'}'/>
<@button text='${action.getText(\'delete\')}' action='delete'/>
<@button text='${action.getText(\'reload\')}' action='reload'/>
">
<@richtable entityName="returning" columns=columns actionColumnWidth="100px" actionColumnButtons=actionColumnButtons bottomButtons=bottomButtons celleditable=false deleteable=false searchable=true/>
</body>
</html></#escape>