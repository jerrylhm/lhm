<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>场所管理</title>
<link rel="stylesheet" href="<%=basePath %>css/ace.css">
<link rel="stylesheet" href="<%=basePath %>css/adminStyle.css">
<link rel="stylesheet" href="<%=basePath %>/zTree/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath %>css/organizationManage.css">
<link rel="stylesheet" href="<%=basePath %>css/addressManage.css">
</head>
<body>
	<div id="address">
		<div class="bread">
			<div class="bread-container">
				<span class="fa fa-home"></span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="组织机构"> 组织机构</span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="场所管理"> 场所管理</span>
			</div>
		</div>
		
		<div class="content">
			<div class="menu">
				<div class="box-header">
					<div class="header-title">场所</div>
					<div class="dropdown header-op">
						<a class="dropdown-toggle" data-toggle="dropdown" id="menu_dropdown" >选择操作<span class="fa fa-angle-down fa-fw "></span></a>
						 <ul class="dropdown-menu" role="menu" aria-labelledby="menu_dropdown">
						 	<li><a onclick="expandAll()"><i class="fa fa-plus-square-o fa-fw"></i>一键展开</a></li>
						 	<li><a onclick="foldAll()"><i class="fa fa-minus-square-o fa-fw"></i>一键关闭</a></li>
						 	<li><a onclick="expandNode()"><i class="fa fa-plus fa-fw"></i>展开所选</a></li>
						 	<li><a onclick="foldNode()"><i class="fa fa-minus fa-fw"></i>关闭所选</a></li>
						 	<li><a onclick="cancelHighLight()"><i class="fa fa-mail-reply fa-fw"></i>取消高亮</a></li>
						 	<li class="divider"></li>
						 	<li><a onclick="showSetModal()"><i class="fa fa-cog fa-fw"></i>批量设置课表</a></li>
						 	<li><a onclick="refreshNode()"><i class="fa fa-refresh fa-fw"></i>刷新</a></li>
						 </ul>
					</div>
				</div>
				
				<div class="menu-content">
					<div class="op-wrapper">
						<div>
							<a class="btn btn-primary btn-sm btn-white" onclick="showAddModal(true)"><i class="fa fa-plus"></i>新增根节点</a>
						</div>
						<div class="input-group input-group-sm search">
							<input type="text" class="form-control" placeholder="搜索场所节点 " v-model="searchValue" v-on:keydown.enter="searchNodes()">
		                    <span class="input-group-btn">
		                        <button class="btn btn-primary btn-white" type="button" title="搜索" onclick="searchNodes()"><span class="fa fa-search"></span></button>
		                    </span>
						</div>
					</div>
					<div class="tree-wrapper">
						<div class="ztree" id="tree"></div>
					</div>
				</div>
			</div>
			
			<div class="opera">
				<div class="box-header">
					<div class="header-title" v-html="address.add_name"></div>
				</div>	
				<div class="opera-content">
					<div class="btn-wrap">
						<a class="btn btn-primary btn-white" v-bind:disabled="isClassRoom" onclick="showAddModal(false)"><i class="fa fa-plus fa-fw"></i>新增子节点</a>
						<a class="btn btn-success btn-white" v-on:click="showUpdateModal(address)"><i class="fa fa-pencil fa-fw"></i>编辑{{typeMap[address.add_type]}}</span></a>
						<a class="btn btn-danger btn-white" v-on:click="showDelete(address,true)"><i class="fa fa-trash fa-fw"></i>删除{{typeMap[address.add_type]}}</a>
						<a class="btn btn-danger btn-white" v-bind:disabled="isClassRoom" onclick="deleteCheck()"><i class="fa fa-trash fa-fw"></i>删除所选子节点</a>
					</div>
					<div class="table-wrapper">
						<!-- 普通场所节点显示子节点列表 -->
						<div v-show="!isClassRoom">
							<div class="title">子节点列表</div>
							<div class="table-contanier">
								<div v-show="haveChildren">
									<table class="table table-bordered table-striped table-hover dataTable" >
										<thead>
											<tr>
												<th style="width:8%"><input type="checkbox" v-model="checkAll" v-on:click="changeCheck()"></th>
												<th style="width:8%;">序号</th>
												<th style="width:25%">{{typeMap[address.add_type]}}名称</th>
												<th style="width:10%">类型</th>
												<th style="width:25%">所属父节点</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<tr v-for="(child,index) in addressChildren">
												<td><input type="checkbox" v-model="child.isChecked"></td>
												<td  v-bind:title="index + 1">{{index + 1}}</td>
												<td v-bind:title="child.add_name">{{child.add_name}}</td>
												<td v-bind:title="typeMap[child.add_type]">{{typeMap[child.add_type]}}</td>
												<td v-bind:title="address.add_name">{{address.add_name}}</td>
												<td >
													<a class="btn-parallel btn btn-success btn-sm btn-white" v-on:click="showUpdateModal(child)" title="编辑" ><i class="fa fa-pencil"></i>编辑</a>
													<a class="btn-parallel btn btn-danger btn-sm btn-white" title="删除" v-on:click="showDelete(child,false)"><i class="fa fa-trash"></i>删除</a>
												</td>
											</tr>
										</tbody>
									</table>
									<div class="page-container">
										<div class="page-left">
											<span>第</span>
											<select v-model="page.selectIndex" v-on:change="changePage()">
												<option v-for="(option,index) in page.indexOptions" v-bind:value="index+1">{{index+1}}</option>
											</select>
											<span>页</span>
											<span> | </span>
											<span>共 {{page.pageCount}} 页</span>
										</div>
										<div class="page-right">
											<ul class="pagination">
												<li ><a v-on:click="pre()">上一页</a></li>
												<li><a>{{page.index}}</a></li>
												<li ><a v-on:click="next()">下一页</a></li>
											</ul>
										</div>
									</div>
								</div>
								<div v-show="!haveChildren" class="table-tip">
									<div class="alert alert-info">
										子节点为空！
									</div>
								</div>
							</div>
						</div>
						
						<!-- 课室节点显示课室信息 -->
						<div v-show="isClassRoom">
							<div class="title" style="background-color: #49B0FC">课室信息</div>
							<div class="info-wrapper">
								<div class="info-list info-list-primary">
									<div class="info-header">
										<span class="fa fa-video-camera"></span>
									</div>
									<div class="info-part">
										<div class="info-title">摄像头IP</div>
										<div class="info-msg">{{cameraTitle}}</div>
									</div>
								</div>
								<div class="info-list info-list-success">
									<div class="info-header">
										<span class="fa fa-calendar"></span>
									</div>
									<div class="info-part">
										<div class="info-title">课表</div>
										<div class="info-msg" v-html="timetableTitle"></div>
									</div>
									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 新增模态框 -->
		<div class="modal fade" tabindex="-1" id="addModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" >新增{{addModal.title}}</h4>
		            </div>
		            <div class="modal-body">
		            	<form class="layui-form" lay-filter="addForm">
		            		<div class="layui-form-item">
		            			<label class="layui-form-label" >{{addModal.title}}名称：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<input type="text" class="layui-input" placeholder="输入场所名称" v-model="addModal.name" v-on:keydown.enter="addAddress()">
		            			 </div>
		            		</div>
		            		<div class="layui-form-item" v-show="addModal.showParent">
		            			<label class="layui-form-label" >所在场所：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<input type="text" class="layui-input" disabled v-bind:value="address.add_name">
		            			 </div>
		            		</div>
		            		<div class="layui-form-item">
		            			<label class="layui-form-label" ><span id="add_tip" class="tip fa fa-question-circle-o"></span>是否课室：</label>
		            			<div class="layui-input-inline layui-input-wrapper" >
		            				<input type="checkbox" lay-skin="switch" lay-text="是|否"  lay-filter="add_classroom" v-model="addModal.isAddClass" name="add">
		            				
		            			</div>
		            		</div>
		            		<div class="layui-form-item" v-show="addModal.isAddClass">
		            			<label class="layui-form-label" >摄像头IP：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<input type="text" class="layui-input" placeholder="输入摄像头IP地址" v-model="addModal.camera" v-on:keydown.enter="addAddress()">
		            			 </div>
		            		</div>
		            		<div class="layui-form-item" v-show="addModal.isAddClass">
		            			<label class="layui-form-label" >课表：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<select lay-search v-model="addModal.timetable" lay-filter="add_timetable">
		            			 		<option value="0">请选择</option>
		            			 		<option v-for="timeTable in addModal.timetables" v-bind:value="timeTable.tt_id">{{timeTable.tt_name}}（第一周时间：{{timeTable.tt_firstweek}}）</option>
		            			 	</select>
		            			 </div>
		            		</div>
		            	</form>
		            </div>
		            <div class="modal-footer">
		                <a class="btn btn-primary btn-white" onclick="addAddress()"><i class="fa fa-check" ></i>新增</a>
		                 <a class="btn btn-default btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</a>
		            </div>
				</div>
			</div>
		</div>
		
		<!-- 编辑模态框 -->
		<div class="modal fade" tabindex="-1" id="updateModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" >编辑{{updateModal.address.add_name}}</h4>
		            </div>
		            <div class="modal-body">
		            	<form class="layui-form" lay-filter="updateForm">
		            		<div class="layui-form-item">
		            			<label class="layui-form-label" >{{typeMap[updateModal.address.add_type]}}名称：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<input type="text" class="layui-input" placeholder="输入场所名称" v-model="updateModal.name" v-bind:value="updateModal.address.add_name" v-on:keydown.enter="updateAddress()">
		            			 </div>
		            		</div>
		            		<div class="layui-form-item" v-show="updateModal.showParent">
		            			<label class="layui-form-label" >所在场所：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<input type="text" class="layui-input" disabled v-bind:value="updateModal.parent.add_name">
		            			 </div>
		            		</div>
		            		<div class="layui-form-item">
		            			<label class="layui-form-label" ><span id="update_tip" class="tip fa fa-question-circle-o"></span>是否课室：</label>
		            			<div class="layui-input-inline layui-input-wrapper" >
		            				<input type="checkbox" lay-skin="switch" lay-text="是|否" v-model="updateModal.isAddClass" lay-filter="update_classroom" name="classroom">
		            			</div>
		            		</div>
		            		<div class="layui-form-item" v-show="updateModal.isAddClass">
		            			<label class="layui-form-label" >摄像头IP：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<input type="text" class="layui-input" placeholder="输入摄像头IP地址" v-model="updateModal.camera" v-on:keydown.enter="updateAddress()">
		            			 </div>
		            		</div>
		            		<div class="layui-form-item" v-show="updateModal.isAddClass">
		            			<label class="layui-form-label" >课表：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<select lay-search v-model="updateModal.timetable" lay-filter="update_timetable">
		            			 		<option value="0">请选择</option>
		            			 		<option v-for="timeTable in updateModal.timetables" v-bind:value="timeTable.tt_id">{{timeTable.tt_name}}（第一周时间：{{timeTable.tt_firstweek}}）</option>
		            			 	</select>
		            			 </div>
		            		</div>
		            		<div class="layui-form-item " v-show="updateModal.address.add_type == 1 && updateModal.isAddClass==true">
		            			<div class="alert alert-danger col-md-offset-1 col-md-10">
		            				注意：从普通类型场所节点编辑成课室类型节点，将会删除该节点的所有子节点
		            			</div>
		            		</div>
		            		<div class="layui-form-item " v-show="updateModal.address.add_type == 0 && !updateModal.isAddClass">
		            			<div class="alert alert-danger col-md-offset-1 col-md-10">
		            				注意：从课室类型节点编辑成普通场所类型节点，将会删除该节点对应的课表信息
		            			</div>
		            		</div>
		            	</form>
		            </div>
		            <div class="modal-footer">
		                <a class="btn btn-primary btn-white" onclick="updateAddress()"><i class="fa fa-pencil" ></i>编辑</a>
		                 <a class="btn btn-default btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</a>
		            </div>
				</div>
			</div>
		</div>
		
		
		<!-- 批量设置课表模态框 -->
		<div class="modal fade" tabindex="-1" id="setModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" >批量设置课表</h4>
		            </div>
		            <div class="modal-body">
		            	<form class="layui-form" lay-filter="setForm">
		            		<div class="layui-form-item">
		            			<label class="layui-form-label" >当前选中场所：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<input type="text" class="layui-input" disabled v-bind:value="address.add_name">
		            			 </div>
		            		</div>
		            		
		            		<div class="layui-form-item">
		            			<label class="layui-form-label" >设置范围：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<input v-for="range in setModal.ranges" name="range" v-bind:value="range.id" type="radio" v-bind:title="range.name" v-bind:checked="range.isChecked" lay-filter="set_range">
		            			 </div>
		            		</div>
		            		<div class="layui-form-item">
		            			<label class="layui-form-label" >课表：</label>
		            			 <div class="layui-input-inline layui-input-wrapper">
		            			 	<select lay-search v-model="setModal.timetable" lay-filter="set_timetable" id="timetable">
		            			 		<option value="0">请选择</option>
		            			 		<option v-for="timeTable in setModal.timetables" v-bind:value="timeTable.tt_id">{{timeTable.tt_name}}（第一周时间：{{timeTable.tt_firstweek}}）</option>
		            			 	</select>
		            			 </div>
		            		</div>
		            	</form>
		            </div>
		            <div class="modal-footer">
		                <a class="btn btn-primary btn-white" onclick="setTimeTable()"><i class="fa fa-check" ></i>完成</a>
		                 <a class="btn btn-default btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</a>
		            </div>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/vue.min.js"></script>
<script type="text/javascript" src="<%=basePath%>layui/layui.js"></script>
<script type="text/javascript" src="<%=basePath%>js/addressManage.js"></script>
</body>
</html>