(function(){Observation.app=function(b){$("form.report,a.report",b).attr("target","_blank");$("#report_format",b).children().click(function(){var a=$(this).data("format");$("a.report",b).attr("href",function(c,d){c=d.indexOf("format=");0<c&&(d=d.substring(0,c-1));return d+("&format="+a)});$("form.report",b).each(function(){var c=$('input[name="format"]',this);c.length?c.val(a):$(this).prepend('<input type="hidden" name="format" value="'+a+'"/>')})});$("#shipped,#paid",b).change(function(){var a=$("span.toggle",
$(this).closest(".controls"));$(this).is(":checked")?a.show():a.hide()});$("#customerName",b).change(function(a){var c=$(a.target);(a=c.val())?$.ajax({url:CONTEXT_PATH+"/biz/customer/json/"+a,dataType:"json",success:function(a){var b=$('select[name="salesman.id"]',c.closest("form")),e=$('select[name="stationId"]',c.closest("form"));a&&a.id&&(a.name==c.val()||a.id==c.val())?(c.siblings("span.info").html(""),a.memo&&(a=$.parseJSON(a.memo),0>document.location.search.indexOf("salesman.id")&&(a.salesman?
b.val(a.salesman):$("option:selected",b).prop("selected",!1)),a.station?e.val(a.station):$("option:selected",e).prop("selected",!1))):(0>document.location.search.indexOf("salesman.id")&&$("option:selected",b).prop("selected",!1),$("option:selected",e).prop("selected",!1),c.siblings("span.info").text("\u81ea\u52a8\u4fdd\u5b58\u65b0\u5ba2\u6237"))}}):c.siblings("span.info").text("")});var f={};$(".customerName",b).typeahead({minLength:2,items:200,source:function(a,c){a in f?c(f[a]):$.ajax({url:CONTEXT_PATH+
"/biz/customer/suggest",type:"post",dataType:"json",data:{keyword:a},success:function(d){var b=[];$.each(d,function(a,c){b.push(c.label)});f[a]=b;c(b)}})},matcher:function(){return!0},updater:function(a){0<a.indexOf("(")&&(a=a.substring(0,a.lastIndexOf("(")));$(this.$element).siblings("span.info").html("");return a}});$("select.fetchprice",b).change(function(a){var c=$(a.target),b=$("input.price:eq(0)",c.closest("tr"));(a=c.val())&&!b.val()&&$.ajax({url:CONTEXT_PATH+"/biz/product/json/"+a,dataType:"json",
success:function(a){a.price&&(b.val(a.price),e());0>=a.stock?c.siblings("span.info").html("\u6ca1\u6709\u5e93\u5b58"):c.siblings("span.info").html("")}})});$("#orderItems input.quantity",b).blur(function(a){e();var c=$(a.target).val();if(c){var b=$("select.fetchprice",$(a.target).closest("tr"));(a=b.val())&&$.ajax({url:CONTEXT_PATH+"/biz/product/json/"+a,dataType:"json",success:function(a){$("input.price:eq(0)",b.closest("tr")).val(a.price||"");a.stock<c?b.siblings("span.info").html("\u5e93\u5b58\u4e0d\u591f"):
b.siblings("span.info").html("")}})}});$("#orderItems .freegift",b).change(function(){var a=$("input.price",$(this).closest("tr"));if($(this).is(":checked"))a.data("oldvalue",a.val()).val("0.00").prop("readonly",!0).next(".field-error").remove();else{var c=a.data("oldvalue");c?a.val(c):a.val("").focus();a.prop("readonly",!1)}e()});$("#orderItems input.price",b).blur(function(){var a=$(this).val();a&&$(".freegift",$(this).closest("tr")).prop("checked",0==parseFloat(a));e()});$("#discount,#freight",
b).blur(function(){e()});$("#orderItems table",b).datagridTable({onremove:e});$("table.unpaid_order a.pay",b).each(function(){this.onsuccess=function(){$(this).closest("tr").remove();var a=$("table.unpaid_order"),c=0;$("tbody tr",a).each(function(){$("td:eq(3)",this).each(function(){c+=parseFloat($(this).text())})});$("tfoot td:eq(3)",a).text(c)}});$("a.vcard",b).click(function(){var a=$(this),c=a.data("orginalhref");c||(c=a.attr("href"),a.data("orginalhref",c));var b=c,c=a.closest("form.richtable"),
a=$('input[name="check"]:checked'),c=$('input[name="keyword"]',c).val();if(a.length){var e=0;a.each(function(){b+=(0==e?"?":"&")+"id="+$(this).val();e++})}else c&&(b+="?keyword="+encodeURIComponent(c));this.href=b})};var e=function(b){if(b){var f=$("input.integer",b).val(),a=$("input.price",b).val(),c=0;f&&""!==a&&(c=f*a,$("td:eq(4) span.info",b).text(c.toFixed(2)));return c}var d=0;$("#orderItems tbody tr").each(function(){d+=e(this)});0<d&&$("#amount").text(d.toFixed(2));(b=$("#discount").val())&&
(d-=parseFloat(b));b=$("#freight");b.val()&&(d=b.hasClass("add")?d+parseFloat(b.val()):d-parseFloat(b.val()));$("#grandTotal").html(0<=d?d.toFixed(2):'<span style="color:red;">\u8d1f\u6570</span>')}})();
