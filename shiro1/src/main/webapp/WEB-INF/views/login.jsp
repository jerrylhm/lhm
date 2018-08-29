<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>登录</title>
</head>
<body class="easyui-layout">

<div class="form-group">  
   <label>验证码 </label> 
   <input name="j_code" type="text" id="kaptchaCode" maxlength="4" class="form-control" />
   <br/> 
   <img src="/shiro1/captcha-image"  id="kaptchaImage"  style="margin-bottom: -3px"/>       
   <a href="#" onclick="changeCode()">看不清?换一张</a>  
</div>
    
<div>
<button id="commitBtn" >提交</button>
</div>
<shiro:guest>
欢迎游客访问，<a href="login">登录</a>
</shiro:guest>
<shiro:authenticated>
    用户[<shiro:principal/>]已身份验证通过
</shiro:authenticated>
<br>
<shiro:notAuthenticated>
    未身份验证（包括记住我）
</shiro:notAuthenticated>
<shiro:hasAnyRoles name="role1,role2">
    用户[<shiro:principal/>]拥有角色role1或role2<br/>
</shiro:hasAnyRoles>
<shiro:lacksRole name="role3">
    用户[<shiro:principal/>]没有角色abc<br/>
</shiro:lacksRole>
<button onclick="login()">登录</button>
<button onclick="role()">权限测试</button>
<button onclick="logout()">退出</button>
<input type="checkbox" id="rm">:记住我
<br>
<script type="text/javascript" src="/shiro1/static/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript">

$(function(){
	//debugger;//生成验证码         
    $('#kaptchaImage').click(function () {  
    $(this).hide().attr('src', '/shiro1/captcha-image').fadeIn(); }); 
	$("#commitBtn").click(function () {
		var kaptchaCode=$("#kaptchaCode").val().trim();
		if(kaptchaCode!=""){
			$.ajax({
				type:"POST",
				url:"captchaVerify",
				data:{"kaptchaCode":kaptchaCode},
				dataType: "json",
				success:function(data){
					console.log(JSON.stringify(data));
					if(data.isOK=="OK"){
						alert("验证码OK");
					}else{
						alert("验证码WRONG");
					}
				}
			});
		}
	});
});   
 
  		  
function changeCode() {  //刷新
    $('#kaptchaImage').hide().attr('src', '/shiro1/captcha-image').fadeIn();  
} 

function login() {
	$.ajax({
		url:"/shiro1/dologin",
		dataType:'text',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{rm:$('#rm').val()},
		success:function(result){
			console.log(result);
		},
		error:function() {
			alert('系统出错了!!')
		}
	});
}

function logout() {
	$.ajax({
		url:"/shiro1/logout",
		dataType:'text',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{},
		success:function(result){
			console.log(result);
		},
		error:function() {
			alert('系统出错了!!')
		}
	});
}

function role() {
	$.ajax({
		url:"/shiro1/role",
		dataType:'text',
		type:"get",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{},
		success:function(result){
			console.log(result);
		},
		error:function() {
			alert('系统出错了!!')
		}
	});
}
</script>
</body>
</html>