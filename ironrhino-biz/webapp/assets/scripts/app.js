(function() {
	Observation.app = function() {
		$('#customerName.ajax').blur(function(event) {
			var ele = $(event.target);
			var val = ele.val();
			if (val) {
				var url = CONTEXT_PATH + '/customer/json/' + val;
				$.ajax({
					url : url,
					dataType : 'json',
					success : function(data) {
						if (data.name) {
							if (data.id) {
								ele.val(data.name).siblings('span.info')
										.html('');
							} else {
								ele
										.focus()
										.siblings('span.info')
										.html('<span style="color:red;">备选:</span>'
												+ data.name);
							}
						} else {
							ele
									.siblings('span.info')
									.html('<span style="color:red;">将自动保存为新客户</span>');
						}
					}
				});
			}
		});
		$('select.fetchprice').change(function(event) {
			var ele = $(event.target);
			var val = ele.val();
			if (val) {
				var url = CONTEXT_PATH + '/product/json/' + val;
				$.ajax({
					url : url,
					dataType : 'json',
					success : function(data) {
						$('input.price:eq(0)', ele.closest('tr'))
								.val(data.price || '');
						if (data.stock <= 0)
							ele
									.siblings('span.info')
									.html('<span style="font-style:italic;">没有库存</span>');
					}
				});
			}
		});
		$('#orderItems input.quantity').blur(function() {
			calculate();
			var quantity = $(event.target).val();
			if(!quantity)return;
			var ele = $('select.fetchprice',$(event.target).closest('tr'));
			var val = ele.val();
			if (val) {
				var url = CONTEXT_PATH + '/product/json/' + val;
				$.ajax({
					url : url,
					dataType : 'json',
					success : function(data) {
						$('input.price:eq(0)', ele.closest('tr'))
								.val(data.price || '');
						if (data.stock < quantity)
							ele
									.siblings('span.info')
									.html('<span style="font-color:red;">库存不够,将自动建立计划</span>');
					}
				});
			}
		});
		$('#orderItems input.price,#discount').blur(function(){calculate()});
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
	var rename = function() {
		$('#orderItems tbody tr').each(function(i) {
			$('input', this).each(function() {
				$(this).removeAttr('id');
				var name = $(this).attr('name');
				name = name.substring(0, name.indexOf('[') + 1) + i
						+ name.substring(name.indexOf(']'));
				$(this).attr('name', name);
			});
		});
	}
	var addRow = function(event) {
		var event = event || window.event;
		var row = $(event.srcElement || event.target).closest('tr');
		var r = row.clone(true);
		row.after(r);
		$('td:eq(3)', r).text('');
		$('select:eq(0)', r).html(function() {
					return $(this).data('innerHTML')
				});
		$('input', r).val('');
		$('input:eq(0)', r).focus();
		rename();
	};
	var removeRow = function(event) {
		var event = event || window.event;
		var row = $(event.srcElement || event.target).closest('tr');
		if ($('tr', row.parent()).length > 1) {
			row.remove();
			rename();
			calculate();
		}
	};

})();