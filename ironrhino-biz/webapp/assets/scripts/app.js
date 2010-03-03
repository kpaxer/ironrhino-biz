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
			if(discount)
				grandTotal-=discount;
			$('#grandTotal').text(grandTotal);
		}
	}

})();