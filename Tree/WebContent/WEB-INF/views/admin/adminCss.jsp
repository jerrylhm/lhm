<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>样式引入</title>
<link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css">
<link rel="stylesheet" href="<%=basePath%>font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="<%=basePath%>css/sweetalert.css" >
</head>
<body>

<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery-ui.min.js"></script>
<script src="<%=basePath%>js/sweetalert-dev.js"></script>

<script>
	//sweetalert调用方法
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