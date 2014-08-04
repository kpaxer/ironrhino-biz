<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if station.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('station')}</title>
</head>
<body>
<@s.form action="${actionBaseUrl}/save" method="post" class="ajax form-horizontal sequential_create">
	<#if !station.new>
		<@s.hidden name="station.id" />
	</#if>
	<@s.textfield label="%{getText('name')}" name="station.name" class="required checkavailable"/>
	<div class="control-group treeselect" data-options="{'url':'<@url value="/region/children"/>'}">
	<@s.hidden class="treeselect-id" name="regionId" />
	<label class="control-label" for="region">地区</label>
	<div class="controls">
	<span class="treeselect-name"><#if station.region??>${station.region.fullname}</#if></span>
	</div>
	</div>
	<@s.textfield label="%{getText('address')}" name="station.address"/>
	<@s.textfield label="%{getText('destination')}" name="station.destination"/>
	<@s.textfield label="%{getText('linkman')}" name="station.linkman" />
	<@s.textfield label="%{getText('phone')}" name="station.phone" />
	<@s.textfield label="%{getText('mobile')}" name="station.mobile" />
	<@s.textfield label="%{getText('fax')}" name="station.fax" />
	<div class="control-group clearfix">
		<label class="control-label" for="cashCondition">${action.getText('cashCondition')}</label>
		<div id="cashCondition" class="controls">
		<table class="datagrid table table-condensed middle nullable">
			<tbody>
			<#assign size = 0>
			<#if station.cashCondition?? && station.cashCondition?size gt 0>
				<#assign size = station.cashCondition?size-1>
			</#if>
			<#list 0..size as index>
			<tr>
				<td style="width:80%;">
					<@s.select theme="simple" name="station.cashCondition[${index}]" cssStyle="width:100px;" list="cashConditionMap" listKey="key" listValue="value" headerKey="" headerValue=""/>
				</td>
				<td class="manipulate"></td>
			</tr>
			</#list>
			</tbody>
		</table>
		</div>
	</div>
	<@s.textarea label="%{getText('memo')}" name="station.memo" class="input-xxlarge" cssStyle="height:40px;"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>