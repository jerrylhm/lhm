<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="System-Login">
<meta name="author" content="yangxy">

<!-- CSS -->
<link rel="shortcut icon" href="/cloud/images/shortcut.ico">
<link rel="stylesheet" href="/cloud/font-awesome/css/font-awesome.css">
<link rel="stylesheet" href="/cloud/background-system-login/css/reset.css">
<link rel="stylesheet" href="/cloud/background-system-login/css/supersized.css">
<link rel="stylesheet" href="/cloud/background-system-login/css/style.css">
<link rel="stylesheet" href="/cloud/css/load.css">
<link rel="stylesheet" href="/cloud/css/sweetalert.css">
<script type="text/javascript" src="/cloud/js/load.js"></script>
<title>校园综合大平台登录入口</title>
</head>
<body>
	 <div class="page-container">
         <h1>校园综合管理平台后台登录</h1>
         <h2>Management platform background</h2>
        <form action="/cloud/admin/doLogin">
	        <div class="border-container">
	        	<div class="login-boder">
					<div>
						<input type="text" name="username" class="username" placeholder="Username"/>
					</div>
		            <div>
						<input type="password" name="password" class="password" placeholder="Password"/>
		            </div>
		            <button id="submit" type="button">登  &nbsp;&nbsp;&nbsp;&nbsp;录</button>
	            </div>
	        </div>
        </form>
        <div class="connect">
            <p></p>
	        <p style="margin-top:20px;"></p>
        </div>
      </div>
	  
	  <script src="/cloud/js/jquery.min.js" type="text/javascript"></script>
      <script src="/cloud/background-system-login/js/supersized.3.2.7.min.js"></script>
      <script src="/cloud/background-system-login/js/supersized-init.js"></script>
      <script src="/cloud/js/sweetalert-dev.js"></script>
	  <script>
		$("#submit").click(function(){
			if(validForm()) {
				login();
			}
		});
		
		$(".password").on('keyup', function(event){
			if(event.keyCode == 13) {
				if(validForm()) {
					login();
				}
			}
		});
		
		function validForm() {
			var username = $('.username').val();
			var password = $('.password').val();
			if(username == '' || password =='')
			{
				sweetAlert("登陆失败", "用户名或密码不能为空~！","error");
				return false;
			}else {
				var reg = /^[0-9A-Za-z]+$/;
				if(!reg.exec(username))
				{
					sweetAlert("登陆失败", "用户名错误", "error");
					return false;
				}
				return true;
			}
		}
		
		function login(username,password) {
			var username = $('.username').val();
			var password = $('.password').val();
			showLoad('form');
			$.ajax({
				url:"/cloud/admin/verifyUser",
				dataType:'json',
				type:"post",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				data:{username:username,
					  password:password},
				success:function(result){
					hideLoad('form');
					if(result.code != '0000') {
						sweetAlert("登陆失败", result.msg,"error");
						return;
					}
					window.location.href = "/cloud/admin";
				},
				error:function() {
					hideLoad('form');
					alert('系统错误')
				}
			});		
		}
		
		window.onload = function()
		{
			$(".connect p").eq(0).animate({"left":"0%"}, 600);
			$(".connect p").eq(1).animate({"left":"0%"}, 400);
		}	
	</script>
	  
	  
</body>
</html>