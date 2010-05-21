(function() {
	Observation.app = function() {
		$('#shipped').click(function() {
					var span = $(this).nextAll('span:eq(0)');
					if ($(this).attr('checked'))
						span.show();
					else
						span.hide();
				});
		// $('#customerName').blur(function(event) {
		// var ele = $(event.target);
		// var val = ele.val();
		// if (val) {
		// var url = CONTEXT_PATH + '/customer/json/' + val;
		// $.ajax({
		// url : url,
		// dataType : 'json',
		// success : function(data) {
		// if (data && data.id)
		// ele.val(data.name).siblings('span.info')
		// .html('');
		// else
		// ele.siblings('span.info').html('将自动保存为新客户');
		// }
		// });
		// }
		// });
		$('.customerName').autocomplete(
				CONTEXT_PATH + "/customer/suggest?decorator=none", {
					max : 1000,
					minChars : 2,
					delay : 500
				});
		$('select.fetchprice').change(function(event) {
					var ele = $(event.target);
					var price = $('input.price:eq(0)', ele.closest('tr'));
					var val = ele.val();
					if (val && !price.val()) {
						var url = CONTEXT_PATH + '/product/json/' + val;
						$.ajax({
									url : url,
									dataType : 'json',
									success : function(data) {
										if (data.price) {
											price.val(data.price);
											calculate();
										}
										if (data.stock <= 0)
											ele.siblings('span.info')
													.html('没有库存');
										else
											ele.siblings('span.info').html('');
									}
								});
					}
				});
		$('#orderItems input.quantity').blur(function(event) {
			calculate();
			var quantity = $(event.target).val();
			if (!quantity)
				return;
			var ele = $('select.fetchprice', $(event.target).closest('tr'));
			var val = ele.val();
			if (val && !ele.siblings('span.info').text()) {
				var url = CONTEXT_PATH + '/product/json/' + val;
				$.ajax({
							url : url,
							dataType : 'json',
							success : function(data) {
								$('input.price:eq(0)', ele.closest('tr'))
										.val(data.price || '');
								if (data.stock < quantity)
									ele.siblings('span.info').html('库存不够');
							}
						});
			}
		});
		$('#orderItems input.price,#discount,#freight').blur(function() {
					calculate()
				});

		$('#orderItems table').datagridTable({
					onremove : calculate
				});
	};

	var calculate = function(row) {
		if (row) {
			var quantity = $('input.integer', row).val();
			var price = $('input.double', row).val();
			var subtotal = 0;
			if (quantity && price) {
				subtotal = quantity * price;
				$('td:eq(3) span.info', row).text(subtotal);
			}
			return subtotal;
		} else {
			var grandTotal = 0;
			$('#orderItems tbody tr').each(function() {
						grandTotal += calculate(this);
					});
			if (grandTotal > 0)
				$('#amount').text(grandTotal);
			var discount = $('#discount').val();
			if (discount)
				grandTotal -= discount;
			var freight = $('#freight').val();
			if (freight)
				grandTotal -= freight;
			$('#grandTotal').html(grandTotal >= 0
					? grandTotal
					: '<span style="color:red;">负数</span>');
		}
	}

})();