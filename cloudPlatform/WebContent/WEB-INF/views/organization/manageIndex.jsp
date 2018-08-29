<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组织管理</title>
<link rel="stylesheet" href="<%=basePath %>css/ace.css">
<link rel="stylesheet" href="<%=basePath %>css/adminStyle.css">
<link rel="stylesheet" href="<%=basePath %>/zTree/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath %>css/organizationManage.css">

</head>
<body>
 	<div id="div_opera">
		<div class="bread">
			<div class="bread-container">
				<span class="fa fa-home"></span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="组织机构"> 组织机构</span>
				<span class="fa fa-lg fa-angle-right"></span>
				<span title="组织管理"> 组织管理</span>
			</div>
		</div>
		
		<div class="content">
			<div class="menu">
				<div class="box-header">
					<div class="header-title">组织</div>
					<div class="dropdown header-op">
						<a class="dropdown-toggle" data-toggle="dropdown" id="menu_dropdown" >选择操作<span class="fa fa-angle-down fa-fw "></span></a>
						 <ul class="dropdown-menu" role="menu" aria-labelledby="menu_dropdown">
						 	<li><a onclick="expandAll()"><i class="fa fa-plus-square-o fa-fw"></i>一键展开</a></li>
						 	<li><a onclick="foldAll()"><i class="fa fa-minus-square-o fa-fw"></i>一键关闭</a></li>
						 	<li><a onclick="expandNode()"><i class="fa fa-plus fa-fw"></i>展开所选</a></li>
						 	<li><a onclick="foldNode()"><i class="fa fa-minus fa-fw"></i>关闭所选</a></li>
						 	<li><a onclick="cancelHighLight()"><i class="fa fa-mail-reply fa-fw"></i>取消高亮</a></li>
						 	<li class="divider"></li>
						 	<li><a onclick="refreshNode()"><i class="fa fa-refresh fa-fw"></i>刷新</a></li>
						 </ul>
					</div>
				</div>
				
				<div class="menu-content">
					<div class="op-wrapper">
						<div>
							<a class="btn btn-primary btn-sm btn-white" onclick="showAcaModal()"><i class="fa fa-plus"></i>新增学院</a>
						</div>
						<div class="input-group input-group-sm search">
							<input type="text" class="form-control" placeholder="搜索组织节点" v-model="searchValue" v-on:keydown.enter="searchNodes()">
		                    <span class="input-group-btn">
		                        <button class="btn btn-primary btn-white" type="button" title="搜索" onclick="searchNodes()"><span class="fa fa-search"></span></button>
		                    </span>
						</div>
					</div>
					<div class="tree-wrapper">
						<div class="ztree" id="tree"></div>
					</div>
				</div>
			</div>
			
			<div class="opera">
				<div class="box-header">
					<div class="header-title" v-html="organization.org_name"></div>
				</div>	
				<div class="opera-content">
					<div class="btn-wrap">
						<a class="btn btn-primary btn-white" v-bind:disabled="isClass" onclick="showAddModal()"><i class="fa fa-plus fa-fw"></i>新增子节点</a>
						<a class="btn btn-success btn-white" v-on:click="showUpdateModal(organization)"><i class="fa fa-pencil fa-fw"></i>编辑<span v-html="orgMap[organization.org_type]"></span></a>
						<a class="btn btn-danger btn-white" v-on:click="deleteOrg(organization)"><i class="fa fa-trash fa-fw"></i>删除<span v-html="orgMap[organization.org_type]"></span></a>
						<a class="btn btn-danger btn-white" v-bind:disabled="isClass" onclick="deleteChecked()"><i class="fa fa-trash fa-fw"></i>删除所选子级组织</a>
					</div>
					
					<div class="table-wrapper">
						<div class="title">子节点列表</div>
						<div class="table-contanier">
							<table class="table table-bordered table-striped table-hover" v-show="haveChildren">
								<thead>
									<tr>
										<th style="width:10%"><input type="checkbox" v-model="checkAll" v-on:click="changeCheck()"></th>
										<th style="width:10%;">序号</th>
										<th style="width:28%"><span v-html="orgMap[organization.org_type + 1]"></span>名称</th>
										<th style="width:28%">所属<span  v-html="orgMap[organization.org_type]"></span></th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="(child,index) in orgChildren">
										<td><input type="checkbox" v-model="child.isChecked"></td>
										<td  v-bind:title="index + 1">{{index + 1}}</td>
										<td v-bind:title="child.org_name">{{child.org_name}}</td>
										<td v-bind:title="organization.org_name">{{organization.org_name}}</td>
										<td >
											<a class="btn-parallel btn btn-success btn-sm btn-white" title="编辑" v-on:click="showUpdateModal(child)"><i class="fa fa-pencil"></i>编辑</a>
											<a class="btn-parallel btn btn-danger btn-sm btn-white" title="删除" v-on:click="deleteOrg(child)"><i class="fa fa-trash"></i>删除</a>
										</td>
									</tr>
								</tbody>
							</table>
							<div v-show="!haveChildren" class="table-tip">
								<div class="alert alert-info">
									子节点为空！
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 新增模态框 -->
		<div class="modal fade" tabindex="-1" id="addModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" >新增{{addModal.title}}</h4>
		            </div>
		            <div class="modal-body">
		            	<form class="form-horizontal" onsubmit="return false;">
		            		<div class="form-group">
		            			<label class="control-label col-md-3">{{orgMap[addModal.type]}}名称:</label>
		            			<div class="col-md-7">
		            				<input class="form-control" v-model="addModal.name" v-on:keydown.enter="addOrganization()" placeholder="输入组织名称">
		            			</div>
		            		</div>
		            		<div class="form-group" v-show="addModal.showParent">
		            			<label class="control-label col-md-3">所属{{orgMap[addModal.type - 1]}}:</label>
		            			<div class="col-md-5">
		            				<input class="form-control " disabled v-bind:value="addModal.parentName">
		            			</div>
		            		</div>
		            	</form>
		            </div>
		            <div class="modal-footer">
		                <a class="btn btn-primary btn-white" onclick="addOrganization()"><i class="fa fa-check" ></i>新增</a>
		                 <a class="btn btn-default btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</a>
		            </div>
				</div>
			</div>
		</div>
		
		<!-- 编辑模态框 -->
		<div class="modal fade" tabindex="-1" id="updateModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" >编辑{{updateModal.org_name}}</h4>
		            </div>
		            <div class="modal-body">
		            	<form class="form-horizontal" onsubmit="return false;">
		            		<div class="form-group">
		            			<label class="control-label col-md-3">
		            				{{orgMap[updateModal.org_type]}}名称：
		            			</label>
		            			<div class="col-md-7">
		            				<input class="form-control" v-model="updateName" v-on:keydown.enter="updateOrganization()"  placeholder="输入组织名称">
		            			</div>
		            		</div>
		            		
		            		<div class="form-group" v-show="updateModal.org_pid != 0">
		            			<label class="control-label col-md-3">
		            				所属{{orgMap[updateModal.org_type-1]}}：
		            			</label>
		            			<div class="col-md-7">
		            				<select class="form-control" v-model="updateModal.updateParent" v-on:change="changeParent()">
		            					<option v-for="parent in modalParents" v-bind:value="parent.org_id">{{parent.fullName}}</option>
		            				</select>
		            			</div>
		            		</div>
		            		<div class="form-gorup clearfix" v-show="isChangeParent">
		            			<div class="col-md-offset-1 col-md-10">
		            				<div class="alert alert-danger">
			            				注意！所属{{orgMap[updateModal.org_type-1]}}与原来不一致，确认是否要修改！
			            			</div>
		            			</div>
		            			
		            		</div>
		            	</form>
		            </div>
		            <div class="modal-footer">
		                <a class="btn btn-primary btn-white" onclick="updateOrganization()"><i class="fa fa-pencil" ></i>编辑</a>
		                 <a class="btn btn-default btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</a>
		            </div>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/vue.min.js"></script>
<script type="text/javascript" src="<%=basePath%>layui/layui.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		//防止a标签多次点击选择文本
		$("a").on("selectstart",function() {
			return false;
		});
		
		initTreeData();
	});
	
	//组织等级映射
	var orgMap = ["学校","学院","专业","年级","班级"];
	
	//zTree
	var zTreeObj;
	//zTree配置
	var zTreeSetting = {
			data: {
				simpleData: {
					enable: true,
					idKey: "org_id",
					pIdKey: "org_pid",
					rootPId: 0
				},
				key: {
					name: "org_name"
				}
				
			},
			callback: {
				onClick: function(event, treeId, treeNode) {
					initOperaPane(treeNode);
					
				}
			},
			view: {
				fontCss: getFontCss,
				
			}
	}
	
	//搜索高亮
	function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {color:"#A60000"} : {color:"#333"};
	}
	//初始化zTree数据
	function initTreeData() {
		//获取组织结构数据
		$.post("<%=basePath%>organization/listOrganizations",{},function(rs) {
			//console.log(rs);
			zTreeObj = $.fn.zTree.init($("#tree"), zTreeSetting, rs.data);
			//自定义图标
			initCustomIcon();
			//展开第一个节点
			initChosen();
		},"json");
	}
	
	//点击搜索按钮，设置节点高亮
	function searchNodes() {
		initHighLight()
		if(opera.searchValue == "") {
			return false;
		}
		var nodes = zTreeObj.getNodesByParamFuzzy("org_name",opera.searchValue,null);
		updateNodes(nodes,true);
		
	}
	
	//节点全部恢复默认
	function initHighLight() {
		var nodes = zTreeObj.getNodesByParamFuzzy("org_name","",null);
		updateNodes(nodes,false);
	}
	
	//更新节点
	function updateNodes(nodeList,isHighLight) {
		for(var i=0;i<nodeList.length;i++) {
			nodeList[i].highlight = isHighLight;
			zTreeObj.updateNode(nodeList[i]);
			if(isHighLight) {
				//获取父亲节点
				var parents = zTreeObj.getNodesByParam("org_id",nodeList[i].org_pid,null);
				if(parents.length > 0) {
					zTreeObj.expandNode(parents[0],true,false,false);
				}
			}
		}
	}
	
	//初始化layer
	layui.use(["layer"],function() {
		var layer = layui.layer;
	})
	
	//初始化vue
	var opera = new Vue({
		el: "#div_opera",
		data: {
			organization: {
				org_name: "未选中",
				org_id: 0,
			},
			isClass: false,
			orgMap: orgMap,
			orgChildren: new Array(),
			haveChildren: true,
			addModal: {
				type: 1,
				name: "",
				parentName: "",
				showParent: false,
				title: "学院"
			},
			updateModal: {
				
			},
			updateName: "",               //编辑模态框输入框的值
			isChangeParent: false,           //是否显示编辑模态框警告框标志
			modalParents: [],
			searchValue: "" ,               //搜索输入框            
			checkAll: false,                     //表格复选框选中
		},
		methods: {
			deleteOrg: function(org) {
				deleteOrg(org.org_id,org.org_name);
			},
			showUpdateModal: function(org) {
				showUpdateModal(org)
			},
			changeParent: function() {        //改变编辑模态框中所属组织下拉框
				this.isChangeParent = (this.updateModal.org_pid != this.updateModal.updateParent);
				//console.log(this.isChangeParent);
			},
			searchNodes: function() {       //搜索节点
				searchNodes();
			},
			changeCheck: function() {
				//点击表格复选框
				for(var i=0;i<this.orgChildren.length;i++) {
					this.orgChildren[i].isChecked = !this.checkAll;
				}
			},
			addOrganization: function() {
				addOrganization();
			},
			updateOrganization: function() {
				updateOrganization();
			}
		}
	});
	
	//初始化组织操作面板
	function initOperaPane(node) {
		opera.organization = node;
		setDisabled(node);
		//获取子节点列表
		getChildren(opera.organization.org_id);
	}
	
	//初始化节点选择
	function initChosen() {
		
		var nodes = zTreeObj.getNodes();
		//console.log("nodes节点数：" + nodes.length);
		//console.log("初始化：");
		if (nodes != null && nodes.length>0) {
			zTreeObj.expandNode(nodes[0], true, true, true);
			//选中第一个节点
			zTreeObj.selectNode(nodes[0]);
			initOperaPane(nodes[0]);
		} else {
			var node = {
					org_id: -1,
					org_name: "未选中",
					org_type: 0,
					
			}
			initOperaPane(node);
		}
	}
	
	//按树节点的层数筛选
	function filter0(node) {
		return (node.level == 0);
	}
	function filter1(node) {
		return (node.level == 1);
	}
	function filter2(node) {
		return (node.level == 2);
	}
	function filter3(node) {
		return (node.level == 3);
	}
	
	function addIcon(nodes,icon) {
		for(var i=0;i<nodes.length;i++) {
			nodes[i].icon = icon;
			zTreeObj.updateNode(nodes[i]);
		}
	}
	//初始化自定义图标
	function initCustomIcon() {
		var icons = ["<%=basePath%>images/tree_acad.png","<%=basePath%>images/tree_major.png",
		             "<%=basePath%>images/tree_grade.png","<%=basePath%>images/tree_class.png"];
		for(var i=0;i<4;i++) {
			var nodes = zTreeObj.getNodesByFilter(eval("filter" + i));
			addIcon(nodes,icons[i]);
		}
	}
	
	//点击一键展开
	function expandAll() {
		expand(true);
	}
	
	//点击一键关闭
	function foldAll() {
		expand(false);
	}
	
	//展开或关闭树节点
	function expand(flag) {
		if(zTreeObj != null) {
			zTreeObj.expandAll(flag);
		}
	}
	
	//点击展开组织
	function expandNode() {
		expandNodeByFlag(true);
	}
	
	//点击关闭组织
	function foldNode() {
		expandNodeByFlag(false);
	}
	
	function expandNodeByFlag(isExpand) {
		//检查是否有选择节点
		if(!checkChosen) {
			return false;
		}
		//获取选中的节点
		var node = zTreeObj.getSelectedNodes();
		zTreeObj.expandNode(node[0],isExpand,true,false);
	}
	
	//点击刷新
	function refreshNode() {
		//获取组织结构数据
		$.post("<%=basePath%>organization/listOrganizations",{},function(rs) {
			//console.log(rs);
			zTreeObj = $.fn.zTree.init($("#tree"), zTreeSetting, rs.data);
			initCustomIcon();
			refresh();
		},"json");
	}
	
	//根据当前选中的节点刷新
	function refresh() {
		var selectedNodes = zTreeObj.getNodesByParam("org_id",opera.organization.org_id,null);
		var nodes = zTreeObj.getNodes();
		var node;
		if(selectedNodes.length > 0) {
			//有选中，则选中原来选中的
			node = selectedNodes[0];
		}else if(nodes.length > 0) {
			//没有选中的，选中第一个节点
			node = nodes[0];
		} else {
			node = {
					org_id: -1,
					org_name: "未选中",
					org_type: 0,
			}
		}
		//展开节点
		zTreeObj.expandNode(node, true, true, true);
		//选中节点
		zTreeObj.selectNode(node);
		initOperaPane(node);
	}
	
	//点击取消高亮
	function cancelHighLight() {
		initHighLight();
	}
	
	//判断节点组织类型，设置新增子节点按钮disabled
	function setDisabled(treeNode) {
		if(treeNode.org_type >= 4) {
			opera.isClass = true;
		}else {
			opera.isClass = false;
		}
	}
	
	//根据id获取组织子节点
	function getChildren(id) {
		//设置表格选中为false
		opera.checkAll = false;
		var data = {
				id: id
		};
		$.post("<%=basePath%>organization/getChildren",data,function(rs) {
			if(rs.code == "0000") {
				console.log(rs);
				opera.orgChildren = rs.data;
				//判断是否有子节点
				if(rs.data.length == 0) {
					opera.haveChildren = false;
				}else {
					opera.haveChildren = true;
				}
			}
		});
	}
	
	//检查是否选中节点
	function checkChosen() {
		 var nodes = zTreeObj.getSelectedNodes();
		 if(nodes.length <= 0) {
			 msgFail("未选择组织节点，请选择！");
			 return false;
		 }
		 return true;
	}
	
	//弹出新增学院模态框
	function showAcaModal() {
		//初始化模态框参数
		opera.addModal = {
				type: 1,
				name: "",
				parentId: 0,
				parentName: "",
				showParent: false,
				title: orgMap[1]
		}
		$("#addModal").modal();
		//layer.msg("成功！",{icon: 1});
	}
	
	//弹出新增子节点模态框
	function showAddModal() {
		if(!checkChosen()) {
			return false;
		}
	//	console.log(opera.organization.org_name);
		//初始化模态框参数
		opera.addModal = {
				type: opera.organization.org_type + 1,
				name: "",
				parentId: opera.organization.org_id,
				parentName: opera.organization.org_name,
				showParent: true,
				title: opera.organization.org_name + "子节点"
		}
		$("#addModal").modal();
	}
	
	//点击新增模态框的新增按钮
	function addOrganization() {
		if(opera.addModal.name == "") {
			msgFail(orgMap[opera.addModal.type] + "名称不能为空！请重输！");
			return false;
		}
		var data = {
				pid: opera.addModal.parentId,
				name: opera.addModal.name,
				type:  opera.addModal.type
		}
		//判断是否存在组织名称
		$.post("<%=basePath%>organization/existsOrganization",data,function(rs) {
			if(rs.code == "0000") {
				if(rs.data.result == 0) {
					//存在
					if(data.type == 1) {
						msgFail("学院名称已存在，请重输！");
					} else {
						msgFail(opera.organization.org_name + "已存在该子节点，请重输！");
					}
				} else {
					//不存在，添加组织
					addLeaf(data);
				}
			}
		});
		
	}
	
	//添加节点
	function addLeaf(data) {
		$.post("<%=basePath%>organization/addOrganization",data,function(rs) {
			if(rs.code == "0000") {
				msgSuccess(orgMap[opera.addModal.type] + "添加成功！");
				if(data.type == 1) {
					//学院，添加根节点
					zTreeObj.addNodes(null,rs.data);
				}else {
					//非学院节点
					var nodes = zTreeObj.getSelectedNodes();
					if(nodes.length > 0) {
						zTreeObj.addNodes(nodes[0],rs.data);
					}
					//刷新子节点列表
					getChildren(opera.organization.org_id);
				}
				//刷新图标
				initCustomIcon();
				$("#addModal").modal("hide");
			}else {
				msgFail(orgMap[opera.addModal.type] + "添加失败！");
			}
		},"json");
	}
	
	//删除节点
	function deleteOrg(id,name) {
		//console.log(id + " " + name);
		//判断是否删除选中节点
		var isSelf = false;
		if(id == opera.organization.org_id) {
			isSelf = true;
		}
		if(isSelf) {
			//检查是否选中节点
			if(!checkChosen()) {
				return false;
			};
		}
		var data = {
				id: id
		};
		layer.confirm("<p>确定删除组织【"+ name + "】</p>及其子节点？", {
			  btn: ['删除','取消'], //按钮
			  icon: 3,
			  offset: "250px",
			  title: "提示"
			}, function(index){
				//请求后端删除数据
				$.post("<%=basePath%>organization/deleteOrganization",data,function(rs){
					if(rs.code == "0000") {
						msgSuccess("【" + rs.data.org_name + "】及其子节点删除成功！");
						//搜索删除的节点
						var node = zTreeObj.getNodeByParam("org_id",id,null);
						//console.log("删除的节点：" + node.org_name);
						//删除该节点以及子节点
						zTreeObj.removeChildNodes(node);
						zTreeObj.removeNode(node);
						
						//判断是否选中的节点
						if(isSelf) {
							//是选中节点,选中第一个
							initChosen();
						}else {
							//刷新子节点列表
							getChildren(opera.organization.org_id);
						}
					} else {
						msgFail(rs.data.org_name + "删除失败！");
					}
				},"json");
			}, function(index){
				layer.close(index);
			});
	}
	
	//弹出编辑模态框
	function showUpdateModal(organization) {
		opera.updateModal = organization;
		opera.updateName = organization.org_name;
		//设置选中
		opera.updateModal.updateParent = organization.org_pid;
		opera.isChangeParent = false;
		//请求父级组织列表
		getParentList(organization.org_type-1,organization.org_pid);
		$("#updateModal").modal();
	}
	
	//获取父级组织列表
	function getParentList(type,pid) {
		if(pid == 0 || type <= 0) {
			return false;
		}
		var data = {
				type: type
		};
		$.post("<%=basePath%>organization/getOrgByType",data,function(rs) {
		//	console.log(rs);
			if(rs.code == "0000") {
				opera.modalParents = new Array();
				for(var i=0;i<rs.data.length;i++) {
					opera.modalParents[i] = rs.data[i].organization;
					//判断是否有父级组织
					if(rs.data[i].parent != null) {
						opera.modalParents[i].fullName = opera.modalParents[i].org_name + " (" + rs.data[i].parent.org_name +")"
					}else {
						opera.modalParents[i].fullName = opera.modalParents[i].org_name;
					}
					
					//设置选中
					/*
					if(opera.modalParents[i].org_id == pid) {
						opera.updateModal.updateParent = opera.modalParents[i].org_id;
					}
					*/
				}
				//console.log(opera.modalParents);
			}else {
				msgFail("父级组织请求失败！");
			}
		},"json").fail(function() {
			msgFail("父级组织请求失败！请检查网络！");
		});
	}
	
	//点击编辑模态框中的编辑按钮，完成组织的编辑
	function updateOrganization() {
		//名称与父节点未改变
		if(opera.updateModal.org_name == opera.updateName && opera.updateModal.org_pid == opera.updateModal.updateParent) {
			msgSuccess("编辑成功！");
			$("#updateModal").modal("hide");
			return false;
		}
		
		//判断是否编辑选中的节点
		var isSelf = false;
		if(opera.organization.org_id == opera.updateModal.org_id) {
			isSelf = true;
		}
		if(isSelf) {
			//检查是否有选中节点
			if(!checkChosen()) {
				return false;
			}
		}
		
		//检查组织名称是否存在
		var data = {
				id: opera.updateModal.org_id,
				name: opera.updateName,
				pid: opera.updateModal.updateParent,
				type: opera.updateModal.org_type
		};
		
		$.post("<%=basePath%>organization/existsOrganization",data,function(rs) {
			if(rs.code == "0000") {
				if(rs.data.result == 0) {
					//存在
					if(data.type == 1) {
						msgFail("学院名称已存在，请重输！");
					} else {
						msgFail("所属"+ orgMap[opera.organization.org_type - 1] +"已存在该子节点，请重输！");
					}
				} else {
					updateLeaf(data,isSelf);
				}
			}
		});
	}
	
	function updateLeaf(data,isSelf) {
		$.post("<%=basePath%>organization/updateOrganization",data,function(rs) {
			//不存在，修改组织
			var nodeUpdate = zTreeObj.getNodesByParam("org_id",opera.updateModal.org_id,null);
			if(nodeUpdate.length>0) {
				//改变节点名称
				nodeUpdate[0].org_name = rs.data.org_name;
				zTreeObj.updateNode(nodeUpdate[0]);
			}
			if(rs.data.org_pid != opera.updateModal.org_pid) {
				var nodeUpdate = zTreeObj.getNodesByParam("org_id",opera.updateModal.org_id,null);
				if(nodeUpdate.length>0 && nodeParent.length>0) {
					zTreeObj.moveNode(nodeParent[0],nodeUpdate[0],"inner");
					
				}
			}
			
			if(isSelf) {
				opera.organization = rs.data;
			}else {
				//刷新子节点
				getChildren(opera.organization.org_id);
			}
			$("#updateModal").modal("hide");
			msgSuccess("编辑成功！");
		},"json");
	}
	
	//删除所选子节点
	function deleteChecked() {
		var checkNum = 0;
		var idGroup = "";
		var nodes = new Array();
		for(var i=0;i<opera.orgChildren.length;i++) {
			if(opera.orgChildren[i].isChecked == true) {
				var id = opera.orgChildren[i].org_id;
				checkNum ++;
				idGroup += id + ",";
				//根据org_id查找对应的树节点
				var node = zTreeObj.getNodesByParam("org_id",id,null);
				if(node.length > 0) {
					nodes.push(node[0]);
				}
				
			}
		}
		if(checkNum <= 0) {
			msgFail("未选择子级组织！");
			return false;
		}
		
		
		//去除最后一个逗号
		idGroup = idGroup.substring(0,idGroup.length-1);
		//console.log("idGroup:" + idGroup);
		var data = {
				ids: idGroup
		};
		
		layer.confirm("<p>确定删除所选的子级组织？共选中" + checkNum + "个子级组织", {
			  btn: ['删除','取消'], //按钮
			  icon: 3,
			  offset: "250px",
			  title: "提示"
			}, function(index){
				//批量删除
				$.post("<%=basePath%>organization/deleteOrgByIds",data,function(rs) {
					if(rs.code == "0000") {
						msgSuccess("删除成功！共删除" + rs.data.num + "个子级组织");
						getChildren(opera.organization.org_id);
						//删除对应的树节点
						for(var i=0;i<nodes.length;i++) {
							//删除该节点以及子节点
							zTreeObj.removeChildNodes(nodes[i]);
							zTreeObj.removeNode(nodes[i]);
						}
					}else {
						msgFail("删除失败");
					}
				},"json");
			},function(index) {
				layer.close(index);
			});
	}
	
	//弹出成功提示框
	function msgSuccess(msg) {
		layer.msg(msg,{icon:1,offset:"200px"});
	}
	
	//弹出失败提示框
	function msgFail(msg) {
		layer.msg(msg,{icon:2,offset:"200px"});
	}
</script>
</body>
</html>