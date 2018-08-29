<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示用户ere</title>
<style>
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
						<th style="width:20%">用户名</th>
						<th style="width:10%">身份</th>
						<th style="width:15%">注册时间</th>
						<th style="text-align: left">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${UserList }" var="user" varStatus="stas">
						<tr id="tr_${user.ur_id}">
							<td>
								<input class="check-user" type="checkbox" value="${user.ur_id}" style="cursor: pointer">
							</td>
							<td title="${stas.count }">
								${stas.count }
							</td>
							<td title="${user.ur_username}">
								${user.ur_username}
							</td>
							<td>
								<c:if test="${user.ur_type == 1 }">
									<span class="span-super" title="管理员">管理员</span>
								</c:if>
								<c:if test="${user.ur_type != 1 }">
									<span class="span-normal" title="普通用户">普通用户</span>
								</c:if>
							</td>
							<td>
								${user.ur_datetime }
							</td>
							<td style="text-align: left">
								<a class="btn btn-success btn-sm" title="编辑" onclick="showUpdateModal('${user.ur_id }','${user.ur_username}','${user.ur_type }')">
									<i class="fa fa-pencil fa-lg fa-fw"></i>编辑
								</a>
								<a class="btn btn-danger btn-sm btn-margin" title="删除" onclick="deleteSingle('${user.ur_id }')">
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
						<a >第<span style="color:red">${Index }</span>/${PageCount } 页</a>
					</li>
					<li class="disabled">
						<a >共${Count}条</a>
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
	</div>
	<!-- 新增模态框 -->
	<div class="modal fade" id="addModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button class="close" data-dismiss="modal">&times;</button>
					<h5 class="modal-title">新增用户</h5>
				</div>
				<div class="modal-body">
					<div class="modal-container">
						<form class="form-horizontal">
							<div class="modal-error col-sm-offset-3" id="error_username"></div>
							<div class="form-group">
								<label class="col-sm-3 control-label">用户名：</label>
								<div class="col-sm-8">
									<input type="text" id="input_username" class="form-control" placeholder="输入5~16位英文数字组合">
								</div>
							</div>
							<div class="modal-error col-sm-offset-3" id="error_password"></div>
							<div class="form-group">
								<label class="col-sm-3 control-label">密码：</label>
								<div class="col-sm-8">
									<input type="password" id="input_password" class="form-control" placeholder="输入5~16位英文数字组合">
								</div>
							</div>
							<div class="modal-error col-sm-offset-3" id="error_confirm"></div>
							<div class="form-group">
								<label class="col-sm-3 control-label">确认密码：</label>
								<div class="col-sm-8">
									<input type="password" id="input_confirm_password" class="form-control" placeholder="输入确认密码">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">身份：</label>
								<div class="col-sm-4">
									<select class="form-control" id="select_type">
										<option value="0">普通用户</option>
										<option value="1">管理员</option>
									</select>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-primary" onclick="addUser()"><i class="fa fa-check fa-fw"></i>新增</button>
					<button class="btn btn-default" data-dismiss="modal"><i class="fa fa-close fa-fw"></i>取消</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 编辑模态框 -->
	<div class="modal fade" id="editModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button class="close" data-dismiss="modal">&times;</button>
					<h5 class="modal-title">编辑用户</h5>
				</div>
				<div class="modal-body">
					<div class="modal-container">
						<form class="form-horizontal">
							<div class="form-group">
								<label class="control-label col-sm-3">用户名：</label>
								<div class="col-sm-8">
									<input class="form-control" disabled value="admin" id="input_update_username">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3">
									<span id="span_tooltip" title="重置密码为123456" style="color:orange;cursor: pointer"><i class="fa fa-question-circle fa-lg"></i></span> 重置密码：
								</label>
								<div class="col-sm-3">
									<input id="input_update_reset" type="checkbox" class="checkbox" style="cursor:pointer;"  title="重置密码为123456">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3">身份：</label>
								<div class="col-sm-5">
									<select id="select_update_type" class="form-control">
										<option value="0">普通用户</option>
										<option value="1">管理员</option>
									</select>
								</div>
							</div>
						</form>
					</div>
					<div style="display:none">
						<input id="input_update_id" >
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-primary" onclick="updateUser()"><i class="fa fa-check fa-fw"></i>修改</button>
					<button class="btn btn-default" data-dismiss="modal"><i class="fa fa-close fa-fw"></i>取消</button>
				</div>
			</div>
		</div>
	</div>
	
<script type="text/javascript">
	$(document).ready(function() {
		//设置admin不可编辑
		$("#tr_1").find("a,input[type=checkbox]").attr("disabled",true);
		$("#tr_1").find("a,input[type=checkbox]").attr("title","不可点击");
		initPage();
		
		
	});
	
	var index = parseInt("${Index}");
	var pageCount = parseInt("${PageCount}");
	//console.log(index);
	
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
		window.location.href = "<%=basePath%>admin/showUser?page=1&&searchValue=${SearchValue}";
	}
	
	//点击上一页
	function pre() {
		if(index <= 1) {
			return false;
		}
		//跳转
		index--;
		window.location.href = "<%=basePath%>admin/showUser?page="+ index +"&&searchValue=${SearchValue}";
	}
	//下一页
	function next() {
		if(index >= pageCount) {
			return false;
		}
		//跳转
		index ++;
		window.location.href = "<%=basePath%>admin/showUser?page="+ index +"&&searchValue=${SearchValue}";
	}
	//尾页
	function last() {
		if(index >= pageCount) {
			return false;
		}
		//跳转
		window.location.href = "<%=basePath%>admin/showUser?page=${PageCount}&&searchValue=${SearchValue}";
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
		window.location.href = "<%=basePath%>admin/showUser?page="+ parseInt(page) +"&&searchValue=${SearchValue}";
	}
	
	//全选中或全不选
	function checkAll(e) {
		var isChecked = $(e).prop("checked");
		if(!isChecked) {
			//全不选
			$(".check-user").prop("checked",false);
		}else {
			$(".check-user").each(function() {
				//判断是否可选
			//	console.log($(this).attr("disabled"));
				if($(this).attr("disabled") != "disabled") {
					$(this).prop("checked",true);
				}
					
			});
		}
	}
	//弹出新增用户模态框
	function showAddModal() {
		//清空输入框文本
		$("#input_username").val("");
		$("#input_password").val("");
		$("#input_confirm_password").val("");
		//身份选中第一个
		$("#select_type").find("option[value=0]").attr("selected",true);
		//隐藏所有错误信息
		$(".modal-error").hide();
		$("#addModal").modal("show");
	}
	
	//新增、修改、删除时未登录和没有权限提示
	function alertNoLogin(noLogin,noPermission) {
		if("${USERID}" == "") {
			alertFail(noLogin);
			setTimeout(function() {
				window.parent.parent.location.href = "<%=basePath%>login";
			},1000);
			return false;
		}
		if("${USERTYPE}" != "1") {
			alertFail(noPermission);
			return false;
		}
		return true;
	}
	
	//点击模态框新增按钮
	function addUser() {
		if(!alertNoLogin("您还未登录！无法新增用户！","您没有权限新增用户！")) {
			return false;
		}
		var username = $("#input_username").val();
		var password = $("#input_password").val();
		var confirm = $("#input_confirm_password").val();
		var type = $("#select_type").val();
		
		if(username == "") {
			alertFail("请输入用户名！");
			return false;
		}
		if(password == "") {
			alertFail("请输入密码！");
			return false;
		}
		
		if(confirm == "") {
			alertFail("请输入确认密码！");
			return false;
		}
		
		var data = {
				"username" : username,
				"password" : password,
				"confirm" : confirm,
				"type" : type
		}
		
		$.post("<%=basePath%>admin/addUser",data,function(rs) {
			//console.log(rs);
			if(rs.result == "0") {
				//验证不通过
				alertFail("信息输入错误！");
				$(".modal-error").show();
				if(rs.username != null) {
					$("#error_username").html("<i class='fa fa-close fa-fw'></i> " + rs.username);
				}else {
					$("#error_username").hide();
				}
				
				if(rs.password != null) {
					$("#error_password").html("<i class='fa fa-close fa-fw'></i> " + rs.password);
				}else {
					$("#error_password").hide();
				}
				
				if(rs.confirm != null) {
					$("#error_confirm").html("<i class='fa fa-close fa-fw'></i> " + rs.confirm);
				}else {
					$("#error_confirm").hide();
				}
			}else if(rs.result == "1") {
				$("#addModal").modal("hide");
				alertSuccess("用户新增成功！");
				setTimeout(function() {
					//跳转
					window.location.href = "<%=basePath%>admin/showUser?page=${Index}&&searchValue=${SearchValue}";
				},1000);
			}
		},"json");
		
	}
	//弹出编辑用户模态框
	function showUpdateModal(id,username,type) {
		$("#input_update_id").val(id);
		$("#input_update_username").val(username);
		$("#select_update_type").find("option[value="+ type +"]").attr("selected",true);
		$("#input_update_reset").prop("checked",false);
		$("#editModal").modal("show");
		
	}
	
	//修改用户
	function updateUser() {
		if(!alertNoLogin("您还未登录！无法修改用户！","您没有权限修改用户！")) {
			return false;
		}
		var username = $("#input_update_username").val();
		var id = $("#input_update_id").val();
		var type = $("#select_update_type").val();
		var reset = 0;
		if($("#input_update_reset").prop("checked") == true) {
			reset = 1;
		}
		
		var data = {
				"id" : id,
				"type" : type,
				"reset" : reset
		};
		$.post("<%=basePath%>admin/updateUser",data,function(rs) {
			if(rs == "1") {
				$("#editModal").modal("hide");
				alertSuccess("修改成功！");
				setTimeout(function() {
					window.location.href = "<%=basePath%>admin/showUser?page=${Index}&&searchValue=${SearchValue}";
				},1000);
				
			}else if(rs == "0") {
				alertFail("修改失败！");
				return false;
			}else {
				alertFail("修改失败！");
				return false;
			}
		},"json");
		
	} 
	
	//批量删除
	function deleteMulti() {
		if(!alertNoLogin("您未登录！无法删除用户！","您没有权限删除用户！")) {
			return false;
		}
		var ids = "";    //用户id字符串，用','隔开，末尾添加-1
		var num = 0;
		//获取选中的复选框
		$(".check-user").each(function() {
			if($(this).prop("checked")) {
				ids += $(this).val() + ",";
				num ++;
			}
		});
		ids += "-1";
		//未选中
		if(num == 0) {
			alertFail("未选中用户！无法删除！");
			return false;
		} 
		deleteUser("确定批量删除选中的用户？",ids);
	}
	
	//单独删除
	function deleteSingle(id) {
		var ids = id + ",-1";
		deleteUser("确定删除该用户？",ids);
	}
	
	//删除用户
	function deleteUser(title,ids) {
		var data = {
			"ids" : ids	
		};
		//弹出提示框
		swal({
			title : title,
			text : "",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "删除",
			cancelButtonText : "取消",
			closeOnConfirm : false
		},function(isConfirm) {
			if(isConfirm) {
				$.post("<%=basePath%>admin/deleteUser",data,function(rs) {
					if(rs == "1") {
						alertSuccess("删除成功！");
						setTimeout(function() {
							window.location.href = "<%=basePath%>admin/showUser?page=${Index}&&searchValue=${SearchValue}";
						},1000);
					}else {
						alertFail("删除失败！");
					}
				},"json");
			}
		});
		
	}
	
</script>
</body>
</html>