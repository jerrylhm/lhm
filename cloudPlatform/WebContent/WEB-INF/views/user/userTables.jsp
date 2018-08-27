<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="User-Check" >
<meta name="author" content="yangxy">
<link rel="stylesheet" href="<%=basePath %>css/ace.css"/>
<link rel="stylesheet" href="<%=basePath %>css/style.css"/>
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<style>
.input-forware {
	position: absolute;
	width: 56px;
	right: 188px;
	top: 12px;
	height:30px;
	z-index: 5;
    transition: all 0.5s;
    -webkit-transition: all 0.5s;
}
</style>
<title>未审核列表</title>
</head>
<body style="background: white;">
	<div >
	 <table class="layui-table" style="margin-bottom: 0px;">
          <thead>
              <tr>
                  <th style="width: 4%; text-align: center"><input id="checkBoxAll" type="checkbox" onchange="selectAll()"/></th>
                  <th style="width: 8%; text-align: center">头像</th>
                  <th style="width: 12%; text-align: center">账号</th>
                  <th style="width: 12%; text-align: center">昵称</th>
                  <th style="width: 6%; text-align: center">性别</th>
                  <th style="width: 11%; text-align: center">角色</th>
                  <th style="width: 20%; text-align: center">班级</th>
                  <th style="width: 8%; text-align: center">注册时间</th>
                  <th style="width: 6%; text-align: center">审核状态</th>
                  <th style="width: 15%; text-align: center;border-right: 1px solid #dddddd;">操作</th>
              </tr>
          </thead>
          <tbody>
          	<c:forEach items="${AllUserList}" var="user" varStatus="status">
          		<tr>
          			<td style="text-align: center;vertical-align: middle;"><input value="${user.ur_id }" title="${user.ur_username }" type="checkbox" class="choose"></td>
          			<td style="text-align: center;vertical-align: middle;"><img class="head-image" src="<%=basePath %>${user.ur_image}"/></td>
          			<td style="text-align: center;vertical-align: middle;">${user.ur_username}</td>
          			<td style="text-align: center;vertical-align: middle;">${user.ur_nickname}</td>
          			<c:if test="${user.ur_sex == 0}">
          				<td style="text-align: center;vertical-align: middle;">男</td>
          			</c:if>
          			<c:if test="${user.ur_sex == 1}">
          				<td style="text-align: center;vertical-align: middle;">女</td>
          			</c:if>
          			<td style="text-align: center;vertical-align: middle;">
	          			<c:if test="${fn:contains(user.ur_type,'1')==true}">
	          				管理员&nbsp;&nbsp;
	          			</c:if>
	          			<c:if test="${fn:contains(user.ur_type,'2')==true}">
          					教师&nbsp;&nbsp;
          				</c:if>
          				<c:if test="${fn:contains(user.ur_type,'3')==true}">
          					学生&nbsp;&nbsp;
          				</c:if>
          				<c:if test="${fn:contains(user.ur_type,'4')==true}">
	          				家长&nbsp;&nbsp;
	          			</c:if>
	          			<c:if test="${fn:contains(user.ur_type,'5')==true}">
	          				领导
	          			</c:if>
          			</td>
          			
          			
          			<td style="text-align: center;vertical-align: middle;">${user.className}</td>
          			<td style="text-align: center;vertical-align: middle;">${user.ur_createdate}</td>
          			<c:if test="${user.ur_status == 0}">
          				<td style="text-align: center;vertical-align: middle;color: red;font-style: italic;font-size: 15px;"><span class="layui-badge">未审核</span></td>
          			</c:if>
          			<c:if test="${user.ur_status == 1}">
          				<td style="text-align: center;vertical-align: middle;color: #2AA259;font-style: italic;font-size: 15px;"><span class="layui-badge layui-bg-blue">已审核</span></td>
          			</c:if>
          			<td style="text-align: center;vertical-align: middle;">
          				<c:if test="${user.ur_status == 0}">
          					<button onclick="passSingle(${user.ur_id},'${user.ur_username}')" class="layui-btn layui-btn-sm">通过</button>
          				</c:if>
          				<c:if test="${user.ur_status == 1}">
          					<button onclick="cancelPassSingle(${user.ur_id},'${user.ur_username}')" class="layui-btn layui-btn-sm layui-btn-warm">取消通过</button>
          				</c:if>
          				<button onclick="deleteSingle(${user.ur_id},'${user.ur_username}')" class="layui-btn layui-btn-sm layui-btn-danger">删除</button>
          			</td>
          		</tr>
          	</c:forEach>
          </tbody>
       </table>
       </div>
	   <!-- 表格下栏 -->
	   <div class="page-tool" style="position: relative;">
			<div style="padding-left: 25px;">
				<button onclick="cancelPassAll()" class="layui-btn layui-btn-warm" id="cancelbtn" style="display: none;">取消通过选中</button>
				<button onclick="passAll()" class="layui-btn" id="surebtn" >通过选中</button>
				<button onclick="deleteAll()" class="layui-btn layui-btn-danger">删除选中</button>
			</div>
<!-- 			<div style="padding-right: 25px;"> -->
<!-- 				<ul class="pagination ng-isolate-scope" style="cursor: pointer;"> -->
<%-- 					<li class="ng-scope"><a class="ng-binding">第<span id="page">${index}</span>/${count}页</a></li> --%>
<%-- 					<li class="ng-scope"><a class="ng-binding">总共${count}页</a></li> --%>
<!-- 					<li class="ng-scope"><a class="ng-binding" onclick="FirlstPage()">首页</a></li> -->
<!-- 					<li class="ng-scope"><a class="ng-binding" onclick="LastPage()">上一页</a></li> -->
<!-- 					<li class="ng-scope"><a class="ng-binding" onclick="NextPage()">下一页</a></li> -->
<!-- 					<li class="ng-scope"><a class="ng-binding" onclick="EndPage()">尾页</a></li> -->
<!-- 					<li class="ng-scope"><a class="ng-binding">跳转到 <input id="inpage" type="text" value="" style="width:35px;height: 17px;text-align: center;background: black;color: white;vertical-align: middle;margin-top: -1px;" onkeyup="if(/\D/.test(this.value)){alert('只能输入数字');this.value='';}" />  页 <span onclick="JumpPage()">&nbsp;&nbsp;GO&nbsp;&nbsp;</span></a></li> -->
<!-- 				</ul> -->
<!-- 		   </div> -->
			<input id="inpage" type="text" name="title" class="layui-input input-forware" onkeyup="if(/\D/.test(this.value)){alertTimer('只能输入数字');this.value='';}">
			<div class="pull-right layui-box layui-laypage layui-laypage-default" style="position: absolute;right: 5px;top: -6px;">
			<a href="javascript:LastPage()" class="layui-laypage-prev" data-page="0">
			上一页
			</a>
			<a href="javascript:;" class="layui-laypage-last" title="尾页" onclick="FirlstPage()">首页</a>
			<a style="margin-right: 64px;">第${index}/${count}页</a>
			<a style="margin-right: 10px;" href="javascript:JumpPage();">跳转</a>
			<a href="javascript:;" class="layui-laypage-last" title="尾页" onclick="EndPage()">尾页</a>
			<a href="javascript:NextPage()" class="layui-laypage-next">下一页</a>
			</div>
	</div>
	
	<script type="text/javascript" src="<%=basePath %>js/layer/layer.js"></script>
	<script type="text/javascript">
		//全局变量（分页:index/当前页码;count/总的页数）
		var index = "${index}";
		var count = "${count}";
		//当前状态
		var status = "${status}";
		if(status == 0){
			$("#cancelbtn").css("display","none");
			$("#surebtn").css("display","");
		}else{
			$("#cancelbtn").css("display","");
			$("#surebtn").css("display","none");
		}
		
		//首页
		function FirlstPage(){
			parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?page=1&&status="+status);
		}
		
		//上一页
		function LastPage(){
			if(index == 1){
				parent.alertTimer("当前已是第一页！");
			}else{
				parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?page="+(parseInt(index)-1)+"&&status="+status);
			}
		}
		
		//下一页
		function NextPage(){
			if(index == count){
				parent.alertTimer("当前已是最后一页！");
			}else{
				parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?page="+(parseInt(index)+1)+"&&status="+status);
			}
		}
		
		//尾页
		function EndPage(){
			parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?page="+parseInt(count)+"&&status="+status);
		}
		
		//跳转到指定页
		function JumpPage(){
			var page = $("#inpage").val();
			if(page<1 || page>count){
				parent.alertTimer("输入的页码超出范围，请重新输入！");
			}else{
				parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?page="+page+"&&status="+status);
			}
		}
		
		//全部选中/全不选
		function selectAll(){
			if(document.getElementById("checkBoxAll").checked == true){
				$(".choose").prop("checked",true);
			}else{
				$(".choose").prop("checked",false);
			}	
		}
	
		//单个审核通过
		function passSingle(id,name){
			parent.layer.confirm("确定审核通过<span style='color:#D15B47'> "+name+" </span>用户?",{
				btn:["确定","取消"],offset:"250px"},function(){
					$.ajax({
						type:"post",
						url:"<%=basePath %>admin/ChangeSingleUserStatus",
						data:{"id":id},
						success:function(msg){
							if(msg == "1"){
								parent.layer.msg("审核通过",{icon:1,offset:"300px"});
								setTimeout(function(){
									parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?page="+index+"&&status="+status);
								},600);
							}else{
								parent.layer.msg("审核失败，请联系管理员",{icon:2,offset:"300px"});
							}
						}
					});
				});
		}
		
		//批量审核通过
		function passAll(){
			var idAll = "";
			$(".choose").each(function() {
				if($(this).prop("checked") == true) {
					if(idAll == ""){
						idAll = $(this).val();
					}else{
						idAll += ","+$(this).val(); 
					}
				}
			});
			
			if(idAll == ""){
				parent.layer.msg("请至少选择一个要审核的用户",{icon:7,offset:"300px;"});
			}else{
				$.ajax({
					type:"post",
					url:"<%=basePath %>admin/ChangeAllUserStatus",
					data:{"idAll":idAll},
					dataType:"json",
					success:function(msg){
						if(msg == "1"){
							parent.layer.msg("批量审核通过成功",{icon:1,offset:"300px"});
							setTimeout(function(){
								parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?status="+status);
							},600);
						}else{
							parent.layer.msg("审核失败，请联系管理员",{icon:2,offset:"300px"});
						}
					}
				});
			}
		}
		
		//单个删除
		function deleteSingle(id,name){
			parent.layer.confirm("是否删除注册用户 <span style='color:#D15B47'> "+name+" </span>",{
				btn:["确定","取消"],offset:"250px"},function(){
					$.ajax({
						type:"post",
						url:"<%=basePath %>admin/DeleteSingleUser",
						data:{"id":id},
						dataType:"json",
						success:function(msg){
							if(msg == "1"){
								parent.layer.msg("删除成功",{icon:1,offset:"300px"});
								setTimeout(function(){
									parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?page="+index+"&&status="+status);
								},600);
							}else{
								parent.layer.msg("删除失败",{icon:2,offset:"300px"});
							}
						}
				  });
			});
		}
		
		//批量删除
		function deleteAll(){
			var idAll = "";
			$(".choose").each(function() {
				if($(this).prop("checked") == true) {
					if(idAll == ""){
						idAll = $(this).val();
					}else{
						idAll += ","+$(this).val(); 
					}
				}
			});
			
			if(idAll == ""){
				parent.layer.msg("请至少选择一个要删除的用户",{icon:7,offset:"300px"});
			}else{
				parent.layer.confirm("是否删除选中的注册用户 ",{
					btn:["确定","取消"],offset:"250px"},function(){
						$.ajax({
							type:"post",
							url:"<%=basePath %>admin/DeleteAllUser",
							data:{"idAll":idAll},
							dataType:"json",
							success:function(msg){
								if(msg == "1"){
									parent.layer.msg("批量删除成功",{icon:1,offset:"300px"});
									setTimeout(function(){
										parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?&&status="+status);
									},600);
								}else{
									parent.layer.msg("删除失败，请联系管理员",{icon:2,offset:"300px"});
								}
							}
						});
					});
				}
			}
		
		//单个取消通过审核
		function cancelPassSingle(id,name){
			parent.layer.confirm("确定取消通过审核<span style='color:#D15B47'> "+name+" </span>用户?",{
				btn:["确定","取消"],offset:"250px"},function(){
					$.ajax({
						type:"post",
						url:"<%=basePath %>admin/CancelChangeSingleUserStatus",
						data:{"id":id},
						success:function(msg){
							if(msg == "1"){
								parent.layer.msg("取消审核通过成功",{icon:1,offset:"300px"});
								setTimeout(function(){
									parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?page="+index+"&&status="+status);
								},600);
							}else{
								parent.layer.msg("取消审核失败，请联系管理员",{icon:2,offset:"300px"});
							}
						}
					});
				});
			}
		
		//批量取消通过
		function cancelPassAll(){
			var idAll = "";
			$(".choose").each(function (){
				if($(this).prop("checked") == true){
					if(idAll == ""){
						idAll = $(this).val();
					}else{
						idAll += ","+$(this).val(); 
					}
				}
			});
			
			if(idAll == ""){
				parent.layer.msg("请至少选择一个需要取消审核的用户",{icon:7,offset:"300px"});
			}else{
				$.ajax({
					type:"post",
					url:"<%=basePath %>admin/CancelChangeAllUserStatus",
					data:{"idAll":idAll},
					dataType:"json",
					success:function(msg){
						if(msg == "1"){
							parent.layer.msg("批量取消审核通过成功",{icon:1,offset:"300px"});
							setTimeout(function(){
								parent.$("#userTab").attr("src","<%=basePath %>admin/UserTables?status="+status);
							},600);
						}else{
							parent.layer.msg("批量取消审核失败，请联系管理员",{icon:2,offset:"300px"});
						}
					}
				});
				
			}
		}
		
	</script>					

</body>
</html>