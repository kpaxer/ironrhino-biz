(function(){Observation.app=function(){$("form.report,a.report").attr("target","_blank");$("#report_format").children().click(function(){var a=$(this).data("format");$("a.report").attr("href",function(c,e){c=e.indexOf("format=");0<c&&(e=e.substring(0,c-1));return e+("&format="+a)});$("form.report").each(function(){var c=$('input[name="format"]',this);c.length?c.val(a):$(this).prepend('<input type="hidden" name="format" value="'+a+'"/>')})});$("#shipped,#paid").change(function(){var a=$("span.toggle",
$(this).closest(".controls"));$(this).is(":checked")?a.show():a.hide()});$("#customerName").blur(function(a){var c=$(a.target);(a=c.val())&&$.ajax({url:CONTEXT_PATH+"/biz/customer/json/"+a,dataType:"json",success:function(a){var d=$('select[name="salesman.id"]',c.closest("form")),b=$('select[name="stationId"]',c.closest("form"));a&&a.id?(c.val(a.name).siblings("span.info").html(""),a.memo&&(a=$.parseJSON(a.memo),0>document.location.search.indexOf("salesman.id")&&(a.salesman?d.val(a.salesman):$("option:selected",
d).removeAttr("selected")),a.station?b.val(a.station):$("option:selected",b).removeAttr("selected"))):(0>document.location.search.indexOf("salesman.id")&&$("option:selected",d).removeAttr("selected"),$("option:selected",b).removeAttr("selected"),c.siblings("span.info").html("\u81ea\u52a8\u4fdd\u5b58\u65b0\u5ba2\u6237"))}})});var b={};$(".customerName").autocomplete({minLength:2,source:function(a,c){a.term in b?c(b[a.term]):$.ajax({url:CONTEXT_PATH+"/biz/customer/suggest",dataType:"json",data:a,success:function(e){b[a.term]=
e;c(e)}})},select:function(a){$(a.target).siblings("span.info").html("")}});$("select.fetchprice").change(function(a){var c=$(a.target),b=$("input.price:eq(0)",c.closest("tr"));(a=c.val())&&!b.val()&&$.ajax({url:CONTEXT_PATH+"/biz/product/json/"+a,dataType:"json",success:function(a){a.price&&(b.val(a.price),f());0>=a.stock?c.siblings("span.info").html("\u6ca1\u6709\u5e93\u5b58"):c.siblings("span.info").html("")}})});$("#orderItems input.quantity").blur(function(a){f();var c=$(a.target).val();if(c){var b=
$("select.fetchprice",$(a.target).closest("tr"));(a=b.val())&&$.ajax({url:CONTEXT_PATH+"/biz/product/json/"+a,dataType:"json",success:function(a){$("input.price:eq(0)",b.closest("tr")).val(a.price||"");a.stock<c?b.siblings("span.info").html("\u5e93\u5b58\u4e0d\u591f"):b.siblings("span.info").html("")}})}});$("#orderItems input.price,#discount,#freight").blur(function(){f()});$("#orderItems table").datagridTable({onremove:f});$("table.unpaid_order a.pay").each(function(){this.onsuccess=function(){$(this).closest("tr").remove();
g()}})};var f=function(b){if(b){var a=$("input.integer",b).val(),c=$("input.double",b).val(),e=0;a&&c&&(e=a*c,$("td:eq(3) span.info",b).text(e));return e}var d=0;$("#orderItems tbody tr").each(function(){d+=f(this)});0<d&&$("#amount").text(d);(b=$("#discount").val())&&(d-=parseFloat(b));b=$("#freight");b.val()&&(d=b.hasClass("add")?d+parseFloat(b.val()):d-parseFloat(b.val()));$("#grandTotal").html(0<=d?d:'<span style="color:red;">\u8d1f\u6570</span>')},g=function(){var b=$("table.unpaid_order"),a=
0;$("tbody tr",b).each(function(){$("td:eq(3)",this).each(function(){a+=parseFloat($(this).text())})});$("tfoot td:eq(3)",b).text(a);return a}})();
