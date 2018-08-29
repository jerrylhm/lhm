<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<HTML>
<HEAD>
	<TITLE> ZTREE DEMO - copyNode / moveNode</TITLE>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="<%=basePath%>zTree/css/demo.css" type="text/css">
	<link rel="stylesheet" href="<%=basePath%>zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="<%=basePath%>zTree/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.excheck.js"></script>
	<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.exedit.js"></script>
	<SCRIPT type="text/javascript">
		<!--
		var setting = {
			view: {
				selectedMulti: false
			},
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
				beforeClick: beforeClick
			}
		};

		var zNodes =[{"chkDisabled":false,"creator":true,"hasP":true,"icon":"","id":434,"isCreator":true,"myUrl":"","name":"A大楼","nodeClass":"","open":false,"pId":0,"scene":"","sn":"","target":"treeValue","title":"A大楼(普通节点)","tpId":"-1","tsType":0,"type":{"type_id":1,"type_name":"normal","type_name_cn":"普通节点","type_pid":0},"url":""},{"chkDisabled":false,"creator":true,"hasP":true,"icon":"","id":435,"isCreator":true,"myUrl":"","name":"1楼","nodeClass":"","open":false,"pId":434,"scene":"","sn":"","target":"treeValue","title":"1楼(普通节点)","tpId":"-1","tsType":0,"type":{"type_id":1,"type_name":"normal","type_name_cn":"普通节点","type_pid":0},"url":""},{"chkDisabled":false,"creator":true,"hasP":true,"icon":"","id":436,"isCreator":true,"myUrl":"","name":"2楼","nodeClass":"","open":false,"pId":434,"scene":"","sn":"","target":"treeValue","title":"2楼(普通节点)","tpId":"-1","tsType":0,"type":{"type_id":1,"type_name":"normal","type_name_cn":"普通节点","type_pid":0},"url":""},{"chkDisabled":false,"creator":true,"hasP":true,"icon":"","id":441,"isCreator":true,"myUrl":"","name":"啊啊阿","nodeClass":"","open":false,"pId":435,"scene":"","sn":"","target":"treeValue","title":"啊啊阿(普通节点)","tpId":"-1","tsType":0,"type":{"type_id":1,"type_name":"normal","type_name_cn":"普通节点","type_pid":0},"url":""},{"chkDisabled":false,"creator":true,"hasP":true,"icon":"","id":442,"isCreator":true,"myUrl":"","name":"啊啊阿","nodeClass":"","open":false,"pId":434,"scene":"","sn":"","target":"treeValue","title":"啊啊阿(普通节点)","tpId":"-1","tsType":0,"type":{"type_id":1,"type_name":"normal","type_name_cn":"普通节点","type_pid":0},"url":""},{"chkDisabled":false,"creator":true,"hasP":true,"icon":"","id":443,"isCreator":true,"myUrl":"","name":"电视上","nodeClass":"","open":false,"pId":436,"scene":"","sn":"","target":"treeValue","title":"电视上(普通节点)","tpId":"-1","tsType":0,"type":{"type_id":1,"type_name":"normal","type_name_cn":"普通节点","type_pid":0},"url":""},{"chkDisabled":false,"creator":true,"hasP":true,"icon":"","id":444,"isCreator":true,"myUrl":"","name":"倒萨打算","nodeClass":"","open":false,"pId":435,"scene":"","sn":"","target":"treeValue","title":"倒萨打算(普通节点)","tpId":"-1","tsType":0,"type":{"type_id":1,"type_name":"normal","type_name_cn":"普通节点","type_pid":0},"url":""}];

		function fontCss(treeNode) {
			var aObj = $("#" + treeNode.tId + "_a");
			aObj.removeClass("copy").removeClass("cut");
			if (treeNode === curSrcNode) {
				if (curType == "copy") {
					aObj.addClass(curType);
				} else {
					aObj.addClass(curType);
				}			
			}
		}

		function beforeDrag(treeId, treeNodes) {
			return false;
		}

		function beforeClick(treeId, treeNode) {
			return !treeNode.isCur;
		}

		var curSrcNode, curType;
		function setCurSrcNode(treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			if (curSrcNode) {
				delete curSrcNode.isCur;
				var tmpNode = curSrcNode;
				curSrcNode = null;
				fontCss(tmpNode);
			}
			curSrcNode = treeNode;
			if (!treeNode) return;

			curSrcNode.isCur = true;			
			zTree.cancelSelectedNode();
			fontCss(curSrcNode);
		}
		function copy(e) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes();
			if (nodes.length == 0) {
				alert("请先选择一个节点");
				return;
			}
			curType = "copy";
			setCurSrcNode(nodes[0]);
			
		}
		function cut(e) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes();
			if (nodes.length == 0) {
				alert("请先选择一个节点");
				return;
			}
			curType = "cut";
			setCurSrcNode(nodes[0]);
		}
		function paste(e) {
			if (!curSrcNode) {
				alert("请先选择一个节点进行 复制 / 剪切");
				return;
			}
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes(),
			targetNode = nodes.length>0? nodes[0]:null;
			if (curSrcNode === targetNode) {
				alert("不能移动，源节点 与 目标节点相同");
				return;
			} else if (curType === "cut" && ((!!targetNode && curSrcNode.parentTId === targetNode.tId) || (!targetNode && !curSrcNode.parentTId))) {
				alert("不能移动，源节点 已经存在于 目标节点中");
				return;
			} else if (curType === "copy") {
				console.log(curSrcNode)
				targetNode = zTree.copyNode(targetNode, curSrcNode, "inner");
			} else if (curType === "cut") {
				targetNode = zTree.moveNode(targetNode, curSrcNode, "inner");
				if (!targetNode) {
					alert("剪切失败，源节点是目标节点的父节点");
				}
				targetNode = curSrcNode;
			}
			//setCurSrcNode();
			//delete targetNode.isCur;
			zTree.selectNode(targetNode);
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$("#copy").bind("click", copy);
			$("#cut").bind("click", cut);
			$("#paste").bind("click", paste);
		});
		//-->
	</SCRIPT>
	<style type="text/css">
.ztree li a.copy{padding-top:0; background-color:#316AC5; color:white; border:1px #316AC5 solid;}
.ztree li a.cut{padding-top:0; background-color:silver; color:#111; border:1px #316AC5 dotted;}

.test {
    position: relative;
    width: 120px;
    height: 40px;
    border: 1px solid #d2d2d2;
    border-radius: 3px;
    transition: all 5.5s ease-in .1s;
}
.test:after {
    position: absolute;
    right: 15px;
    top: 18px;
    width: 0;
    height: 0;
    content: "";
    border-width: 6px 6px 0 6px;
    border-style: solid;
    border-color: #fff transparent;
    -webkit-transition: all .25s;
       -moz-transition: all .25s;
        -ms-transition: all .25s;
         -o-transition: all .25s;
            transition: all .25s;
}

.test:before {
    position: absolute;
    right: 13px;
    top: 18px;
    width: 0;
    height: 0;
    content: "";
    border-width: 8px 8px 0 8px;
    border-style: solid;
    border-color: #d36969 transparent;
    -webkit-transition: transform .25s;
       -moz-transition: transform .25s;
        -ms-transition: transform .25s;
         -o-transition: transform .25s;
            transition: transform .25s;
}
.test.active:after{       
    top: 20px;
    -webkit-transform: rotate(180deg);
       -moz-transform: rotate(180deg);
        -ms-transform: rotate(180deg);
         -o-transform: rotate(180deg);
            transform: rotate(180deg); 
}
.test.active:before{
    -webkit-transform: rotate(180deg);
       -moz-transform: rotate(180deg);
        -ms-transform: rotate(180deg);
         -o-transform: rotate(180deg);
            transform: rotate(180deg);        
}
	</style>
</HEAD>

<BODY>
<h1>用 zTree 方法 移动 / 复制节点</h1>
<h6>[ 文件路径: exedit/drag_fun.html ]</h6>
<div class="content_wrap">
	<div class="zTreeDemoBackground left">
		<ul id="treeDemo" class="ztree"></ul>
	</div>
	<div class="right">
		<ul class="info">
			<li class="title"><h2>1、copyNode / moveNode 方法操作说明</h2>
				<ul class="list">
				<li>利用 copyNode / moveNode 方法也可以实现 复制 / 移动 节点的目的，这里简单演示使用方法</li>
				<li><p>对节点进行 复制 / 剪切，试试看：<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;[ <a id="copy" href="#" title="复制" onclick="return false;">复制</a> ]
					&nbsp;&nbsp;&nbsp;&nbsp;[ <a id="cut" href="#" title="剪切" onclick="return false;">剪切</a> ]
					&nbsp;&nbsp;&nbsp;&nbsp;[ <a id="paste" href="#" title="粘贴" onclick="return false;">粘贴</a> ]</p>
				<li class="highlight_red">使用 zTreeObj.copyNode / moveNode 方法，详细请参见 API 文档中的相关内容</li>
				</ul>
			</li>
			<li class="title"><h2>2、setting 配置信息说明</h2>
				<ul class="list">
				<li>同 "拖拽 节点 基本控制"</li>
				</ul>
			</li>
			<li class="title"><h2>3、treeNode 节点数据说明</h2>
				<ul class="list">
				<li>同 "拖拽 节点 基本控制"</li>
				</ul>
			</li>
		</ul>
	</div>
</div>
<div class="test"></div>
<button onclick="change()">dasd</button>
<script type="text/javascript">
    function change() {
    	$('.test').css('width', '500px');
    	$('.test').css('background-color', 'red');
    	$('.test').css('border-radius', '50px');
    	$('.test').css('box-shadow', '10px 10px #ccc');
    }
</script>
</BODY>
</HTML>