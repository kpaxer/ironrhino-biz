<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>${action.getText('report')}</title>
</head>
<body>
<div id="report_format" class="switch" style="text-align:center;margin-bottom:10px;">
	<@button class="selected" text="PDF" format="PDF" />
	<@button text="EXCEL" format="XLS"/>
</div>

<div class="portal">
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">订单报表</div>
			<div class="portlet-content">
				<h3>订单详情</h3>
				<span>按天</span>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><@s.textfield id="" name="date" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按客户和区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<input id="customerId" type="hidden" name="customer"/>
					<div class="field"><span id="customer" class="listpick" pickoptions="{'url':'<@url value="/biz/customer/pick?columns=name,fullAddress"/>','name':'customer','id':'customerId'}">请点击选择客户</span></div>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按业务员和区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><@s.select theme="simple" cssClass="required" cssStyle="width:120px;" name="salesman" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按配送员和区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><@s.select theme="simple" cssClass="required" cssStyle="width:120px;" name="deliveryman" list="deliverymanList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按销售方式和区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:120px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">产品销量统计</h3>
				<div>按区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="productsales"/>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按客户和区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="productsales"/>
					<input id="customerId2" type="hidden" name="customer"/>
					<div class="field"><span id="customer2" class="listpick" pickoptions="{'url':'<@url value="/biz/customer/pick?columns=name,fullAddress"/>','name':'customer2','id':'customerId2'}">请点击选择客户</span></div>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按业务员和区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="productsales"/>
					<div class="field"><@s.select theme="simple" cssClass="required" cssStyle="width:120px;" name="salesman" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按销售方式和区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="productsales"/>
					<div class="field"><@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:120px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="请选择"/></div>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">按天统计销量报表</h3>
				<div>区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="dailysales"/>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
	</div>
	 
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">工资报表</div>
			<div class="portlet-content">
				<h3>所有人工资详单</h3>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="privatereward"/>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><select name="negative" style="width:80px;"><option value="">全部</option><option value="false">收入</option><option value="true">支出</option></select></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">个人工资详单</h3>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="privatereward"/>
					<input id="employeeId" type="hidden" name="id"/>
					<div class="field"><span id="employee" class="listpick" pickoptions="{'url':'<@url value="/biz/employee/pick?columns=name"/>','name':'employee','id':'employeeId'}">请点击选择员工</span></div>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple" cssStyle="width:80px;"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple" cssStyle="width:80px;"/></div>
					<div class="field"><select name="negative" style="width:60px;"><option value="">全部</option><option value="false">收入</option><option value="true">支出</option></select></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">工资清单</h3>
				<div>按天</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="reward"/>
					<div class="field"><@s.textfield id="" name="date" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按区间</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="reward"/>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">所有员工工资统计</h3>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="aggregationreward"/>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><select name="negative" style="width:60px;"><option value="">全部</option><option value="false">收入</option><option value="true">支出</option></select></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
	</div>
	 
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">产品和原料库存报表</div>
			<div class="portlet-content">
				<h3>库存清单</h3>
				<@button text="库存单" type="link" href="report/jasper?type=product" class="report"/>
				<@button text="欠货单" type="link" href="report/jasper?type=product&negative=true" class="report"/>
				<@button text="原料库存单" type="link" href="report/jasper?type=stuff" class="report"/>
				<h3>出入库统计</h3>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="stuffflow"/>
					<div class="field"><@s.textfield id="" name="from" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.textfield id="" name="to" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
		<div class="portlet">
			<div class="portlet-header">客户报表</div>
			<div class="portlet-content">
				<h3>客户资料</h3>
				<div>按天</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="customer"/>
					<div class="field"><@s.textfield id="" name="date" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按地区</div>
				<form action="report/jasper" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="customer"/>
					<input id="regionId" type="hidden" name="id"/>
					<div class="field" style="margin-right:5px;"><span id="region" class="treeselect" treeoptions="{'url':'<@url value="/region/children"/>','name':'region','id':'regionId'}">请点击选择地区</span></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
	</div>
</div>

</body>
</html></#escape>
