<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<link rel="stylesheet" href="<%=basePath %>css/index/index.css">
</head>
<body background="<%=basePath%>images/home/bg.jpg">
<div class="bg-img">
	<img style="top: 50px;left: 300px;z-index:1;" src="<%=basePath%>images/home/CREATOR.png">
	<img style="top: 70px;left: 330px;" src="<%=basePath%>images/home/CREATOR-.png">
	<img style="top: 690px;left: 330px;" src="<%=basePath%>images/home/yuan1.png">
	<img style="top: 80px;left: 1300px;" src="<%=basePath%>images/home/yuan1.png">
	<img style="top: 80px;left: 1300px;" src="<%=basePath%>images/home/yuan1.png">
	<img style="top: 390px;left: 400px;" src="<%=basePath%>images/home/yuan2.png">
	<img style="top: 320px;left: 400px;" src="<%=basePath%>images/home/yuan4.png">
</div>
<div class="space">
	<div class="head">
		<div class="head-nav">
			<div class="nav-item"><a class="active" href="/cloud/index">首页<div></div></a></div>
			<div class="nav-item"><a class="" href="/cloud/introduce">关于我们<div></div></a></div>
			<div class="nav-item"><a class="" href="#">新闻动态<div></div></a></div>
			<div class="nav-item"><a class="" href="#">联系我们<div></div></a></div>
		</div>
	</div>
	<div class="user-space">
		<div class="user-cark">
			<div class="user-title">
				<img class="user-logo" src="<%=basePath%>images/home/logo.png">
			</div>
			<div class="user-head" data-userid="${user.ur_id }">
				<c:if test="${user != null}">
					<img class="user-image" src="<%=basePath%>${user.ur_image }">
					<span class="user-name">${user.ur_nickname }</span>
					<a class="user-btn btn1" href="javascript:logout()"><i class="fa fa-sign-out"></i></a>
					<a class="user-btn btn2" href="/cloud/personalSpace"><i class="fa fa-user-circle"></i></a>
				</c:if>
				<c:if test="${user == null}">
					<img class="user-image" src="<%=basePath%>images/unlogin.jpg">
					<span class="user-name" style="cursor: pointer;" onclick="window.location.href='/cloud/login'">请登录</span>
					<a class="user-btn btn3" href="/cloud/login"><i class="fa fa-sign-in"></i></a>
				</c:if>

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
		<div class="content-items">
			<div class="content-colum">
				<div class="colum-2 colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b1.png">
					<div class="text-colum">
						<span class="FZCYSJW">丰富的学习资源</span>					
						<span class="FZCYSJW">便捷的交流学习互动</span>
					</div>
				</div>
				<div class="colum-2 colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b2.png">
					<div class="text-colum text-right">
						<span class="TRENDS cn">点播</span>					
						<span class="TRENDS en">On demand</span>
					</div>
				</div>
				<div class="colum colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b3.png">
					<div class="text-colum text-left-top">
						<span class="TRENDS cn">监控</span>					
						<span class="TRENDS en">Monitoring</span>
					</div>
				</div>
			</div>
			<div class="content-colum">
				<div class="colum colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b4.png">
					<div class="text-colum text-left-top">
						<span class="TRENDS cn">直播平台</span>					
						<span class="TRENDS en">Live broadcast</span>
					</div>
				</div>
				<div class="colum colum-animation">
					<img class="colum-bg" src="/cloud/images/home/b5.png">
					<div class="text-colum text-left-center">
						<span class="FZCYSJW">课表轻松管理</span>					
						<span class="FZCYSJW">课程批量预约</span>
						<span class="FZCYSJW">教师实况直播</span>
					</div>
				</div>
			</div>
			<div class="content-colum">
				<div class="colum colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b6.png">
					<div class="text-colum text-left-bottom">
						<span class="TRENDS cn">文档中心</span>					
						<span class="TRENDS en">Document center</span>
					</div>
				</div>
				<div class="content-row">
					<div class="colum-4 colum-animation marginR20">
					<img class="colum-bg" src="/cloud/images/home/b7.png">
						<div class="text-colum text-right-bottom">
							<span class="TRENDS cn">照片录入</span>					
							<span class="TRENDS en">Photo entry</span>
						</div>
					</div>
					<div class="colum-4 colum-animation">
					<img class="colum-bg" src="/cloud/images/home/b8.png">
						<div class="text-colum">
							<span class="TRENDS cn">试题库</span>					
							<span class="TRENDS en">Item bonk</span>
						</div>
					</div>	
				</div>
				<div class="colum-2 colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b9.png">
					<div class="text-colum text-right">
						<span class="TRENDS cn" style="font-size: 26px;">互动问答</span>					
						<span class="TRENDS en" style="font-size: 20px;">Q&A interoction</span>
					</div>
				</div>
			</div>
			<div class="content-colum">
				<div class="content-row">
					<div class="colum-4 colum-animation marginR20">
					<img class="colum-bg" src="/cloud/images/home/b10.png">
						<div class="text-colum text-left-bottom">
							<span class="TRENDS cn">电子班牌</span>					
							<span class="TRENDS en" >Class card</span>
						</div>
					</div>
					<div class="colum-4 colum-animation">
					<img class="colum-bg" src="/cloud/images/home/b11.png">
						<div class="text-colum">
							<span class="TRENDS cn">会议预约</span>					
							<span class="TRENDS en" >Appointment</span>
						</div>
					</div>	
				</div>
				<div class="colum colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b12.png">
					<div class="text-colum text-left-top">
						<span class="TRENDS cn">名师讲堂</span>					
						<span class="TRENDS en">Teacher lecture</span>
					</div>
				</div>
				<div class="colum-2 colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b13.png">
					<div class="text-colum text-right-center">
						<span class="FZCYSJW">集优质课堂</span>					
						<span class="FZCYSJW">构名师专辑</span>
					</div>
				</div>
			</div>
			<div class="content-colum">
				<div class="colum colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b14.png">
					<div class="text-colum text-left-center">
						<span class="FZCYSJW">优质的学习资料</span>					
						<span class="FZCYSJW">灵活选择</span>
						<span class="FZCYSJW">方便实用</span>
					</div>
				</div>
				<div class="colum-2 colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b15.png">
					<div class="text-colum text-left-bottom">
						<span class="TRENDS cn" style="font-size: 18px;">数据可视化信息屏</span>					
						<span class="TRENDS en" style="font-size: 16px;">Data visualization</span>
					</div>
				</div>
				<div class="colum-2 colum-animation">
				<img class="colum-bg" src="/cloud/images/home/b16.png">
					<div class="text-colum text-right">
						<span class="TRENDS cn">能源</span>					
						<span class="TRENDS en">Energy</span>
					</div>
				</div>
			</div>
		</div>
			<div class="content-foot">
				<div class="foot-nav">
					<a class="button-cricle active">全部</a>
					<a class="button-cricle">资源</a>
					<a class="button-cricle">照片</a>
					<a class="button-cricle">可视化</a>
					<a class="button-cricle">集控</a>
				</div>
			</div>
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
<script type="text/javascript" src="<%=basePath %>layer/layer.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/index/index-common.js"></script>
<script type="text/javascript" src="<%=basePath%>js/index/index.js"></script>
</html>