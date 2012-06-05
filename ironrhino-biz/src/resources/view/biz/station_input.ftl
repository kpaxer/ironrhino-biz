<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title><#if station.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('station')}</title>
</head>
<body>
<@s.form action="${getUrl(actionBaseUrl+'/save')}" method="post" cssClass="ajax form-horizontal">
	<#if !station.new>
		<@s.hidden name="station.id" />
	</#if>
	<@s.textfield label="%{getText('name')}" name="station.name" cssClass="required"/>
	<div class="control-group treeselect" data-options="{'url':'<@url value="/region/children"/>','name':'region','id':'regionId'}">
	<@s.hidden id="regionId" name="regionId" />
	<label class="control-label" for="region"><span style="cursor:pointer;">请选择地区</span></label>
	<div class="controls">
	<#if station.region??><span id="region">${station.region.fullname}<a class="remove" href="#">&times;</a></span><#else><span id="region">...</span></#if>
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
				<td width="80%">
					<@s.select theme="simple" name="station.cashCondition[${index}]" cssStyle="width:100px;" list="cashConditionMap" listKey="key" listValue="value" headerKey="" headerValue="请选择"/>
				</td>
				<td class="manipulate"></td>
			</tr>
			</#list>
			</tbody>
		</table>
		</div>
	</div>
	<@s.textarea label="%{getText('memo')}" name="station.memo" cssStyle="width:400px;height:40px;"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>