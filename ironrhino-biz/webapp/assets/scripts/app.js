Observation.app = function(){
$('#customerId.ajax').blur(function(event){
	var ev = event || window.event;
	var input = $(event.srcElement || event.target);
	if(input.val()){
		var url = CONTEXT_PATH+'/customer/json/'+input.val();
		$.ajax({
  		url: url,
  		dataType: 'json',
  		success: function(customer){
  			if(customer.name)
  				input.next('span').text(customer.name);
  			else
  				input.val('').focus().next('span').html('<span style="color:red;">没有此客户</span>');
  		}
		});
	}
});
}