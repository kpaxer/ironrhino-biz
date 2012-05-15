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
	<div class="control-group treeselect" data-treeoptions="{'url':'<@url value="/region/children"/>','name':'region','id':'regionId'}">
	<@s.hidden id="regionId" name="regionId" />
	<label class="control-label" for="region"><span style="cursor:pointer;">请选择地区</span></label>
	<div class="controls">
	<#if station.region??><span id="region">${station.region.fullname}</span><#else><span id="region">...</span></#if>
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
		<table border="0" class="datagrid highlightrow nullable">
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
				<td class="manipulate"><button type="button" class="btn add">+</button><button type="button" class="btn remove">-</button></td>
			</tr>
			</#list>
			</tbody>
		</table>
		</div>
	</div>
	<@s.textarea label="%{getText('memo')}" name="station.memo" cols="50" rows="10"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>