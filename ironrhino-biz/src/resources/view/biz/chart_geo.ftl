<#macro renderTR region>
<tr id="node-${region.id}"<#if region.parent??&&region.parent.id gt 0> class="child-of-node-${region.parent.id}"</#if>>
        <td>${region.name}</td>
        <td><#if !region.leaf><a href="chart/view?type=region&location=${region.id}" target="_blank">查看该地区销量</a></#if></td>
        <td><a href="customer?regionId=${region.id}" target="_blank">查看该地区客户</a></td>
</tr>
<#if region.leaf>
	<#return>
<#else>
<#list region.children as var>
	<@renderTR var/>
</#list>
</#if>
</#macro>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>选择地区</title>
</head>
<body>
<table class="treeTable" width="100%">
  <thead>
    <tr>
      <th width="30%"></th>
      <th width="35%"></th>
       <th width="35%"></th>
    </tr>
  </thead>
  <tbody>
    <#list regionTree.children as var>
      <@renderTR var/>
    </#list>
  </tbody>
</table>
</body>
</html></#escape>