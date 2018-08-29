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
<title>个人空间</title>
<link rel="shortcut icon" href="<%=basePath %>images/shortcut.ico">
<link rel="stylesheet" href="<%=basePath %>font-awesome/css/font-awesome.css">
<link rel="stylesheet" href="<%=basePath %>css/myFont.css">
<style type="text/css">
html {
	height: 100%;
}

body {    
	background: #f4f5f7;
	height: 100%;
	margin: 0;
    background: url(/cloud/images/b2.jpg);
    background-size: 100% 100%;
	-moz-user-select: none; /*火狐*/
	-webkit-user-select: none; /*webkit浏览器*/
	-ms-user-select: none; /*IE10*/
	-khtml-user-select: none; /*早期浏览器*/
	user-select: none;
}

ul {
    margin: 0;
    list-style-type: none;
    padding: 0;
}

iframe {
    border: none;
    width: 100%;
    height: 100%;
}

.head {
	opacity: .5;
    position: relative;
    width: 100%;
    height: 50px;
    background: white;
    box-shadow: 0 1px 2px rgba(0,0,0,.1);
    -webkit-transition: all 0.2s ease;
    transition: all 0.2s ease;
}

.head:hover {
    opacity: 1;
}

.content {
	position: relative;
    width: 100%;
}

.wrapper {
	display: flex;
	justify-content: space-between;
    width: 1300px;
    height: 100%;
    margin: 0 auto;
    z-index: 2;
    position: relative;
}

.flex-cloumn {
	display: flex;
	flex-direction: column;
}

.flex-row {
	display: flex;
	flex-direction: row;
}

.head-nav {
	position: relative;
	height: 100%;
	display: flex;
}

.nav-item {
	display: flex;
	margin-right: 31px;
}

.nav-item>a {
	display: block;
	padding: 15px 0px;
    position: relative;
    color: #666;
    cursor: pointer;
}

.nav-icon {
	padding: 15px 0px;
    display: inline-block;
    font-size: 20px;
    margin-right: 5px;
    color: #03A9F4;
}

.nav-cursor {
	position: absolute;
    height: 0px;
    border: 2px solid #00a1d6;
    bottom: 5px;
    -webkit-transition: all 0.2s ease;
    transition: all 0.2s ease;
}

.head-mask {
	position: absolute;
	width: 100%;
	height: 100%;
    box-shadow: 0 1px 2px rgba(0,0,0,.1);
    z-index: 1;
}

.content-head {
	width: 100%;
	min-height: 250px;
}

.float-left {
	float: left;
}

.user-space {
    width: 550px;
    height: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
}

.user-image {
	width: 150px;
    height: 150px;
    border-radius: 200px 200px;
    margin-top: 30px;
    margin-bottom: 20px;
    margin-left: 50px;
    box-shadow: 0px 0px 1px 1px #666;
}

.user-nickname {
	padding: 0 15px;
	font-size: 30px;
}

.sex-male {
	color: #2196f3;
    margin-top: 5px;
}

.sex-female {
	color: #e8779e;
    margin-top: 5px;
}

.user-setting {
	padding: 6px;
    background: white;
    border-radius: 20px 20px;
    box-shadow: 0px 0px 1px 1px #666;
    width: 16px;
    height: 16px;
    text-align: center;
    color: #2196F3;
    position: absolute;
    top: 170px;
    left: 170px;
	cursor: pointer;
    -webkit-transition: all 0.5s;
    transition: all 0.5s;
}

.user-setting:hover {
	box-shadow: 0px 0px 3px 1px #666;
}

.user-groups {
    width: 300px;
}

.user-group {
    padding: 2px 20px;
    background: #FF9800;
    color: white;
    border-radius: 5px;
    font-size: 13px;
    margin-left: 10px;
    display: inline-block;
    margin-bottom: 5px;
}

.user-classs {
    width: 600px;
}

.user-class {
    box-shadow: 0 0 1px 1px #ccc;
    padding: 2px 20px;
    background: white;
    color: #6f6d6d;
    border-radius: 5px;
    font-size: 13px;
    margin-left: 10px;
    display: inline-block;
    margin-bottom: 5px;
}

.user-group:nth-child(1) {
    background: #90d7ec;
}

.user-group:nth-child(2) {
    background: #faa755;
}

.user-group:nth-child(3) {
    background: #84bf96;
}

.user-group:nth-child(4) {
    background: #9b95c9;
}

.user-group:nth-child(5) {
    background: #f173ac;
}

.user-information {
    padding: 50px 30px;
}

.app-space {
	float: right;
	min-height: 250px;
	width: 450px;
    padding: 20px;
    box-sizing: border-box;
    display: flex;
    align-items: center;
}

.app-item {
    display: inline-block;
    background: #000;
    padding: 5px 20px;
    color: white;
   	border-radius: 50px 0px;
    opacity: .55;
    border: 2px solid white;
    margin: 7px 10px;
    -webkit-transition: all 0.5s;
    transition: all 0.5s;
}

.app-item:nth-child(1) {
	background: #f44336;
}

.app-item:nth-child(6) {
	background: #f7b245;
}

.app-item:nth-child(2) {
	background: #e85d8a;
}

.app-item:nth-child(3) {
	background: #b263c0;
}

.app-item:nth-child(4) {
	background: #5cb1ef;
}

.app-item:nth-child(5) {
	background: #7ac27d;
}
.app-item:hover {
    border-radius: 7px;
	cursor: pointer;
    opacity: 1;
}

.content-body {
	display: flex;
    width: 100%;
    margin-top: 20px;
}

.body-menu {
	margin-right: 20px;
	margin-bottom: 50px;
	min-height: 800px;
    width: 200px;
    display: flex;
    flex-direction: column;
    background: #fff;
    border: 1px solid #ccc;
}

.menu-head {
    width: 100%;
    height: 150px;
    background: url('/cloud/images/personal/personal_menu_b1.jpg')
}

.menu-head-img {
    width: 100px;
    height: 100px;
    border-radius: 50px;
    box-shadow: 0px 0px 1px 1px #666;
}

.img-space {
	position: relative;
	padding: 5px 50px;
}

.menu-ul {
-webkit-transition: all 0.3s;
transition: all 0.3s;
}

.menu-icon {
	margin-right: 10px;
}

.menu-username {
    margin-right: 5px;
    color: #fff;
    font-size: 17px;
    font-weight: 600;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    width: 80px;
    display: block;
}

.menu-ul>li {
	height: 36px;
    color: #333;
    display: unset;
}

.menu-ul>li>ul {
	overflow: hidden;
	height: 0px;
	padding-left: 20px;
	background: #f6f6f6;
}

.menu-ul>li.active>ul {
	overflow: hidden;
	padding-left: 20px;
}

.menu-ul>li>a:hover {
    color: white !important;
    background: #468fc1;
}

.menu-ul>li>a:hover>i {
    color: white !important;
}

.menu-ul>li>a {
	padding: 7px 20px;
    display: block;
    border-left: 3px solid rgba(0,0,0,0);
    cursor: pointer;
    color: #333;
    font-size: 14px;
}

.menu-highlight {
    color: white !important;
    background: #468fc1;
}

.menu-highlight>.menu-icon {
    color: white !important;
}



.menu-sub {
	float: right;
    -webkit-transition: all 0.5s;
    transition: all 0.5s;
}

.hr {
	display: block;	
    overflow: hidden;
    height: 0;
    font-size: 0;
    margin: 10px 12px;
    border-top: 1px solid #ddd;
}

.body-content {
    width: 1080px;
    height: 800px;
}
</style>
</head>
<body style="margin: 0;">
<div class="head">
	<div class="head-mask"></div>
    <div class="wrapper">
    	<div class="head-left">
    		<img src="/cloud/images/home/logo.png">
    	</div>
    	<div class="head-center">
    		<div class="head-nav">
 		    	<div class="nav-item"><i class="fa fa-home nav-icon"></i><a>首页</a></div>
    			<div class="nav-item active"><a>个人中心</a></div>
    			<div class="nav-item"><a>你妹</a></div>
    			<div class="nav-item"><a>公公吃大佬狗的十</a></div>
    			<div class="nav-cursor"></div>
    		</div>
    	</div>
    	<div class="head-right"></div>
    </div>
</div>
<div class="content">
	<div class="wrapper flex-cloumn" style="z-index: 0;">
<!-- 		<div class="content-head" style="background: url('/cloud/images/personal/personal_menu_b1.jpg')"> -->
<!-- 			<div class="user-space float-left"> -->
<!-- 				<img class="user-image" src="/cloud/upload/head-image/2018-07-02/admin.jpg"> -->
<!-- 				<i class="fa fa-cog user-setting"></i> -->
<!-- 				<div class="user-information">				 -->
<!-- 				<div style="margin-bottom: 10px;"><span class="user-nickname">李老八</span><i class="fa fa-mars sex-male"></i></div> -->
<!-- 				<div style="display: flex;"> -->
<!-- 					<i class="fa fa-star" style="color: #ffc107;padding-top: 2.5px;"></i> -->
<!-- 					<div class="user-classs"> -->
<!-- 						<span class="user-class">机械与电气工程学院电子信息工程141班</span> -->
<!-- 						<span class="user-class">机械与电气工程学院电子信息工程142班</span> -->
<!-- 						<span class="user-class">机械与电气工程学院电子信息工程143班</span> -->
<!-- 						<span class="user-class">机械与电气工程学院电子信息工程144班</span> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				<div style="display: flex;"> -->
<!-- 					<i class="fa fa-user-circle" style="color: #40c146;padding-top: 2.5px;"></i> -->
<!-- 					<div class="user-groups"> -->
<!-- 						<span class="user-group">教师组</span> -->
<!-- 						<span class="user-group">管理员组</span> -->
<!-- 						<span class="user-group">专家组</span> -->
<!-- 						<span class="user-group">专家组</span> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="app-space"> -->
<!-- 				<div> -->
<!-- 					<a class="app-item">个人课表</a> -->
<!-- 					<a class="app-item">视频资源</a> -->
<!-- 					<a class="app-item">直播预约</a> -->
<!-- 					<a class="app-item">有点东西</a> -->
<!-- 					<a class="app-item">老人与海</a> -->
<!-- 					<a class="app-item">公公与狗</a> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
		<div class="content-body">
			
			<div class="body-menu">
				<div class="menu-head">
					<div class="img-space">
						<img class="menu-head-img" src="${user.ur_image }">
						<i style="width: 10px;top: 80px;
    							height: 10px;
    							font-size: 12px;
    							left: 120px;"	 class="fa fa-cog user-setting"></i>
 						<div style="margin-top: 5px;text-align: center;display: flex;"><span class="menu-username" title="${user.ur_nickname }">${user.ur_nickname }</span>
 							<c:if test="${user.ur_sex == 0 }">
 								<i class="fa fa-mars sex-male"></i>
 							</c:if>
 							<c:if test="${user.ur_sex == 1 }">
 								<i class="fa fa-mars sex-female"></i>
 							</c:if>
 						</div>
					</div>
					
				</div>
				<ul class="menu-ul">
					<li><a class="menu-highlight"><i class="fa fa-user menu-icon"></i>主页</a></li>
					<li><a><i class="fa fa-user menu-icon"></i>个人课表</a></li>
					<p class="hr"></p>
					<li>		
						<a><i class="fa fa-user menu-icon"></i>几把<i class="fa fa-angle-right menu-sub"></i></a>		
						<ul class="menu-ul">
							<li><a><i class="fa fa-user menu-icon"></i>子菜单1</a></li>
							<li><a><i class="fa fa-user menu-icon"></i>子菜单2</a></li>
						</ul>
					</li>

				</ul>
			</div>
			<div class="body-content">
				<iframe src="/cloud/personalSpace/userEdit"></iframe>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript" src="<%=basePath%>js/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
var timer;

$('.nav-cursor').css('width', $('.nav-item.active').css('width'));
$('.nav-cursor').css('left', $('.nav-item.active').position().left);

$('.nav-item').mouseover(function() {
	$('.nav-cursor').css('width', $(this).css('width'));
	$('.nav-cursor').css('left', $(this).position().left);
	clearTimeout(timer);
})

$('.nav-item').mouseout(function() {
	timer = setTimeout(function() {
		$('.nav-cursor').css('width', $('.nav-item.active').css('width'));
		$('.nav-cursor').css('left', $('.nav-item.active').position().left);
	}, 1000);
})

$('.menu-ul>li').on('click', function(event) {
	event.stopPropagation();//阻止事件冒泡
	event.cancelBubble = true;
	$('.menu-highlight').removeClass('menu-highlight');
	$(this).children('a').addClass('menu-highlight');
	

	
	if($(this).hasClass('active')) {
		$(this).children('a').children('i:eq(1)').addClass('fa-angle-right');
		$(this).children('a').children('i:eq(1)').removeClass('fa-angle-down');
		
		$(this).removeClass('active');
		$(this).children('.menu-ul').css('height', 0);
	}else {
		$(this).children('a').children('i:eq(1)').removeClass('fa-angle-right');
		$(this).children('a').children('i:eq(1)').addClass('fa-angle-down');
		
		var height = 0;
		$(this).addClass('active');
		$(this).children('.menu-ul').children('li').each(function() {
			height = height + parseInt($(this).css('height').replace('px',''));
		})
		$(this).children('.menu-ul').css('height', height + 'px');
	}
});

function updateImage(imageUrl) {
	$('.menu-head-img').attr('src', imageUrl);
}
</script>
</html>