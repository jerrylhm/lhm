<%@page import="com.creator.common.util.PropertiesUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<%@ include file="../common/crypto-js.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="<%=basePath %>images/shortcut.ico">
<link rel="stylesheet" href="<%=basePath%>css/login.css">
<title>登录页面</title>
</head>
<body>
	<div class="box" id="app">
		<div class="box-mask">
			<span class="fa fa-soundcloud  icon"></span>
			<p class="title">校园综合管理平台</p>
			<p class="text">教育业务与教育资源云平台</p>
			<p class="text">行业领先 <span style="margin-left: 10px;">安全保障</span></p>
		</div>
		<div class="box-login">
			<div class="login-header">
				<span>用户登录</span>
				<small>LOGIN</small>
			</div>
			<div>
				<div class="form-group">
					<div class="input-group ">
			            <span class="input-group-addon"><i class="fa fa-user"></i></span>
			            <input type="text" class="form-control" placeholder="请输入用户名" id="username">
			        </div>
				</div>
				<div >
					<div class="input-group ">
			            <span class="input-group-addon"><i class="fa fa-lock"></i></span>
			            <input type="password" class="form-control" placeholder="请输入密码" id="password"  >
			        </div>
				</div>
				<div class="login-action">
					<div class="checkbox login-checkbox">
						<label>
							<input type="checkbox" id="rememberMe">记住我
						</label>
					</div>
					<div class="login-tip">
						<span>没有账号？</span>
						<a class="text-primary" title="注册新用户" href="<%=basePath%>register">注册新用户</a>
					</div>
				</div>
				<div>
					<a class="btn btn-primary login-btn" id="login">登录</a>
				</div>
				
			</div>
		</div>
	</div>
	
	<input id="url" type="hidden" name="url" value="${url}">
	<input id="params" type="hidden" name="params" value="${params}">
	<input id="method" type="hidden" name="method" value="${method}">
	
	<form id="loginForm" action="" method="get">
		<input id="token" type="hidden" name="CREATORTOKEN" value="">
	</form>
	<script type="text/template" id="formParam">
		<input type="hidden" name="{name}" value="{value}">
	</script>
<script type="text/javascript" src="<%=basePath%>layer/layer.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/sso/login.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
<script type="text/javascript">
	var usernameKey = "login_username";
	var passwordKey = "login_password";
	var rememberMeKey = "login_rememberme";
	var clients = "<%=PropertiesUtil.getClientUrl() %>";
	var msg = '${msg}';
	if(msg != '') {
		alertFail(msg);
	}
	doLogin('${pageContext.request.contextPath }',clients,setCookie);
	
	$(document).ready(function() {
		initPage();
		var name = $.cookie(usernameKey);
		console.log("cookie 用户名：" + name);
		if(name != null) {
			$("#username").val(decryptByDESModeEBC(name));
		}
		var psw = $.cookie(passwordKey);
		console.log("cookie密码：" + psw);
		if(psw != null) {
			$("#password").val(decryptByDESModeEBC(psw));
		}
		
		var remember = $.cookie(rememberMeKey);
		console.log("cookie记住我：" + remember);
		if(remember != null && remember == "1") {
			$("#rememberMe").prop("checked",true);
		}
	});
	
	$(window).resize(function() {
		initPage();
	});
	
	//登录成功回调函数
	function setCookie() {
		var isRemember = $("#rememberMe").prop("checked");
		if(isRemember) {
			var username = $("#username").val();
			var password = $("#password").val();
			//设置cookie
			$.cookie(usernameKey,encryptByDES(username));
			$.cookie(passwordKey,encryptByDES(password));
			$.cookie(rememberMeKey,"1");
		}else {
			//清空cookie
			clearCookie();
		}
	}
	//初始化页面
	function initPage() {
		//设置body高度
		var height = $(window).height();
		//$("body").css("height",height + "px");
		//设置padding-top
		var boxHeight = $(".box").height();
		var padding = (height - boxHeight) / 2.0;
		console.log(boxHeight);
		if(padding <= 0) {
			padding = 0;
		}
		$("body").css("paddingTop",padding + "px")
	}
	
	
	
	function addCookie(key,value,expires) {
		$.cookie(key,value,{expires: expires,path: "/"});
	}
	//清空cookie
	function clearCookie() {
		$.cookie(usernameKey,null);
		$.cookie(passwordKey,null);
		$.cookie(rememberMeKey,null);
	}
	
	//键盘输入事件
	$("#username").keydown(keydown);
	$("#password").keydown(keydown);
	
	//监控按钮按下
	function keydown(event) {
		if(event.keyCode == 13) {
			//回车，模拟点击登录按钮
			$("#login").trigger("click");
		}
	}
</script>
</body>
</html>