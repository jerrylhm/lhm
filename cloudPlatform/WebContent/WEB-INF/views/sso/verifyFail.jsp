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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>跳转失败</title>
</head>
<body>
<h1>跳转失败</h1>
<h5>请查看SSOServer.properties文件中配置的客户端地址是否错误!</h5>
<script type="text/javascript" src="<%=basePath %>js/jquery-3.2.1.min.js"></script>
</body>
</html>