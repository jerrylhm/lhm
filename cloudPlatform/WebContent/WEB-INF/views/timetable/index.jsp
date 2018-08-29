<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>作息表</title>
<link rel="stylesheet" href="<%=basePath %>css/style.css">
<link rel="stylesheet" href="<%=basePath %>assets/js/button/ladda/ladda.min.css">
<link rel="stylesheet" href="<%=basePath %>font-awesome/css/font-awesome.css">

<link rel="stylesheet" href="<%=basePath %>css/button.css">
<link rel="stylesheet" href="<%=basePath %>css/load.css">
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath %>css/timetable.css">
</head>
<body style="height:100%" >
  <div id="app_table">
	<div class="content" style="background:#f4f4f5;height:100%;">
		<div class="breadcrumb">
			<div class="bread-container">
				<span class="fa fa-home"></span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="作息表"> 作息表</span>
			</div>
		</div>
		
		<div class="content-wrap" style="padding: 0px 30px;">

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
				                        <input id="like" style="border-radius:15px" placeholder="输入作息表名称查询" class="form-control" type="text" v-model="like">
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
                                			<td>名称</td>
                                			<td>课节数</td>
                                			<td>所属学期</td>
                                			<td>更新日期</td>
                                			<td>操作</td>
                                		</tr>
                                	</thead>
                                    <tbody>
                                    <tr v-for="tt in timetable" class="unread" v-bind:data-ttid="tt.tt_id">
                                        <td class="small-col">
                                            <input type="checkbox">
                                        </td>
                                        <td class="name">{{tt.tt_name}}</td>
                                        <td class="small-col tt-num">{{tt.tt_num}}</td>
                                        <td class="tt-termid" v-if="tt.term != null" v-bind:data-termid="tt.term.term_id">{{tt.term.term_name}}</td>
                                        <td class="tt-termid" v-else v-bind:data-termid="0">未选择所属学期</td>
                                        <td class="tt-createdate">{{tt.tt_updatedate}}</td>
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
					添加课表
				</h4>
			</div>
			<div class="modal-body">
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						课表名称：<input name="tt_name" id="add_name" type="text" placeholder="输入课表名称" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
 							所属学期：<div  class="layui-form" style="width:200px;display: inline-block;">
	 									<select id="add_termid" class="add-org-select" lay-verify="" lay-search>
	 										<option value="0">未选择</option>
											<option v-for="term in terms" v-bind:value="term.term_id">{{term.term_name}}</option>
										</select>
									</div>
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						每日节数：<select class="form-control" style="width: auto;" name="tt_num" id="add_num" onchange="numSelected('add')"> 	
	 							    <c:forEach items="0,1,2,3,4,5,6,7,8,9,10,11,12" var="num">						
		 								<option value="${num }">${num }</option>							
	 								</c:forEach>
	 						</select>
	 					</div>
 				</div>
 				<hr><!-- --------------------课节时间设置区域--------------------------------- --> 		
				<div class="row">
	 					<div class="col-sm-12" style="color: #000">
	 						<table class="modal-table" id="add_table" style="text-align: center;width: 100%">
	 							<thead>
	 								<tr>
	 									<td style="width: 20%">课节</td>
	 									<td style="width: 80%">上课时间</td>
	 								</tr>
	 							</thead>
	 							<tbody>
	 								<c:forEach items="1,2,3,4,5,6,7,8,9,10,11,12" var="num">						
		 								<tr class="animation-over add-tr" data-index="${num}">
											<td>第${num}节</td>
											<td><input id="add_time_${num}" type="text" placeholder="选择上课时间" class="form-control laydate-time" style="width: 200px"></td>
										</tr>							
	 								</c:forEach>
	 							</tbody>
	 						</table>
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
<div class="modal fade" id="update_modal" tabindex="-1" role="dialog" aria-hidden="true" data-ttid="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title">
					更新课表
				</h4>
			</div>
			<div class="modal-body">
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						课表名称：<input id="update_name" type="text" placeholder="输入课表名称" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						所属学期：<div  class="layui-form" style="width:200px;display: inline-block;">
	 									<select id="update_termid" class="add-org-select" lay-verify="" lay-search>
											<option value="0">未选择</option>
											<option v-for="term in terms" v-bind:value="term.term_id">{{term.term_name}}</option>
										</select>
									</div>
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						每日节数：<select class="form-control" style="width: auto;" name="tt_num" id="update_num" onchange="numSelected('update')"> 	
	 							    <c:forEach items="0,1,2,3,4,5,6,7,8,9,10,11,12" var="num">						
		 								<option value="${num }">${num }</option>							
	 								</c:forEach>
	 						</select>
	 					</div>
 				</div>
 				<hr><!-- --------------------课节时间设置区域--------------------------------- --> 		
				<div class="row">
	 					<div class="col-sm-12" style="color: #000">
	 						<table class="modal-table" id="update_table" style="text-align: center;width: 100%">
	 							<thead>
	 								<tr>
	 									<td style="width: 20%">课节</td>
	 									<td style="width: 80%">上课时间</td>
	 								</tr>
	 							</thead>
	 							<tbody>
	 								<c:forEach items="1,2,3,4,5,6,7,8,9,10,11,12" var="num">						
		 								<tr class="animation-over update-tr" data-index="${num}">
											<td>第${num}节</td>
											<td><input id="update_time_${num}" type="text" placeholder="选择上课时间" class="form-control laydate-time" style="width: 200px"></td>
										</tr>							
	 								</c:forEach>
	 							</tbody>
	 						</table>
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

<!-- 预览课表 -->
<div class="modal fade" role="dialog" aria-hidden="true" tabindex="-1" id="view" style="color:#525152">
 	<div class="modal-dialog">
 		<div class="modal-content">
 			<div class="modal-header">
 				<button class="close" data-dismiss="modal">&times;</button>
 				<h4 id="viewName"></h4>
 			</div>
 			
 			<div class="modal-body" >
 				<table class="table table-bordered" >
				<!--   </thead> -->
				  <tbody id="viewBody">
				   <tr >
				      <th style="text-align:center;"></th>
				      <th class="trView tdFont">星期一</th>
				      <th class="tdFont trView">星期二</th>
				      <th class="tdFont trView">星期三</th>
				      <th class="tdFont trView">星期四</th>
				      <th class="tdFont trView">星期五</th>
				      <th class="tdFont trView">星期六</th>
				      <th class="tdFont trView">星期天</th>
				    </tr>
				  <c:forEach items="1,2,3,4,5,6,7,8,9,10,11,12" var="num">
				      <tr id="view_tr_${num}"></tr>
				  </c:forEach>
				
				  </tbody>
				</table>
	 		</div>
	 		<div class="modal-footer">
	 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
	 		</div>
	 	</div>
 	</div>
 </div>
 </div>
<script type="text/javascript" src="<%=basePath %>js/load.js"></script>
<script type="text/javascript" src="<%=basePath %>js/vue.min.js"></script>
<script type="text/javascript" src="<%=basePath %>layui/layui.js"></script>
<%-- <script type="text/javascript" src="<%=basePath %>laydate/laydate.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>layer/layer.min.js"></script> --%>
<script type="text/javascript" src="<%=basePath %>js/timetable.js"></script>
</body>
</html>