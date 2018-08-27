<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="User-Group">
<meta name="author" content="ilongli">
<link rel="stylesheet" href="<%=basePath%>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath%>css/style.css">
<link rel="stylesheet" href="<%=basePath%>css/userManage.css">
<link rel="stylesheet" href="<%=basePath %>/zTree/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript" src="<%=basePath%>js/angularjs/angular.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/angularjs/angular-animate.min.js"></script>
<title>系统用户</title>
</head>
<body>
	<div class="bread">
		<div class="bread-container">
			<span class="fa fa-home"></span> 
			<span class="fa fa-lg fa-angle-right"></span> 
			<span title="用户管理">&nbsp;用户管理</span>
			<span class="fa fa-lg fa-angle-right"></span> 
			<span title="系统用户">&nbsp;系统用户</span>
		</div>
	</div>

	<div ng-app="userApp" class="main-div">
		<div ng-controller="tableCtrl" >
			<!-- loading -->
			<div class="load-outer" ng-show="isLoad">
				<i class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop load-i"></i>
			</div>
			
			<!-- main-outer -->
			<div class="main-outer" ng-class="{blur: isLoad&&isBlurEffect}">
				<!-- 顶部 -->
				<div class="top-div ibox-title">
	             	<div class="col-lg-3">
						<div class="input-group">
							<input type="text" class="form-control ace-input" placeholder="输入账号或者昵称查找" ng-model="queryStr" ng-keypress="dokeydown($event)">
							<span class="input-group-btn">
								<button class="btn btn-success ace-button" type="button" style="height: 34px;line-height: 12px;" ng-click="query()">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div>
					</div>
	
					<!-- 日期选择器 -->
					<div class="col-lg-2">
						<input type="text" class="date-input layui-input" placeholder="请选择日期" id="date">
					</div>
					
					<!-- 角色选择 -->
					<div class="col-sm-4 layui-tab">
						<ul class="layui-tab-title" style="position: relative;top: 5px;font-weight: 400;">
							<li ng-repeat="(x,y) in TYPE_GROUP" ng-class="{'layui-this': role==x}" ng-click="changeType(x)">{{y}}</li>
						</ul>
					</div>
					
					<!-- 审核状态下拉框 -->
					<div class="ibox-tools">
						<div class="layui-form" style="width: 200px; position: relative;">
							<select id="select_status" lay-filter="showStatus">
								<option value="-1">全部</option>
								<option value="0">未审核</option>
								<option value="1">已审核</option>
							</select>
						</div>
					</div>
					
					<!-- 用户组下拉框 -->
					<div class="ibox-tools">
						<div class="layui-form" style="width: 200px; position: relative;">
							<select id="select_status" lay-filter="showUserGroup">
								<option value="-1">全部</option>
								<option ng-repeat="ug in usergroups" ng-value="ug.ug_id" ng-selected="ug.ug_id == ug_id">{{ug.ug_name}}</option>
								<!-- <option value="1">管理员</option>
								<option value="2">用户组1</option> -->
							</select>
						</div>
					</div>
				</div>
			
				<table class="layui-table ur-table" lay-even>
					<thead>
						<tr>
							<th><input type="checkbox" ng-model="select_all" ng-change="selectAll()"></th>
							<th>头像</th>
							<th>
								账号
								<i ng-class="{'fa fa-sort-asc': !isDESC&&orderBy=='ur_username', 'fa fa-sort-desc': isDESC&&orderBy=='ur_username', 'fa fa-unsorted': orderBy!='ur_username'}" 
									ng-click="changeOrderBy('ur_username')"></i>
							</th>
							<th>昵称</th>
							<th>性别</th>
							<th>角色</th>
							<th>用户组</th>
							<th>班级</th>
							<th>
								注册时间
								<i ng-class="{'fa fa-sort-asc': !isDESC&&orderBy=='ur_createdate', 'fa fa-sort-desc': isDESC&&orderBy=='ur_createdate', 'fa fa-unsorted': orderBy!='ur_createdate'}" 
									ng-click="changeOrderBy('ur_createdate')"></i>
							</th>
							<th>审核状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="u in users">
							<td><input type="checkbox" ng-model="u.checked" ng-change="selectOne()"></td>
							<td>
								<img class="ur-image" ng-src="<%=basePath%>{{u.ur_image}}">
							</td>
							<td>{{u.ur_username}}</td>
							<td>{{u.ur_nickname}}</td>
							<td>{{SEX_GROUP[u.ur_sex]}}</td>
							<td>{{u.ur_type | roleFilter}}</td>
							<td>{{u.ur_groupname}}</td>
							<td>{{u.ur_classname}}</td>
							<td>{{u.ur_createdate}}</td>
							<td>
								<span class="layui-badge" ng-show="u.ur_status == 0">未审核</span>
								<span class="layui-badge layui-bg-blue" ng-show="u.ur_status == 1">已审核</span>
							</td>
							<td>
								<button class="layui-btn layui-btn-sm" ng-click="updateOne(u)">修改</button>
								<button class="layui-btn layui-btn-sm layui-btn-danger" ng-click="deleteOne(u)">删除</button>
							</td>
						</tr>
					</tbody>
				</table>
				
				<h2 class="data-none" ng-show="users.length === 0">没有数据。</h2>
	
				<!-- 底部 -->
				<div class="bottom-div page-tool">
	 				<div class="bottom-btn-group">
						<button class="layui-btn" ng-click="addOne()">新增</button>
						<button class="layui-btn layui-btn-danger" ng-click="deleteSelect()">删除</button>
						<button class="layui-btn layui-btn-normal" ng-click="importUser()">用户导入</button>
						<button class="layui-btn layui-btn-xs button-blur" ng-class="{'layui-btn-primary': !isBlurEffect,'layui-btn-normal': isBlurEffect}" 
							ng-click="isBlurEffect = !isBlurEffect" title="如果你发现有页面抖动的情况，可以关闭此效果。">模糊效果</button>
					</div>
					<input id="inpage" type="text" name="title"
						class="layui-input input-forware" ng-model="jumpPage" ng-keypress="dokeydownPage($event)"
						onkeyup="if(/\D/.test(this.value)){alertTimer('只能输入数字');this.value='';}">
					<div class="pull-right layui-box layui-laypage layui-laypage-default" style="position: absolute; right: 5px; top: -6px;">
						<a href="javascript:void(0);" class="layui-laypage-prev" ng-click="changePage(curPage-1)"> 上一页 </a> 
						<a href="javascript:void(0);" class="layui-laypage-last" title="尾页" ng-click="changePage(1)">首页</a>
						<a style="margin-right: 64px;">第{{curPage}}/{{totalPage}}页</a> 
						<a style="margin-right: 10px;" href="javascript:;" ng-click="jumpToPage(jumpPage)">跳转</a> 
						<a href="javascript:void(0);" class="layui-laypage-last" title="尾页" ng-click="changePage(totalPage)">尾页</a> 
						<a href="javascript:void(0);" class="layui-laypage-next" ng-click="changePage(curPage+1)">下一页</a>
					</div>
				</div>
			</div>
			
			<!-- 模态框 -->
			<!-- 模态框(删除) -->
			<div class="modal fade" id="delModal" tabindex="-1" role="dialog" aria-labelledby="delTitle" aria-hidden="true" data-backdrop="false">
				<div class="modal-dialog modal-dialog-delete">
					<div class="modal-content">
						<div id="modal_header" class="modal-header well" style="background-color:#B22222;">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
								&times;
							</button>
							<h4 class="modal-title" id="delTitle"><font color="white">删除用户</font></h4>
						</div>
						<div class="modal-body">
							<label id="delStuList">确定删除用户?</label>
							<ul>
								<li ng-repeat="u in delList">
									<img class="user-img-sm" ng-src="<%=basePath%>{{u.ur_image}}">
									&nbsp;{{u.ur_username}}({{u.ur_nickname}})&nbsp;{{u.ur_classname==null?'':'-&nbsp;'+u.ur_classname}}
								</li>
							</ul>
						</div>
						<div class="modal-footer" style="height:60px;">
							<button class="layui-btn layui-btn-sm layui-btn-danger" ng-click="deleteUser()">删除</button>
							<button class="layui-btn layui-btn-sm layui-btn-primary" data-dismiss="modal">关闭</button>
						</div>
					</div>
				</div>
			</div>
			
			<!-- 模态框(用户导入) -->
			<div class="modal fade" id="importModal" tabindex="-1" role="dialog" aria-labelledby="importTitle" aria-hidden="true" data-backdrop="false">
				<div class="modal-dialog modal-dialog-import">
					<div class="modal-content">
						<div id="modal_header" class="modal-header well">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
								&times;
							</button>
							<h4 class="modal-title" id="importTitle">用户导入</h4>
						</div>
						<div class="modal-body">
							<form id="uploadForm" enctype="multipart/form-data">
								<div>
									<button type="button" class="layui-btn layui-btn-normal" ng-click="selectFile()">
									  	<i class="layui-icon">&#xe67c;</i>上传文件
									</button>
									<span id="fileName"></span>
								</div>
								<span id="errorSpan" ng-show="errorNum != 0"><i class="fa fa-warning"></i>&nbsp;发现{{errorNum}}个错误，请按照提示修改数据后重新上传。</span>
								<i class="fa fa-question-circle-o fa-lg question-i" title="帮助" ng-click="showHelp()"></i>
								<input id="xlsFile" class="upload-file-input" type="file" name="file" onchange="angular.element(this).scope().fileChange(this.files)"
									accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ng-model="file">
							</form>
							<div class="iur-table-outer">
								<table class="layui-table ur-table iur-table" lay-even>
									<thead>
										<tr>
											<th ng-repeat="(x,y) in excelUsers[0]">{{y}}</th>
										</tr>
									</thead>
									<tbody id="importUserTbody">
										<tr ng-repeat="user in excelUsers" ng-if="$index>0">
											<td ng-repeat="(x,y) in user track by $index" title="{{y}}">{{y}}</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="modal-footer" style="height:60px;">
							<button class="layui-btn layui-btn-sm layui-btn-warm pull-left" onclick="window.open('<%=basePath %>template/用户导入模板.xlsx')">下载模板</button>
							<button class="layui-btn layui-btn-sm layui-btn-normal" ng-class="{'layui-btn-disabled':(!allowImport || isImporting)}" 
								ng-click="doImport()" ng-disabled="!allowImport || isImporting">
								<span ng-show="isImporting">
									<i class="layui-icon layui-icon-loading-1 layui-anim layui-anim-rotate layui-anim-loop load-i cursor-not-allow"></i> 
								</span>
								{{isImporting?'':'导入'}}
							</button>
							<button class="layui-btn layui-btn-sm" ng-class="{'layui-btn-disabled':isUploading}" ng-click="uploadFile()" ng-disabled="isUploading">
								<span ng-show="isUploading">
									<i class="layui-icon layui-icon-loading-1 layui-anim layui-anim-rotate layui-anim-loop load-i cursor-not-allow"></i> 
								</span>
								{{isUploading?'':'上传'}}
							</button>
							<button class="layui-btn layui-btn-sm layui-btn-primary" data-dismiss="modal">关闭</button>
						</div>
					</div>
				</div>
			</div>
			
			
			<!-- 模态框(修改/新增) -->
			<div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="userTitle" aria-hidden="true" data-backdrop="false">
				<div class="modal-dialog modal-dialog-update">
					<div class="modal-content">
						<div id="modal_header" class="modal-header well">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
								&times;
							</button>
							<h4 class="modal-title" id="userTitle">{{isAddUser?'新增用户':'修改用户'}}</h4>
						</div>
						<form class="layui-form" action="">
							<div class="modal-body">
								<div class="layui-form-item" ng-class="{blur: (showClassTree||showUserGroup)&&isBlurEffect}">
									<label class="layui-form-label">用户名</label>
									<div class="layui-input-block">
										<input id="input-username" type="text" name="ur_username" placeholder="请输入用户名" required lay-verify="required|username"
											 autocomplete="off" class="layui-input" ng-value="upUser.ur_username" ng-disabled="!isAddUser">
									</div>
								</div>
								<div class="layui-form-item" id="resetPwd" ng-class="{blur: (showClassTree||showUserGroup)&&isBlurEffect}" ng-show="!isAddUser">
									<label class="layui-form-label">重置密码</label>
									<div class="layui-input-block">
										<input type="checkbox" name="isResetPwd" lay-skin="switch" lay-text="ON|OFF">
									</div>
								</div>
								<div class="layui-form-item" id="inputPwd" ng-class="{blur: (showClassTree||showUserGroup)&&isBlurEffect}" ng-show="isAddUser">
									<label class="layui-form-label">密码</label>
									<div class="layui-input-block">
										<input type="password" name="ur_password" required lay-verify="{{isAddUser?'required|pass':''}}" 
											placeholder="请输入密码" autocomplete="off" class="layui-input">
									</div>
								</div>
								<div class="layui-form-item" ng-class="{blur: (showClassTree||showUserGroup)&&isBlurEffect}">
									<label class="layui-form-label">昵称</label>
									<div class="layui-input-block">
										<input type="text" name="ur_nickname" required
											lay-verify="required|nickname" placeholder="请输入昵称" autocomplete="off"
											class="layui-input" ng-value="upUser.ur_nickname">
									</div>
								</div>
								<div class="layui-form-item" ng-class="{blur: (showClassTree||showUserGroup)&&isBlurEffect}">
									<label class="layui-form-label">手机</label>
									<div class="layui-input-block">
										<input type="text" name="ur_phone" lay-verify="phone"
											placeholder="请输入手机号码" autocomplete="off"
											class="layui-input" ng-value="upUser.ur_phone">
									</div>
								</div>
								<div class="layui-form-item" ng-class="{blur: (showClassTree||showUserGroup)&&isBlurEffect}">
									<label class="layui-form-label">邮箱</label>
									<div class="layui-input-block">
										<input type="text" name="ur_email" lay-verify="email"
											placeholder="请输入电子邮箱" autocomplete="off"
											class="layui-input" ng-value="upUser.ur_email">
									</div>
								</div>
<!-- 								<div class="layui-form-item" id="grade-item">
									<label class="layui-form-label">年级</label>
									<div class="layui-input-block">
										<select lay-filter="grade" name="ur_grade" lay-verify="required">
											<option ng-repeat="g in gradeList" ng-value="g.org_id">{{g.org_name}}</option>
										</select>
									</div>
								</div>
								<div class="layui-form-item" id="class-item">
									<label class="layui-form-label">班级</label>
									<div class="layui-input-block">
										<select name="ur_classid" lay-verify="required">
											<option ng-repeat="c in classList" ng-value="c.org_id">{{c.org_name}}</option>
										</select>
									</div>
								</div> -->
								<div class="layui-form-item" ng-class="{blur: showUserGroup&&isBlurEffect}">
									<label class="layui-form-label">班级</label>
									<div class="layui-input-block">
										<div class="class-tree-outer" ng-class="{active: showClassTree}">
											<div class="class-tree-div" ng-class="{active: showClassTree}">
												<div class="class-tree" ng-show="showClassTree">
													<h3>选择班级</h3>
													<div class="tree-tools">
														<a class="layui-btn layui-btn-xs layui-btn-primary" onclick="expandAll()">展开所有</a>
														<a class="layui-btn layui-btn-xs layui-btn-primary" onclick="collapseAll()">收起所有</a>
														<div class="input-group input-group-sm search">
															<input type="text" class="form-control" placeholder="搜索组织节点" ng-model="searchValue" onkeydown="if(event.keyCode == 13){dokeydownClass();return false;}">
										                    <span class="input-group-btn">
										                        <button class="btn btn-primary btn-white" type="button" title="搜索" ng-click="searchNodes()"><span class="fa fa-search"></span></button>
										                    </span>
														</div>
													</div>
													<div class="ztree" id="class_tree"></div>
												</div>
												<a class="layui-btn layui-btn-sm layui-btn-primary" ng-click="showClassTree = !showClassTree">{{showClassTree?'关闭':'选择班级'}}</a>
											</div>
										</div>
									</div>
								</div>
								<div class="layui-form-item" ng-class="{blur: showClassTree&&isBlurEffect}">
									<label class="layui-form-label">用户组</label>
									<div class="layui-input-block">
										<div class="usergroup-outer" ng-class="{active: showUserGroup}">
											<div class="usergroup-div" ng-class="{active: showUserGroup}">
												<div class="usergroup" ng-show="showUserGroup">
													<h3>选择用户组</h3>
													<div class="usergroup-container">
														<input type="checkbox" name="ur_group_{{$index}}" ng-value="ug.ug_id"  title="{{ug.ug_name}}"
										 					ng-repeat="ug in usergroups" ng-checked="isContain(upUser.ur_group, ug.ug_id)">
													</div>
												</div>
												<a class="layui-btn layui-btn-sm layui-btn-primary" ng-click="showUserGroup = !showUserGroup">{{showUserGroup?'关闭':'选择用户组'}}</a>
											</div>
										</div>
									</div>
								</div>
<!-- 								<div class="layui-form-item" ng-class="{blur: (showClassTree||showUserGroup)&&isBlurEffect}">
									<label class="layui-form-label">身份</label>
									<div class="layui-input-block">
										<input type="checkbox" name="ur_type_{{x}}" ng-value="x"  title="{{y}}"
										 	ng-repeat="(x,y) in TYPE_GROUP" ng-if="$index>0" ng-checked="isContain(upUser.ur_type, x)">
									</div>
								</div> -->
								<div class="layui-form-item" ng-class="{blur: (showClassTree||showUserGroup)&&isBlurEffect}">
									<label class="layui-form-label">性别</label>
									<div class="layui-input-block">
										<input type="radio" name="ur_sex" value="0" title="男" ng-checked="upUser.ur_sex == 0"> 
										<input type="radio" name="ur_sex" value="1" title="女" ng-checked="upUser.ur_sex == 1">
									</div>
								</div>
							</div>

							<div class="modal-footer" style="height:60px;" ng-class="{blur: (showClassTree||showUserGroup)&&isBlurEffect}">
								<div class="layui-form-item">
									<div class="layui-input-block">
										<button class="layui-btn layui-btn-sm" lay-submit lay-filter="formSubmit">{{isAddUser?'新增':'修改'}}</button>
										<button id="reset" type="reset" class="layui-btn layui-btn-sm layui-btn-warm">重置</button>
										<button class="layui-btn layui-btn-sm layui-btn-primary" data-dismiss="modal">关闭</button>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<script type="text/javascript" src="<%=basePath%>layui/layui.js"></script>
<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/userManage.js"></script>
</html>