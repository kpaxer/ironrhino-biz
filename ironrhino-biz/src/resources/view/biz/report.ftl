<!DOCTYPE html>
<#escape x as x?html><html>
<head>
<title>${action.getText('report')}</title>
</head>
<body>
<div class="row">
	<div id="report_format" class="btn-group switch span2 offset5" style="margin-bottom:10px;">
	  <button class="btn active" data-format="PDF">PDF</button>
	  <button class="btn" data-format="XLS">XLS</button>
	</div>
</div>

<div class="portal">
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">订单报表</div>
			<div class="portlet-content">
				<h3>订单详情</h3>
				<div style="clear:left;">按天</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.textfield id="" name="date" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按客户和区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<input id="customerId" type="hidden" name="customer"/>
					<span id="customer" class="listpick" data-options="{'url':'<@url value="/biz/customer/pick?columns=name,fullAddress"/>','name':'customer','id':'customerId'}">请点击选择客户</span>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按业务员和区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.select theme="simple" cssClass="required" cssStyle="width:80px;" name="salesman" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按配送员和区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.select theme="simple" cssClass="required" cssStyle="width:80px;" name="deliveryman" list="deliverymanList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按销售方式和区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="order"/>
					<@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:80px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="请选择"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">产品销量统计</h3>
				<div>按区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="productsales"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按客户和区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="productsales"/>
					<input id="customerId2" type="hidden" name="customer"/>
					<span id="customer2" class="listpick" data-options="{'url':'<@url value="/biz/customer/pick?columns=name,fullAddress"/>','name':'customer2','id':'customerId2'}">请点击选择客户</span>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按业务员和区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="productsales"/>
					<@s.select theme="simple" cssClass="required" cssStyle="width:80px;" name="salesman" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按销售方式和区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="productsales"/>
					<@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:80px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="请选择"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">按天统计销量报表</h3>
				<div>区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="dailysales"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
	</div>
	 
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">工资报表</div>
			<div class="portlet-content">
				<h3>所有人工资详单</h3>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="privatereward"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<select name="negative" style="width:80px;"><option value="">全部</option><option value="false">收入</option><option value="true">支出</option></select>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">个人工资详单</h3>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="privatereward"/>
					<input id="employeeId" type="hidden" name="id"/>
					<span id="employee" class="listpick" data-options="{'url':'<@url value="/biz/employee/pick?columns=name"/>','name':'employee','id':'employeeId'}">选择员工</span>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple" cssStyle="width:80px;"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple" cssStyle="width:80px;"/>
					<select name="negative" style="width:60px;"><option value="">全部</option><option value="false">收入</option><option value="true">支出</option></select>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">工资清单</h3>
				<div>按天</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="reward"/>
					<@s.textfield id="" name="date" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按区间</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="reward"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<h3 style="clear:left;">所有员工工资统计</h3>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="aggregationreward"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<select name="negative" style="width:60px;"><option value="">全部</option><option value="false">收入</option><option value="true">支出</option></select>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
	</div>
	 
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">产品和原料库存报表</div>
			<div class="portlet-content">
				<h3>库存清单</h3>
				<form>
					<a class="btn report" href="report/jasper?type=product">库存单</a>
					<a class="btn report" href="report/jasper?type=product&negative=true">欠货单</a>
					<a class="btn report" href="report/jasper?type=stuff">原料库存单</a>
				</form>
				<h3>出入库统计</h3>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="stuffflow"/>
					<@s.textfield id="" name="from" cssClass="date required" theme="simple"/>	
					<@s.textfield id="" name="to" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
		<div class="portlet">
			<div class="portlet-header">客户报表</div>
			<div class="portlet-content">
				<h3>客户资料</h3>
				<div>按天</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="customer"/>
					<@s.textfield id="" name="date" cssClass="date required" theme="simple"/>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
				<div style="clear:left;">按地区</div>
				<form action="report/jasper" method="post" class="report form-inline">
					<input type="hidden" name="type" value="customer"/>
					<input id="regionId" type="hidden" name="id"/>
					<span id="region" class="treeselect" data-options="{'url':'<@url value="/region/children"/>','name':'region','id':'regionId'}">请点击选择地区</span>
					<@s.submit theme="simple" value="%{getText('confirm')}"/>
				</form>
			</div>
		</div>
	</div>
</div>

</body>
</html></#escape>
