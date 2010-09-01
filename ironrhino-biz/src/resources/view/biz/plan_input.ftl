<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><#if plan.new>${action.getText('create')}<#else>${action.getText('edit')}</#if>${action.getText('plan')}</title>
</head>
<body>
<@s.form action="${getUrl('/biz/plan/save')}" method="post" cssClass="ajax">
	<#if !plan.new>
		<@s.hidden name="plan.id" />
	</#if>
	<#if !plan.completed>
		<div>
		<label class="field" for="filterproduct">${action.getText('product')}</label>
		<input id="filterproduct" type="text" size="5" class="filterselect" style="margin-right:3px;"/><@s.select theme="simple" name="product.id" cssClass="required" cssStyle="width:230px;" list="productList" listKey="id" listValue="fullname" headerKey="" headerValue="请选择"/>
		</div>
		<@s.textfield label="%{getText('quantity')}" name="plan.quantity" cssClass="integer positive required"/>
		<@s.textfield label="%{getText('planDate')}" name="plan.planDate" cssClass="date required"/>
	</#if>
	<@s.textarea label="%{getText('memo')}" name="plan.memo" cols="50" rows="10"/>
	<@s.submit value="%{getText('save')}" />
</@s.form>
</body>
</html></#escape>