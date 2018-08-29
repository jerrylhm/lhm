<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<style>
	body{
		padding : 30px 30px;
		background-color: #F0F3F4;
	}
	.location {
		margin : 10px 2px;
		margin-bottom : 20px;
	}
	.container-box {
		min-height : 600px;
		border : 1px solid #ccc;
		background-color: #fff;
		border-radius : 3px;
		
	}
	.container-header {
		padding : 10px 20px;
		border-left : 4px solid #80D0FF;
		border-bottom : 1px solid #ccc;
	}
	.container-op {
		margin : 15px 30px;
	}
	.btn-margin{
		margin-left : 10px;
	}
	
	.container-table {
		margin : 2px 30px;
		margin-top:15px;
	}
	.search {
		width : 300px;
		display : inline-block;
		float:right;
	}
	
		#table td {
		text-align: center;
	}
	#table th {
		text-align: center;
		color : #666;
	}
	.btn-margin {
		margin-left : 8px;
	}
	
	.span-super {
		display : inline-block;
		background-color: #d9534f;
		color : #fff;
		padding : 3px 8px;
		border-radius : 3px;
		font-size : 12px;
	}
	
	.span-normal {
		display : inline-block;
		background-color: #5cb85c;
		color : #fff;
		padding : 3px 8px;
		border-radius : 3px;
		font-size : 12px;
	}
	
	.container-table {
		border : 1px solid #ccc;
		border-radius : 5px;
		padding : 5px 10px;
		padding-top : 10px;
	}
	.container-page {
		margin-right : 50px;
		margin-bottom : 40xp;
	}
	
	.page-input {
		width : 30px;
		height :19px;
		padding-left :2px;
		padding-right : 2px;
	}
	
	#page a {
		color : #666;
	}
	
	.modal-container {
		padding : 10px 20px;
	}
	.modal-error {
		color :red;
		margin-bottom : 5px;
		padding-left : 10px;
		display : none;
	}
	
	
</style>
</head>
<body>
	<div>
		<div class="location">
			<p class="text-primary">
				<i class="fa fa-home fa-fw fa-lg"></i>
				当前位置 ： 用户组管理
			</p>
		</div>
		<div class="container-box">
			<div class="container-header">
				<h4>用户管组理</h4>
			</div>
			<div class="container-op">
				<a onclick="showAddUserGroup()" class="btn btn-primary" title="新增"><i class="fa fa-plus fa-fw"></i>新增</a>
				<a onclick="deleteChecked()" class="btn btn-danger btn-margin" title="删除选中"><i class="fa fa-trash fa-fw"></i>删除选中</a>
			</div>
			<div class="container-table">
				<div>
					<table class="table table-striped table-hover" id="table">
						<thead>
							<tr>
								<th style="width:8%">
									<label style="margin:0;cursor: pointer;" >
										<input type="checkbox" id="input_check" onclick="checkAll(this)"> 选择
									</label>
								</th>
								<th style="width:8%;">序号</th>
								<th style="width:20%">用户组名</th>
								<th style="width:15%">所属树</th>
								<c:if test="${userType == 1}">
									<th style="width:10%">创建人</th>
								</c:if>
								<th style="text-align: left">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${UserGroupList }" var="group" varStatus="stas">
								<tr id="tr_${group.urg_id}">
									<td>
										<input class="check-user" type="checkbox" value="${group.urg_id}" style="cursor: pointer">
									</td>
									<td title="${stas.count }">
										${stas.count }
									</td>
									<td title="${group.urg_name}">
										${group.urg_name}
									</td>
									<td title="${group.node_name}">
										${group.node_name}
									</td>
									<c:if test="${userType == 1}">
										<td title="${group.ur_username}">
											${group.ur_username}
										</td>
									</c:if>
									<td style="text-align: left">
										<a class="btn btn-success btn-sm" title="编辑" onclick="showUpdateModal('${group.urg_id }','${group.urg_name}','${group.urg_treeid}')">
											<i class="fa fa-pencil fa-lg fa-fw"></i>编辑
										</a>
										<a class="btn btn-info btn-sm btn-margin" title="权限设置" onclick="showPermission('${group.urg_id }','${group.urg_treeid}')">
											<i class="fa fa-key fa-lg fa-fw"></i>权限设置
										</a>
										<a class="btn btn-danger btn-sm btn-margin" title="删除" onclick="deleteSingle('${group.urg_id }')">
											<i class="fa fa-trash fa-lg fa-fw"></i>删除
										</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="clearfix container-page">
					<div class="pull-right">
						<ul id="page" class="pagination pagination-sm" style="cursor: pointer;">
							<li class="disabled">
								<a >第<span style="color:red">${index }</span>/${countPage } 页</a>
							</li>
							<li class="disabled">
								<a >共${CountNum}条</a>
							</li>
							<li id="li_first">
								<a onclick="first()" title="首页">首页</a>
							</li>
							<li id="li_pre">
								<a onclick="pre()" title="上一页">上一页</a>
							</li>
							<li id="li_next">
								<a onclick="next()" title="下一页">下一页</a>
							</li>
							<li id="li_last">
								<a onclick="last()" title="尾页">尾页</a>
							</li>
							<li >
								<a  style="height : 29px;" >跳转到
									<input class="page-input" type="text" id="input_page"> 页
									<span onclick="gotoPage()" title="跳转"><i class="fa fa-send fa-fw"></i></span>
								</a>
							</li>
						</ul>
					</div>
				</div>
				
				<!-- 				新增模态框 -->
				<div class="modal fade" id="addModal">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" data-dismiss="modal">&times;</button>
								<h5 class="modal-title">新增用户组</h5>
							</div>
							<div class="modal-body">
								<div class="modal-container">
									<form class="form-horizontal">
										<div class="form-group">
											<label class="col-sm-3 control-label">用户组名：</label>
											<div class="col-sm-8">
												<input type="text" id="input_Groupname" class="form-control" placeholder="输入用户组名">
											</div>
										</div>
										
										<div class="form-group">
											<label class="col-sm-3 control-label">所属树：</label>
											<div class="col-sm-8">
												<select class="form-control" id="input_Grouptree">
													<c:forEach items="${TreeList}" var="tree">
														<option value="${tree.node_id}">${tree.node_name}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</form>
								</div>
							</div>
							<div class="modal-footer">
								<button class="btn btn-primary" onclick="addUserGroup()"><i class="fa fa-check fa-fw"></i>新增</button>
								<button class="btn btn-default" data-dismiss="modal"><i class="fa fa-close fa-fw"></i>取消</button>
							</div>
						</div>
					</div>
				</div>
				<!-- 				编辑模态框 -->
				<div class="modal fade" id="updateModal">
					<div class="modal-dialog">
						<div class="modal-content">	
							<div class="modal-header">
								<button class="close" data-dismiss="modal">&times;</button>
								<h5 class="modal-title">编辑用户组</h5>
							</div>
							<div class="modal-body">
								<div class="modal-container">
									<form class="form-horizontal">
										<input type="hidden" id="update_Groupid" class="form-control">
										<div class="form-group">
											<label class="col-sm-3 control-label">用户组名：</label>
											<div class="col-sm-8">
												<input type="text" id="update_Groupname" class="form-control">
											</div>
										</div>
										
										<div class="form-group">
											<label class="col-sm-3 control-label">所属树：</label>
											<div class="col-sm-8">
												<select class="form-control" id="update_Grouptree">
													<c:forEach items="${TreeList}" var="tree">
														<option value="${tree.node_id}">${tree.node_name}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</form>
								</div>
							</div>
							<div class="modal-footer">
								<button class="btn btn-primary" onclick="updateUserGroup()"><i class="fa fa-check fa-fw"></i>确定</button>
								<button class="btn btn-default" data-dismiss="modal"><i class="fa fa-close fa-fw"></i>取消</button>
							</div>
						</div>
					</div>
				</div>
				<!-- 				权限设置模态框 -->
				<div class="modal fade" id="permissionModal">
					<div class="modal-dialog" style="width: 70%;">
						<div class="modal-content">	
							<div class="modal-header">
								<button class="close" data-dismiss="modal">&times;</button>
								<h5 class="modal-title">权限设置</h5>
							</div>
							<iframe width="100%" id="set_Permission" src="<%=basePath %>permission" frameborder="0"></iframe>
						</div>
					</div>
				</div>
				
				
				
				
				
				
				
			</div>
		</div>
	</div>
	<script type="text/javascript">
		//iframe动态自适应高度
		
		var browserVersion = window.navigator.userAgent.toUpperCase();
	    var isOpera = browserVersion.indexOf("OPERA") > -1 ? true : false;
	    var isFireFox = browserVersion.indexOf("FIREFOX") > -1 ? true : false;
	    var isChrome = browserVersion.indexOf("CHROME") > -1 ? true : false;
	    var isSafari = browserVersion.indexOf("SAFARI") > -1 ? true : false;
	    var isIE = (!!window.ActiveXObject || "ActiveXObject" in window);
	    var isIE9More = (! -[1, ] == false);
	    function ChangeIframe(iframeId, minHeight){
	        try {
	            var iframe = document.getElementById(iframeId);
	            var bHeight = 0;
	            if (isChrome == false && isSafari == false)
	                bHeight = iframe.contentWindow.document.body.scrollHeight;
	            	var dHeight = 0;
	            	if (isFireFox == true)
	                	dHeight = iframe.contentWindow.document.documentElement.offsetHeight + 2;
	            	else if (isIE == false && isOpera == false)
	                	dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
	            	else if (isIE == true && isIE9More) {//ie9+
	                	var heightDeviation = bHeight - eval("window.IE9MoreRealHeight" + iframeId);
	                if (heightDeviation == 0){
	                    bHeight += 3;
	                }else if (heightDeviation != 3){
	                    eval("window.IE9MoreRealHeight" + iframeId + "=" + bHeight);
	                    bHeight += 3;
	                }
	            }else
	                bHeight += 3;
	            var height = Math.max(bHeight, dHeight);
	            if (height < minHeight) height = minHeight;
	            	iframe.style.height = height + "px";
	        }catch (ex){ }
	    }
	    function startInit(iframeId, minHeight) {
	        eval("window.IE9MoreRealHeight" + iframeId + "=0");
	        window.setInterval("ChangeIframe('" + iframeId + "'," + minHeight + ")", 100);
	    }
	    var minHeight = $(window).height();
	    startInit('set_Permission', minHeight);
	
	
	
	
		//弹出新增用户组框
		function showAddUserGroup(){
			$("#input_Groupname").val("");
			$("#addModal").modal("show");
		}
		
		//确定新增用户
		function addUserGroup(){
			var name = $("#input_Groupname").val();
			var treeid = $("#input_Grouptree").val();
			var Group_data = {
					"name":name,
					"treeid":treeid
			};
			
			$.post("<%=basePath %>admin/addUserGroup",Group_data,function(msg){
				if(msg == "1"){
					$("#addModal").modal("hide");
					alertSuccess("用户组新增成功！");
					setTimeout(function(){
						window.location.href = "<%=basePath%>admin/userGroupManage?page=${index}";
					}, 1000);
				}else{
					alertFail("用户组新增失败！");
				}
			},"json");
		}
		
		var index = parseInt("${index}");
		var pageCount = parseInt("${countPage}");
		
		//初始化分页操作
		function initPage() {
			if(index <= 1) {
				$("#li_first").addClass("disabled");
				$("#li_pre").addClass("disabled");
			}
			
			if(index >= pageCount) {
				$("#li_next").addClass("disabled");
				$("#li_last").addClass("disabled");
			}
		}
		
		//点击首页
		function first() {
			if(index <= 1) {
				return false;
			}
			//跳转
			window.location.href = "<%=basePath%>admin/userGroupManage?page=1";
		}
		
		//点击上一页
		function pre() {
			if(index <= 1) {
				return false;
			}
			//跳转
			index--;
			window.location.href = "<%=basePath%>admin/userGroupManage?page="+ index;
		}
		
		//下一页
		function next() {
			if(index >= pageCount) {
				return false;
			}
			//跳转
			index ++;
			window.location.href = "<%=basePath%>admin/userGroupManage?page="+ index;
		}
		
		//尾页
		function last() {
			if(index >= pageCount) {
				return false;
			}
			//跳转
			window.location.href = "<%=basePath%>admin/userGroupManage?page=${countPage}";
		}
		
		//跳转到指定页码
		function gotoPage() {
			var page = $("#input_page").val();
			var pattern = /^[0-9]+$/;
			if(!pattern.test(page)) {
				alertFail("只能输入数字！");
				$("#input_page").val("");
				$("#input_page").focus();
				return false;
			}
			//检查数字有效性
			if(parseInt(page) < 1 || parseInt(page) > pageCount) {
				alertFail("输入的页码超出范围！");
				$("#input_page").focus();
				return false;
			}
			
			//跳转
			window.location.href = "<%=basePath%>admin/userGroupManage?page="+ parseInt(page);
		}
		
		
		//全选中或全不选
		function checkAll(e) {
			var isChecked = $(e).prop("checked");
			if(!isChecked) {
				//全不选
				$(".check-user").prop("checked",false);
			}else {
				$(".check-user").prop("checked",true);
			}
		}
		
		//删除选中
		function deleteChecked(){
			var idAll = "";
			var Num = 0;
			$(".check-user").each(function(){
				if($(this).prop("checked")){
					if(idAll == ""){
						idAll = $(this).val();
					}else{
						idAll += ","+$(this).val();
					}
					Num++;
				}
			});
			if(Num == 0){
				alertFail("未选中用户组；无法删除！");
				return false;
			}
			deleteUserGroup("确定删除选中的"+Num+"条用户组？",idAll);
		}
		
		//单个删除
		function deleteSingle(id){
			deleteUserGroup("确定删除该条用户组？",id);
		}
		
		
		//删除操作
		function deleteUserGroup(title,idAll){
			//弹出警告提示框
			swal({
				title : title,
				text : "",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "删除",
				cancelButtonText : "取消",
				closeOnConfirm : false
			},function(isConfirm){
				if(isConfirm) {
					$.ajax({
						type:"post",
						url:"<%=basePath %>admin/deleteUserGroup",
						data:{"ids":idAll},
						success:function(msg){
							if(msg == "true"){
								alertSuccess("删除成功！");
								setTimeout(function() {
									window.location.href = "<%=basePath%>admin/userGroupManage?page=${index}";
								},1000);
							}else{
								alertFail("删除失败！");
							}
						}
					});
				}
			});
		}
		
		//弹出编辑框
		function showUpdateModal(id,name,treeid){
			$("#update_Groupid").val(id);
			$("#update_Groupname").val(name);
			$("#update_Grouptree").val(treeid);
			$("#updateModal").modal("show");
		}
		
		//修改用户组
		function updateUserGroup(){
			var id = $("#update_Groupid").val();
			var name = $("#update_Groupname").val();
			var tree = $("#update_Grouptree").val();
			if(name == ""){
				alertFail("用户组名不能为空！");
				return false;
			}
			
			$.ajax({
				type:"post",
				url:"<%=basePath %>admin/updateUserGroup",
				data:{"id":id,"name":name,"treeid":tree},
				success:function(msg){
					if(msg == "true"){
						alertSuccess("修改成功！");
						setTimeout(function(){
							window.location.href = "<%=basePath%>admin/userGroupManage?page=${index}";
						}, 1000);
					}else{
						alertFail("修改失败！");
					}
				}
			});
		}
		
		//弹出权限设置模态框
		function showPermission(id,treeid){
			document.getElementById("set_Permission").src = "<%=basePath %>permission?id="+treeid+"&&groupid="+id;
			$("#permissionModal").modal("show");
		}
		
	</script>
	
</body>
</html>