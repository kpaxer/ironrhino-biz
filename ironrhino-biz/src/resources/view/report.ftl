<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>报表</title>
<script>
function toggleFormat(btn,format){
		if(format=='xls'){
			$(btn).html('<span><span>当前是EXCEL,点击切换到PDF</span></span>');
		}else if(format=='pdf'){
			$(btn).html('<span><span>当前是PDF,点击切换到EXCEL</span></span>');
		}
		$('a.report').attr('href',function(i,href){
			var i = href.indexOf('format=');
			if(i>0)
				href = href.substring(0,i-1);
			return href+='&format='+format;
		});
		$('form.report').each(function(){
			var hidden = $('input[name="format"]',this);
			if(hidden.length){
				hidden.val(format);
			}else{
				$(this).prepend('<input type="hidden" name="format" value="'+format+'"/>');
			}
		});
}
$(function(){
	$('.report').attr('target','_blank');
	$('#format').toggle(function(){
		toggleFormat(this,'xls');
	},function(){
		toggleFormat(this,'pdf');
	});
});
</script>
</head>
<body>
<div style="text-align:center;margin-bottom:10px;">
<@button id="format" text="当前是PDF,点击切换到EXCEL"/>
</div>

<div class="portal">
	<div class="portal-column">
		<div class="portlet">
			<div class="portlet-header">订单报表</div>
			<div class="portlet-content">
				<h3>订单详情</h3>
				<span>按天</span>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><@s.textfield id="date1" name="date" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按客户和区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><input type="text" name="customer" class="required customerName" style="width:120px;"/></div>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按业务员和区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><@s.select theme="simple" cssClass="required" cssStyle="width:120px;" name="salesman" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按销售方式和区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="order"/>
					<div class="field"><@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:120px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="请选择"/></div>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">产品销量统计</h3>
				<div>按区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="productsales"/>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按客户和区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="productsales"/>
					<div class="field"><input type="text" name="customer" class="required customerName" style="width:120px;"/></div>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按业务员和区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="productsales"/>
					<div class="field"><@s.select theme="simple" cssClass="required" cssStyle="width:120px;" name="salesman" list="salesmanList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按销售方式和区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="productsales"/>
					<div class="field"><@s.select theme="simple" name="saletype" cssClass="required" cssStyle="width:120px;" list="@com.ironrhino.biz.model.SaleType@values()" listKey="name" listValue="displayName" headerKey="" headerValue="请选择"/></div>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">按天统计销量报表</h3>
				<div>区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="dailysales"/>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
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
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="privatereward"/>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><span>包括支出</span><input type="checkbox" name="includePaid" value="true"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">个人工资详单</h3>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="privatereward"/>
					<div class="field"><@s.select theme="simple" cssClass="required" cssStyle="width:80px;" name="id" list="employeeList" listKey="id" listValue="name" headerKey="" headerValue="请选择"/></div>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><span>包括支出</span><input type="checkbox" name="includePaid" value="true"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">工资清单</h3>
				<div>按天</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="reward"/>
					<div class="field"><@s.textfield id="date2" name="date" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按区间</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="reward"/>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<h3 style="clear:left;">所有员工工资统计</h3>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="aggregationreward"/>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><span>包括支出</span><input type="checkbox" name="includePaid" value="true"/></div>
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
				<@button text="库存单" type="link" href="${getUrl('/report/jasper?type=product')}" class="report"/>
				<@button text="欠货单" type="link" href="${getUrl('/report/jasper?type=product&negative=true')}" class="report"/>
				<@button text="原料库存单" type="link" href="${getUrl('/report/jasper?type=stuff')}" class="report"/>
				<h3>出入库统计</h3>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="stuffflow"/>
					<div class="field"><input type="text" name="from" class="date required"/></div>
					<div class="field"><input type="text" name="to" class="date required"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
		<div class="portlet">
			<div class="portlet-header">客户报表</div>
			<div class="portlet-content">
				<h3>客户资料</h3>
				<div>按天</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="customer"/>
					<div class="field"><@s.textfield id="date3" name="date" cssClass="date required" theme="simple"/></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
				<div style="clear:left;">按地区</div>
				<form action="${getUrl('/report/jasper')}" method="post" class="report line clearfix">
					<input type="hidden" name="type" value="customer"/>
					<input id="regionId" type="hidden" name="id"/>
					<div class="field" style="margin-right:5px;"><span id="region" class="selectregion" regionname="region" full="true" regionid="regionId">请点击选择地区</span></div>
					<div class="field"><@s.submit theme="simple" value="%{getText('confirm')}"/></div>
				</form>
			</div>
		</div>
	</div>
</div>

</body>
</html></#escape>
