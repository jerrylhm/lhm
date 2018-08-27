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
<title>跳转页面</title>
<h5>正在准备跳转到子系统......</h5>
</head>
<body>
<form id="verifyForm" action="${requestUrl}" method="post">
	<input id="CREATORISOK" type="hidden" name="CREATORISOK" value="">
</form>
<script type="text/template" id="formParam">
	<input type="hidden" name="{name}" value="{value}">
</script>
<script type="text/javascript" src="<%=basePath %>js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/sso/verify.js"></script>
<script type="text/javascript">
var url = "${requestUrl}";
var params = '${requestParam}';
var method = "${requestMethod}";
var clients = "<%=PropertiesUtil.getClientUrl() %>";
var token = "${token }";

doVerify(url,params,method,token,clients);

//替换字符串中所有'{属性名}'
function replaceParam(str, param, value) {
	return str.replace(new RegExp('{' + param + '}',"gm"), value);
}
</script>
</body>
</html>