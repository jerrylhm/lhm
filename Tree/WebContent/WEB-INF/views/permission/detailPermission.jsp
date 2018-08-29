<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=basePath%>font-awesome/css/font-awesome.css?v=4.4.0" rel="stylesheet">
<link href="<%=basePath%>hAdmin/css/animate.css" rel="stylesheet">
<link href="<%=basePath%>hAdmin/css/style.css?v=4.1.0" rel="stylesheet">

<title>详细权限设置</title>
<style type="text/css">
	.tab{
		width: 100%;
		height: 100%;
	}
	.tab tr{
		height: 300px;
	}
	.tab tr td{
		font-size: 17px;
	}
	.check-user{
		width: 18px;
		height: 18px;
	}
</style>
</head>
<body>
	<div class="ibox-title">
	    <h5>设置节点 <span style="color: red;">${name}</span>的权限</h5>
	</div>
	<div class="ibox-content">
		<table class="tab" cellpadding="0" cellspacing="0">
			<tr>
				<td width="15%" align="center">客户端权限:</td>
				<td align="left">
					<div style="margin-left: 23px;">
						<c:forEach items="${ClientList}" var="client">
							<input id="client_${client.per_id}" class="check-user" onchange="ChangePermission()" type="checkbox" value="${client.per_id}" style="cursor: pointer;"> ${client.per_name}
						</c:forEach>
					</div>
				</td>
			</tr>
			<tr style="border-top: 1px solid #cccccc">
				<td width="15%" align="center">服务端权限:</td>
				<td align="left">
					<div style="margin-left: 20px;">
						<input id="admin_1" class="check-user" onchange="ChangePermission()" type="checkbox" value="admin_1" style="cursor: pointer;">后台权限
					</div>
				</td>
			</tr>
		</table>
	</div>
	
	<script type="text/javascript">
		//显示该节点当前已有权限
		var NodePermission = "${NodePermission}";
		var arr = NodePermission.split(",");
		for(var i=0;i<arr.length;i++){
			if(arr[i] == "admin_1"){
				$("#admin_1").prop("checked",true);
			}else if(arr[i] == "admin_2"){
				$("#admin_2").prop("checked",true);
			}else if(arr[i] == "admin_3"){
				$("#admin_3").prop("checked",true);
			}else if(arr[i] == "admin_4"){
				$("#admin_4").prop("checked",true);
			}else{
				$("#client_"+arr[i]).prop("checked",true);
			}
		}
		
		//修改当前节点的权限
		function ChangePermission(){
			
			var idAll = "";
			$(".check-user").each(function(){
				if($(this).prop("checked")){
					if(idAll == ""){
						idAll = $(this).val();
					}else{
						idAll += ","+$(this).val(); 
					}
				}
			});
			
			$.ajax({
				type:"post",
				url:"<%=basePath %>permission/changePermission",
				data:{"idAll":idAll,"groupid":"${groupid}","nodeid":"${nodeid}"},
				success:function(msg){
				}
			});
			
		}
		
	</script>
</body>
</html>