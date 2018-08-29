<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title></title>
<link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css">
<link rel="stylesheet" href="<%=basePath%>font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="zTree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="zTree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="zTree/js/jquery.ztree.exedit.js"></script>
<SCRIPT type="text/javascript">
		<!--
		var setting = {
			edit: {
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeDrag: beforeDrag,
				beforeDrop: beforeDrop
			}
		};

		var zNodes =[
			{ id:1, pId:0, name:"父节点 1", open:true},
			{ id:11, pId:1, name:"叶子节点 1-1"},
			{ id:12, pId:1, name:"叶子节点 1-2"},
			{ id:13, pId:1, name:"叶子节点 1-3"},
			{ id:2, pId:0, name:"父节点 2", open:true},
			{ id:21, pId:2, name:"叶子节点 2-1"},
			{ id:22, pId:2, name:"叶子节点 2-2"},
			{ id:23, pId:2, name:"叶子节点 2-3"},
			{ id:3, pId:0, name:"父节点 3", open:true},
			{ id:31, pId:3, name:"叶子节点 3-1"},
			{ id:32, pId:3, name:"叶子节点 3-2"},
			{ id:33, pId:3, name:"叶子节点 3-3"}
		];

		function beforeDrag(treeId, treeNodes) {
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					return false;
				}
			}
			return true;
		}
		function beforeDrop(treeId, treeNodes, targetNode, moveType) {
			return targetNode ? targetNode.drop !== false : true;
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$.fn.zTree.init($("#treeDemo2"), setting);
			
		});
		//-->
	</SCRIPT>
</HEAD>

<BODY>
<h1>多棵树之间 的 数据交互</h1>
<h6>[ 文件路径: exedit/multiTree.html ]</h6>
<div class="content_wrap">
	<div>
		<ul class="info">
			<li class="title"><h2>1、setting 配置信息说明</h2>
				<ul class="list">
				<li>zTree 对于多棵树之间拖拽的操作非常简单，只需要创建两棵可拖拽的树即可，同时可根据 各种事件回调函数 以及 zTree 的方法配合实现较复杂的操作规则，这里只是基本演示。</li>
				<li class="highlight_red">关于配置信息请参考拖拽、编辑等 Demo 的详细说明</li>
				</ul>
			</li>
			<li class="title"><h2>2、treeNode 节点数据说明</h2>
				<ul class="list">
				<li>对 节点数据 没有特殊要求，用户可以根据自己的需求添加自定义属性</li>
				</ul>
			</li>
		</ul>
	</div>
	<div class="zTreeDemoBackground left">
		<ul id="treeDemo" class="ztree"></ul>
	</div>
	<div class="right">
		<ul id="treeDemo2" class="ztree"></ul>
	</div>
</div>
</BODY>
</HTML>