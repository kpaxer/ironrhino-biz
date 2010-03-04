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
						if (customer.name) {
							if (customer.id)
								input.val(customer.id).next('span')
										.text(customer.name);
							else
								input
										.val('')
										.focus()
										.next('span')
										.html('<span style="color:red;">备选:</span>'
												+ customer.name);
						} else {
							input
									.val('')
									.focus()
									.next('span')
									.html('<span style="color:red;">没有此客户</span>');
						}
					}
				});
			}
		});
		$('#orderItems input').blur(function() {
					calculate()
				});
		$('#orderItems button.add').click(addRow);
		$('#orderItems button.remove').click(removeRow);
	};

	var calculate = function(row) {
		if (row) {
			var quantity = $('input.integer', row).val();
			var price = $('input.double', row).val();
			var subtotal = 0;
			if (quantity && price) {
				subtotal = quantity * price;
				$('td:eq(3)', row).text(subtotal);
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
	var addRow = function(event) {
		var event = event || window.event;
		var row = $(event.srcElement || event.target).closest('tr');
		var r = row.clone(true);
		row.after(r);
		$('td:eq(3)', r).text('');
		$('input,select', r).val('').first().focus();
		$('input', r).each(function() {
			var name = $(this).attr('name');
			var temp = name.substring(0, name.indexOf('[') + 1);
			temp += parseInt(name.substring(name.indexOf('[') + 1, name
							.indexOf(']')))
					+ 1;
			temp += name.substring(name.indexOf(']'));
			$(this).attr('name', temp);
		});
	}
	var removeRow = function(event) {
		var event = event || window.event;
		var row = $(event.srcElement || event.target).closest('tr');
		if ($('tr', row.parent()).length > 1){
			row.remove();
			calculate();
		}
	}
})();