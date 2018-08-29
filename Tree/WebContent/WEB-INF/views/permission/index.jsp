<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=basePath%>zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link href="<%=basePath %>selected/jquery.searchableSelect.css" rel="stylesheet" type="text/css">
<title>设置权限</title>
</head>
<body>
	<div class="modal-body" style="margin-left:30px;width: 40%;float: left;">
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
                    <button class="btn btn-white" type="button" onclick="openNode('tree',true)">一键打开</button>
		<button class="btn btn-white" type="button" onclick="openNode('tree',false)">一键关闭</button>
                 <div class="input-group" style="margin-top: 5px">
                         <input class="form-control input-sm" id="psnodeSearchKey" placeholder="输入节点名称搜索" type="text">
                         <div class="input-group-btn">
                             <button id="pssearchNode" type="button" class="btn btn-sm btn-primary">
                                                                                              搜索
                             </button>
                         </div>
                 </div>
		    <ul id="tree" class="ztree" style="font-size: 10px"></ul>
	    </div>
	</div>
	<iframe width="57%" id="detail" src="<%=basePath %>permission/detailPermission?id=0&&name=0&&treeid=${TreeID}&&groupid=${GroupID}" frameborder="0" style="min-height: 760px;border-left: 1px solid #cccccc;"></iframe>
	<div class="modal-footer">
		<button type="button" id="savePsBtn" class="btn btn-sm btn-success" onclick="savePermission()">保存</button>
		<button type="button" class="btn btn-sm btn-default" onclick="cancelPermission()">取消</button>
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
  				    showIcon: false
       			},
       			check: {
       				enable: false,
       				chkStyle: "checkbox",
       				chkboxType: { "Y": "s", "N": "" }               				
       			},
       			data: {
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
					onClick:setNodesPermission,
       			}
				
			};
	        eval('var zNodes=' + '${ztree}');
	    	$(document).ready(function(){
				$.fn.zTree.init($("#tree"), setting, zNodes);
			});
	    	
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
	    	
	    	//取消设置权限
	    	function cancelPermission(){
	    		parent.$("#permissionModal").modal("hide");
	    	}
	    	
	    	//一键打开或关闭节点
			function openNode(ztreeId,open) {
				var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
				treeObj.expandAll(open);
			}
	    	
	    	//点击某一节点
	    	function setNodesPermission(event, treeId, treeNode){
	    		document.getElementById("detail").src = "<%=basePath %>permission/detailPermission?id="+treeNode.id+"&&name="+treeNode.name+"&&groupid=${GroupID}";
	    	}
	    	
	    	//搜索
	    	$('#pssearchNode').on("click",function(){
				searchNode('tree','psnodeSearchKey');
			});
	    	
	    	$('#psnodeSearchKey').on("keydown",function(event){
				if(event.keyCode == 13) {
					searchNode('tree','psnodeSearchKey');
				}
			});
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
						treeObj.expandNode(nodes[key], true, false, true);
					}
				}
				
			}
	    	
	    	//保存权限
	    	function savePermission(){
	    		alertSuccess("设置成功！");
	    		setTimeout(function(){
	    			parent.$("#permissionModal").modal("hide");
	    		}, 1000);
	    		
	    	}
		
	</script>
</body>
</html>