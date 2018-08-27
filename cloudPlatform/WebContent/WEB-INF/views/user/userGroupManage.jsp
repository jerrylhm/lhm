<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户组管理</title>
<link rel="stylesheet" href="<%=basePath %>css/ace.css">
<link rel="stylesheet" href="<%=basePath %>css/adminStyle.css">
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath %>css/userGroupManage.css">
</head>
<body>
	<div id="usergroup">
		<div class="bread">
			<div class="bread-container">
				<span class="fa fa-home"></span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="用户管理"> 用户管理</span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="用户组管理"> 用户组管理</span>
			</div>
		</div>
		<div class="content">
			<div class="group-container">
				<a class="btn btn-primary btn-sm btn-white" @click="showModal(true)"><span class="fa fa-user-plus fa-fw"></span>创建用户组</a>
				<div class="dd group-list-wrapper" v-show="usergroups.length > 0">
					<ol class="dd-list " >
						<li class="dd-item " v-for="(usergroup,index) in usergroups">
							<div class="dd2-content usergroup ng-scope" :id="index">
								<button v-show="usergroup.ug_id > 5" type="button" @click="deleteUserGroup(usergroup)" class="close" style="margin-left:8px;" title="删除"><i class="fa fa-close"></i></button>
								<button  v-show="usergroup.ug_id > 5" type="button" class="close" @click="showModal(false,usergroup)"><i class="fa fa-pencil" title="编辑"></i></button>
								
								<div  v-on:click="changeUserGroup(index)">
									<span :title="usergroup.ug_name" style="cursor: pointer">{{usergroup.ug_name}}</span>
								</div>
							</div>
						</li>
					</ol>
					<!--
					<div class="group-page">
						<a title="首页"><span class="fa fa-angle-double-left fa-lg"></span></a>
						<a title="上一页"><span class="fa fa-angle-left fa-lg"></span></a>
						<span>{{groupPage.index}} / {{groupPage.pageCount}}</span>
						<a title="下一页"><span class="fa fa-angle-right fa-lg"></span></a>
						<a title="尾页"><span class="fa fa-angle-double-right fa-lg"></span></a>
					</div>
					  -->
					  <div class="group-page" v-show="groupPage.pageCount>1">
					  	<ul class="pagination pagination-sm">
						    <li class="page-first" title="首页" ><a @click="first()"><span class="fa fa-angle-double-left fa-lg"></span>&nbsp;</a></li>
						    <li title="上一页"><a @click="pre()"><span class="fa fa-angle-left fa-lg"></span>&nbsp;</a></li>
						    <li><a ><span style="color:red">{{groupPage.index}}</span> / {{groupPage.pageCount}}</a></li>
						    <li title="下一页"><a @click="next()">&nbsp;<span class="fa fa-angle-right fa-lg"></span></a></li>
						    <li title="尾页 " class="page-last"><a @click="last()">&nbsp;<span class="fa fa-angle-double-right fa-lg"></span></a></li>
						</ul>
					  </div>
				</div>
				
				<div class="group-list-wrapper" v-show="usergroups.length <= 0">
					<div class="group-tip">
						
					</div>
				</div>
			</div>
			
			<div class="app-container ">
				<div>
					<span class="text-block"><i class="fa fa-cogs fa-fw"></i> 应用列表</span>
				</div>
				<div class="dd dd-success group-list-wrapper" style="margin-top: 18px;" v-show="applications.length > 0">
					<ol class="dd-list">
						<li class="dd-item" v-for="(app,index) in applications" :title="app.app_name" v-on:click="changeApplication(app)">
							<div class="dd2-content"  :class="{activesuccess: app.isActive}">
								<span class="fa-fw fa fa-hand-o-right"></span>
								<span>{{app.app_name}}</span>
							</div>
						</li>
					</ol>
				</div>
				<div class="group-list-wrapper" v-show="applications.length <= 0">
					<div class="group-tip"></div>
				</div>
			</div>
			
			<div class="box box-permission">
				<div class="box-header">
					<div class="box-header-title">权限管理 - {{usergroup.ug_name}} [ {{application.app_name}} ]
						<span class="text-danger" v-show="usergroup.ug_id == 1" style="margin-left: 5px;"> 注：管理员权限无法修改</span>
				</div>
				</div>
				<div class="box-content">
					<div v-show="permissions.length > 0">
						<a class="btn btn-success btn-white" onclick="quickAuth()" :disabled="usergroup.ug_id == 1"><i class="fa fa-handshake-o fa-fw"></i>一键授权</a>
						<a class="btn btn-danger btn-white" onclick="quickUnauth()" :disabled="usergroup.ug_id == 1"><i class="fa fa-trash-o fa-fw"></i>一键取消</a>
						<div class="table-wrapper">
							<table class="table table-bordered table-striped table-hover table-permission" >
								<thead>
									<tr>
										<th style="width:10%;">序号</th>
										<th style="width:35%">权限名称</th>
										<th style="width:20%">是否授权</th>
										<th>授权操作</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="(permission,index) in permissions">
										<td>{{index + 1}}</td>
										<td>{{permission.ps_name}}</td>
										<td><span class="text-block" v-bind:class="{danger: !permission.hasPermission}">{{permission.hasPermission==true?"已授权":"未授权"}}</span></td>
										<td>
											<label>
												<input :disabled="usergroup.ug_id == 1" type="checkbox" v-model="permission.hasPermission" name="switch-filed-1" class="ace ace-switch ace-switch-4 btn-empty" @click="authorize(permission)">
												<span class="lbl lbl-success"></span>
											</label>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<!-- 没有权限提示 -->
					<div class="no-permission" v-show="permissions.length <= 0">
						<div class="tip-img"></div>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 新增用户组模态框 -->
		<div class="modal fade" tabindex="-1" id="usergroupModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" >{{usergroupModal.isCreate?"新增":"编辑"}}用户组</h4>
		            </div>
		            <div class="modal-body">
		            	<form class="form-horizontal" onsubmit="return false;">
		            		<div class="form-group">
		            			<label class="control-label col-md-3">用户组名称:</label>
		            			<div class="col-md-7">
		            				<input class="form-control"  placeholder="输入用户组名称" v-model="usergroupModal.name" v-on:keydown.enter="finishModal()">
		            			</div>
		            		</div>
		            	</form>
		            </div>
		            <div class="modal-footer">
		                <a class="btn btn-primary btn-white" @click="finishModal()"><i class="fa fa-check" ></i>{{usergroupModal.isCreate?"新增":"编辑"}}</a>
		                 <a class="btn btn-default btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</a>
		            </div>
				</div>
			</div>
		</div>
	</div>
	

<script type="text/javascript" src="<%=basePath%>js/vue.min.js"></script>
<script type="text/javascript" src="<%=basePath%>layer/layer.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/userGroupManage.js"></script>
</body>
</html>