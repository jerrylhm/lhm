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


<title>树管理</title>

<link rel="shortcut icon" href="favicon.ico">
<link href="<%=basePath%>hAdmin/css/bootstrap.min.css?v=3.3.6"
	rel="stylesheet">
<link href="<%=basePath%>font-awesome/css/font-awesome.css?v=4.4.0"
	rel="stylesheet">

<link href="<%=basePath%>hAdmin/css/animate.css" rel="stylesheet">
<link href="<%=basePath%>hAdmin/css/style.css?v=4.1.0" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link href="<%=basePath %>selected/jquery.searchableSelect.css" rel="stylesheet" type="text/css">
<link href="<%=basePath %>css/editNode.css" rel="stylesheet" type="text/css">
<style type="text/css">
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
.ztree li span.button.value {margin-left:2px; margin-right: -1px; vertical-align:top; *vertical-align:middle}

</style>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="row">
			<div class="col-sm-12">
			 <div class="row" style="margin-bottom: 10px">
			    <div class="col-sm-6">
			            <a href="<%=basePath %>tree/index" class="btn btn-default " type="button" style="border: none"><i class="fa fa-reply"></i>&nbsp;&nbsp;返回</a>
			            <span style="font-size: 21px;margin-left: 7px;position: relative;top: 5px;">项目管理&nbsp;&nbsp;/&nbsp;&nbsp;<span style="color: #23b7e5">${tree.node_name }(id:${tree.node_id })</span></span>                       
			    </div>
             </div>          
			</div>
			<div class="col-sm-3">
                <div class="ibox float-e-margins">
                    <div class="ibox-content mailbox-content">
                        <button class="btn btn-white" type="button" onclick="openNode('tree',true)">一键打开</button>
						<button class="btn btn-white" type="button" onclick="openNode('tree',false)">一键关闭</button>
<!-- 						<button class="btn btn-white" type="button" onclick="cancalFocus()">取消选中</button><br> -->
						<button class="btn btn-white" type="button" onclick="copy()">复制</button>
						<button class="btn btn-white" type="button" onclick="paste()">黏贴</button>		
                        <div class="input-group">
                            <input class="form-control input-sm" id="nodeSearchKey" placeholder="输入节点名称搜索" type="text">
                            <div class="input-group-btn">
                                <button id="searchNode" type="button" class="btn btn-sm btn-primary">
                                                                                                 搜索
                                </button>
                            </div>
                        </div>
                        <div class="loading" style="width: 100%;height: 100%;position: absolute;top:0;left:0;">		                                    	
                        </div>
                        <div class="sk-spinner sk-spinner-wave loading">
                                <div class="sk-rect1"></div>
                                <div class="sk-rect2"></div>
                                <div class="sk-rect3"></div>
                                <div class="sk-rect4"></div>
                                <div class="sk-rect5"></div>
                        </div>  
                        <ul id="tree" class="ztree" style="font-size: 10px"></ul>
                    </div>
                </div>
            </div>
            <div class="col-sm-9 animated fadeInRight">
						<input id="editingNodeId" type="hidden" value="${tree.node_id }">					
                <div class="mail-box" style="height: 800px">
                        <iframe id="forwareIframe" name="forwareIframe" src="" width="100%" height="100%"></iframe>
                </div>
            </div>
		</div>
	</div>
	<!--数据管理模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="valueModal" style="width: 100%">
	 	<div class="modal-dialog" style="width: 900px">
	 		<div class="modal-content animated fadeIn">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>数据管理</h4>
	 			</div>	 			
	 			<div class="modal-body" style="margin-left:30px">	    
	 				<iframe id="treeValueIframe" name="treeValue" src="" width="100%" height="600px"></iframe>
		 		</div>
		 		<div class="modal-footer">
<!-- 		 			<button type="button" id="addNodeBtn" class="btn btn-sm btn-success" onclick="addNode()">确定</button> -->
<!-- 		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button> -->
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 	 <!--编辑协议模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="editProtocolModal">
	 	<div class="modal-dialog" style="width: 900px">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>编辑协议</h4>
	 			</div>
	 			
	 			<div class="modal-body">   
		 				<iframe id="treeProtocolIframe" name="treeProtocol" src="" width="100%" height="600px"></iframe>
<!-- 	 				<div class="col-sm-2 col-sm-offset-10 tr" style="padding-right: 5px;">                -->
<!-- 			              <a type="button" class="btn btn-default" style="margin-left: 10px" data-toggle="modal" data-target="#datapoint_add"> -->
<!-- 			                + 新建协议 -->
<!-- 			              </a>                    -->
<!-- 			        </div> -->
			        
			        
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
	 				<h4>新增节点</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	 				<div class="form-group row">   
	 				    <label class="col-sm-2">节点名称：</label> 
	 				    <div class="col-sm-6">
	 						<input class="input-sm form-control" type="text" id=addName placeholder="输入节点的名称">
	 					</div>	
	 				</div>
	 				<div class="form-group row node-class">
	 				    <label class="col-sm-2">节点所属类：</label>
	 				    <div class="col-sm-6">
	 						<input class="input-sm form-control" type="text" id=addClass placeholder="输入设备所属类">
	 					</div>
	 				</div>
	 				<div class="form-group row" style="display:none;">
	 				    <label class="col-sm-2">节点跳转地址：</label>
	 				    <div class="col-sm-6">
	 						<input class="input-sm form-control" type="text" id=addUrl placeholder="输入点击节点跳转的地址">
	 					</div>
	 				</div>
	 				<div class="form-group row">
	 				   <label class="col-sm-2">节点类型：</label>
	 				   <select id="addNodeType"  style="margin-left: 15px;">
	 				      <c:forEach items="${types }" var="type">
	 				       <option value="${type.type_id }" pid="${type.type_pid }">${type.type_name_cn }</option>	
	 				       </c:forEach>	   
	 				   </select> 
	 			    </div>
	 			    <div class="form-group row">
	 				   <label class="col-sm-2">传输类型：</label>
	 				   <select id="addNodeTransmissionType"  style="margin-left: 15px;">
	 				       <option value="0">协议传输</option>
	 				       <option value="1">透传</option>		  
	 				   </select> 
	 			    </div>
	 			    <div class="form-group row">
	 				   <label class="col-sm-2">场景：</label>
	 			    </div>
	 			    <div class="form-group row add-scene-space" style="border: 1px solid #ccc;margin-right: 15px;">
	 				    <div style="margin: 10px 10px;">
	 				        <div class="row add-scene-model" style="margin-bottom: 10px;margin-top: 10px;">
	 				        <div class="col-sm-5">
                                 <select class="scene-select" style="font-size: 20px;margin-left: 20px;">
                                    <c:forEach items="${scenes }" var="scene">
                                      <option value="${scene.sc_id}">${scene.sc_name }</option>
                                    </c:forEach>
                                 </select>
                            </div>	 				      
	 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-danger delete-scene-button">删除</button></div>
	 				        </div>
	 				        <div>
	 				        <button type="button" class="btn btn-outline btn-success" id=add_scene_button>+ 添加场景</button>
	 				        </div>
	 				    </div>
	 				</div>
	 			    <div class="equipment-space" style="display:none">
	 			    <div class="form-group row sn">
		 			    <label class="col-sm-2">设备SN：</label>
		 				<div class="col-sm-6">
		 						<input name="node_sn" id="add_sn" type="text" placeholder="输入设备SN值" class="form-control">
		 				</div>
	 				</div>

	 			    <div class="form-group row">
		 				   <label class="col-sm-2">选择模板：</label>
		 				   <select id="addNodeTemplate"  style="margin-left: 15px;">
		 				      <option value="-1">不启用</option>
		 				      <c:forEach items="${templates }" var="template">
		 				       <option value="${template.tp_id }">${template.tp_name }</option>	
		 				       </c:forEach>	   
		 				   </select> 
	 			    </div>	 			    
		 		</div>   
	 				
		 		<div class="modal-footer">
		 			<button type="button" id="addNodeBtn" class="btn btn-sm btn-success" onclick="addNode()">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 </div>
	<!--编辑模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="editor">
	 	<div class="modal-dialog ">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>编辑节点名称</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="height:80px;margin-left:30px">
	 				<input class="input-sm form-control" type="text" id="nodeName" placeholder="输入节点的名称">
		 		</div>
		 		
		 		<input type="hidden" id="nodeId">
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="editorNodeName()">保存</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 <!--编辑节点属性模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="editUrlModal">
	 	<div class="modal-dialog modal-lg">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>编辑节点属性</h4>
	 			</div>
	 			<div class="modal-body" style="margin-left:30px">
	 			    <div class="equipment-space-update" style="display: none">
	                <div id="update_sn_div" class="form-group row sn">
			 			    <label class="col-sm-2">设备SN：</label>
			 				<div class="col-sm-6">
			 						<input id="nodeSn" type="text" placeholder="输入设备SN值" class="form-control">
			 				</div>
		 			</div>
		 			<div class="form-group row">
		 				   <label class="col-sm-2">选择模板：</label>
		 				   <select id="templates"  style="margin-left: 15px;">
		 				      <option value="-1">不启用</option>
		 				      <c:forEach items="${templates }" var="template">
		 				       <option value="${template.tp_id }">${template.tp_name }</option>	
		 				       </c:forEach>	   
		 				   </select> 
	 			    </div>
		 			</div>
		 			<div id="update_class_div" class="form-group row node-class">
			 			    <label class="col-sm-2">节点所属类：</label>
			 				<div class="col-sm-6">
			 						<input id="nodeClass" type="text" placeholder="输入设备所属类名称" class="form-control">
			 				</div>
		 			</div>
			 		<div class="form-group row" style="display:none;">
		 				    <label class="col-sm-2">节点跳转地址：</label>
		 				    <div class="col-sm-6">
		 						<input class="input-sm form-control" type="text" id=nodeUrl placeholder="输入点击节点跳转的地址">
		 					</div>
		 			</div>
		 			<div class="form-group row">
	 				   <label class="col-sm-2">节点类型：</label>
	 				   <select id="nodeType"  style="margin-left: 15px;">
	 				      <c:forEach items="${types }" var="type">
	 				       <option value="${type.type_id }" pid="${type.type_pid }">${type.type_name_cn }</option>	
	 				       </c:forEach>	   
	 				   </select> 
	 			    </div>
	 			    <div class="form-group row">
	 				   <label class="col-sm-2">传输类型：</label>
	 				   <select id="transmissionType"  style="margin-left: 15px;" disabled="disabled">
	 				       <option value="0">协议传输</option>
	 				       <option value="1">透传</option>		  
	 				   </select> 
	 			    </div>
	 			    <div class="form-group row">
	 				   <label class="col-sm-2">场景：</label>
	 			    </div>
	 			    <div class="form-group row update-scene-space" style="border: 1px solid #ccc;margin-right: 15px;">
	 				    <div style="margin: 10px 10px;">
	 				        <div class="row update-scene-model" style="margin-bottom: 10px;margin-top: 10px;">
	 				        <div class="col-sm-5">
                                 <select class="scene-select" style="font-size: 20px;margin-left: 20px;">
                                    <c:forEach items="${scenes }" var="scene">
                                      <option value="${scene.sc_id}">${scene.sc_name }</option>
                                    </c:forEach>
                                 </select>
                            </div>	 				      
	 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-danger update-delete-scene-button">删除</button></div>
	 				        </div>
	 				        <div>
	 				        <button type="button" class="btn btn-outline btn-success" id=update_scene_button>+ 添加场景</button>
	 				        </div>
	 				    </div>
	 				</div>
	 			</div>
		 		<input type="hidden" id="editUrlNodeId">
		 		<input type="hidden" id="editUrlNodeType">
		 		<input type="hidden" id="editUrlNodeOldSn">
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="editorNodeUrl()">保存</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>

		 <!--权限管理模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="permissionModal">
	 	<div class="modal-dialog">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>权限管理</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	 			    选择想要赋予权限的用户：
	 			    <select class="form-control m-b use-searchable" name="account" id="userList">
	 			       <c:forEach items="${users }" var="user">
	                        <option value="${user.ur_id }">${user.ur_username }</option>
                       </c:forEach>
                    </select>
                    <!-- 加载中动画 -->
                    <div class="ibox-content" id="loading" style="display: none;">
                        <div class="spiner-example">
                            <div class="sk-spinner sk-spinner-three-bounce">
                                <div class="sk-bounce1"></div>
                                <div class="sk-bounce2"></div>
                                <div class="sk-bounce3"></div>
                            </div>
                        </div>
                    </div>
                    <div id="permissionSpace">
                        <button class="btn btn-white" type="button" onclick="openNode('permissionTree',true)">一键打开</button>
						<button class="btn btn-white" type="button" onclick="openNode('permissionTree',false)">一键关闭</button>
	                    <div class="input-group" style="margin-top: 5px">
	                            <input class="form-control input-sm" id="psnodeSearchKey" placeholder="输入节点名称搜索" type="text">
	                            <div class="input-group-btn">
	                                <button id="pssearchNode" type="button" class="btn btn-sm btn-primary">
	                                                                                                 搜索
	                                </button>
	                            </div>
	                    </div>
		 			    <ul id="permissionTree" class="ztree" style="font-size: 10px"></ul>
	 			    </div>
	 			    <input type="hidden" id="alreadEdit" value="${alreadEdit }">
	 			    <input type="hidden" id="editPSUser" value="">
		 		</div>
		 		<div class="modal-footer">
		 			<button type="button" id="savePsBtn" class="btn btn-sm btn-success" onclick="savePermission()">保存</button>
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
    <script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.exhide.js"></script>
    <script src="<%=basePath %>selected/jquery.searchableSelect.js"></script>
	<script type="text/javascript">
     var oldScene = "";
     var newScene = "";
	    //初始化下拉框，可以根据option的text搜索
		$(function(){
			$('.use-searchable').searchableSelect();
		});
	
	if('${permission.urnode_iscreator}' != 1) {
		
		$('#permissionManage').remove();
	}
	var btnClickNode;
	var setting = {
			//设置是否允许同时设置多个节点
			view: {
				addHoverDom: addHoverDom,
				removeHoverDom: removeHoverDom,
				selectedMulti: false,
				fontCss: getFontCss,
				showIcon: false
			},
			check: {
				enable: false
				
			},
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
			edit: {
				enable: true,
				editNameSelectAll: true,
				showRemoveBtn: showRemoveBtn,
				showRenameBtn: showRenameBtn,
				removeTitle: "删除节点",
				renameTitle: "编辑名称"
			},
			callback: {
				beforeCollapse: zTreeBeforeCollapse,
				beforeExpand: zTreeBeforeExpand,
				beforeDrag: beforeDrag,
				beforeRename: beforeRename,
				beforeRemove: beforeRemove,
				onClick: forwareUrl
			}
			
		};
        eval('var zNodes=' + '${ztree}');
		$(document).ready(function(){
			$.fn.zTree.init($("#tree"), setting, zNodes);
		});

		//显示删除节点按钮
		function showRemoveBtn(treeId, treeNode){
			if(!!treeNode.hasP) {
				return true;
			}else{
				return false;
			}
		}
		
		//显示编辑节点名称按钮
		function showRenameBtn(treeId, treeNode){
			if(!!treeNode.hasP) {
				return true;
			}else{
				return false;
			}
		}
		function zTreeBeforeCollapse(treeId, treeNode) {

		    return true;
		};
		//节点展开前会调函数
		function zTreeBeforeExpand(treeId, treeNode) {

		    return true;
		};
		
		//取消节点聚焦
		function cancalFocus() {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getSelectedNodes();
			for(var key in nodes) {
				treeObj.cancelSelectedNode(nodes[key]);
			}
		}
		function beforeDrag(treeId, treeNodes) {
			return false;
		}
		function beforeRemove(treeId, treeNode) {
			return removeNode(treeNode);
		}
		
		//改变ztree字体颜色
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
		
		//节点名称修改前回调函数
		function beforeRename(treeId, treeNode, newName) {
			var suc = false;
			if (newName.length == 0) {
				layer.alert('节点名称不能为空', {
				    icon: 2,
				    skin: 'layer-ext-moon' 
				})
				var zTree = $.fn.zTree.getZTreeObj("tree");
				setTimeout(function(){zTree.editName(treeNode)}, 10);
			} else {
				$.ajax({
		 			url:"<%=basePath%>tree/updateTreeName",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{id:treeNode.id,name:newName},
		 			success:function(result){
	 					if(parseInt(result) > 0) {
	 						suc = true;
	 					} else{
	 						layer.msg('编辑节点时出错了', {icon: 2}); 
	 					}
	 					return suc;
		 			}
		 		});
			}

		}

		//鼠标悬停节点上显示自定义按钮
		function addHoverDom(treeId, treeNode) {
			if(!treeNode.hasP) {
				return;
			}
			//新增节点按钮
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='新增子节点' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			if (btn) btn.bind("click", function(){
				var zTree = $.fn.zTree.getZTreeObj("tree");
				    btnClickNode = treeNode;
					$('#addName').val('')
					$('#addUrl').val('')
					$('#add_sn').val('')
					$('#addModal').modal('show');
				return false;
			});
			//数据管理按钮
			if (treeNode.editNameFlag || $("#valueBtn_"+treeNode.tId).length>0) return;
			var valueStr = "<span class='button value value-icon' id='valueBtn_" + treeNode.tId
				+ "' title='数据管理' onfocus='this.blur();'></span>";
			sObj.after(valueStr);	
			var valuebtn = $("#valueBtn_"+treeNode.tId);
			if (valuebtn) valuebtn.bind("click", function(){
				var zTree = $.fn.zTree.getZTreeObj("tree");
				    btnClickNode = treeNode;
				    editNodeValue(treeId, treeNode);
					$('#valueModal').modal('show');
				return false;
			});
			
			//节点属性编辑按钮
			if (treeNode.editNameFlag || $("#editUrlBtn_"+treeNode.tId).length>0) return;	
			var editUrlStr = "<span class='button value editUrl-icon' id='editUrlBtn_" + treeNode.tId
				+ "' title='编辑节点属性' onfocus='this.blur();'></span>";
			sObj.after(editUrlStr);
			var editUrlbtn = $("#editUrlBtn_"+treeNode.tId);
			if (editUrlbtn) editUrlbtn.bind("click", function(){
				$('#editUrlNodeId').val(treeNode.id);				
	        	$('#nodeUrl').val(treeNode.myUrl);
	        	var type_id = treeNode.type.type_id;
	        	$('#editUrlNodeType').val(type_id);	
	        	$('#nodeType').val(type_id);
	        	var tpId = treeNode.tpId;
	        	$('#transmissionType').find('option[value=' + treeNode.tsType + ']').prop("selected", true);
        		$('#nodeClass').val(treeNode.nodeClass);
	        	if(type_id == 2) {
	        		$('.equipment-space-update').css('display', "");
	        		$('#nodeSn').val(treeNode.sn);
	        		$('#editUrlNodeOldSn').val(treeNode.sn);
	        		$('#templates').find("option[value="+tpId+"]").prop("selected",true);
	        	}else {
	        		$('.equipment-space-update').css('display', "none");
	        	}      	
	        	$('.update-scene-item').remove();
	        	var scenes = treeNode.scene.split(",");
	        	for(var i in scenes) {
	        		if(scenes[i] != "") {
		        		var $scene = addSceneItem(".update-scene-space", "update");
		        		$scene.find(".scene-select").find("option[value="+scenes[i]+"]").prop("selected", true);
		        		updateSceneEvent($scene, treeNode.id);
	        		}
	        	}
				editUrl();
				return false;
			});			
			
			//权限管理编辑按钮
// 			if (treeNode.editNameFlag || $("#permissionBtn_"+treeNode.tId).length>0 || !treeNode.isCreator) return;			
// 			var valueStr = "<span class='button value permission-icon' id='permissionBtn_" + treeNode.tId
// 				+ "' title='权限管理' onfocus='this.blur();'></span>";
// 			sObj.after(valueStr);
// 			var permissionbtn = $("#permissionBtn_"+treeNode.tId);
// 			if (permissionbtn) permissionbtn.bind("click", permissionManage);

			//协议管理按钮
			if (treeNode.editNameFlag || $("#protocolBtn_"+treeNode.tId).length>0) return;		
			if(treeNode.tsType == 0) {
				var valueStr = "<span class='button value protocol-icon' id='protocolBtn_" + treeNode.tId
				+ "' title='协议管理' onfocus='this.blur();'></span>";
				sObj.after(valueStr);
				var protocolbtn = $("#protocolBtn_"+treeNode.tId);
				if (protocolbtn) protocolbtn.bind("click", {treeId:treeId, treeNode:treeNode},protocolManage);
			}
		};
		
		//鼠标离开节点移除自定义按钮
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
			$("#valueBtn_"+treeNode.tId).unbind().remove();
// 			$("#permissionBtn_"+treeNode.tId).unbind().remove();
			$("#editUrlBtn_"+treeNode.tId).unbind().remove();
			$("#protocolBtn_"+treeNode.tId).unbind().remove();
		};
		//编辑节点名称
		function edit(node) {
			var zTree = $.fn.zTree.getZTreeObj("tree"),
			treeNode = node;
			zTree.editName(treeNode);
		};
		var newCount = 0;
		
		//更新选择场景下拉框的事件
		function updateSceneEvent($scene, nodeId) {
			$scene.find('.scene-select').bind('click', function() {
                oldScene = $(this).val();
		    });
  		    $scene.find('.scene-select').bind('change', function() {
                newScene = $(this).val();
                var scenes = getSceneArray('update');
                if(scenes == null) {
                	layer.alert('场景选择重复', {icon: 3, skin: 'layer-ext-moon'});
                	return;
                }
                $.ajax({
		 			url:"<%=basePath%>tree/updateScene",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{oldScene:oldScene,newScene:newScene,nodeId:nodeId},
		 			success:function(result){
		 				     if(result > 0) {
		 				    	layer.msg('修改场景成功', {icon: 1}); 		    	 
		 				     }else {
		 				    	layer.msg('修改场景失败', {icon: 2}); 
		 				     }
		 			}
		 		});	
		    });
		}
		
		//获取选中的场景的id数组，参数action表示操作的类型（add或update）
		function getSceneArray(action) {
			var scenes = new Array();
            var count = 0;
            var repeat = true;
            $('.' + action + '-scene-item').each(function() {
            	var sceneId = $(this).find('.scene-select').val();
            	if(sceneId == '' || sceneId == null) {
            		repeat = false;
        			return;
            	}
           		for(var i=0;i<scenes.length;i++) {
               		if(sceneId == scenes[i]) {
               			repeat = false;
               			return;
               		}
               	}
            	scenes[count++] = sceneId;
            });
            if(repeat) {
            	return scenes;
            }else {
            	return null;
            }
            
		}
		
		//把数组输出为以逗号分隔的字符串
		function arrayToStr(array) {
			var result = "";
			for(var i = 0;i < array.length;i++) {
				result = result + array[i] + ',';
			}
			if(result.length > 0) {
				result = result.substring(0, result.length - 1);
			}
			return result;
		}
		
		//添加节点
		function addNode() {
			//节点名称
			var name = $('#addName').val();
			//点击节点跳转地址
			var url = $('#addUrl').val();
			//节点类型
			var type = $('#addNodeType').val();
			//节点类型中文名称
			var type_name_cn = $('#addNodeType').find("option:selected").html();
			//设备sn号
			var sn = $('#add_sn').val();
			//设备所属类
			var nodeClass = $('#addClass').val();
			//所用模板的id
            var tpId = $('#addNodeTemplate').val();            
            //选用的场景的id数组
            var scenes = getSceneArray('add');
            //传输类型
            var tsType = $('#addNodeTransmissionType').val();
            
            if(scenes == null) {
            	layer.alert('场景选择重复或为空', {icon: 3, skin: 'layer-ext-moon'});
            	return;
            }
            var sceneStr = arrayToStr(scenes);
            
			if(url == "") {
				url = '';
			}
			if(name == "") {
				layer.alert('节点名称不能为空', {
				    icon: 3,
				    skin: 'layer-ext-moon' 
				})
				return;
			}
			if(type == 2) {

				$.ajax({
		 			url:"<%=basePath%>tree/validSN",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{sn:sn,action:'add'},
		 			success:function(result){
		 				     if(result > 0) {
		 				    	addNodeHandle(name,url,type,type_name_cn,nodeClass,tpId,sceneStr,tsType,sn);			    	 
		 				     }else {
		 				    	layer.alert('sn值已存在或sn值为空', {
		 						    icon: 3,
		 						    skin: 'layer-ext-moon' 
		 						})
		 				     }
		 			}
		 		});	
			
			}else {
				addNodeHandle(name,url,type,type_name_cn,nodeClass,tpId,sceneStr,tsType,'');	
			}
			templates = new Array();
		}
		
		//通过ajax新增节点
		function addNodeHandle(name,url,type,type_name_cn,nodeClass,tpId,scene,tsType,sn) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			var node = btnClickNode;
			layer.tips('新增节点中，请稍等...', '#addNodeBtn', {
			    tips: [1, '#3595CC'],
			    time: 1000
			});
			$('#addNodeBtn').prop('disabled','disabled');
			$.ajax({
	 			url:"<%=basePath%>tree/addTree",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{isParent:false,pid:node.id,name:name,url:url,type:type,sn:sn,nodeClass:nodeClass,tpId:tpId,scene:scene,tsType:tsType},
	 			success:function(result){
	 				eval('var tree=' + result);
 					if(tree.node_id > 0) {
 						var isCreator;
 						tree.isCreator == 1?isCreator = true:isCreator = false;
 						treeNode = zTree.addNodes(node, {id:tree.node_id, pId:tree.node_pid, name:tree.node_name, myUrl:url, type:{type_id:type}, sn:sn, title:tree.node_name+"("+type_name_cn+")", tsType:tsType, nodeClass:nodeClass, tpId:tpId, target:"treeValue", hasP:true, isCreator:isCreator,scene:scene});
 						layer.msg('新增节点成功', {icon: 1}); 
 						$('#addModal').modal('hide');
 					} else{
 						layer.msg('新增节点出错了', {icon: 2}); 
 					}
 					$('#addNodeBtn').prop('disabled','');					
	 			}
	 		});	
		}
		
		//删除节点
		function removeNode(node) {		
			var zTree = $.fn.zTree.getZTreeObj("tree");
				 layer.confirm('确定删除节点吗？', {
			       	    btn: ['确定','取消'], //按钮
			       	    shade: false //不显示遮罩
				       	}, function(){
				       		$.ajax({
					 			url:"<%=basePath%>tree/removeTree",
					 			dataType:'text',
					 			type:"post",
					 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					 			data:{id:node.id},
					 			success:function(result){
				 					if(parseInt(result) > 0) {
				 						zTree.removeNode(node,false);
				 						checkEditingNode(node.id);
				 						layer.msg('删除节点成功!', {icon: 1});  
				 					} else{
				 						layer.msg('删除节点失败!', {icon: 2});  
				 					}
					 			}
					 		});	
				       	}, function(){
				       	});	
				 return false;
		}
		
		function editNodeValue(treeId, treeNode) {
			$('#treeValueIframe').attr('src', '<%=basePath%>' + "tree/treeValue?nodeId=" + treeNode.id + "&nodeName=" + treeNode.name);
		}
		//初始化打开权限管理模态框
        function permissionManage() {
        	initPermission();
        	$('#permissionModal').modal('show');
        	return false;
        }

		//初始化权限管理界面
		function initPermission() {
			$('#loading').show();
			$('#permissionSpace').hide();
			var userId = $('#userList').val();
			$('#editPSUser').val(userId);
			initTree(userId);
		}
		
		//初始化协议管理模态框
		function protocolManage(event) {
			editNodeProtocol(event.data.treeId, event.data.treeNode);
			$('#editProtocolModal').modal();
			return false;
		}
		
		function editNodeProtocol(treeId, treeNode) {
			$('#treeProtocolIframe').attr('src', '<%=basePath%>' + "tree/treeProtocol?nodeId=" + treeNode.id);
		}
		//获取正在查看的树的所有节点
		function initTree(userId) {
			    
				$.ajax({
 		 			url:"<%=basePath%>tree/getTree",
 		 			dataType:'text',
 		 			type:"post",
 		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
 		 			data:{treeId:'${tree.node_id}'},
 		 			success:function(result){
 	                  var PTSetting = {
 	                			//设置是否允许同时设置多个节点
 	                			view: {
 	                				selectedMulti: false,
 	               				    fontCss: getFontCss,
 	               				    showIcon: false,
 	               				    showTitle: true
 	                			},
 	                			check: {
 	                				enable: true,
 	                				chkStyle: "checkbox",
 	                				chkboxType: { "Y": "s", "N": "" }               				
 	                			},
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
 	                			edit: {
 	                				enable: false
 	                			},
 	                			callback: {

 	                			}
 	                			
 	                		};
 	                     eval('var PSNodes=' + result);
 	                     $.fn.zTree.init($("#permissionTree"), PTSetting, PSNodes);
 	                     getUserPermission(userId);
 		 			}
 		 		});	
		}		
		
		//获取用户对该树的权限并刷新ztree
		function getUserPermission(userId) {
			$('#loading').show();
			$('#permissionSpace').hide();
			var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
			treeObj.checkAllNodes(false);
			$.ajax({
	 			url:"<%=basePath%>tree/getUserPermission",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{userId:userId,treeId:'${tree.node_id}'},
	 			success:function(result){	 				
                   if(result != '') {
                	   $('#alreadEdit').val(true);
                	   eval('var userPermission=' + result);
                	   var ids = userPermission.urnode_nodeid.split(',');
                	   for(var key in ids) {
                		   var node = treeObj.getNodeByParam("id", ids[key], null);
                		   treeObj.expandNode(node, true, false, true);
                		   treeObj.checkNode(node, true, false);
                	   }
                   } else {
                	   $('#alreadEdit').val(false);
                   }                          
                   $('#loading').hide();
                   $('#permissionSpace').show();
	 			}
	 		});	
		}
		
		//保存权限设置
		function savePermission() {
			var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
			var nodes = treeObj.getCheckedNodes(true);
			var userid = $('#editPSUser').val();
			var alreadEdit = $('#alreadEdit').val();
			var ids = "";
			for(var i = 0;i < nodes.length;i++) {
				if(i == 0) {
					ids = ids + nodes[i].id;
				} else {
					ids = ids + ',' + nodes[i].id;
				}
			}
			layer.tips('保存中，请稍等...', '#savePsBtn', {
			    tips: [1, '#3595CC'],
			    time: 1000
			});
			$('#savePsBtn').prop('disabled','disabled');
			$.ajax({
	 			url:"<%=basePath%>tree/savePermission",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{ids:ids,treeId:'${tree.node_id}',userid:userid,alreadEdit:alreadEdit},
	 			success:function(result){	 				
                   if(result > 0) {
                	   layer.msg('权限设置成功!', {icon: 1,time:1000}); 
                	   $('#savePsBtn').prop('disabled','');
                   }
	 			}
	 		});	
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
		
		//点击节点跳转页面
		function forwareUrl(event, treeId, treeNode){
			if(treeNode.myUrl != "") {
				$('#forwareIframe').attr('src',treeNode.myUrl + "?nodeId=" + treeNode.id);
			}
		}
		
		//ajax保存节点属性
		function editorNodeUrl() {
			//节点id
			var nodeid = $('#editUrlNodeId').val();
			//节点跳转地址
			var url = $('#nodeUrl').val();
			//设备的sn值
			var sn = $('#nodeSn').val();
			//节点的类型
			var type = $('#editUrlNodeType').val();
			//节点所属类
			var nodeClass = $('#nodeClass').val();
			//
			var old_sn = $('#editUrlNodeOldSn').val();
			var tpId = $('#templates').val();
			var newType = $('#nodeType').val();
			var tsType = $('#transmissionType').val();
			
			//如果节点类型变为设备，先检查sn码是否重复
			if(newType == 2) {
					$.ajax({
			 			url:"<%=basePath%>tree/validSN",
			 			dataType:'text',
			 			type:"post",
			 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			 			data:{sn:sn,action:'update',oldSn:old_sn},
			 			success:function(result){
		 				     if(result > 0 && sn != "") {
		 				    	$.ajax({
		 				 			url:"<%=basePath%>tree/editUrl",
		 				 			dataType:'text',
		 				 			type:"post",
		 				 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 				 			data:{nodeid:nodeid,url:url,sn:sn,nodeClass:nodeClass,oldType:type,type:newType,tpId:tpId,tsType:tsType},
		 				 			success:function(result){	 				
		 			                   if(result > 0) {
		 			                	   layer.msg('节点属性设置成功!', {icon: 1,time:1000}); 
		 			                	   var zTree = $.fn.zTree.getZTreeObj("tree");
		 			                	   var node = zTree.getNodeByParam("id", nodeid, null);
		 			                	   node.myUrl = url;
		 			                	   node.type.type_id = newType;
		 			                	   node.title = node.name+"("+$('#nodeType').find('option[value='+newType+']').html()+")"
		 			                	   node.sn = sn;
		 			                	   node.nodeClass = nodeClass;
		 			                	   node.tpId = tpId;
		 			                	   node.tsType = tsType;
		 			                	   zTree.updateNode(node);
		 			                	   $('#editUrlModal').modal('hide');               	   
		 			                   }else {
		 			                	  layer.msg('节点属性设置失败!', {icon: 2,time:1000}); 
		 			                   }
		 				 			}
		 				 		});				    	 
		 				     }else {
		 				    	layer.alert('sn值已存在或sn值为空', {
		 						    icon: 3,
		 						    skin: 'layer-ext-moon' 
		 						})
		 				     }
			 			}
			 		});		
			
			}else {
					$.ajax({
				 			url:"<%=basePath%>tree/editUrl",
				 			dataType:'text',
				 			type:"post",
				 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				 			data:{nodeid:nodeid,oldType:type,type:newType,nodeClass:nodeClass,url:url,tsType:tsType},
				 			success:function(result){	 				
			                   if(result > 0) {
			                	   layer.msg('节点属性设置成功!', {icon: 1,time:1000}); 
			                	   var zTree = $.fn.zTree.getZTreeObj("tree");
			                	   var node = zTree.getNodeByParam("id", nodeid, null);
			                	   node.myUrl = url;
			                	   node.type.type_id = newType;
			                	   node.title = node.name+"("+$('#nodeType').find('option[value='+newType+']').html()+")";
			                	   node.tsType = tsType;
 			                	   node.nodeClass = nodeClass;
			                	   zTree.updateNode(node);   	   
			                	   $('#editUrlModal').modal('hide');               	   
			                   }else {
	 			                	  layer.msg('节点属性设置失败!', {icon: 2,time:1000}); 
 			                   }
				 			}
				 		});	
			}
		}
		
		//在页面上添加一个场景
		function addSceneItem(space, action) {
			$scene = $('.' + action + '-scene-model:eq(0)').clone(true);
			$scene.removeClass(action + '-scene-model');
			$scene.addClass(action + '-scene-item')
			$(space).prepend($scene);
			return $scene;
		}
		
		//一键打开或关闭节点
		function openNode(ztreeId,open) {
			var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
			treeObj.expandAll(open);
		}
		srcNode = null;
		//复制节点
		function copy() {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			srcNode = treeObj.getSelectedNodes();
			srcNode = srcNode[0];
			if(srcNode == null) {
				layer.msg('请先选中一个节点!', {icon: 3,time:3000});
			}
			return srcNode;
		}
		
		//黏贴节点
		function paste() {
			if(srcNode == null) {
			   layer.msg('请先复制节点!', {icon: 3,time:3000});
			   return;
			}
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getSelectedNodes();
			var targetNode = nodes.length>0? nodes[0]:null;
			//targetNode = treeObj.copyNode(targetNode, srcNode, "inner");
			//treeObj.hideNodes([targetNode]);
			srcNode.pId = targetNode.id;
			var simpleJSON = treeObj.transformToArray(srcNode);
			var str = JSON.stringify(simpleJSON);
			$('.loading').show();
			$.ajax({
	 			url:"<%=basePath%>tree/copyNode",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{nodes:str},
	 			success:function(result){
	 				treeObj.copyNode(targetNode, treeObj.transformTozTreeNodes(JSON.parse(result))[0], "inner");
 					$('.loading').hide();
 			}
	 		});
		}
		
		//把正在编辑的节点的id保存在隐藏输入框
		function saveEditingNodeId(id) {
			$('#editingNodeId').val(id);
		}
		
		//判断删除的节点是否当前编辑节点
		function checkEditingNode(removeId) {
			var id = $('#editingNodeId').val();
			if(removeId == id) {
				$('#treeValueIframe').attr('src','');
			}
		}
		
        //为新增节点按钮绑定事件
        $(function() {
    		$("#permissionManage").bind("click", permissionManage);
    		
        })      
        
        //打开编辑跳转地址模态框
        function editUrl() {       	
        	$('#editUrlModal').modal('show');
        	return false;
        }
        
        
		//绑定事件
		$(function() {			
			$('.content_wrap').on('click',function(e){
				var id = e.target.id;
				id = id.substring(id.length - 4,id.length);
				if(id != 'span') {
					cancalFocus();
				}
			})
			
			
			$('#userList').on('change',function(e){
				var userId = $(this).val();
				$('#editPSUser').val(userId);
				getUserPermission(userId);
			})
			
			$('.searchable-select-item').on('click',function(e){
				var userId = $(this).attr('data-value');
				$('#editPSUser').val(userId);
				getUserPermission(userId);
			})
			
			//查找节点按钮点击事件
			$('#searchNode').on("click",function(){
				searchNode('tree','nodeSearchKey');
			});
			
			//查找节点输入框回车事件
			$('#nodeSearchKey').on("keydown",function(event){
				if(event.keyCode == 13) {
					searchNode('tree','nodeSearchKey');
				}
			});
			
			$('#pssearchNode').on("click",function(){
				searchNode('permissionTree','psnodeSearchKey');
			});
			
			$('#psnodeSearchKey').on("keydown",function(e){
				if(e.keyCode == 13) {
					searchNode('permissionTree','psnodeSearchKey');
				}
			});
			
			//新增模态框中节点类型改变事件
			$('#addNodeType').on('change', function() {
				var type_id = $(this).val();
				if(type_id == 2) {
					$('.equipment-space').css('display','');
				}else {
					$('.equipment-space').css('display','none');
				}
			});
			
			//编辑模态框中节点类型改变事件
			$('#nodeType').on('change', function() {
				var type_id = $(this).val();
				if(type_id == 2) {
					$('.equipment-space-update').css('display','');
				}else {
					$('.equipment-space-update').css('display','none');
				}
			});
			
			//新增模态框中添加场景按钮事件
			$('#add_scene_button').bind('click', function() {
				addSceneItem('.add-scene-space', 'add');
			});
			
			//新增模态框中删除场景按钮事件
			$('.delete-scene-button').bind('click', function() {
				$(this).parents('.add-scene-item').remove();
			});
			
			//编辑按钮事件
			$('#update_scene_button').bind('click', function() {
				var nodeId = $('#editUrlNodeId').val();
				var sceneArray = getSceneArray("update");
				var $scene;
				var count = 0;
				$('.scene-select:eq(0) > option').each(function() {
					var key = true;
					for(var i in sceneArray) {
						if(sceneArray[i] == $(this).val()) {
							key = false;
						}		
					}
					
					if(key && count <= 0) {
						$scene = addSceneItem('.update-scene-space', 'update');
						$scene.find('.scene-select').find('option[value='+$(this).val()+']').prop("selected", true);
						updateSceneEvent($scene, nodeId);
						var scene = $(this).val();
						count++;
						console.log(count)
						$.ajax({
				 			url:"<%=basePath%>tree/addScene",
				 			dataType:'text',
				 			type:"post",
				 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				 			data:{scene:scene,nodeId:nodeId},
				 			success:function(result){
				 				     if(result > 0) {
				                	    var zTree = $.fn.zTree.getZTreeObj("tree");
				                	    var node = zTree.getNodeByParam("id", nodeId, null);
				                	    if(node.scene == null) {
				                	    	node.scene = "";
				                	    }
				                	    if(node.scene == "") {
				                	    	node.scene = node.scene + scene;				           
				                	    }else {
				                	    	node.scene = node.scene + ',' + scene;
				                	    }
				                	    zTree.updateNode(node); 
				 				    	layer.msg('新增场景成功', {icon: 1}); 		    	 
				 				     }else {
				 				    	layer.msg('新增场景失败', {icon: 2}); 
				 				     }
				 			}
				 		});	
						return;
					}
				});
				if($scene == null) {
					layer.msg('场景数量已达上限!', {icon: 3});  
				}
			});
			
			//修改模态框中删除场景按钮点击事件
			$('.update-delete-scene-button').bind('click', function() {
				var nodeId = $('#editUrlNodeId').val();
				var scene = $(this).parents('.update-scene-item').find('.scene-select').val();
				var button = $(this);
				 layer.confirm('确定删除该场景吗？', {
			       	    btn: ['确定','取消'], //按钮
			       	    shade: false //不显示遮罩
				       	}, function(){
				       		$.ajax({
					 			url:"<%=basePath%>tree/removeScene",
					 			dataType:'text',
					 			type:"post",
					 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					 			data:{nodeId:nodeId,scene:scene},
					 			success:function(result){
				 					if(parseInt(result) > 0) {
				 						button.parents('.update-scene-item').remove();
				                	    var zTree = $.fn.zTree.getZTreeObj("tree");
				                	    var node = zTree.getNodeByParam("id", nodeId, null);
				                	    var sc = node.scene;
				                	    var scArray = sc.split(",");
				                	    var scResult = "";
				                	    for(var i in scArray) {
				                	    	if(scArray[i] == scene) {
				                	    		scArray[i] = "";				                
				                	    	}				                	    	
				                	    }
				                	    for(var i in scArray) {
				                	    	if(i == 0) {
				                	    		scResult = scResult + scArray[i];
				                	    	}else {
				                	    		scResult = scResult + ',' + scArray[i];
				                	    	}				                	    	
				                	    }
				                	    node.scene = scResult;
				                	    zTree.updateNode(node); 
				 						layer.msg('删除场景成功!', {icon: 1});  
				 					} else{
				 						layer.msg('删除场景失败!', {icon: 2});  
				 					}
					 			}
					 		});	
				       	}, function(){
				       	});	
			});

		})
		
	</script>
</body>

</html>
