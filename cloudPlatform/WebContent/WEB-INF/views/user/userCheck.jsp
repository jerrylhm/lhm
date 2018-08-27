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
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath %>css/style.css"/>
<link href="<%=basePath %>css/select.css" rel="stylesheet" type="text/css">
<style>
i {
	padding: 0px;
}
/* ace */
.ace-input {
    font-weight: 400;
    border-radius: 0 !important;
    color: #858585;
    background-color: #ffffff;
    border: 1px solid #d5d5d5;
    padding: 5px 4px 6px;
    font-size: 14px;
    font-family: inherit;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
    -webkit-transition-duration: 0.1s;
    transition-duration: 0.1s;
}

.ace-button {
	background: rgb(51, 122, 183);
    border-width: 1px;
    border-color: #8aafce;
    color: #6688a6 !important;
    text-shadow: none !important;
    background-color: #FFF !important;
    outline:none !important;
}
</style>
<title>用户审核</title>
</head>
<body class="gray-bg">
	<div class="bread">
		<div class="bread-container">
			<span class="fa fa-home"></span>
			<span class="fa fa-lg fa-angle-right"></span>
			<span title="用户管理"> 用户管理</span>
			<span class="fa fa-lg fa-angle-right"></span>
			<span title="用户审核"> 用户审核</span>
		</div>
	</div>
	
	<div class="wrapper wrapper-content">
		 <div class="row">
		 	 <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title" style="top: 31px;position: relative;border-bottom: none;background:#fdfcfc">
                        <div style="margin-top: -5px;">
	                        <div class="col-lg-3">
								<div class="input-group">
									<input type="text" class="form-control ace-input" placeholder="输入账号或者昵称查找" id="inputValue">
									<span class="input-group-btn">
										<button class="btn btn-success ace-button" type="button" style="height: 34px;line-height: 12px;" onclick="search()">
											<i class="fa fa-search"></i>
										</button>
									</span>
								</div>
							</div>
						</div>
						<!-- 日期选择器 -->
						<div class="col-lg-2">
							<input type="text" class="date-input layui-input" placeholder="请选择日期" id="date" onpropertychange="queryDate()" >
						</div>
						<!-- 角色选择 -->
						<div class="col-lg-3 layui-tab">
<!-- 							 <select id="sel"> -->
<!-- 							      <option value="0">全 部</option> -->
<!-- 							      <option value="1">管理员</option> -->
<!-- 							      <option value="2">教 师</option> -->
<!-- 							      <option value="3">学 生</option> -->
<!-- 							      <option value="4">家 长</option> -->
<!-- 							      <option value="5">其 他</option> -->
<!-- 							   </select> -->
<!-- 							<a id="selectAll" class="sel active" onclick="choiceRole(0)">全 部</a> -->
<!-- 							<a id="selectTea" class="sel" onclick="choiceRole(2)">教 师</a> -->
<!-- 							<a id="selectStu" class="sel" onclick="choiceRole(3)">学 生</a> -->
<!-- 							<a id="selectOth" class="sel" onclick="choiceRole(100)">其 他</a> -->
						  <ul id="tab_role" class="layui-tab-title" style="top: -10px;font-weight: 400;">
						    <li class="layui-this" onclick="choiceRole(0)" data-role="0">全 部</li>
						    <li onclick="choiceRole(2)" data-role="2">教 师</li>
						    <li onclick="choiceRole(3)" data-role="3">学 生</li>
						    <li onclick="choiceRole(100)" data-role="100">其 他</li>
						  </ul>
						</div>

                        <div class="ibox-tools">
	                        <div  class="layui-form" style="width:200px;position: relative;top: -3px;">
		                        <select id="select_status" lay-filter="showStatus" name="interest">
							        <option value="0">未审核</option>
							        <option value="1">已审核</option>
				       			</select>
					        </div>
                        </div>
                    </div>
                    <div class="ibox-content">
                    	<iframe id="userTab" width="100%" height="650" src="<%=basePath %>admin/UserTables" frameborder="0" scrolling="no" name="tab"></iframe>
                    </div>
                </div>
            </div>
		 </div>
	</div>
	<script type="text/javascript" src="<%=basePath %>layui/layui.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/select.js"></script>
	<script type="text/javascript">
		layui.use([ 'form', 'laydate' ], function() {
			var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
			var laydate = layui.laydate;
			form.on('select(showStatus)', function(data) {
				var status = data.value;
				showStatus(status);
			});
			laydate.render({
				elem : '#date', //指定元素
				theme : 'grid',
				done : function(value, date, endDate) { //选择日期完成之后触发
					$("#userTab").attr("src","<%=basePath%>admin/UserTables?date="+value);
				}
			});
		});
	
// 		$(function(){
// 			$('#sel').searchableSelect();
// 		});

	//按条件搜索
	function search(){
		var inputValue = $("#inputValue").val();
		if(inputValue == ""){
			alertTimer("查找的内容不能为空！");
			return false;
		}else{
			$("#userTab").attr("src","<%=basePath %>admin/UserTables?queryValue="+inputValue);
		}
	}
	
	//按角色筛选
	function choiceRole(type){
		$('#tab_role').find('li').removeClass('layui-this');
		$('#tab_role').find('li[data-role=' + type + ']').addClass('layui-this');
		$("#userTab").attr("src","<%=basePath %>admin/UserTables?role="+type);
		if(type == 0){
			$("#selectAll").addClass("active");
			$("#selectTea").removeClass("active");
			$("#selectStu").removeClass("active");
			$("#selectOth").removeClass("active");
		}else if(type == 2){
			$("#selectAll").removeClass("active");
			$("#selectTea").addClass("active");
			$("#selectStu").removeClass("active");
			$("#selectOth").removeClass("active");
		}else if(type == 3){
			$("#selectAll").removeClass("active");
			$("#selectTea").removeClass("active");
			$("#selectStu").addClass("active");
			$("#selectOth").removeClass("active");
		}else if(type == 100){
			$("#selectAll").removeClass("active");
			$("#selectTea").removeClass("active");
			$("#selectStu").removeClass("active");
			$("#selectOth").addClass("active");
		}
		
	}
	
	//按审核状态帅选
	function showStatus(status){
		$("#userTab").attr("src","<%=basePath %>admin/UserTables?status="+status);
	}
	
	</script>
	
</body>
</html>