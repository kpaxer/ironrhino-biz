(function(){Observation.app=function(){$("#shipped").click(function(){var a=$(this).nextAll("span:eq(0)");$(this).attr("checked")?a.show():a.hide()});$(".customerName").autocomplete(CONTEXT_PATH+"/customer/suggest?decorator=none",{max:1E3,minChars:2,delay:500});$("select.fetchprice").change(function(a){var b=$(a.target),c=$("input.price:eq(0)",b.closest("tr"));(a=b.val())&&!c.val()&&$.ajax({url:CONTEXT_PATH+"/product/json/"+a,dataType:"json",success:function(d){if(d.price){c.val(d.price);f()}d.stock<=
0?b.siblings("span.info").html("\u6ca1\u6709\u5e93\u5b58"):b.siblings("span.info").html("")}})});$("#orderItems input.quantity").blur(function(a){f();var b=$(a.target).val();if(b){var c=$("select.fetchprice",$(a.target).closest("tr"));(a=c.val())&&!c.siblings("span.info").text()&&$.ajax({url:CONTEXT_PATH+"/product/json/"+a,dataType:"json",success:function(d){$("input.price:eq(0)",c.closest("tr")).val(d.price||"");d.stock<b&&c.siblings("span.info").html("\u5e93\u5b58\u4e0d\u591f")}})}});$("#orderItems input.price,#discount,#freight").blur(function(){f()});
$("#orderItems table").datagridTable({onremove:f})};var f=function(a){if(a){var b=$("input.integer",a).val(),c=$("input.double",a).val(),d=0;if(b&&c){d=b*c;$("td:eq(3) span.info",a).text(d)}return d}else{var e=0;$("#orderItems tbody tr").each(function(){e+=f(this)});e>0&&$("#amount").text(e);if(a=$("#discount").val())e-=a;if(a=$("#freight").val())e-=a;$("#grandTotal").html(e>=0?e:'<span style="color:red;">\u8d1f\u6570</span>')}}})();
