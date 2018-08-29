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
<title>智慧教育综合平台</title>
<link rel="shortcut icon" href="<%=basePath %>images/shortcut.ico">
<link rel="stylesheet" href="<%=basePath %>font-awesome/css/font-awesome.css">
<link rel="stylesheet" href="<%=basePath %>css/index/index-common.css">
</head>
<body background="<%=basePath%>images/home/bg.jpg">
<div class="bg-img">
	<img style="top: 50px;left: 300px;z-index:1;" src="<%=basePath%>images/home/CREATOR.png">
	<img style="top: 70px;left: 330px;" src="<%=basePath%>images/home/CREATOR-.png">
	<img style="top: 690px;left: 330px;" src="<%=basePath%>images/home/椭圆 1 拷贝.png">
	<img style="top: 80px;left: 1300px;" src="<%=basePath%>images/home/椭圆 1 拷贝.png">
	<img style="top: 80px;left: 1300px;" src="<%=basePath%>images/home/椭圆 1 拷贝.png">
	<img style="top: 390px;left: 400px;" src="<%=basePath%>images/home/椭圆 1 拷贝 2.png">
	<img style="top: 320px;left: 400px;" src="<%=basePath%>images/home/椭圆 1 拷贝 4.png">
</div>
<div class="space">
	<div class="head">
		<div class="head-nav">
			<div class="nav-item"><a class="" href="/cloud/index">首页<div></div></a></div>
			<div class="nav-item"><a class="active" href="/cloud/introduce">关于我们<div></div></a></div>
			<div class="nav-item"><a class="" href="#">新闻动态<div></div></a></div>
			<div class="nav-item"><a class="" href="#">联系我们<div></div></a></div>
		</div>
	</div>
	<div class="user-space">
		<div class="user-cark">
			<div class="user-title">
				<img class="user-logo" src="<%=basePath%>images/home/logo.png">
			</div>
			<div class="user-head">
				<img class="user-image" src="<%=basePath%>upload/head-image/2018-07-02/admin.jpg">
				<span class="user-name">Faker</span>
				<a class="user-btn btn1"><i class="fa fa-sign-out"></i></a>
				<a class="user-btn btn2"><i class="fa fa-user-circle"></i></a>
			</div>
			<div class="user-time">
				<div class="time-item yellow">17</div>
				<div class="time-item white" style="margin-left: 13px;">1月</div>
				<div class="time-item white" style="margin-left: 13px;">2017</div>
			</div>
			<div class="user-content">
				<div class="msg-title">
					<span class="title-lg">新闻动态</span>
					<span class="title-sm">NEWS</span>
				</div>
				<div class="msg-content">
					<div class="msg-item">
						<div class="msg-time">
							<span class="md">07.14</span>
							<span class="week">周一</span>
						</div>
						<p>我是你爹我是你爹我是你爹</p>
					</div>
					<div class="msg-item">
						<div class="msg-time">
							<span>07.14</span>
							<span>周一</span>
						</div>
						<p>你是我爹你是我爹你是我爹你是我爹你是我爹</p>
					</div>
					<div class="msg-item">
						<div class="msg-time">
							<span>07.14</span>
							<span>周一</span>
						</div>
						<p>我是你爹我是你爹我是你爹</p>
					</div>
				</div>
				<div class="user-foot">
					<input class="ace-input search">
					<img src="<%=basePath%>images/home/search.png">
				</div>
			</div>
		</div>
	</div>
	<div class="content">


	</div>
</div>
		<div class="foot">
			<div>
				<p>Copyright©广州市天誉创高电子科技有限公司 版权所有</p>
				<p>粤ICP备08888888号-1</p>
			</div>
		</div>
</body>
<script type="text/javascript" src="<%=basePath%>js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/index/index-common.js"></script>
</html>