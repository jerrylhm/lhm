<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学科管理</title>
<link rel="stylesheet" href="<%=basePath %>css/style.css">
<link rel="stylesheet" href="<%=basePath %>assets/js/button/ladda/ladda.min.css">
<link rel="stylesheet" href="<%=basePath %>font-awesome/css/font-awesome.css">

<link rel="stylesheet" href="<%=basePath %>css/button.css">
<link rel="stylesheet" href="<%=basePath %>css/load.css">
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath %>css/subject.css">
</head>
<body style="height:100%">
	<div class="content" style="background:#f4f4f5;height:100%;">
		<div class="breadcrumb">
			<div class="bread-container">
				<span class="fa fa-home"></span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="学科管理"> 学科管理</span>
			</div>
		</div>
		
		<div id="app_table" class="content-wrap" style="padding: 0px 30px;">

                <div class="row">
                    <!-- CONTENT MAIL -->
                    <div class="col-sm-12">
                        <div class="mail_header">
                            <div class="row">
								<div class="col-sm-6" style="display:flex;">
									<button type="button" class="am-btn am-btn-success" onclick="showAddModal()"><span class="fa fa-plus"></span> 新增</button>
									<button type="button" class="am-btn am-btn-default am-btn-danger" style="margin-left: 10px" onclick="deleteChecked()"><span class="fa fa-trash"></span> 删除选中</button>
									<div  class="layui-form" style="width:200px;margin-left: 100px;">
				 						<select lay-verify="" lay-search lay-filter="major">
											<option value="">请选择专业</option>
											<option v-for="org in orgs" v-bind:value="org.organization.org_id">{{org.organization.org_name}}</option>
										</select>
									</div>
								</div>
								<div class="col-sm-6">
				                    <div class="input-group input-widget pull-right" style="width: 400px">
				                        <input id="like" style="border-radius:15px" placeholder="输入学科名称查询" class="form-control" type="text" v-model="like">
				                    </div>
								</div>
                            </div>
                        </div>

                        <div id="content-mail" style="position:relative;height: 700px;border:1px solid #d2d2d2;color:#666369">
                            <div class="table-responsive">
                                <!-- THE MESSAGES -->
                                <table class="table table-mailbox layui-table">
                                	<thead>
                                		<tr>
                                			<td style="width:5%;"><input type="checkbox" onchange="onCheck(this)"></td>
                                			<td style="width:20%;">名称</td>
                                			<td style="width:10%;">学科类型</td>
                                			<td style="width:40%;">专业</td>
                                			<td>操作</td>
                                		</tr>
                                	</thead>
                                    <tbody>
                                    <tr v-for="sub in subject" class="unread" v-bind:data-subid="sub.sub_id">
                                        <td class="small-col">
                                            <input type="checkbox">
                                        </td>
                                        <td class="name">{{sub.sub_name}}</td>
                                        <td class="type" v-bind:data-type="sub.sub_type">
                                        	<template v-if="sub.sub_type == 0">公共学科</template>
                                    		<template v-if="sub.sub_type == 1">专业学科</template>
                                   		</td>
                                        <td class="major" v-bind:data-orgid="sub.sub_orgid"><span v-for="(major, key) in sub.majors"><template v-if="key != 0">,</template>{{major.org_name}}</span></td>
                                        <td>
                                        	<div class="tpl-table-black-operation">
                                                <a href="javascript:;" class="tpl-table-black-operation-suc" onclick="view(this)">
                                                    <i class="fa fa-eye"></i> 预览
                                                </a>
                                                <a href="javascript:;" onclick="updateOne(this)">
                                                    <i class="fa fa-pencil"></i> 编辑
                                                </a>
                                                <a href="javascript:;" class="tpl-table-black-operation-del" onclick="deleteOne(this)">
                                                    <i class="fa fa-trash"></i> 删除
                                                </a>
                                           	</div>
                                        </td>
                                    </tr>
                                    
                                    <!-- END OF THREE -->
                                </tbody></table>
                            </div>
                            <!-- /.table-responsive -->
                            <div class="row pull-right" id="app_page" style="position:absolute;width: 100%;bottom:30px;">
									<input type="text" name="title" class="layui-input input-forware" >
									<div class="pull-right layui-box layui-laypage layui-laypage-default" style="position: absolute;right: 0px;top: -30px;">
									<a href="javascript:forwarePage('last')" class="layui-laypage-prev" data-page="0">
									上一页
									</a>
									<a href="javascript:;" class="layui-laypage-last" title="尾页" onclick="forwarePage('first')">首页</a>
									<a style="margin-right: 64px;">第{{currentPage}}/{{totalPage}}页</a>
									<a style="margin-right: 10px;" href="javascript:forwareByInput();">跳转</a>
									<a href="javascript:;" class="layui-laypage-last" title="尾页" onclick="forwarePage('end')">尾页</a>
									<a href="javascript:forwarePage('next')" class="layui-laypage-next">下一页</a>
									</div>
		                    </div>
                        </div>
                    </div>
                    <!-- /CONTENT MAIL -->
                </div>
            </div>
	</div>
	
<!-- 新增模态框 -->
<div class="modal fade" id="add_modal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title">
					添加学科
				</h4>
			</div>
			<div class="modal-body">
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						学科名称：<input name="tt_name" id="add_name" type="text" placeholder="输入课表名称" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						学科类型：<select class="form-control" style="width: auto;" name="tt_num" id="add_type"> 							
		 								<option value="0">公共学科</option>
		 								<option value="1">专业学科</option>							
	 						</select>
	 					</div>
 				</div>
				<div class="row" id="add_major" style="display: none;">
	 					<div class="col-sm-12 table-orgs" style="color: #000;margin-left: 15px;">
						<span id="add_org_title" style="width: 80px;">所属专业：</span>
						<div>					
							<div id="table-org-btns" class='layui-btn-group'>
								<button onclick="addOrg(this)" class="layui-btn layui-btn-primary layui-btn-sm"><i class="fa fa-plus"></i></button>
							<button onclick="removeOrg(this)" class="layui-btn layui-btn-primary layui-btn-sm"><i class="fa fa-trash"></i></button>
							</div>
						</div>
						</div>
 				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="add_btn" class="modal-suc btn btn-primary">
					添加
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

<!-- 更新模态框 -->
<div class="modal fade" id="update_modal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title">
					修改学科
				</h4>
			</div>
			<div class="modal-body">
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						学科名称：<input id="update_name" type="text" placeholder="输入课表名称" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						学科类型：<select class="form-control" style="width: auto;" id="update_type"> 							
		 								<option value="0">公共学科</option>
		 								<option value="1">专业学科</option>							
	 						</select>
	 					</div>
 				</div>
				<div class="row" id="update_major" style="display: none;">
	 					<div class="col-sm-12 table-orgs" style="color: #000;margin-left: 15px;">
						<span id="update_org_title" style="width: 80px;">所属专业：</span>
						<div>					
							<div id="table-org-btns" class='layui-btn-group'>
								<button onclick="addOrg(this)" class="layui-btn layui-btn-primary layui-btn-sm"><i class="fa fa-plus"></i></button>
							<button onclick="removeOrg(this)" class="layui-btn layui-btn-primary layui-btn-sm"><i class="fa fa-trash"></i></button>
							</div>
						</div>
						</div>
 				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="update_btn" class="modal-suc btn btn-primary">
					修改
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
<!-- 预览 -->
<div class="modal fade" role="dialog" aria-hidden="true" tabindex="-1" id="view" style="color:#525152">
 	<div class="modal-dialog">
 		<div class="modal-content">
 			<div class="modal-header">
 				<button class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					学科信息
				</h4>
 			</div>
 			
 			<div class="modal-body" >
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						学科名称：<span id="view_name">高等数学</span>
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						学科类型：<span id="view_subject">公共学科</span>
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						所属专业：<span id="view_major">电子信息，语文教育</span>
	 					</div>
 				</div>
	 		</div>
	 		<div class="modal-footer">
	 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
	 		</div>
	 	</div>
 	</div>
 </div>
 <script type="text/template" id="template_org">
						<div  class="layui-form" style="width:200px;">
	 						<select class="add-org-select" lay-verify="" lay-search>
								{option}
							</select>
						</div>
</script>
<script type="text/template" id="template_error">
	 <span class="msg-error"><i class="fa fa-close"></i><span class="msg-content">{msg}</span></span>
</script>
<script type="text/javascript" src="<%=basePath %>js/load.js"></script>
<script type="text/javascript" src="<%=basePath %>js/vue.min.js"></script>
<script type="text/javascript" src="<%=basePath %>layui/layui.js"></script>
<script type="text/javascript" src="<%=basePath %>laydate/laydate.js"></script>
<script type="text/javascript" src="<%=basePath %>layer/layer.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/subject.js"></script>
</body>
</html>