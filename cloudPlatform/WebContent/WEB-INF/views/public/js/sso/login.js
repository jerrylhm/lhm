

	function doLogin(contextPath,clients,successCallback) {
		
		var clientArray = clients.split(",");
		var ajaxCount = clientArray.length;
		$('#login').on('click', function() {
			var username = $('#username').val();
			var password = $('#password').val();
			var url = $('#url').val();
			var params = $('#params').val();
			var method = $('#method').val();
			var index = layer.load();
			$.ajax({
				url:contextPath + "/sso/loginUser",
				dataType:'json',
				type:"post",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				data:{username:username,
					  password:password,
					  url:url,
					  params:params,
					  method:method},
				success:function(result){
					layer.close(index);
					if(result == null || !result.result) {
						alertFail("用户名/密码错误!");
						return;
					}else if(result.type == "normal") {
						if(successCallback != null) {
							//执行成功回调函数
							successCallback();
						}
						ajaxAddCookie(result.token);	
						
					}else if(result.type == "forware"){
						var url = result.url;
						var params = JSON.parse(result.paramJsonStr);
						var method = result.method;
						var token = result.token;
						$('#loginForm').attr('method', method);
						$('#loginForm').attr('action', url);
						$('#token').val(token);
						if(params != null && params != "") {
							for(var key in params) {
								var $param = $('#formParam').html();
								$param = replaceParam($param, 'name', key);
								$param = replaceParam($param, 'value', params[key]);
								$('#loginForm').append($param);
							}					
						}
						
						if(successCallback != null) {
							//执行成功回调函数
							successCallback();
						}
						$('#loginForm').submit();
					}else {
						alertFail("系统异常!");
					}
				},
				error:function() {
					layer.close(index);
					alertFail('系统出错了!!')
				}
			});
		})
		
		function ajaxAddCookie(token) {
			if(token == null || token == "") {
				alertFail('token生成失败！！');
				return;
			}
			for(var i in clientArray) {
				$.ajax({
					url:clientArray[i]+"/addCookie",
					dataType:'text',
					type:"post",
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
					window.location.href = contextPath + "/index";
					clearInterval(cookieInterval);
				}
			}, 100);
		}
	}
	
	//替换字符串中所有'{属性名}'
	function replaceParam(str, param, value) {
		return str.replace(new RegExp('{' + param + '}',"gm"), value);
	}