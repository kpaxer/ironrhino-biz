(function() {

	Observation.app = function() {
		$('form.report,a.report').attr('target', '_blank');
		$('#report_format').children().click(function() {
			var format = $(this).attr('format');
			$('a.report').attr('href', function(i, href) {
						var i = href.indexOf('format=');
						if (i > 0)
							href = href.substring(0, i - 1);
						return href += '&format=' + format;
					});
			$('form.report').each(function() {
				var hidden = $('input[name="format"]', this);
				if (hidden.length) {
					hidden.val(format);
				} else {
					$(this)
							.prepend('<input type="hidden" name="format" value="'
									+ format + '"/>');
				}
			});
		});
		$('#shipped,#paid').click(function() {
					var span = $(this).nextAll('span:eq(0)');
					if ($(this).is(':checked'))
						span.show();
					else
						span.hide();
				});
		$('#customerName').blur(function(event) {
			var ele = $(event.target);
			var val = ele.val();
			if (val) {
				var url = CONTEXT_PATH + '/biz/customer/json/' + val;
				$.ajax({
					url : url,
					dataType : 'json',
					success : function(data) {
						var salesman = $('select[name="salesman.id"]', ele
										.closest('form'));
						var station = $('select[name="stationId"]', ele
										.closest('form'));
						if (data && data.id) {
							ele.val(data.name).siblings('span.info').html('');
							if (data.memo) {
								var obj = $.parseJSON(data.memo);
								if (document.location.search
										.indexOf('salesman.id') < 0) {
									if (!obj.salesman)
										$('option:selected', salesman)
												.removeAttr('selected');
									else
										salesman.val(obj.salesman);
								}
								if (!obj.station)
									$('option:selected', station)
											.removeAttr('selected');
								else
									station.val(obj.station);
							}
						} else {
							if (document.location.search.indexOf('salesman.id') < 0)
								$('option:selected', salesman)
										.removeAttr('selected');
							$('option:selected', station)
									.removeAttr('selected');
							ele.siblings('span.info').html('将自动保存为新客户');
						}
					}
				});
			}
		});
		var cache = {};
		$(".customerName").autocomplete({
					minLength : 2,
					source : function(request, response) {
						if (request.term in cache) {
							response(cache[request.term]);
							return;
						}
						$.ajax({
									url : CONTEXT_PATH
											+ '/biz/customer/suggest',
									dataType : "json",
									data : request,
									success : function(data) {
										cache[request.term] = data;
										response(data);
									}
								});
					},
					select : function(event, ui) {
						$(event.target).siblings('span.info').html('');
					}
				});

		$('select.fetchprice').change(function(event) {
					var ele = $(event.target);
					var price = $('input.price:eq(0)', ele.closest('tr'));
					var val = ele.val();
					if (val && !price.val()) {
						var url = CONTEXT_PATH + '/biz/product/json/' + val;
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
				var url = CONTEXT_PATH + '/biz/product/json/' + val;
				$.ajax({
							url : url,
							dataType : 'json',
							success : function(data) {
								$('input.price:eq(0)', ele.closest('tr'))
										.val(data.price || '');
								if (data.stock < quantity)
									ele.siblings('span.info').html('库存不够');
								else
									ele.siblings('span.info').html('');
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

		$('table.unpaid_order a.pay').each(function() {
					this.onsuccess = function() {
						$(this).closest('tr').remove();
						calculateUnpaidOrder();
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
				grandTotal -= parseFloat(discount);
			var freight = $('#freight');
			if (freight.val())
				if (freight.hasClass('add'))
					grandTotal += parseFloat(freight.val());
				else
					grandTotal -= parseFloat(freight.val());
			$('#grandTotal').html(grandTotal >= 0
					? grandTotal
					: '<span style="color:red;">负数</span>');
		}
	}

	var calculateUnpaidOrder = function() {
		var table = $('table.unpaid_order');
		var total = 0;
		$('tbody tr', table).each(function() {
					$('td:eq(3)', this).each(function() {
								total += parseFloat($(this).text());
							});
				});

		$('tfoot td:eq(3)', table).text(total);
		return total;
	}

})();