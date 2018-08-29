	$('.button-cricle').on('click', function() {
		$('.colum').each(function() {
			if(!$(this).hasClass('hide')) {
				$(this).addClass('hide');
			}else {
				$(this).removeClass('hide');
			}
		})
	})
	
	$('.colum-animation').mouseover(function(){
		  $(this).children('img').addClass('active');
	});
	
	$('.colum-animation').mouseout(function(){
		  $(this).children('img').removeClass('active');
	});
	
	function logout() {
		 layer.confirm('确定注销当前用户吗？', {
		 	    btn: ['确定','取消'], //按钮
		 	    shade: false //不显示遮罩
			       	}, function(){
			       		window.location.href = "/cloud/sso/logout";
		  		});
	}