<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>用户登录</title>
<link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css">
<link rel="stylesheet" href="<%=basePath%>font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="<%=basePath%>css/sweetalert.css" >
<link rel="shortcut icon" href="<%=basePath %>images/shortcut.ico">
<style>
	.container-box {
		width : 500px;
		color : #fff;
		background-color :rgba(0,0,0,0.1); 
		margin : 0 auto;
		border-radius : 10px;
		
		margin-top:200px;
	}
	.box-header {
		width : 100%;
		padding:10px 15px;
		border-bottom:2px solid rgba(230,230,230,0.5);
	}
	
	.box-content {
		padding : 40px 10px;
	}
	
	.div-input {
		padding : 8px 25px;
		border : 1px solid #fff;
		border-radius : 25px;
	}
	
	.input {
		background-color: rgba(255,255,255,0);
		margin-left : 10px;
		border:none;
		width : 80%;
		padding:3px 8px;
		outline:none;
	}
	
	.div-group {
		margin-bottom : 35px;
	}
	
	.span-label {
		width : 20px;
		text-align: center;
	}
	.btn-login {
		padding : 10px 12px;
		border-radius : 30px;
		width:100%;
	}
	.span-check {
		width:50px;
		height : 50px;
		border-radius : 50%;
		display : inline-block;
		background-color: #5cb85c;
		color : #fff;
		text-align: center;
		padding-top:10px;
		
	}
	/* webkit */
	::-webkit-input-placeholder {
		color : #eee;
	}
	
	/* firefox18 */
	:moz-placeholder {
		color : #eee;
	}
	/* firefox19+ */
	::moz-placeholder {
		color : #eee;
	}
	
	:-ms-input-placeholder {
		color : #eee;
	}
	a{
		text-decoration: none;
		cursor: pointer;
	}
	a:hover{
		text-decoration: none;
	}
	a:active {
		text-decoration: none;
	}
	a:visited {
		text-decoration: none;
	}
	#btn_mobile:hover {
		color : #fff;
	}
	#btn_mobile:visited {
		color : #fff;
	}
</style>
</head>
<body >
	<div>
		<div id="div_content" class="container-box">
			<div class="box-header text-center">
				<h4>后台管理登录页面</h4>
			</div>
			<div class="box-content clearfix">
				<div class="col-xs-8 col-xs-offset-2">
					<div class="div-input div-group">
							<span class="fa fa-user-circle fa-lg span-label" ></span>
							<input type="text" id="input_name" class="input" placeholder="请输入用户名">
					</div>
					<div class="div-input div-group" >
						<span class="fa fa-lock fa-lg span-label" ></span>
						<input type="password" id="input_psw" class="input" placeholder="请输入密码">
					</div>
					<div >
						<a style="font-size:16px;"  class="btn btn-success  btn-login" id="btn_login" onclick="login()" >
							<i class="fa fa-check fa-fw"></i>
							<span style="letter-spacing:25px;"> 登录</span>
						</a>
						<p class="text-center">
							<span id="span_check" class="span-check fa fa-check fa-2x" style="display:none;"></span>
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery-ui.min.js"></script>
<script src="<%=basePath%>js/sweetalert-dev.js"></script>
<script src="<%=basePath%>js/jquery.backstretch.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$.backstretch([
	                    "<%=basePath%>images/bkg_login.jpg",
	                    "<%=basePath%>images/4.jpg"
	                ], {
	                    fade: 1000,
	                    duration: 8000
	                });
		init();
		
	});
	$(window).resize(function() {
		init();
	});
	//初始化界面
	function init() {
		//设置登录框垂直居中
		var winHeight = $(window).height();
		var boxHeight = $(".container-box").height();
		var marginHeight = (winHeight-boxHeight)/2.0;
		if(marginHeight <= 0) {
			marginHeight = 0;
		}
		$(".container-box").css("margin-top",marginHeight + "px");
	}
	
	//回车按键监控
	$(document).keyup(function(event) {
		//判断输入框是否获取到焦点
		if(event.keyCode == 13) {
			if($("#input_name").is(":focus") || $("#input_psw").is(":focus")) {
				login();
			}
		}
	});
	
	
	//点击登录
	function login() {
		//用户名、密码是否为空
		var username = $("#input_name").val();
		var password = $("#input_psw").val();
		
		if(username == "" || password == "") {
			alertFail("用户名或密码不能为空！");
			return false;
		}
		
		//验证登录
		var data = {
				username : username,
				password : password
		};
		$.post("<%=basePath%>login/verifyLogin",data,function(rs) {
			if(rs == "0") {
				alertFail("用户名或密码错误!");
				return false;
			}else if(rs == "1") {
				validUserPermission(username)
			}else if(rs == "2") {
				alertFail("请勿重复登录！");
				return false;
			}else if(rs == "3") {
				alertFail("账号已在异地登录！");
				return false;
			}
		},"json");
	}
	
	//验证用户后台管理权限
	function validUserPermission(username) {
		$.post("<%=basePath%>login/validUserPermission",
		{username:username},
		function(rs) {
			if(rs == 0) {
				alert('该用户没有后台管理权限');
			}else if(rs == 1) {
				//隐藏登录按钮
				$("#btn_login").hide("scale",{
					direction : "horizontal",
					percent : 40,
					origin : ["top","center"],
				},function() {
					//显示正确图标
					$("#span_check").fadeIn("middle",function() {
						alertSuccess("登录成功！");
						setTimeout(function() {
							//跳转至主页
							window.location.href = "<%=basePath%>admin/index";
						},1000);
					});	
				});
			}
		},"json");
	}
	
	//弹出定时对话框
	function alertTimer(title) {
		swal({
			title : title,
			text : "3秒后自动关闭",
			timer : 3000,
			showConfirmButton:true
		});
	}
	
	//弹出失败对话框
	function alertFail(title) {
		swal(title,"","error");
	}
	
	//弹出成功对话框
	function alertSuccess(title) {
		swal(title,"","success");
	}
</script>
</body>
</html>