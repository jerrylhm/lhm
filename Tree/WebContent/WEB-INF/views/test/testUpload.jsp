<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css">
<link rel="stylesheet" href="<%=basePath%>font-awesome/css/font-awesome.min.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<title>测试上传</title>
<style>
	body{
		padding: 50px;
	}
	.div-group {
		margin-bottom: 30px;
	}
</style>
</head>
<body>
	<div class="div-group">
		<form id="form" action="<%=basePath%>client/uploadTpFile" method="post" target="frame" enctype="multipart/form-data">
			<div class="form-group">
				<input type="file" name="file">
			</div>
			<div class="form-group">
				<a class="btn btn-success" onclick="submit()">提交</a>
			</div>
			<div style="display:none">
				<input name="nodeid" value="168">
				<input name="key" value="file">
			</div>
		</form>
	</div>
	<div class="div-group">
		<iframe id="frame" name="frame"></iframe>
	</div>
	
	
<script type="text/javascript">
	function submit() {
		$("#form").submit();
	}
</script>
</body>
</html>