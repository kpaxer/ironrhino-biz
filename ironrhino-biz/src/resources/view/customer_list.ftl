<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>List Customers</title>
</head>
<body>
<#assign config={"code":{},"name":{"cellEdit":"input"},"address":{"cellEdit":"input"},"postCode":{"cellEdit":"input"},"phone":{"cellEdit":"input"},"mobile":{"cellEdit":"input"},"description":{"cellEdit":"input"}}>
<#assign actionColumnButtons=btn(action.getText('save'),"Richtable.save('#id')")+btn(action.getText('edit'),"Richtable.input('#id')")+btn(action.getText('region'),"Richtable.open(Richtable.getUrl('region','#id'),true)")+btn(action.getText('delete'),"Richtable.del('#id')")>
<@richtable entityName="customer" config=config actionColumnWidth="180px" actionColumnButtons=actionColumnButtons/>
</body>
</html></#escape>
