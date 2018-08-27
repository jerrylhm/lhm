<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学期管理</title>
<link rel="stylesheet" href="<%=basePath %>css/style.css">
<link rel="stylesheet" href="<%=basePath %>assets/js/button/ladda/ladda.min.css">
<link rel="stylesheet" href="<%=basePath %>font-awesome/css/font-awesome.css">

<link rel="stylesheet" href="<%=basePath %>css/button.css">
<link rel="stylesheet" href="<%=basePath %>css/load.css">
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath %>css/term.css">
</head>
<body style="height:100%">
	<div class="content" style="background:#f4f4f5;height:100%;">
		<div class="breadcrumb">
			<div class="bread-container">
				<span class="fa fa-home"></span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="学期管理">学期管理</span>
			</div>
		</div>
		
		<div id="app_table" class="content-wrap" style="padding: 0px 30px;">

                <div class="row">
                    <!-- CONTENT MAIL -->
                    <div class="col-sm-12">
                        <div class="mail_header">
                            <div class="row">
								<div class="col-sm-6">
									<button type="button" class="am-btn am-btn-success" onclick="showAddModal()"><span class="fa fa-plus"></span> 新增</button>
									<button type="button" class="am-btn am-btn-default am-btn-danger" style="margin-left: 10px" onclick="deleteChecked()"><span class="fa fa-trash"></span> 删除选中</button>
								</div>
								<div class="col-sm-6">
				                    <div class="input-group input-widget pull-right" style="width: 400px">
				                        <input id="like" style="border-radius:15px" placeholder="输入学期名称查询" class="form-control" type="text" v-model="like">
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
                                			<td><input type="checkbox" onchange="onCheck(this)"></td>
                                			<td>学期名称</td>
                                			<td>开学日期</td>
                                			<td>结束日期</td>
                                			<td>操作</td>
                                		</tr>
                                	</thead>
                                    <tbody>
                                    <tr v-for="term in terms" class="unread" v-bind:data-id="term.term_id">
                                        <td class="small-col">
                                            <input type="checkbox">
                                        </td>
                                        <td class="term-name">{{term.term_name}}</td>
                                        <td class="term-start">{{term.term_start}}</td>
                                        <td class="term-end">{{term.term_end}}</td>
                                        <td>
                                        	<div class="tpl-table-black-operation">
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
<!-- 			                    <div class="col-sm-12"> -->
<!-- 		                   		   <div class="btn-group pull-right" style="margin-left:10px;"> -->
<!-- 			                            <button type="button" class="btn" onclick="forwarePage('end')">尾页</button> -->
<!-- 			                        </div> -->
<!-- 			                        <div class="btn-group pull-right"> -->
<!-- 			                            <button type="button" class="btn" onclick="forwarePage('last')"> -->
<!-- 			                                <span class="fa fa-angle-left"></span> -->
<!-- 			                            </button> -->
<!-- 			                            <button type="button" class="btn"><span style="color:red">{{currentPage}}</span>/{{totalPage}}</button> -->
<!-- 			                            <button type="button" class="btn" onclick="forwarePage('next')"> -->
<!-- 			                                <span class="fa fa-angle-right"></span> -->
<!-- 			                            </button> -->
<!-- 			                        </div> -->
<!-- 			                        <div class="btn-group pull-right" style="margin-right:10px;"> -->
<!-- 			                            <button type="button" class="btn" onclick="forwarePage('first')">首页</button> -->
<!-- 			                        </div> -->
<!-- 			                    </div> -->
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
					添加学期
				</h4>
			</div>
			<div class="modal-body">
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						学期名称：<input id="add_name" type="text" placeholder="输入学期名称" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						开学日期：<input id="add_start" type="text" placeholder="输入开学日期" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						开学日期：<input id="add_end" type="text" placeholder="输入结束日期" class="form-control">
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
<div class="modal fade" id="update_modal" tabindex="-1" role="dialog" aria-hidden="true" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title">
					更新学期
				</h4>
			</div>
			<div class="modal-body">
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						学期名称：<input id="update_name" type="text" placeholder="输入学期名称" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						开学日期：<input id="update_start" type="text" placeholder="输入开学日期" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						结束日期：<input id="update_end" type="text" placeholder="输入结束日期" class="form-control">
	 					</div>
 				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="update_btn" class="modal-suc btn btn-primary">
					确认
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

<script type="text/javascript" src="<%=basePath %>js/load.js"></script>
<script type="text/javascript" src="<%=basePath %>js/vue.min.js"></script>
<script type="text/javascript" src="<%=basePath %>laydate/laydate.js"></script>
<script type="text/javascript" src="<%=basePath %>layer/layer.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/term.js"></script>
</body>
</html>