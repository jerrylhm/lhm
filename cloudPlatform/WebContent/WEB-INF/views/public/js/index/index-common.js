	$('.user-image').on('click', function() {
		$('.user-btn').each(function() {
			if($(this).hasClass('active')) {
				$(this).removeClass('active');
			}else {
				$(this).addClass('active');
			}
		});
	});