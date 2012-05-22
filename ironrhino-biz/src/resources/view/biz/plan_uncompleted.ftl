<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>未完成的计划</title>
</head>
<body>
<#if uncompletedPlans.size() gt 0>
<table class="table table-striped">
	<thead>
		<tr>
			<td>${action.getText('product')}</td>
			<td>${action.getText('quantity')}</td>
			<td>${action.getText('planDate')}</td>
			<td width="12%"></td>
		</tr>
	</thead>
	<tbody>
	<#list uncompletedPlans as var>
		<tr>
			<td>${var.product?string}</td>
			<td>${var.quantity}</td>
			<td>${var.planDate?string("yyyy年MM月dd日")}</td>
			<td><a class="btn ajax" href="${getUrl('/biz/plan/complete/'+var.id)}" onsuccess="$(this).closest('tr').remove()">${action.getText('complete')}</a></td>
		</tr>
	</#list>
	</tbody>
</table>
</#if>
</body>
</html></#escape>
