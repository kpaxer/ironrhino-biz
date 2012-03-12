<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if station.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('station')}</title>
</head>
<body>
<@s.form action="${getUrl(actionBaseUrl+'/save')}" method="post" cssClass="ajax">
	<#if !station.new>
		<@s.hidden name="station.id" />
	</#if>
	<@s.textfield label="%{getText('name')}" name="station.name" cssClass="required"/>
	<div class="field treeselect" treeoptions="{'url':'<@url value="/region/children"/>','name':'region','id':'regionId'}">
	<@s.hidden id="regionId" name="regionId" />
	<label class="field" for="region"><span style="cursor:pointer;">请选择地区</span></label>
	<#if station.region??><span id="region">${station.region.fullname}</span><#else><span id="region">...</span></#if>
	</div>
	<@s.textfield label="%{getText('address')}" name="station.address"/>
	<@s.textfield label="%{getText('destination')}" name="station.destination"/>
	<@s.textfield label="%{getText('linkman')}" name="station.linkman" />
	<@s.textfield label="%{getText('phone')}" name="station.phone" />
	<@s.textfield label="%{getText('mobile')}" name="station.mobile" />
	<@s.textfield label="%{getText('fax')}" name="station.fax" />
	<div class="field clearfix">
		<label class="field" for="cashCondition">${action.getText('cashCondition')}</label>
		<div id="cashCondition" style="float:right;">
		<table border="0" width="600px;" class="datagrid nullable">
			<tbody>
			<#if !station.new&&station.cashCondition?size gt 0>
				<#list 0..station.cashCondition?size-1 as index>
				<tr>
					<td width="80%">
						<@s.select theme="simple" name="station.cashCondition[${index}]" cssStyle="width:100px;" list="cashConditionMap" listKey="key" listValue="value" headerKey="" headerValue="请选择"/>
					</td>
					<td><@button text="+" class="add"/><@button text="-" class="remove"/></td>
				</tr>
				</#list>
			<#else>
				<tr>
					<td width="80%">
						<@s.select theme="simple" name="station.cashCondition[0]" cssStyle="width:100px;" list="cashConditionMap" listKey="key" listValue="value" headerKey="" headerValue="请选择"/>
					</td>
					<td><@button text="+" class="add"/><@button text="-" class="remove"/></td>
				</tr>
			</#if>
			</tbody>
		</table>
		</div>
	</div>
	<@s.textarea label="%{getText('memo')}" name="station.memo" cols="50" rows="10"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>