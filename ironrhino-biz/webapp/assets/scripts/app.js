(function() {
	Observation.app = function() {
		$('#customerId.ajax').blur(function(event) {
			var ev = event || window.event;
			var input = $(event.srcElement || event.target);
			if (input.val()) {
				var url = CONTEXT_PATH + '/customer/json/' + input.val();
				$.ajax({
					url : url,
					dataType : 'json',
					success : function(customer) {
						if (customer.name)
							input.next('span').text(customer.name);
						else
							input
									.val('')
									.focus()
									.next('span')
									.html('<span style="color:red;">没有此客户</span>');
					}
				});
			}
		});
		$('#orderItems input').blur(function() {
					calculate()
				});
		$('#orderItems tr input').last().bind('keyup',function(event) {
					if (event.keyCode && event.keyCode == 13) {
						event.stopPropagation();
						var event = event || window.event;
						var row = $(event.srcElement || event.target).closest('tr');
						addRow(row);
						return false;
					}
				});
	};

	var calculate = function(row) {
		if (row) {
			var quantity = $('input.integer', row).val();
			var price = $('input.double', row).val();
			var subtotal = 0;
			if (quantity && price) {
				subtotal = quantity * price;
				$('td', row).last().text(subtotal);
			}
			return subtotal;
		} else {
			var grandTotal = 0;
			$('#orderItems tbody tr').each(function() {
						grandTotal += calculate(this);
					});
			var discount = $('#discount').val();
			if (discount)
				grandTotal -= discount;
			if (grandTotal > 0)
				$('#grandTotal').text(grandTotal);
		}
	}
	var addRow = function(row) {
		var r = row.clone(true);
		row.after(r);
		$('td',r).last().text('');
		$('input,select',r).val('').first().focus();
		$('input',r).each(function(){
			var name = $(this).attr('name');
			var temp = name.substring(0,name.indexOf('[')+1);
			temp+=parseInt(name.substring(name.indexOf('[')+1,name.indexOf(']')))+1;
			temp+=name.substring(name.indexOf(']'));
			$(this).attr('name',temp);
		});
	}
})();