function doVerify(url,params,method,token,clients) {
	var paramsJSON = JSON.parse(params);
	var clientArray = clients.split(",");

	var ajaxCount = clientArray.length;
	for(var i in clientArray) {
		$.ajax({
			url:clientArray[i]+"/addCookie",
			dataType:'text',
			type:"post",
			timeout : 1000,
			xhrFields:{withCredentials:true},
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			data:{cookieName:"CREATORTOKEN",cookieValue:token},
			success:function(result){
				ajaxCount--;
			},
			error:function() {
				ajaxCount--;
				console.log(clientArray[i] + "连接失败")
			}
		});
	}

	var cookieInterval = setInterval(function() {
		if(ajaxCount == 0) {
			var verifyKey = false;
			$('#verifyForm').attr('method', method);
			$('#CREATORISOK').val(token);
			for(var key in paramsJSON) {
				var $param = $('#formParam').html();
				$param = replaceParam($param, 'name', key);
				$param = replaceParam($param, 'value', paramsJSON[key]);
				$('#verifyForm').append($param);
			}
			for(var i in clientArray) {
				var patt1 = new RegExp('^' + clientArray[i]);
				var match = url.match(patt1);
				if(match) {
					verifyKey = true;
					break;
				}
			}
			if(verifyKey) {
				$('#verifyForm').submit();
			}else {
				window.location.href = "/cloud/sso/verifyFail";
			}
			clearInterval(cookieInterval);
		}
	}, 100);
}

//替换字符串中所有'{属性名}'
function replaceParam(str, param, value) {
	return str.replace(new RegExp('{' + param + '}',"gm"), value);
}