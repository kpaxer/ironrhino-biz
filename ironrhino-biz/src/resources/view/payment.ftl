<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>test</title>
<script src="<@url value="/assets/scripts/payment.js"/>" type="text/javascript"></script>
<script>

$(function(){
	$('#payment').payment(function(name){
		return {
		orderCode:1234,
		service:'create_partner_trade_by_buyer'
		};
	});
});
</script>
</head>
<body>
<div id="payment"></div>
</body>
</html></#escape>