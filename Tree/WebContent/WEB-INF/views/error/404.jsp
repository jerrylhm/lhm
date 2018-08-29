<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>页面未找到</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico"> <link href="<%=basePath%>hAdmin/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="<%=basePath%>hAdmin/css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <link href="<%=basePath%>hAdmin/css/animate.css" rel="stylesheet">
    <link href="<%=basePath%>hAdmin/css/style.css?v=4.1.0" rel="stylesheet">

</head>

<body class="gray-bg">


    <div class="middle-box text-center animated fadeInDown">
        <h1>404</h1>
        <h3 class="font-bold">页面未找到！</h3>

        <div class="error-desc">
            抱歉，页面好像去火星了~
        </div>
    </div>

    <!-- 全局js -->
    <script src="<%=basePath%>hAdmin/js/jquery.min.js?v=2.1.4"></script>
    <script src="<%=basePath%>hAdmin/js/bootstrap.min.js?v=3.3.6"></script>

    
    

</body>

</html>
