<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <title>云平台后台管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Le styles -->
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/loader-style.css">
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="assets/js/progress-bar/number-pb.css">
    <style type="text/css">
	    canvas#canvas4 {
	        position: relative;
	        top: 20px;
	    }
	    body {
	    	background-size: 100% 100%;
	    	background-repeat: repeat;
	    	background-attachment: fixed;
	    }
	    
	    .title-menu-left .menu-title {
	    	letter-spacing: 1px;
	    	font-size: 12px;
	    	height: 20px;
	    	line-height:20px;
	    }
	    .menu-icon {
	    	top: 0 !important;
	    }
	    
	    .percent2:after {
	    	content: "%"
	    }
	    
    </style>
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    <!-- Fav and touch icons -->
    <link rel="shortcut icon" href="images/shortcut.ico">
    <link rel="stylesheet" href="css/animate.css">
</head>
<body>
    <!-- Preloader -->
    <div id="preloader">
        <div id="status">&nbsp;</div>
    </div>
    <!-- TOP NAVBAR -->
    <nav role="navigation" class="navbar navbar-static-top">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button data-target="#bs-example-navbar-collapse-1" data-toggle="collapse" class="navbar-toggle" type="button">
                    <span class="entypo-menu"></span>
                </button>
                <button class="navbar-toggle toggle-menu-mobile toggle-left" type="button">
                    <span class="entypo-list-add"></span>
                </button>
                <div id="logo-mobile" class="visible-xs" >
                   <h1 >CREATOR<span>v1.0</span></h1>
                </div>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div id="bs-example-navbar-collapse-1" class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i style="font-size:20px;" class="icon-conversation"></i><div class="noft-red">23</div></a>
                        
                        <!-- 消息下拉框，已屏蔽 -->
                        <!-- <ul style="margin: 11px 0 0 9px;" role="menu" class="dropdown-menu dropdown-wrap">
                            <li>
                                <a href="#">
                                    <img alt="" class="img-msg img-circle" src="http://api.randomuser.me/portraits/thumb/men/1.jpg">Jhon Doe <b>Just Now</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">
                                    <img alt="" class="img-msg img-circle" src="http://api.randomuser.me/portraits/thumb/women/1.jpg">Jeniffer <b>3 Min Ago</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">
                                    <img alt="" class="img-msg img-circle" src="http://api.randomuser.me/portraits/thumb/men/2.jpg">Dave <b>2 Hours Ago</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">
                                    <img alt="" class="img-msg img-circle" src="http://api.randomuser.me/portraits/thumb/men/3.jpg"><i>Keanu</i>  <b>1 Day Ago</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">
                                    <img alt="" class="img-msg img-circle" src="http://api.randomuser.me/portraits/thumb/men/4.jpg"><i>Masashi</i>  <b>2 Mounth Ago</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <div>See All Messege</div>
                            </li>
                        </ul> -->
                    </li>
                    <li>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i style="font-size:19px;" class="icon-warning tooltitle"></i><div class="noft-green">5</div></a>
                        <!-- 警告下拉框，已屏蔽 -->
                        <!-- <ul style="margin: 12px 0 0 0;" role="menu" class="dropdown-menu dropdown-wrap">
                            <li>
                                <a href="#">
                                    <span style="background:#DF2135" class="noft-icon maki-bus"></span><i>From Station</i>  <b>01B</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">
                                    <span style="background:#AB6DB0" class="noft-icon maki-ferry"></span><i>Departing at</i>  <b>9:00 AM</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">
                                    <span style="background:#FFA200" class="noft-icon maki-aboveground-rail"></span><i>Delay for</i>  <b>09 Min</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">
                                    <span style="background:#86C440" class="noft-icon maki-airport"></span><i>Take of</i>  <b>08:30 AM</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">
                                    <span style="background:#0DB8DF" class="noft-icon maki-bicycle"></span><i>Take of</i>  <b>08:30 AM</b>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <div>See All Notification</div>
                            </li>
                        </ul> -->
                    </li>
                    <li><a href="#"><i data-toggle="tooltip" data-placement="bottom" title="帮助" style="font-size:20px;" class="icon-help tooltitle"></i></a>
                    </li>
                </ul>
                <div id="nt-title-container" class="navbar-left running-text visible-lg">
                    <ul class="date-top">
                        <li class="entypo-calendar" style="margin-right:5px"></li>
                        <li id="li_date"></span></li>
                    </ul>
                    <ul id="digital-clock" class="digital">
                        <li class="entypo-clock" style="margin-right:5px"></li>
                        <li id="hour"></li>
                        <li>:</li>
                        <li id="min"></li>
                        <li>:</li>
                        <li id="sec"></li>
                    </ul>
                    <ul id="nt-title">
                        <li><i class="wi-day-lightning"></i><span class="wi-day-span">&#160;&#160;北京&#160;
                            <b>30</b>℃&#160;; 多云</span>
                        </li>
                        <li><i class="wi-day-lightning"></i><span class="wi-day-span">&#160;&#160;上海&#160;
                            <b>28</b>℃&#160;; 多云</span>
                        </li>
                        <li><i class="wi-day-lightning"></i><span class="wi-day-span">&#160;&#160;广州&#160;
                            <b>34</b>℃&#160;; 多云</span>
                        </li>
                        <li><i class="wi-day-lightning"></i><span class="wi-day-span">&#160;&#160;天津&#160;
                            <b>35</b>℃&#160;; 多云</span>
                        </li>
                        <li><i class="wi-day-lightning"></i><span class="wi-day-span">&#160;&#160;杭州&#160;
                            <b>30</b>℃&#160;; 多云</span>
                        </li>
                    </ul>
                </div>
                <ul style="margin-right:0;" class="nav navbar-nav navbar-right">
                    <li>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <img alt="" class="admin-pic img-circle" src="${userImage }">你好！${userName }<b class="caret"></b>
                        </a>
                        <ul style="margin-top:14px;" role="menu" class="dropdown-setting dropdown-menu">
                            <li>
                                <a href="#">
                                    <span class="fa fa-cloud fa-fw"></span>&#160;&#160;平台首页</a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="fa fa-star fa-fw"></span>&#160;&#160;我的空间</a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="fa fa-lock fa-fw"></span>&#160;&#160;修改密码</a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="/cloud/admin/logout">
                                    <span class="fa fa-power-off fa-fw"></span>&#160;&#160; 注销</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="icon-gear"></span>&#160;&#160;设置</a>
                        <ul role="menu" class="dropdown-setting dropdown-menu">
                            <li class="theme-bg">
                                <div id="button-bg"></div>
                                <div id="button-bg2"></div>
                                <div id="button-bg3"></div>
                                <div id="button-bg5"></div>
                                <div id="button-bg6"></div>
                                <div id="button-bg7"></div>
                                <div id="button-bg8"></div>
                                <div id="button-bg9"></div>
                                <div id="button-bg10"></div>
                                <div id="button-bg11"></div>
                                <div id="button-bg12"></div>
                                <div id="button-bg13"></div>
                            </li>
                        </ul>
                    </li>
                    <li class="hidden-xs">
                        <a class="toggle-left" href="#">
                            <span style="font-size:20px;" class="entypo-list-add"></span>
                        </a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
    <!-- /END OF TOP NAVBAR -->
    <!-- SIDE MENU -->
    <div id="skin-select">
        <div id="logo">
         <h1 style="text-align: center">CREATOR<span>v1.0</span></h1>
        </div>
        <a id="toggle">
            <span class="entypo-menu"></span>
        </a>
        <div class="dark">
            <form action="#">
                <span>
                    <input type="text" name="search" value="" class="search rounded id_search" placeholder="搜索..." autofocus="">
                </span>
            </form>
        </div>
        <div class="search-hover">
            <form id="demo-2">
                <input type="search" placeholder="搜索..." class="id_search">
            </form>
        </div>
        <div class="skin-part">
            <div id="tree-wrap">
                <div class="side-bar">
                    <ul class="topnav menu-left-nest">
                        <li>
                            <a href="#" style="border-left:0px solid!important;" class="title-menu-left">
                                <span class="menu-title">平台菜单</span>
                                <i data-toggle="tooltip" class="entypo-cog pull-right config-wrap"></i>
                            </a>
                        </li>
                        <li>
                            <a class="tooltip-tip ajax-load" href="#" title="用户管理">
                                <i class="fa fa-users menu-icon"></i>
                                <span>用户管理</span>
                            </a>
                            <ul>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" onclick="showContent('<%=basePath %>admin/userManage','后台管理首页')" title="系统用户"><i class="fa fa-user"></i><span>系统用户</span></a>
                                </li>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" onclick="showContent('<%=basePath %>admin/userCheck','用户审核')" title="用户审核"><i class="fa fa-check"></i><span>用户审核</span></a>
                                </li>
                                <li>
                                	 <a class="tooltip-tip2 ajax-load"  title="用户组管理" onclick="showContent('<%=basePath %>admin/userGroupManage','用户组管理')"><i class="fa fa-user-circle"></i><span>用户组管理</span></a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a class="tooltip-tip ajax-load" href="#" title="课表管理">
                                <i class="fa fa-calendar menu-icon"></i>
                                <span>课表管理</span>
                            </a>
                            <ul>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" target="iframe" onclick="showContent('<%=basePath %>term','学期管理')" title="学期管理"><i class="fa fa-cog"></i><span>学期管理</span></a>
                                </li>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" target="iframe" onclick="showContent('<%=basePath %>timetable','作息表')" title="作息表"><i class="fa fa-clock-o"></i><span>作息表</span></a>
                                </li>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" target="iframe" onclick="showContent('<%=basePath %>studenttable', '课表管理')" title="课表管理"><i class="fa fa-table"></i><span>课表管理</span></a>
                                </li>
                            </ul>
                        </li>
                          <li>
                            <a class="tooltip-tip ajax-load" href="#" title="组织机构">
                                <i class="fa fa-sitemap menu-icon"></i>
                                <span>组织机构</span>
                            </a>
                            <ul>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" onclick="showContent('<%=basePath %>organization/manageIndex','组织管理')" title="组织管理"><i class="fa fa-bar-chart"></i><span>组织管理</span></a>
                                </li>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" onclick="showContent('<%=basePath %>organization/addressIndex','场所管理')" title="场所管理"><i class="fa fa-home"></i><span>场所管理</span></a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a class="tooltip-tip ajax-load" href="#" title="学科管理">
                                <i class="fa fa-navicon menu-icon"></i>
                                <span>学科管理</span>
                            </a>
                            <ul>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" onclick="showContent('<%=basePath %>subject','学科管理')" title="学科管理"><i class="fa fa-book"></i><span>学科管理</span></a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <ul class="topnav menu-left-nest">
                        <li>
                            <a href="#" style="border-left:0px solid!important;" class="title-menu-left">
                                <span class="menu-title">资源菜单</span>
                                <i data-toggle="tooltip" class="entypo-cog pull-right config-wrap"></i>
                            </a>
                        </li>
                        <li>
                            <a class="tooltip-tip ajax-load" href="#" title="资源管理">
                                <i class="fa fa-book menu-icon"></i>
                                <span>资源管理</span>
                            </a>
                            <ul>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" href="blog-list.html" title="视频中心"><i class="fa fa-video-camera"></i><span>视频中心</span></a>
                                </li>
                            </ul>
                        </li>
                        
                        <li>
                            <a class="tooltip-tip ajax-load" href="#" title="直播课堂">
                                <i class="fa fa-video-camera menu-icon"></i>
                                <span>直播课堂</span>
                            </a>
                            <ul>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" href="blog-list.html" title="直播管理"><i class="fa fa-tablet"></i><span>直播管理</span></a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <ul id="menu-showhide" class="topnav menu-left-nest">
                        <li>
                            <a href="#" style="border-left:0px solid!important;" class="title-menu-left">
                                <span class="menu-title">集控菜单</span>
                                <i data-toggle="tooltip" class="entypo-cog pull-right config-wrap"></i>
                            </a>
                        </li>
                        <li>
                            <a class="tooltip-tip" href="#" title="集控管理">
                                <i class="fa fa-sliders menu-icon"></i>
                                <span>集控管理</span>
                            </a>
                            <ul>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" href="element.html" title=""><i class="fa fa-tree"></i><span>节点管理</span></a>
                                </li>
                            </ul>
                        </li>
                         <li>
                            <a class="tooltip-tip" href="#" title="UI Element">
                                <i class="fa fa-tv menu-icon"></i>
                                <span>监控管理</span>
                            </a>
                            <ul>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" href="element.html" title="监控管理"><i class="fa fa-tv"></i><span>监控管理</span></a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <ul id="menu-showhide" class="topnav menu-left-nest">
                        <li>
                            <a href="#" style="border-left:0px solid!important;" class="title-menu-left">
                                <span class="menu-title">考勤菜单</span>
                                <i data-toggle="tooltip" class="entypo-cog pull-right config-wrap"></i>
                            </a>
                        </li>
                        <li>
                            <a class="tooltip-tip" href="#" title="录入管理">
                                <i class="fa fa-photo menu-icon"></i>
                                <span>录入管理</span>
                            </a>
                        </li>
                    </ul>
                    <div class="side-dash">
                    	<!--  
                        <h3>
                            <span>Device</span>
                        </h3>
                        <ul class="side-dashh-list">
                            <li>Avg. Traffic
                                <span>25k<i style="color:#44BBC1;" class="fa fa-arrow-circle-up"></i>
                                </span>
                            </li>
                            <li>Visitors
                                <span>80%<i style="color:#AB6DB0;" class="fa fa-arrow-circle-down"></i>
                                </span>
                            </li>
                            <li>Convertion Rate
                                <span>13m<i style="color:#19A1F9;" class="fa fa-arrow-circle-up"></i>
                                </span>
                            </li>
                        </ul>
                        <h3>
                            <span>Traffic</span>
                        </h3>
                        <ul class="side-bar-list">
                            <li>Avg. Traffic
                                <div class="linebar">5,7,8,9,3,5,3,8,5</div>
                            </li>
                            <li>Visitors
                                <div class="linebar2">9,7,8,9,5,9,6,8,7</div>
                            </li>
                            <li>Convertion Rate
                                <div class="linebar3">5,7,8,9,3,5,3,8,5</div>
                            </li>
                        </ul>
                        -->
                        <h3>
                            <span>访问次数</span>
                        </h3>
                        <div id="g1" style="height:180px" class="gauge"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- END OF SIDE MENU -->
    <!--  PAPER WRAP -->
    <div class="wrap-fluid">
        <div class="container-fluid paper-wrap bevel tlbr animated fadeIn">
            <!-- CONTENT -->
            <!--TITLE -->
             <div class="row">
                <div id="paper-top">
                    <div class="col-lg-3 col-sm-5">
                        <h2 class="tittle-content-header">
                            <i class="icon-window"></i>
                            <span id="title">后台管理首页
                            </span>
                        </h2>
                    </div>
                    <div class="col-lg-7 col-sm-7">
                        <div class="devider-vertical visible-lg"></div>
                        <div class="tittle-middle-header">
                            <div class="alert">
                                <button type="button" class="close" data-dismiss="alert">×</button>
                                <span class="tittle-alert entypo-info-circled"></span>
                              	  欢迎回来,&nbsp;
                                <strong>admin!</strong>&nbsp;&nbsp;
                            </div>
                        </div>
                    </div>
                 </div>
              </div>
            <div>
            	<iframe id="iframe" name="iframe" src="<%=basePath %>admin/indexContent" style="width:100%;min-width:1000px;height:1000px;border: none;" scrolling="auto" seamless></iframe>
            </div>
            <!-- FOOTER -->
            <div class="footer-space"></div>
            <div id="footer">
                <div class="devider-footer-left"></div>
                <div class="time">
                    <p id="spanDate">
                    <p id="clock">
                </div>
                <div class="copyright">Copyright © 2011 - 2017 , All Rights Reserved . 管理云平台
                
            </div>
            <!-- / END OF FOOTER -->
        </div>
    </div>
    <!--  END OF PAPER WRAP -->
    <!-- RIGHT SLIDER CONTENT -->
    <div class="sb-slidebar sb-right">
        <div class="right-wrapper">
            <div class="row">
                <h3>
                    <span><i class="entypo-gauge"></i>&nbsp;&nbsp;数据统计</span>
                </h3>
                <div class="col-lg-12">
                    <div class="widget-knob">
                        <span class="chart" style="position:relative" data-percent="46">
                            <span class="percent"></span>
                        </span>
                    </div>
                    <div class="widget-def">
                        <b>在线时长</b>
                        <br>
                        <i>在线时长占比46%</i>
                    </div>
                    <div class="widget-knob">
                        <span class="speed-car" style="position:relative" data-percent="50">
                            <span class="percent2"></span>
                        </span>
                    </div>
                    <div class="widget-def">
                        <b>在线人数</b>
                        <br>
                        <i>在线人数占比50%</i>
                    </div>
                    <div class="widget-knob">
                        <span class="overall" style="position:relative" data-percent="50">
                            <span class="percent3"></span>
                        </span>
                    </div>
                    <div class="widget-def">
                        <b>离线人数</b>
                        <br>
                        <i>离线人数占比50%</i>
                    </div>
                </div>
            </div>
        </div>
        <!--  
        <div style="margin-top:0;" class="right-wrapper">
            <div class="row">
                <h3>
                    <span><i class="entypo-chat"></i>&nbsp;&nbsp;CHAT</span>
                </h3>
                <div class="col-lg-12">
                    <span class="label label-warning label-chat">Online</span>
                    <ul class="chat">
                        <li>
                            <a href="#">
                                <span>
                                    <img alt="" class="img-chat img-circle" src="http://api.randomuser.me/portraits/thumb/men/20.jpg">
                                </span><b>Dave Junior</b>
                                <br><i>Last seen : 08:00 PM</i>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <span>
                                    <img alt="" class="img-chat img-circle" src="http://api.randomuser.me/portraits/thumb/men/21.jpg">
                                </span><b>Kenneth Lucas</b>
                                <br><i>Last seen : 07:21 PM</i>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <span>
                                    <img alt="" class="img-chat img-circle" src="http://api.randomuser.me/portraits/thumb/men/22.jpg">
                                </span><b>Heidi Perez</b>
                                <br><i>Last seen : 05:43 PM</i>
                            </a>
                        </li>
                    </ul>
                    <span class="label label-chat">Offline</span>
                    <ul class="chat">
                        <li>
                            <a href="#">
                                <span>
                                    <img alt="" class="img-chat img-offline img-circle" src="http://api.randomuser.me/portraits/thumb/men/23.jpg">
                                </span><b>Dave Junior</b>
                                <br><i>Last seen : 08:00 PM</i>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <span>
                                    <img alt="" class="img-chat img-offline img-circle" src="http://api.randomuser.me/portraits/thumb/women/24.jpg">
                                </span><b>Kenneth Lucas</b>
                                <br><i>Last seen : 07:21 PM</i>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <span>
                                    <img alt="" class="img-chat img-offline img-circle" src="http://api.randomuser.me/portraits/thumb/men/25.jpg">
                                </span><b>Heidi Perez</b>
                                <br><i>Last seen : 05:43 PM</i>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <span>
                                    <img alt="" class="img-chat img-offline img-circle" src="http://api.randomuser.me/portraits/thumb/women/25.jpg">
                                </span><b>Kenneth Lucas</b>
                                <br><i>Last seen : 07:21 PM</i>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <span>
                                    <img alt="" class="img-chat img-offline img-circle" src="http://api.randomuser.me/portraits/thumb/men/26.jpg">
                                </span><b>Heidi Perez</b>
                                <br><i>Last seen : 05:43 PM</i>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            
        </div>
        -->
    </div>
    <!-- END OF RIGHT SLIDER CONTENT-->
    <script type="text/javascript" src="assets/js/jquery.js"></script>
    <script src="assets/js/progress-bar/src/jquery.velocity.min.js"></script>
    <script src="assets/js/progress-bar/number-pb.js"></script>
    <script src="assets/js/progress-bar/progress-app.js"></script>
    <!-- MAIN EFFECT -->
    <script type="text/javascript" src="assets/js/preloader.js"></script>
    <script type="text/javascript" src="assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="assets/js/app.js"></script>
    <script type="text/javascript" src="assets/js/load.js"></script>
    <script type="text/javascript" src="assets/js/main.js"></script>
    <!-- GAGE -->
    <!-- <script type="text/javascript" src="assets/js/chart/jquery.flot.js"></script>
    <script type="text/javascript" src="assets/js/chart/jquery.flot.resize.js"></script> -->
    <!-- <script type="text/javascript" src="assets/js/chart/realTime.js"></script> -->
    <script type="text/javascript" src="assets/js/speed/canvasgauge-coustom.js"></script>
    <script type="text/javascript" src="assets/js/countdown/jquery.countdown.js"></script>
    <script src="assets/js/jhere-custom.js"></script>
<script type="text/javascript">
	const weatherAPI = "http://api.map.baidu.com/telematics/v3/weather";  //天气请求接口
    $(document).ready(function() {
    	console.log(getDate());
    	$("#li_date").html(getDate());
    	
    	//获取城市天气
    	getWeathers();
    	setInterval(function() {
    		refreshTime();
    	}, 1000);
    });
    
    //获取当前时间，返回格式为XXXX年XX月XX日 星期X
    function getDate() {
    	var date = new Date();
    	var year = date.getFullYear();
    	var month = date.getMonth();
    	month++;
    	if(month < 10) {
    		month = "0" + month;
    	}
    	var day = date.getDate();
    	if(day < 10) {
    		day = "0" + day;
    	}
    	var days = ["天","一","二","三","四","五","六"];
    	var week = date.getDay();
    	
    	return year + "年" + month + "月" + day + "日 " + "星期" + days[week];
    }
    
    //刷新当前时间
    function refreshTime() {
    	var date = new Date();
    	var hour = date.getHours();
    	var min = date.getMinutes();
    	var sec = date.getSeconds();
    	
    	if(hour < 10) {
    		hour = "0" + hour;
    	}
    	
    	if(min < 10) {
    		min = "0" + min;
    	}
    	if(sec < 10) {
    		sec = "0" + sec;
    	}
    	
    	$("#hour").html(hour);
    	$("#min").html(min);
    	$("#sec").html(sec);
    }
    
    var citys = ["北京","上海","广州","天津","杭州","重庆","长沙"];       //城市列表
    //设置城市天气
    function getWeathers() {
    	var index = 0;
    	$(".wi-day-span").each(function() {
    		var that = this;
    		$.ajax({
    			type: "GET",
    			url: weatherAPI,
    			data: {
    				location: citys[index],
    				output: "json",
    				ak: "EGgzZ22dsboWQEcPQ6KDQLknQd3YkkkP"
    			},
    			dataType: "jsonp",
    			success: function(data) {
    				//console.log(data);
    				if(data.status == "success") {
    					var weather = data.results[0].weather_data[0];
    					$(that).html("&#160;&#160;"+ data.results[0].currentCity +"&#160;"+ weather.temperature +"&#160;; &#160" + weather.weather);
    				}
    			}
    		});
    		index ++;
    	})
    }
    //设置iframe跳转的链接和设置标题名称
    function showContent(src,title) {
    	console.log(src);
    	console.log(title);
    	$(".paper-wrap").hide();
    	$("#title").text(title);
    	$("#iframe").attr("src",src);
    	$(".paper-wrap").fadeIn("fast");
    }
</script>
</body>
</html>