<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!--360浏览器优先以webkit内核解析-->


<title>- 主页示例</title>

<link rel="shortcut icon" href="favicon.ico">
<link href="<%=basePath%>hAdmin/css/bootstrap.min.css?v=3.3.6"
	rel="stylesheet">
<link href="<%=basePath%>font-awesome/css/font-awesome.css?v=4.4.0"
	rel="stylesheet">

<link href="<%=basePath%>hAdmin/css/animate.css" rel="stylesheet">
<link href="<%=basePath%>hAdmin/css/style.css?v=4.1.0" rel="stylesheet">
<style type="text/css">
   .ibox-title{
       background-color:white;
   }
</style>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="row">
			<div class="col-sm-12">
			<div class="ibox">
			   <div class="ibox-title">
			      <h5>创建的项目</h5>
			   </div>
			   <div class="ibox-content" style="background-color: #eef1f1">
			 <div class="row">
				    <div class="col-sm-6">
				       <button id="addParent" class="btn btn-primary btn-lg" type="button" style="margin-bottom: 10px">新增项目</button>
				    </div>
				    <div class="col-sm-6">
				      <form action="<%=basePath %>tree/index" id="searchPage" method="post">
					       <div class="input-group">
	                                <input type="hidden" name="page.currentPage" id="currentPage" value="${searchParam.page.currentPage}">
	                                <input type="hidden" name="page.totalPage" id="totalPage" value="${searchParam.page.totalPage}">    			
	                                <span id="likeBox" style="display: none">${like }</span>
	                                <input id="input_search" name="like" placeholder="请输入项目名称" class="input-sm form-control" type="text" value="${like }"> <span class="input-group-btn">
	                                <button type="button" class="btn btn-sm btn-primary" onclick="searchByLike()"> 搜索</button> </span>
		                   </div>
	                  </form>
	                </div>
                </div>
                <!-- 树Item的模板 ------------------------------------------------------>
                <div class="col-sm-4 tree-model" style="display:none">		
								<div class="panel padder-v item">
									  <a href="#">
										   <div class="treeItem" style="width: 100%;height: 100%">
												<div class="item-name font-thin h1"></div>
												<span class="item-urname text-muted text-xs" style="color: #635d5d"></span>
												<div class="bottom text-left"></div>
											</div>
									  </a>
								      <div class="displayBtn" style="position: absolute;top: 40px;display: none">
											<button class="btn btn-success btn-circle btn-lg item-btn-editor" type="button" title="编辑名称" style="position: absolute;" onclick=""><i class="fa fa-pencil"></i>								
			                                </button>
			                                <button class="btn btn-danger btn-circle btn-lg item-btn-add" type="button" title="删除" style="position: absolute;margin-left: 60px" onclick=""><i class="fa fa-trash"></i>								
			                                </button>
		                              </div>
								</div>         
				</div>
				<!---------------------------------------------------------------------->
				<div class="row row-sm text-center item-space">
					<c:forEach items="${trees }" var="tree">
						<div id="treeDiv${tree.node_id}" class="col-sm-4">
							
								<div class="panel padder-v item" title="点击进入">
								  <a href="<%=basePath%>tree/editNode?treeId=${tree.node_id}&creator=true">
								   <div id="treeItem${tree.node_id}" class="treeItem" style="width: 100%;height: 100%">
									<div class="item-name font-thin h1">${tree.node_name }</div>
									<span class="text-muted text-xs" style="color: #635d5d"><i class="fa fa-user fa-fw "></i>${tree.ur_name }</span>
									<div class="bottom text-left"></div>
									</div>
									</a>
									<c:if test="${tree.isCreator == 1 }">
										<div class="displayBtn" style="position: absolute;top: 40px;display: none">
										<button class="btn btn-success btn-circle btn-lg item-btn-editor" type="button" title="编辑名称" style="position: absolute;" onclick="editorModal(${tree.node_id},'${tree.node_name }','${tree.node_title }')"><i class="fa fa-pencil"></i>								
		                                </button>
		                                <button class="btn btn-danger btn-circle btn-lg item-btn-add" type="button" title="删除" style="position: absolute;margin-left: 60px" onclick="removeNode(${tree.node_id})"><i class="fa fa-trash"></i>								
		                                </button>
		                                </div>
	                                </c:if>
								</div>
						</div>
					</c:forEach>
				</div>
				<div class="text-center">
                            <div class="btn-group">
                                <button class="btn btn-white" type="button" onclick="searchPage(${searchParam.page.currentPage-1})"><i class="fa fa-chevron-left"></i>
                                </button>
                                <button id="firstPage" class="btn btn-white" onclick="searchPage(1)">首页</button>
                                <button class="btn btn-white" onclick="searchPage(${searchParam.page.totalPage })">尾页</button>
                                <button class="btn btn-white  active"><span id="page" style="color: red">${searchParam.page.currentPage}</span>/${searchParam.page.totalPage }</button>
                            </div>
                            <input type="text" id="searchInput" style="height: 30px;width: 40px">
                            <div class="btn-group">
                                <button class="btn btn-white" onclick="forware(this)">跳转</button>
                                <button class="btn btn-white" type="button" onclick="searchPage(${searchParam.page.currentPage+1})"><i class="fa fa-chevron-right"></i>
                                </button>
                            </div>
               </div>
               </div>
               </div>
               <div class="ibox">
	                <div class="ibox-title">
				      <h5>加入的项目</h5>
				    </div>
				    <div class="ibox-content" style="background-color: #eef1f1">
		               <c:if test="${tree != null}">
		                  <div class="row row-sm text-center">
			               <div id="treeDiv${tree.node_id}" class="col-sm-4">	
											<div class="panel padder-v item" title="点击进入">
											  <a href="<%=basePath%>tree/editNode?treeId=${tree.node_id}&creator=false">
											   <div id="treeItem${tree.node_id}" class="treeItem" style="width: 100%;height: 100%">
												<div class="item-name font-thin h1">${tree.node_name }</div>
												<span class="text-muted text-xs" style="color: #635d5d"><i class="fa fa-user fa-fw "></i>${tree.ur_name }</span>
												<div class="bottom text-left"></div>
												</div>
												</a>
											</div>
							</div>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--新增模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="addModal">
	 	<div class="modal-dialog">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>新增项目</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="height:100px;margin-left:30px">
	 				<input class="input-sm form-control" type="text" id=addName placeholder="输入项目的名称">
	 				<input class="input-sm form-control" type="text" id=addTitle placeholder="输入项目的标题" style="margin-top: 10px;margin-bottom: 10px;">
		 		</div>
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="addNode()">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	<!--编辑模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="editor">
	 	<div class="modal-dialog">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>编辑项目名称</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="height:100px;margin-left:30px">
	 				<input class="input-sm form-control" type="text" id="nodeName" placeholder="输入项目的名称">
	 				<input class="input-sm form-control" type="text" id="nodeTitle" placeholder="输入项目的标题" style="margin-top: 10px;margin-bottom: 10px;">
		 		</div>
		 		<input type="hidden" id="nodeId">
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="editorNodeName()">保存</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	
	<!-- 全局js -->
	<script src="<%=basePath%>hAdmin/js/jquery.min.js?v=2.1.4"></script>
	<script src="<%=basePath%>hAdmin/js/bootstrap.min.js?v=3.3.6"></script>
	<!-- layer javascript -->
    <script src="<%=basePath%>hAdmin/js/plugins/layer/layer.min.js"></script>
	<script type="text/javascript">	
	    //初始化编辑和删除按钮的位置
        $('.displayBtn').css("left",parseInt($('.treeItem:eq(1)').css('width'))*0.7)
        
        //删除树
        function removeNode(id) {
        	layer.confirm('确定删除该项目？', {
        	    btn: ['确定','取消'], //按钮
        	    shade: false //不显示遮罩
        	}, function(){
        		$.ajax({
    	 			url:"<%=basePath%>tree/removeTree",
    	 			dataType:'text',
    	 			type:"post",
    	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
    	 			data:{id:id},
    	 			success:function(result){
    	 				 layer.msg('删除成功', {icon: 1,time:1000});
     					$("#treeDiv" + id).remove();
    	 			}
    	 		});
        	   
        	}, function(){

        	});
        	
        }
        
	    //打开编辑名称模态框
        function editorModal(id,name,title) {
        	$('#nodeId').val(id);
        	$('#nodeName').val(name);
        	$('#nodeTitle').val(title);
            $('#editor').modal('show');	
        }
        
	 	//分页查询文档信息，参数为要查询哪一页
	 	function searchPage(currenPage) {
	 		if(currenPage<=0){
	 			currenPage = 1;
	 		}
	 		$('#currentPage').val(currenPage);
	 		$('#searchPage').submit();	
	 	}
	 	
	 	//根据输入框跳转页数跳转
	 	function forware(obj){
	 		var page = $('#searchInput').val();
	 		if(page!=""&&page<="${searchParam.page.totalPage }"&&page>0){
	 			searchPage(page);
	 		}else{
	 			layer.msg('输入页数不合法', {icon: 2}); 
	 		}
	 	}
        
	 	//触发根据搜索关键字查找
	 	function searchByLike(){
	 		$('#searchPage').submit();
	 	}
	 	
	 	//编辑节点名称
	 	function editorNodeName() {
	 		var id = $('#nodeId').val();
	 		var name = $('#nodeName').val();
	 		var title = $('#nodeTitle').val();
		 	var suc = false;
			if (name == '' || title == '') {
				layer.msg('项目的名称和标题不能为空', {icon: 2}); 
			} else {
				$.ajax({
		 			url:"<%=basePath%>tree/updateTreeName",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{id:id,name:name},
		 			success:function(result){
		 				//如果返回值大于0，表示操作成功
	 					if(parseInt(result) > 0) {
	 						$('#editor').modal('hide');
	 						suc = true;
	 						$("#treeItem" + id).children('.item-name').html(name);					
	 					} else{
	 						layer.msg('编辑节点名称出错了', {icon: 2}); 
	 					}
	 					return suc;
		 			}
		 		});
			}
	 	}
	 	
	 	//新增节点
	 	function addNode() {
	 		var name = $('#addName').val();
	 		var title = $('#addTitle').val();
	 		if(name == '' || title == '') {
	 			layer.msg('项目的名称和标题不能为空', {icon: 2}); 
	 			return; 
	 		}
	 		$.ajax({
	 			url:"<%=basePath%>tree/addTree",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{isParent:true,pid:0,name:name,title:title},
	 			success:function(result){
	 				//把返回的json字符串转换为json对象
	 				eval('var tree=' + result);
	 				//如果返回的节点id大于0，表示新增成功，否则新增失败
 					if(tree.node_id > 0) {
 						$('#addModal').modal('hide');
 						addTreeItem(tree.node_id, tree.node_name, tree.ur_name,tree.node_title);
 						layer.msg('新增项目成功', {icon: 1,time:1000});
 					} else{
 						layer.msg('新增项目出错了', {icon: 2}); 
 					}
	 			}
	 		});
	 	}
	 	
	 	//在页面上新增一个树元素
	 	function addTreeItem(id,name,urname,title) {
 		    $treeItem = $('.tree-model:eq(0)').clone(true);
			$treeItem.removeClass('tree-model');
			$treeItem.attr('id','treeDiv' + id);
			$treeItem.find('a').attr('href','<%=basePath%>tree/editNode?treeId=' + id + '&creator=true');
			$treeItem.find('.treeItem').attr('id','treeItem' + id)
			$treeItem.find('.item-name').html(name);
			$treeItem.find('.item-urname').html(urname);
			$treeItem.find('.item-btn-editor').attr('onclick',"editorModal(" + id + ",'" + name + "','"+title+"')");
			$treeItem.find('.item-btn-add').attr('onclick',"removeNode(" + id + ")");		
			$treeItem.show();
			$('.item-space').prepend($treeItem);
			$treeItem.find('.displayBtn').css("left",parseInt($('.treeItem:eq(1)').css('width'))*0.7)
	 	}
	 	
	 	//新增树按钮绑定事件
	 	$(function() {
	 		$('#addParent').bind('click',function() {
	 			$('#addModal').modal('show');
	 		});
	 	})
	 	
	 	//按钮事件绑定
	 	$('#searchInput').on('keydown',function(e) {
	 		if(e.keyCode == 13) {
	 			forware(this);
	 		}
	 	})

	 	//鼠标移动到树item上显示按钮
	 	$('.item').on('mouseenter',function() {
	 		$(this).children('.displayBtn').removeClass('animated bounceOut');
	 		$(this).children('.displayBtn').css('display','block');
	 		$(this).children('.displayBtn').addClass('animated bounceIn');
	 	})
	 	
	 	//鼠标移除树item隐藏按钮
	 	$('.item').on('mouseleave',function() {
	 		$(this).children('.displayBtn').removeClass('animated bounceIn');
	 		$(this).children('.displayBtn').addClass('animated bounceOut');
	 	})
	</script>
</body>

</html>
