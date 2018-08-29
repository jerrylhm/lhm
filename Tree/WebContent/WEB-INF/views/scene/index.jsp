<%@page import="net.sf.json.JSONObject"%>
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


<title>场景管理</title>

<link rel="shortcut icon" href="favicon.ico">
<link href="<%=basePath%>hAdmin/css/bootstrap.min.css?v=3.3.6"
	rel="stylesheet">
<link href="<%=basePath%>font-awesome/css/font-awesome.css?v=4.4.0"
	rel="stylesheet">

<link href="<%=basePath%>hAdmin/css/animate.css" rel="stylesheet">
<link href="<%=basePath%>hAdmin/css/style.css?v=4.1.0" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link href="<%=basePath %>selected/jquery.searchableSelect.css" rel="stylesheet" type="text/css">

<style type="text/css">
.line {
height: auto;
} 
iframe {
  border: none;
}

.searchable-select-dropdown {
  z-index:100;
}
.container-table {
  border: 1px solid #ccc;
  padding: 0px 15px;
  border-radius: 5px; 
}
.page-input {
    width: 30px;
    height: 19px;
    padding-left: 2px;
    padding-right: 2px;
}
</style>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox">
                    <div class="ibox-content">
                      <div class="row">
                         <div class="col-sm-10"><h2>场景管理</h2></div>
                         <div class="col-sm-2">
	                            <a id=add_modal_button class="btn btn-lg btn-success btn-outline">
	                            + 添加场景
	                        	</a>
                        </div>
                      </div>
                                              
                        <div class="input-group" style="margin-top: 10px;">
                             <form action="<%=basePath %>scene/index" id="searchPage" method="post">
                                 <input type="hidden" name="page.currentPage" id="currentPage" value="${searchParam.page.currentPage}">
                                 <input type="hidden" name="page.totalPage" id="totalPage" value="${searchParam.page.totalPage}"> 	
                                 <span id="likeBox" style="display: none">${like }</span>
                                 <input id="input_search" type="text" name="like" placeholder="输入场景名称查找模板" value="${like }" class="input form-control">
                             </form>                          
                            <span class="input-group-btn">
                                        <button type="button" class="btn btn btn-primary" onclick="searchByLike()"> <i class="fa fa-search"></i> 搜索</button>
                            </span>
                        </div>
                        <div class="clients-list">
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane active">
                                    <div class="slimScrollDiv" style="position: relative; width: auto; height: 100%;"><div class="full-height-scroll" style="width: auto; height: 100%;">
                                        <div class="container-table">
	                                        <div class="table-responsive">
	                                            <table class="table table-striped table-hover">
	                                                <thead>
	                                                  <tr><td style="width: 20%">场景名称</td><td style="width: 20%">所属树</td><td style="width: 40%">动作</td><td>操作</td></tr>
	                                                </thead>
	                                                <tbody>  
	                                                   <c:forEach items="${scenes }" var="s">                                               
		                                                    <tr id=scene_${s.sc_id}>
		                                                        <td class="sc_name"> ${s.sc_name }</td>
		                                                        <td class="sc_tree_id" style="display: none;"> ${s.sc_treeid }</td>
		                                                        <td class="sc_tree_name"> ${s.treeName }</td>
		                                                        <td class="sc_action_id" style="display: none;"> ${s.sc_action }</td>
		                                                        <td class="sc_action_name"> ${s.actionName }</td>
		                                                        <td><a class="btn btn-info btn-rounded" onclick="initScene(${s.sc_id})">编辑</a><a class="btn btn-danger btn-rounded" style="margin-left: 15px;" onclick="deleteScene(${s.sc_id})">删除</a></td>
		                                                    </tr>
	                                                   </c:forEach>  
	                                                </tbody>
	                                            </table>
	                                            <div class="clearfix container-page">
			<div class="pull-right">
				<ul id="page" class="pagination pagination-sm" style="cursor: pointer;">
					<li>
						<a>第<span style="color:red">${searchParam.page.currentPage}</span>/${searchParam.page.totalPage } 页</a>
					</li>
					<li id="li_first">
						<a onclick="searchPage(1)" title="首页">首页</a>
					</li>
					<li id="li_pre">
						<a onclick="searchPage(${searchParam.page.currentPage-1})" title="上一页">上一页</a>
					</li>
					<li id="li_next">
						<a onclick="searchPage(${searchParam.page.currentPage+1})" title="下一页">下一页</a>
					</li>
					<li id="li_last">
						<a onclick="searchPage(${searchParam.page.totalPage })" title="尾页">尾页</a>
					</li>
					<li>
						<a style="height : 27px;">跳转到
							<input class="page-input" type="text" id="input_page"> 页
							<span onclick="forware(this)" title="跳转"><i class="fa fa-send fa-fw"></i></span>
						</a>
					</li>
				</ul>
			</div>
		</div>
	                                        </div>
                                        </div>
                                    </div><div class="slimScrollBar" style="width: 4px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 7px; z-index: 99; right: 1px; height: 365.112px; background: rgb(0, 0, 0);"></div><div class="slimScrollRail" style="width: 4px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; opacity: 0.2; z-index: 90; right: 1px; background: rgb(51, 51, 51);"></div></div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
            
        </div>
    </div>

	<!--新增模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="addModal">
	 	<div class="modal-dialog modal-lg">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>新增场景</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
                    <div class="form-group row">
		 			    <label class="col-sm-2">场景名称：</label>
		 				<div class="col-sm-6">
		 						<input id="add_scene_name" type="text" placeholder="输入场景名称" class="form-control">
		 				</div>
	 				</div>
	 				 <div class="form-group row">
		 			    <label class="col-sm-2">所属树：</label>
		 				<div class="col-sm-6">
		 				      <select id="add_scene_tree">
		 						<c:forEach items="${trees }" var="tree">
		 						     <option value="${tree.node_id }">${tree.node_name }</option>
		 						</c:forEach>
		 					  </select>
		 				</div>
	 				</div>
	 				<div class="form-group row"><label class="col-sm-3">场景动作：</label></div>
	 				<div class="form-group row add_template-space" style="border: 1px solid #ccc;margin-right: 15px;">
	 				    <div style="margin: 10px 10px;">
	 				        <button class="btn btn-white" type="button" onclick="openNode('addTree',true)">一键打开</button>
							<button class="btn btn-white" type="button" onclick="openNode('addTree',false)">一键关闭</button>
		                    <div class="input-group" style="margin-top: 5px">
		                            <input class="form-control input-sm" id="addNodeSearchKey" placeholder="输入节点名称搜索" type="text">
		                            <div class="input-group-btn">
		                                <button id="addSearchNode" type="button" class="btn btn-sm btn-primary">
		                                                                                                 搜索
		                                </button>
		                            </div>
		                    </div>
	 				        <ul id="addTree" class="ztree" style="font-size: 10px"></ul>
	 				    </div>
	 				</div>
	 				
		 		<div class="modal-footer">
		 			<button type="button" id="addSceneBtn" class="btn btn-sm btn-success" onclick="addScene()">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 </div>
	 <!--更新模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="updateModal">
	 	<div class="modal-dialog modal-lg">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>更新模板</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
                    <div class="form-group row">
		 			    <label class="col-sm-2">场景名称：</label>
		 				<div class="col-sm-6">
		 						<input id="update_scene_name" type="text" placeholder="输入场景名称" class="form-control">
		 				</div>
	 				</div>
	 				 <div class="form-group row">
		 			    <label class="col-sm-2">所属树：</label>
		 				<div class="col-sm-6">
		 				      <span id="update_scene_tree"></span>
		 				</div>
	 				</div>
	 				<div class="form-group row update-scene-space" style="border: 1px solid #ccc;margin-right: 15px;">
	 				    <div style="margin: 10px 10px;">
                            <button class="btn btn-white" type="button" onclick="openNode('updateTree',true)">一键打开</button>
							<button class="btn btn-white" type="button" onclick="openNode('updateTree',false)">一键关闭</button>
		                    <div class="input-group" style="margin-top: 5px">
		                            <input class="form-control input-sm" id="updateNodeSearchKey" placeholder="输入节点名称搜索" type="text">
		                            <div class="input-group-btn">
		                                <button id="updateSearchNode" type="button" class="btn btn-sm btn-primary">
		                                                                                                 搜索
		                                </button>
		                            </div>
		                    </div>
	 				        <ul id="updateTree" class="ztree" style="font-size: 10px"></ul>
	 				    </div>
	 				</div>
	 				<input type="hidden" id=update_scene_id>
		 	</div>
		 	<div class="modal-footer">
		 			<button type="button" id="updateSceneBtn" class="btn btn-sm btn-success" onclick="updateScene()">保存</button>
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
    <script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.excheck.js"></script>
	<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.exedit.js"></script>
    <script src="<%=basePath %>selected/jquery.searchableSelect.js"></script>
	<script type="text/javascript">             
	var setting = {
 			//设置是否允许同时设置多个节点
 			view: {
 				selectedMulti: false,
				    fontCss: getFontCss,
				    showIcon: false,
				    showTitle: true
 			},
 			//打开节点的勾选框
 			check: {
 				enable: true,
 				chkStyle: "checkbox",
 				chkboxType: { "Y": "s", "N": "" }               				
 			},
 			//设置节点数据
 			data: {
 				key: {
					title:'title'
				    },
 				simpleData: {
 					enable: true,
 					idKey: 'id',
 					pIdKey: 'pId',
 					rootPId: 0
 				}
 			},
 			//关闭编辑节点名称功能
 			edit: {
 				enable: false
 			},
 			callback: {

 			}
 			
 		};
	
		 $(function() {
			 //初始化新增模态框的树
			 updateAddZtree();
		 })
	      
		 //更新新增模态框的树
		 function updateAddZtree() {
			 //需要更新的节点id
			 var treeId = $('#add_scene_tree').val();
			     if(treeId != null) {
			    	 $.ajax({
							url:"<%=basePath%>scene/getZtreeJSON",
							dataType:'text',
							type:"post",
							contentType: "application/x-www-form-urlencoded;charset=UTF-8",
							data:{treeId:treeId},
							success:function(result){
								 eval('var zNodes=' + result);
									$(document).ready(function(){
										//获取到要编辑的场景的所属树后，在编辑模态框初始化这棵树
										$.fn.zTree.init($("#addTree"), setting, zNodes);
									});
							}
						}); 
			     }
		 }
	
		 //更新更新模态框的树
		 function updateUpdateZtree(treeId,actions) {
			 var treeId = treeId;
				 $.ajax({
						url:"<%=basePath%>scene/getZtreeJSON",
						dataType:'text',
						type:"post",
						contentType: "application/x-www-form-urlencoded;charset=UTF-8",
						data:{treeId:treeId},
						success:function(result){
							 eval('var zNodes=' + result);
								$(document).ready(function(){
									$.fn.zTree.init($("#updateTree"), setting, zNodes);
						     		for(var i in actions) {
						     			checkedNode("updateTree",actions[i]);
						     		}
								});
						}
					}); 
		 }
		 
	    function getFontCss(treeId, treeNode) {
			var color = '';
			var fontWeight = '';
			if(!!treeNode.highlight) {
				color = "#A60000";
				fontWeight = "bold";
			} else {
				color = "#333";
				fontWeight = "normal";
			}
			var backgroundColor = '';
			if(!treeNode.hasP) {
				backgroundColor = "#c9c9ce";
			}
			return {color:color,"font-weight":fontWeight,"background-color":backgroundColor};
		}
	
     	//分页查询文档信息，参数为要查询哪一页
     	function searchPage(currenPage) {

     		if(currenPage<=0){
     			currenPage = 1;
     		}
     		$('#currentPage').val(currenPage);
     		$('#searchPage').submit();	

     	}
     	
     	//根据跳转页码跳转
     	function forware(obj){
     		var page = $('#input_page').val();

     		if(page!=""&&page<="${searchParam.page.totalPage }"&&page>0){
     			searchPage(page);
     		}else{
     			layer.msg('输入页数超出范围!', {icon: 2,time:2000}); 
     		}			
     	
     	}
     	
     	//检查obj是否为空
     	function validEntity(obj) {
     		if(obj == null || obj == "") {
     			return false;
     		}else {
     			return true;
     		}
     	}
     	
     	//添加场景
     	function addScene() {
     		var scene_name = $('#add_scene_name').val();
     		var scene_treeId = $('#add_scene_tree').val();
     		if(scene_treeId == null || scene_treeId == "") {
     			layer.msg('所属树为空!', {icon: 2,time:2000}); 
     			return;
     		}
     		if(!validEntity(scene_name)) {
     			layer.msg('场景名称不能为空!', {icon: 2,time:2000}); 
     			return;
     		}
     		
     		var nodeStr = getCheckedAction("addTree");
     		if(nodeStr == null) {
     			return;
     		}
     		$.ajax({
				url:"<%=basePath%>scene/addScene",
				dataType:'text',
				type:"post",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				data:{sc_name:scene_name,sc_treeid:scene_treeId,sc_action:nodeStr},
				success:function(result){
					if(parseInt(result) > 0) {
 						layer.msg('新增场景成功!', {icon: 1});  
 	 					setTimeout(function() {
 							window.location.href = "<%=basePath%>scene/index";
 						},1000);
 					} else{
 						layer.msg('新增场景失败!', {icon: 2});  
 					}
				}
			});
     	}
     	
     	//删除场景
     	function deleteScene(id) {
     		layer.confirm('确定删除场景吗？', {
	       	    btn: ['确定','取消'], //按钮
	       	    shade: false //不显示遮罩
		       	}, function(){
		       		$.ajax({
			 			url:"<%=basePath%>scene/deleteScene",
			 			dataType:'text',
			 			type:"post",
			 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			 			data:{id:id},
			 			success:function(result){
		 					if(parseInt(result) > 0) {
		 						layer.msg('删除场景成功!', {icon: 1});  
		 						setTimeout(function() {
		 							window.location.href = "<%=basePath%>scene/index";
		 						},1000);
		 					} else{
		 						layer.msg('删除场景失败!', {icon: 2});  
		 					}
			 			}
			 		});	
		       	}, function(){
		       	});	
     	}
     	
     	//初始化更新模态框
     	function initScene(id) {
     		$('#update_scene_id').val(id);
            //场景名称
     		var scene_name = $('#scene_' + id).find('.sc_name').html();
            //所属树名称
     		var scene_tree_name = $('#scene_' + id).find('.sc_tree_name').html();
            //所属树id
     		var scene_tree_id = $('#scene_' + id).find('.sc_tree_id').html();
            //动作组id
     		var scene_action_id = $('#scene_' + id).find('.sc_action_id').html();
     		$('#update_scene_name').val(scene_name);
     		$('#update_scene_tree').html(scene_tree_name);
     		var actions = scene_action_id.split(",");
     		updateUpdateZtree(scene_tree_id,actions);
            $('#updateModal').modal('show');
        }
     	
     	//更新场景
     	function updateScene() {
     		var id = $('#update_scene_id').val();
     		var name = $('#update_scene_name').val();
            var action = getCheckedAction("updateTree");
            if(action == null) {
            	return;
            }
            $.ajax({
	 			url:"<%=basePath%>scene/updateScene",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{sc_id:id,sc_name:name,sc_action:action},
	 			success:function(result){
 					if(parseInt(result) > 0) {
 						layer.msg('更新场景成功!', {icon: 1});  
 						setTimeout(function() {
 							window.location.href = "<%=basePath%>scene/index";
 						},1000);
 					} else{
 						layer.msg('更新场景失败!', {icon: 2});  
 					}
	 			}
	 		});	
     	}

     	//获取选中的动作
     	function getCheckedAction(ztreeId) {
     		var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
     		var nodes = treeObj.getCheckedNodes(true);
     		var nodeStr = "";
     		for(var i in nodes) {
     			nodeStr = nodeStr + nodes[i].id + ",";
     		}
     		if(nodeStr.length > 0) {
     			nodeStr = nodeStr.substring(0, nodeStr.length - 1);
     		}
     		if(!validEntity(nodeStr)) {
     			layer.msg('请选择至少一个场景动作!', {icon: 2,time:2000}); 
     			return null;
     		}
     		return nodeStr;
     	}
     	
     	//勾选某个节点
     	function checkedNode(ztreeId,id) {
     		id = id.replace(" ","");
            if(id != "" && id != null) {
            	var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
            	var nodes = treeObj.getNodesByParam("id", id, null);
            	treeObj.checkNode(nodes[0], true, false);
            }	
     	}
     	
		//一键打开或关闭节点
		function openNode(ztreeId,open) {
			var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
			treeObj.expandAll(open);
		}
     	
     	//触发根据搜索关键字查找
     	function searchByLike(){

     		$('#searchPage').submit();
     		
     	}       
     	
     	//模糊查找节点
		function searchNode(id,keyId) {
			var treeObj = $.fn.zTree.getZTreeObj(id);
			var searchKey = $('#' + keyId).val();
			nodes = treeObj.transformToArray(treeObj.getNodes());
			
			for(var key in nodes) {
				nodes[key].highlight = false;
				treeObj.updateNode(nodes[key]);
			}
			
			if(searchKey != '') {
				var nodes = treeObj.getNodesByParamFuzzy("name", searchKey, null);
				for(var key in nodes) {
					nodes[key].highlight = true;
					treeObj.updateNode(nodes[key]);
					if(nodes[key].children == null) {	
						treeObj.expandNode(nodes[key].getParentNode(), true, false, true);
					}else {
						treeObj.expandNode(nodes[key], true, false, true);
					}
				}
			}
			
		}
     	
     	$(function() {
     		//添加场景按钮事件
     		$("#add_modal_button").bind("click", function() {
     			 $('#addModal').modal('show');
     		});
     		
     		//添加模场景态框中查找节点按钮
     		$('#addSearchNode').on("click",function(){
				searchNode('addTree','addNodeSearchKey');
			});
			
     		//添加模场景态框中点击回车查找节点
			$('#addNodeSearchKey').on("keydown",function(e){
				if(e.keyCode == 13) {
					searchNode('addTree','addNodeSearchKey');
				}
			});
			
			//添加模场景态框中所属树下拉框改变事件
			$('#add_scene_tree').bind('change', function() {
				var treeId = $(this).val();
				
				$.ajax({
					url:"<%=basePath%>scene/getZtreeJSON",
					dataType:'text',
					type:"post",
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					data:{treeId:treeId},
					success:function(result){
						 eval('var zNodes=' + result);
							$(document).ready(function(){
								$.fn.zTree.init($(".ztree"), setting, zNodes);
							});
					}
				});
			});
     	});
	</script>
</body>

</html>
