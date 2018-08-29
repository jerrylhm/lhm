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
	
</style>
</head>
<body>
	<div>
		<div class="location">
			<p class="text-primary">
				<i class="fa fa-home fa-fw fa-lg"></i>
				当前位置 ： 审核管理
			</p>
		</div>
		<div class="container-box">
			<div class="container-header">
				<h4>${title }</h4>
			</div>
<!-- 			<div class="container-op">
				<a onclick="showModal()" class="btn btn-primary" title="新增"><i class="fa fa-plus fa-fw"></i>新增</a>
				<a onclick="deleteChecked()" class="btn btn-danger btn-margin" title="删除选中"><i class="fa fa-trash fa-fw"></i>删除选中</a>
				<div class="search ">
					<div class="input-group">
						<input class="form-control" id="input_search" placeholder="输入用户名搜索">
						<span class="input-group-btn">
							<button class="btn btn-primary" onclick="searchUser()">
								搜索
							</button>
						</span>
					</div>
				</div>
			</div> -->
			<div class="container-table">
				<iframe id="userFrame" name="userFrame" src="<%=basePath %>${href}" width="100%" height="800px" frameborder="0" ></iframe>
				
			</div>
		</div>
	</div>
<script type="text/javascript">
	//点击新增用户
	function showModal() {
		$("#userFrame")[0].contentWindow.showAddModal();
	}
	
	//点击删除选中
	function deleteChecked() {
		$("#userFrame")[0].contentWindow.deleteMulti();
	}
	
	//点击搜索用户
	function searchUser() {
		var value = $("#input_search").val();
		//对输入值进行转义
		var re = new RegExp("\"","g");
		var searchValue = value.replace(re,"\\\"");
		$("#userFrame")[0].src = "<%=basePath%>admin/showUser?searchValue=" + searchValue;
	}
	
	//输入按钮监控
	$("#input_search").keyup(function(e) {
		if(e.keyCode == 13) {
			searchUser();
		}
	});
</script>
</body>
</html>