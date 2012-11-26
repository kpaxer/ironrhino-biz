<#macro renderTR region>
<tr id="node-${region.id}"<#if region.parent??&&region.parent.id gt 0> class="child-of-node-${region.parent.id}"</#if>>
        <td>${region.name}</td>
        <td><#if !region.leaf><a href="<@url value="/biz/chart/view?type=region&location=${region.id}"/>" target="_blank">查看该地区销量</a></#if></td>
        <td><a href="<@url value="/biz/customer?regionId=${region.id}"/>" target="_blank">查看该地区客户</a></td>
</tr>
<#if region.leaf>
	<#return>
<#else>
<#list region.children as var>
	<@renderTR var/>
</#list>
</#if>
</#macro>
<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>选择地区</title>
</head>
<body>
<table class="treeTable table table-condensed">
  <thead>
    <tr>
      <th style="width:30%;">地区</th>
      <th style="width:35%;">销量</th>
       <th style="width:35%;">客户</th>
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
