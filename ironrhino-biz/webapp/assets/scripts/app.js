(function() {

	Observation.app = function(container) {
		$('form.report,a.report', container).attr('target', '_blank');
		$('#report_format', container).children().click(function() {
			var format = $(this).data('format');
			$('a.report', container).attr('href', function(i, href) {
						var i = href.indexOf('format=');
						if (i > 0)
							href = href.substring(0, i - 1);
						return href += '&format=' + format;
					});
			$('form.report', container).each(function() {
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
		$('#shipped,#paid', container).change(function() {
					var span = $('span.toggle', $(this).closest('.controls'));
					if ($(this).is(':checked'))
						span.show();
					else
						span.hide();
				});
		$('#customerName', container).change(function(event) {
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
						if (data
								&& data.id
								&& (data.name == ele.val() || data.id == ele
										.val())) {
							ele.siblings('span.info').html('');
							if (data.memo) {
								var obj = $.parseJSON(data.memo);
								if (document.location.search
										.indexOf('salesman.id') < 0) {
									if (!obj.salesman)
										$('option:selected', salesman).prop(
												'selected', false);
									else
										salesman.val(obj.salesman);
								}
								if (!obj.station)
									$('option:selected', station).prop(
											'selected', false);
								else
									station.val(obj.station);
							}
						} else {
							if (document.location.search.indexOf('salesman.id') < 0)
								$('option:selected', salesman).prop('selected',
										false);
							$('option:selected', station).prop('selected',
									false);
							ele.siblings('span.info').text('自动保存新客户');
						}
					}
				});
			} else {
				ele.siblings('span.info').text('');
			}
		});
		var cache = {};
		$(".customerName", container).typeahead({
					minLength : 2,
					items : 200,
					source : function(query, process) {
						if (query in cache) {
							process(cache[query]);
							return;
						}
						$.ajax({
									url : CONTEXT_PATH
											+ '/biz/customer/suggest',
									type : 'post',
									dataType : "json",
									data : {
										keyword : query
									},
									success : function(data) {
										var arr = [];
										$.each(data, function(i, v) {
													arr.push(v.label);
												});
										cache[query] = arr;
										process(arr);
									}
								});
					},
					matcher : function(item) {
						return true;
					},
					updater : function(item) {
						if (item.indexOf('(') > 0)
							item = item.substring(0, item.lastIndexOf('('));
						$(this.$element).siblings('span.info').html('');
						return item;
					}
				});

		$('select.fetchprice', container).change(function(event) {
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
		$('#orderItems input.quantity', container).blur(function(event) {
			calculate();
			var quantity = $(event.target).val();
			if (!quantity)
				return;
			var ele = $('select.fetchprice', $(event.target).closest('tr'));
			var val = ele.val();
			if (val) {
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
		$('#orderItems .freegift', container).change(function() {
			var price = $('input.price', $(this).closest('tr'));
			if ($(this).is(':checked')) {
				price.data('oldvalue', price.val()).val('0.00').prop(
						'readonly', true).next('.field-error').remove();
			} else {
				var oldvalue = price.data('oldvalue');
				if (oldvalue)
					price.val(oldvalue);
				else
					price.val('').focus();
				price.prop('readonly', false);
			}
			calculate();
		});
		$('#orderItems input.price', container).blur(function() {
			var value = $(this).val();
			if (value) {
				$('.freegift', $(this).closest('tr')).prop('checked',
						parseFloat(value) == 0);
			}
			calculate()
		});
		$('#discount,#freight', container).blur(function() {
					calculate()
				});

		$('#orderItems table', container).datagridTable({
					onremove : calculate
				});

		$('table.unpaid_order a.pay', container).each(function() {
					this.onsuccess = function() {
						$(this).closest('tr').remove();
						calculateUnpaidOrder();
					}
				});
	};

	var calculate = function(row) {
		if (row) {
			var quantity = $('input.integer', row).val();
			var price = $('input.price', row).val();
			var subtotal = 0;
			if (quantity && price !== '') {
				subtotal = quantity * price;
				$('td:eq(4) span.info', row).text(subtotal.toFixed(2));
			}
			return subtotal;
		} else {
			var grandTotal = 0;
			$('#orderItems tbody tr').each(function() {
						grandTotal += calculate(this);
					});
			if (grandTotal > 0)
				$('#amount').text(grandTotal.toFixed(2));
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
					? grandTotal.toFixed(2)
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