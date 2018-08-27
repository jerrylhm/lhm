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
<title>Insert title here</title>
</head>
<body>
<h1>登录页面</h1>
<input id="username" type="text" name="username"><br>
<input id="password" type="password" name="password"><br>
<input id="url" type="hidden" name="url" value="${url}">
<input id="params" type="hidden" name="params" value="${params}">
<input id="method" type="hidden" name="method" value="${method}">
<input id="login" type="button" value="提交">

<form id="loginForm" action="" method="get">
	<input id="token" type="hidden" name="CREATORTOKEN" value="">
</form>
<script type="text/template" id="formParam">
	<input type="hidden" name="{name}" value="{value}">
</script>
<script type="text/javascript" src="<%=basePath %>js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/sso/login.js"></script>
<script type="text/javascript">
	var clients = "<%=PropertiesUtil.getClientUrl() %>";
	var msg = '${msg}';
	if(msg != '') {
		alert(msg);
	}
	doLogin('${pageContext.request.contextPath }',clients);
	
	
</script>
</body>
</html>