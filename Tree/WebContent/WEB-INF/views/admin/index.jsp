<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">

    <title> 后台管理主页</title>

    <meta name="keywords" content="">
    <meta name="description" content="">

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <link rel="shortcut icon" href="<%=basePath%>images/shortcut.ico"> 
    <link href="<%=basePath %>admin/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=basePath%>font-awesome/css/font-awesome.min.css">
    <link href="<%=basePath %>admin/css/animate.css" rel="stylesheet">
    <link href="<%=basePath %>admin/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=basePath%>css/sweetalert.css" >
    <style>
    	.modal-container {
			padding : 10px 20px;
		}
    </style>
</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div class="dropdown profile-element">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                                    <span class="block m-t-xs" style="font-size:20px;">
                                        <img style="width:180px;height:41px" src="<%=basePath%>images/logodefault.png">
                                    </span>
                                </span>
                            </a>
                        </div>
                        <div class="logo-element">CREATOR
                        </div>
                    </li>
                    <li class="hidden-folded padder m-t m-b-sm text-muted text-xs">
                        <span class="ng-scope">管理</span>
                    </li>
                    <li class="line dk"></li>
                     <li>
                        <a class="J_menuItem" href="<%=basePath %>tree/index" target="iframe">
                            <i class="fa fa-sitemap fa-lg"></i>
                            <span class="nav-label">项目管理</span>
                        </a>
                    </li>
                    <c:if test="${USERTYPE == 1 }">
                    	<li>
	                        <a class="J_menuItem" href="<%=basePath %>admin/userManage" target="iframe">
	                            <i class="fa fa-user-circle fa-lg"></i>
	                            <span class="nav-label">用户管理</span>
	                        </a>
	                    </li>	                    
                    </c:if>
                    <li>
                        <a class="J_menuItem" href="<%=basePath %>template/index" target="iframe">
                            <i class="fa fa-clone fa-lg"></i>
                            <span class="nav-label">模板管理</span>
                        </a>
                    </li>
                    <li>
                        <a class="J_menuItem" href="<%=basePath %>scene/index" target="iframe">
                            <i class="fa fa-gears fa-lg"></i>
                            <span class="nav-label">场景管理</span>
                        </a>
                    </li>
                    <li>
                       <a class="J_menuItem" href="<%=basePath %>admin/userGroupManage" target="iframe">
                           <i class="fa fa-group fa-lg"></i>
                           <span class="nav-label">用户组管理</span>
                       </a>
                    </li>
                    <!--  <li><a class="J_menuItem" href="<%= basePath %>admin/auditUserManage" target="iframe">用户审核</a> -->
                   	<li>
                   		<a class="J_menuItem" href="<%= basePath %>meetingManagement/index" target="iframe">
                   			<i class="fa fa-handshake-o fa-lg"></i>
                           <span class="nav-label">会议审核</span>
                   		</a>
                    <li class="line dk"></li>
                    <!-- 
                     <li>
	                   <a href="#">
	                       <i class="fa fa fa-bar-chart-o"></i>
	                       <span class="nav-label">统计图表</span>
	                       <span class="fa arrow"></span>
	                   </a>
	                   <ul class="nav nav-second-level">
	                       <li>
	                           <a class="J_menuItem" href="graph_echarts.html">百度ECharts</a>
	                       </li>
	                       <li>
	                           <a class="J_menuItem" href="graph_flot.html">Flot</a>
	                       </li>
	                       <li>
	                           <a class="J_menuItem" href="graph_morris.html">Morris.js</a>
	                       </li>
	                       <li>
	                           <a class="J_menuItem" href="graph_rickshaw.html">Rickshaw</a>
	                       </li>
	                       <li>
	                           <a class="J_menuItem" href="graph_peity.html">Peity</a>
	                       </li>
	                       <li>
	                           <a class="J_menuItem" href="graph_sparkline.html">Sparkline</a>
	                       </li>
	                       <li>
	                           <a class="J_menuItem" href="graph_metrics.html">图表组合</a>
	                       </li>
	                   </ul>
	               </li>
	                -->
                </ul>
               
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom">
                <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                    <ul class="nav navbar-top-links navbar-right" style="margin-right:80px;">
                        <li class="dropdown">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#" style="padding:8px 10px;">
                                <img id="img_user" class="img-circle" style="width:35px;height:35px" src="<%=basePath%>images/user.gif">
                            </a>
                            
                            <ul class="dropdown-menu dropdown-alerts " style="width:200px;right:auto;left:-40px;">
                                <li>
                                    <a style="padding-left:40px" title="修改密码" onclick="showPswModal()">
                                    	<i class="fa fa-lock fa-fw fa-lg"></i> 修改密码
                                    </a>
                                </li>
                                <li class="divider"></li>
                                <li>
                                	<a href="<%=basePath %>login/logout" style="padding-left:40px" title="注销">
                                		<i class="fa fa-sign-out fa-fw fa-lg"></i >注销
                                	</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                    		<span>欢迎您！${USERNAME}</span>
                    	</li>
                    </ul>
                </nav>
            </div>
            <div class="row J_mainContent" id="content-main">
            	<iframe id="iframe" name="iframe" width="100%" height="100%" src="<%=basePath %>tree/index" frameborder="0"  seamless></iframe>
            </div>
        </div>
        <!--右侧部分结束-->
    </div>
    
    <!-- 修改密码模态框 -->
	<div class="modal fade" id="pswModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button class="close" data-dismiss="modal">&times;</button>
					<h5 class="modal-title">修改密码</h5>
				</div>
				<div class="modal-body">
					<div class="modal-container">
						<form class="form-horizontal">
							<div class="form-group">
								<label class="control-label col-sm-3">原密码：</label>
								<div class="col-sm-8">
									<input type="password" class="form-control" id="input_old_psw" placeholder="原来的密码">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3">新密码：</label>
								<div class="col-sm-8">
									<input type="password" class="form-control" id="input_new_psw" placeholder="5~16数字英文组合">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3">确认密码：</label>
								<div class="col-sm-8">
									<input type="password" class="form-control" id="input_confirm_psw" placeholder="确认密码">
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-primary" onclick="updatePsw()"><i class="fa fa-check fa-fw"></i>修改</button>
					<button class="btn btn-default" data-dismiss="modal"><i class="fa fa-close fa-fw"></i>取消</button>
				</div>
			</div>
		</div>
	</div>

    <!-- 全局js -->
    <script src="<%=basePath %>admin/js/jquery.min.js"></script>
    <script src="<%=basePath %>admin/js/bootstrap.min.js"></script>
    <script src="<%=basePath %>admin/js/jquery.metisMenu.js"></script>
    <script src="<%=basePath %>admin/js/jquery.slimscroll.min.js"></script>
    <script src="<%=basePath %>admin/js/layer.min.js"></script>
    <script src="<%=basePath%>js/sweetalert-dev.js"></script>

    <!-- 自定义js -->
    <script src="<%=basePath %>admin/js/hAdmin.js"></script>

    <!-- 第三方插件 -->
    <script src="<%=basePath %>admin/js/pace.min.js"></script>
    
    <script type="text/javascript">
    	
    	$(document).ready(function() {
    		//判断是否登录
    		if("${USERID}" == "") {
    			alertTimer("您当前未登录！正在跳转到登录页面！");
    			setTimeout(function() {
    				window.location.href = "<%=basePath%>login";
    			},2000);
    			return false;
    		}
    		
    		//给用户头像添加title
    		var type = "${USERTYPE}";
    		if(type == "1") {
    			$("#img_user").attr("title","超级管理员");
    		}else {
    			$("#img_user").attr("title","管理员");
    		}
    	});
    	
    	//弹出定时对话框
    	function alertTimer(title) {
    		swal({
    			title : title,
    			text : "3秒后自动关闭",
    			timer : 3000,
    			showConfirmButton:true
    		});
    	}
    	
    	//弹出修改密码模态框
    	function showPswModal() {
    		//清空输入框
    		$("#input_old_psw").val("");
    		$("#input_new_psw").val("");
    		$("#input_confirm_psw").val("");
    		$("#pswModal").modal("show");
    	}
    	
    	//修改密码
    	function updatePsw() {
    		var oldPsw = $("#input_old_psw").val();
    		var newPsw = $("#input_new_psw").val();
    		var confirmPsw = $("#input_confirm_psw").val();
    		
    		if(oldPsw == "") {
    			alertFail("原密码不能为空！");
    			return false;
    		}
    		if(newPsw == "") {
    			alertFail("新密码不能为空！");
    			return false;
    		}
    		
    		if(confirmPsw == "") {
    			alertFail("确认密码不能为空！");
    			return false;
    		}
    		
    		var data = {
    				oldPsw : oldPsw,
    				newPsw : newPsw,
    				confirmPsw : confirmPsw
    		};
    		
    		$.post("<%=basePath%>admin/updatePsw",data,function(rs) {
    			if(rs == "1") {
    				$("#pswModal").modal("hide");
    				alertSuccess("修改成功！");
    			}else if(rs == "0") {
    				alertFail("您未登录！");
    			}else if(rs == "2") {
    				alertFail("原密码输入错误！请重输！");
    			}else if(rs == "3") {
    				alertFail("新密码不符合格式！请重输！")
    			}else if(rs == "4") {
    				alertFail("新密码与确认密码不一致！请重输");
    			}
    		},"json");
    		
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
