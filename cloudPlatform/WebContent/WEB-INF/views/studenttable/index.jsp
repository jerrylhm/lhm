<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<%@ include file="../common/crypto-js.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>课表管理</title>
<link rel="stylesheet" href="<%=basePath %>css/style.css">
<link rel="stylesheet" href="<%=basePath %>assets/js/button/ladda/ladda.min.css">
<link rel="stylesheet" href="<%=basePath %>font-awesome/css/font-awesome.css">
<link rel="stylesheet" href="<%=basePath %>css/button.css">
<link rel="stylesheet" href="<%=basePath %>css/load.css">
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath %>/zTree/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath %>css/studentTable.css">
</head>
<body style="height:100%;overflow-x: hidden;background: #f4f4f5;">

	<div id="app" class="content" style="background:#f4f4f5;">
		<div class="breadcrumb">
			<div class="bread-container">
				<span class="fa fa-home"></span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="学生课表">学生课表</span>
			</div>
		</div>
		<div id="app_table" class="content-wrap">
			<div class="row">
				<div class="col-sm-12 st-head">
					<div style="width: 300px;height:50px;position: relative;">
						<div class="mask" id="btn1_mask"></div>
						<div class="st-head-btn1" data-classid="" onclick="showCrs()">
							<i class="fa fa-home"></i>&nbsp;<span>点击选择教室</span>
						</div>
						<div class="st-head-content">
							<div style="border-top: 2px solid #249db9;">
							     <div class="input-group input-group-sm search">
								     <input id="searchInput" type="text" placeholder="搜索场所节点 " class="form-control ace-input" style="width: 250px;"> 
								     <span class="input-group-btn">
									     <button type="button" title="搜索" onclick="searchNodes('ztree','#searchInput')" style="background: #337ab7" class="btn btn-primary btn-white ace-button">
									     	<span class="fa fa-search"></span>
									     </button>
								     </span>
							     </div>
							     <div class="space-extend">
							     	<button class="layui-btn layui-btn-primary  extend" onclick="openNode('ztree',true)">全部展开</button>
							     	<button class="layui-btn layui-btn-primary  extend" onclick="openNode('ztree',false)">全部收起</button>
							     	<button class="layui-btn layui-btn-primary  extend" onclick="cleanAddid()">清空选择</button>
							     </div>
							</div>
							<div>
								<div class="ztree" id="ztree"></div>
							</div>		
						</div>
					</div>
					<div style="width: 400px;height:50px;align-items:center">
						<div class="btn-group"><button type="button" class="btn st-head-btn2" onclick="lastWeek()">上一周</button></div>
						<div class="btn-group" style="height: 25px;font-size: 18px;color: #333;">
							第<input id="head_input" type="text" class='form-control st-head-week' value="" style="display: inline-block;width: 60px;margin: 0 3px;">周</div>
						<div class="btn-group"><button type="button" class="btn st-head-btn2" onclick="nextWeek()">下一周</button></div>
					</div>
					<div class="st-head-right">
						<div style="margin: 0 10px;"><div class="child"></div></div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 st-content">
 					<table class="table table-bordered st-table">
 					  <thead>
  					    <tr >
					      <th style="width: 9%"><span class="y" style="display: block;margin-bottom: 7px;font-size: 18px;"></span></th>
					      <th><span>星 期 一</span><span class="md" data-day="1" style="display:block;font-weight: 200;"></span></th>
					      <th><span>星 期 二</span><span class="md" data-day="2" style="display:block;font-weight: 200;"></span></th>
					      <th><span>星 期 三</span><span class="md" data-day="3" style="display:block;font-weight: 200;"></span></th>
					      <th><span>星 期 四</span><span class="md" data-day="4" style="display:block;font-weight: 200;"></span></th>
					      <th><span>星 期 五</span><span class="md" data-day="5" style="display:block;font-weight: 200;"></span></th>
					      <th><span>星 期 六</span><span class="md" data-day="6" style="display:block;font-weight: 200;"></span></th>
					      <th><span>星 期 日</span><span class="md" data-day="7" style="display:block;font-weight: 200;"></span></th>
					    </tr>
 					  </thead>
					  <tbody>
					      <tr v-for="node in week_nodes" style="height: 120px;">
					      	<td>第{{node.node_index}}节<br>{{node.node_start}} ~ {{node.node_end}}</td>
					      	<c:forEach  items="1,2,3,4,5,6,7" var="num">
					      		<td class="st-body-content" v-bind:data-index="node.node_index" data-day="${num }" onclick="showModal(this)">
					      			<span class="st-name"></span>
					      			<span class="st-add"></span>
					      			<span class="st-teacher"></span>
					      			<div class="st-orgs">
					      			</div>
					      		</td>
					      	</c:forEach>
					      </tr>
					  </tbody>
					</table>
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
	 						课程名称：<input name="st_name" id="add_name" type="text" placeholder="输入课程名称" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
						所属课表：	<div  class="layui-form" style="width:200px;">
			 						<select class="add-tt" lay-verify="" lay-search id="add_ttid" disabled="disabled">		
										<option v-for="tt in timetables" v-bind:value="tt.tt_id">{{tt.tt_name}}</option>
									</select>
								</div>  
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
						任课老师：	<div  class="layui-form" style="width:200px;">
			 						<select id="add_teacherid" lay-verify="" lay-search>
										<option v-for="teacher in teachers" v-bind:value="teacher.ur_id">{{teacher.ur_nickname}}</option>
									</select>
								</div>	  
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
						上课地点：<div class="class-modal" id="add_addid" data-addid="">
									<span class="class-modal-title" id="ztree_title_add">点击选择</span>
									<div class="class-modal-content">
										<div class="close-div">
											<i class="fa fa-times modal-close"></i>
										</div>
										<div class="class-modal-head">
											<button class="layui-btn  layui-btn-primary layui-btn-sm" onclick="openNode('ztree_add',true)">展开所有</button>
											<button class="layui-btn  layui-btn-primary layui-btn-sm" onclick="openNode('ztree_add',false)">收起所有</button>
											<div class="input-group input-group-sm search" style="padding:0;">
												<input id="ztree_search_add" type="text" class="layui-input class-modal-search" placeholder="搜索节点" onkeydown="if(event.keyCode == 13) {searchNodes('ztree_add','#ztree_search_add')}">
												<button class="layui-btn layui-btn-primary layui-btn-xs search-button" onclick="searchNodes('ztree_add','#ztree_search_add')"><i class="fa fa-search"></i></button>
											</div>
										</div>
										<div class="class-modal-body">
											<div class="ztree" id="ztree_add"></div>
										</div>
									</div>
								</div>
								<div style="height: 15px;"></div>	  
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						上课人数：<input name="st_num" id="add_num" type="text" placeholder="输入上课学生人数" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						节&emsp;&emsp;数：
	 						<select class="form-control" style="width: auto;" id="add_day" onchange=""> 	
		 								<option v-for="day in week_day" v-bind:value="day.index">{{day.name }}</option>							
	 						</select>
	 						<select class="form-control" style="width: auto;margin-left: 20px;" id="add_index_begin" onchange=""> 							
		 								<option v-for="index in week_indexs" v-bind:value="index">第{{index }}节</option>							
	 						</select>~
	 						<select class="form-control" style="width: auto;" id="add_index_end"> 	
										<option v-for="index in week_indexs" v-bind:value="index">第{{index }}节</option>
	 						</select>
	 					</div>
 				</div>

				<div class="row">
	 					<div class="col-sm-12" style="color: #000;margin-left: 15px;">
	 						上课周数：						
								<table class="table table-bordered" id="add_table_week" style="width: 200px;display: inline;">
									<tbody>
										<c:forEach items="1,2,3,4,5" var="row">
											<tr>
												<c:forEach items="1,2,3,4,5" var="colum">
													<td class="table-week active" data-week="${(row-1)*5 + colum}">${(row-1)*5 + colum}</td>
												</c:forEach>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<div class="table-week-type">
									<div class="table-week-check" data-type="single">单周</div>
									<div class="table-week-check" data-type="double">双周</div>
									<div class="table-week-check" data-type="check">全选</div>
									<div class="table-week-check" data-type="uncheck">全不选</div>
								</div>
	 					</div>
				</div>
				
				<div class="row">
	 					<div class="col-sm-12 table-orgs" style="color: #000;margin-left: 15px;">
						<span id="add_org_title" style="width: 80px;">上课班级：</span>
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
				<button type="button" id="add_btn" lay-submit="" class="modal-suc btn btn-primary">
					添加
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

<!-- 更新模态框 -->
<div class="modal fade" id="update_modal" data-stid="" tabindex="-1" role="dialog" aria-hidden="true">
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
	 						课程名称：<input name="st_name" id="update_name" type="text" placeholder="输入课程名称" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
						所属课表：	<div  class="layui-form" style="width:200px;">
			 						<select class="update-tt" lay-verify="" lay-search id="update_ttid" disabled="disabled">		
										<option v-for="tt in timetables" v-bind:value="tt.tt_id">{{tt.tt_name}}</option>
									</select>
								</div>  
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
						任课老师：	<div  class="layui-form" style="width:200px;">
			 						<select id="update_teacherid" lay-verify="" lay-search>
										<option v-for="teacher in teachers" v-bind:value="teacher.ur_id">{{teacher.ur_nickname}}</option>
									</select>
								</div>	  
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
						上课地点：<div class="class-modal" id="update_addid" data-addid="">
									<span class="class-modal-title" id="ztree_title_update">点击选择</span>
									<div class="class-modal-content">
										<div class="close-div">
											<i class="fa fa-times modal-close"></i>
										</div>
										<div class="class-modal-head">
											<button class="layui-btn  layui-btn-primary layui-btn-sm" onclick="openNode('ztree_update',true)">展开所有</button>
											<button class="layui-btn  layui-btn-primary layui-btn-sm" onclick="openNode('ztree_update',false)">收起所有</button>
											<div class="input-group input-group-sm search" style="padding:0;">
												<input id="ztree_search_update" type="text" class="layui-input class-modal-search" placeholder="搜索节点" onkeydown="if(event.keyCode == 13) {searchNodes('ztree_update','#ztree_search_update')}">
												<button class="layui-btn layui-btn-primary layui-btn-xs search-button" onclick="searchNodes('ztree_update','#ztree_search_update')"><i class="fa fa-search"></i></button>
											</div>
										</div>
										<div class="class-modal-body">
											<div class="ztree" id="ztree_update"></div>
										</div>
									</div>
								</div>
								<div style="height: 15px;"></div>	  
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						上课人数：<input name="st_num" id="update_num" type="text" placeholder="输入上课学生人数" class="form-control">
	 					</div>
 				</div>
				<div class="form-group" style="height: 30px;">
	 					<div class="col-sm-12" style="color: #000">
	 						节&emsp;&emsp;数：
	 						<select class="form-control" style="width: auto;" id="update_day" onchange=""> 	
		 								<option v-for="day in week_day" v-bind:value="day.index">{{day.name }}</option>							
	 						</select>
	 						<select class="form-control" style="width: auto;margin-left: 20px;" id="update_index_begin" onchange=""> 							
		 								<option v-for="index in week_indexs" v-bind:value="index">第{{index }}节</option>							
	 						</select>~
	 						<select class="form-control" style="width: auto;" id="update_index_end"> 	
										<option v-for="index in week_indexs" v-bind:value="index">第{{index }}节</option>
	 						</select>
	 					</div>
 				</div>

				<div class="row">
	 					<div class="col-sm-12" style="color: #000;margin-left: 15px;">
	 						上课周数：						
								<table class="table table-bordered" id="update_table_week" style="width: 200px;display: inline;">
									<tbody>
										<c:forEach items="1,2,3,4,5" var="row">
											<tr>
												<c:forEach items="1,2,3,4,5" var="colum">
													<td class="table-week active" data-week="${(row-1)*5 + colum}">${(row-1)*5 + colum}</td>
												</c:forEach>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<div class="table-week-type">
									<div class="table-week-check" data-type="single">单周</div>
									<div class="table-week-check" data-type="double">双周</div>
									<div class="table-week-check" data-type="check">全选</div>
									<div class="table-week-check" data-type="uncheck">全不选</div>
								</div>
	 					</div>
				</div>
				
				<div class="row">
	 					<div class="col-sm-12 table-orgs" style="color: #000;margin-left: 15px;">
						<span id="update_org_title" style="width: 80px;">上课班级：</span>
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
				<button type="button" id="update_btn" lay-submit="" class="modal-suc btn btn-primary">
					添加
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

</div>
<script type="text/template" id="template_org">
						<div  class="layui-form" style="width:200px;">
	 						<select class="add-org-select" lay-verify="" lay-search>
								{option}
							</select>
						</div>
</script>
<script type="text/template" id="template_class">
						<div  class="layui-form" style="width:200px;">
	 						<select class="select-org" lay-verify="" lay-search lay-filter="changeClass">
								<option value="0">选择班级</option>
								{option}
							</select>
						</div>
</script>
<script type="text/template" id="template_layui_select">
						<div  class="layui-form" style="width:200px;">
	 						<select class="select-tt" lay-verify="" lay-search lay-filter="changeTt">		
								{option}
							</select>
						</div>
</script>
<script type="text/template" id="template_error">
	 <span class="msg-error"><i class="fa fa-close"></i><span class="msg-content">{msg}</span></span>
</script>
<script type="text/javascript" src="<%=basePath %>layui/layui.js"></script>
<script type="text/javascript" src="<%=basePath %>js/load.js"></script>
<script type="text/javascript" src="<%=basePath %>js/vue.min.js"></script>
<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/studentTable.js"></script>
</body>
</html>