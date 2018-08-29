<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>登录</title>
</head>
<body class="easyui-layout">
<a href="javascript:send()">发送消息</a>
<script type="text/javascript" src="/ActiveMQTest/static/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript">

function send() {
	$.ajax({
		url:"/ActiveMQTest/send",
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

</script>
</body>
</html>