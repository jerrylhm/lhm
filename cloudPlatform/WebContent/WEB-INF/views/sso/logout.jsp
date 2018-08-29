<%@page import="com.creator.common.util.PropertiesUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%	
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>注销页面</title>
</head>
<body>
<h1>注销成功</h1>
<script type="text/javascript" src="<%=basePath %>js/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
	var clients = "<%=PropertiesUtil.getClientUrl() %>";
	var clientArray = clients.split(",");
	var ajaxCount = clientArray.length;	

	for(var i in clientArray) {
		$.ajax({
			url:clientArray[i]+"/removeCookie",
			dataType:'text',
			type:"post",
			xhrFields:{withCredentials:true},
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			data:{},
			success:function(result){
				ajaxCount--;
			},
			error:function() {
				ajaxCount--;
				console.log(clientArray[i] + "连接失败")
			}
		});
	}

// 	var cookieInterval = setInterval(function() {
// 		if(ajaxCount == 0) {
			var url = "${url}";
			if(url != "") {
				window.location.href = "${url}";
			}else {
				window.location.href = "/cloud/sso/login";
			}
// 			clearInterval(cookieInterval);
// 		}
// 	}, 100);

	
	//替换字符串中所有'{属性名}'
	function replaceParam(str, param, value) {
		return str.replace(new RegExp('{' + param + '}',"gm"), value);
	}
</script>
</body>
</html>