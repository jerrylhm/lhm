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
<SCRIPT type="text/javascript">

		var setting = {
			check: {
				enable: true
				
			},
			data: {
				simpleData: {
					enable: true,
					idKey: 'id',
					pIdKey: 'pId',
					rootPId: 0
				}
			},
			callback: {
				beforeCollapse: zTreeBeforeCollapse,
				beforeExpand: zTreeBeforeExpand
			}
			
		};

		var zNodes =[
			{ id:1, pId:0, name:"父节点1 - 展开", open:true},
			{ id:11, pId:1, name:"父节点11 - 折叠"},
			{ id:111, pId:11, name:"叶子节点111"},
			{ id:112, pId:11, name:"叶子节点111"},
			{ id:113, pId:11, name:"叶子节点113"},
			{ id:114, pId:11, name:"叶子节点114"},
			{ id:12, pId:1, name:"父节点12 - 折叠"},
			{ id:121, pId:12, name:"叶子节点121"},
			{ id:122, pId:12, name:"叶子节点122"},
			{ id:123, pId:12, name:"叶子节点123"},
			{ id:124, pId:12, name:"叶子节点124"},
			{ id:13, pId:1, name:"父节点13 - 没有子节点", isParent:true},
			{ id:2, pId:0, name:"父节点2 - 折叠"},
			{ id:21, pId:2, name:"父节点21 - 展开"},
			{ id:211, pId:21, name:"叶子节点211"},
			{ id:212, pId:21, name:"叶子节点212"},
			{ id:213, pId:21, name:"叶子节点213"},
			{ id:214, pId:21, name:"叶子节点214"},
			{ id:22, pId:2, name:"父节点22 - 折叠"},
			{ id:221, pId:22, name:"叶子节点221"},
			{ id:222, pId:22, name:"叶子节点222"},
			{ id:223, pId:22, name:"叶子节点223"},
			{ id:224, pId:22, name:"叶子节点224"},
			{ id:23, pId:2, name:"父节点23 - 折叠"},
			{ id:231, pId:23, name:"叶子节点231"},
			{ id:232, pId:23, name:"叶子节点232"},
			{ id:233, pId:23, name:"叶子节点233"},
			{ id:234, pId:23, name:"叶子节点234"},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true},
			{ id:998, pId:0, name:"看过来", isParent:true}
		];

		$(document).ready(function(){
			$.fn.zTree.init($("#tree"), setting, zNodes);
		});
        function test() {
			
			
			var treeObj = $.fn.zTree.getZTreeObj("tree");
// 			var node = treeObj.getNodeByTId("tree_24");
// 			console.log(node)
// 			treeObj.expandNode(node, true, false, true, true);
            //精确查找(keyString需要精确匹配的属性名称value?需要精确匹配的属性值，可以是任何类型，只要保证与 key 指定的属性值保持一致即可parentNodeJSON可以指定在某个父节点下的子节点中搜索
			var nodes = treeObj.getNodesByParam("id", "998", null);
            for(var key in nodes) {
            	treeObj.expandNode(nodes[key], true, false, true, true);
            }
        }
		function getMyCheckNode() {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getCheckedNodes(true);
			for(var key in nodes) {
				console.log(nodes[key]);
				treeObj.expandNode(nodes[key], true, false, true, true);
			}
			return nodes;
		}
		function zTreeBeforeCollapse(treeId, treeNode) {
			console.log(treeId + ',' + treeNode.id)
		    return true;
		};
		function zTreeBeforeExpand(treeId, treeNode) {
			console.log(treeId + ',' + treeNode.id)
		    return true;
		};
		function cancalFocus() {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getSelectedNodes();
			for(var key in nodes) {
				treeObj.cancelSelectedNode(nodes[key]);
			}
		}
		
		//不点击节点时取消焦点
		$(function() {
			$('body').on('click',function(e){
				var id = e.target.id;
				id = id.substring(id.length - 4,id.length);
				if(id != 'span') {
					cancalFocus();
				}
			})
		})
		
	</SCRIPT>
 </head>

<body >
<a href="tel:0755-10086">打电话给:0755-10086</a>
<a href="sms:10086">发短信给: 10086</a>
<a href="mailto:863139978@qq.com">点击我发邮件</a>
<h1>最简单的树 -- 简单 JSON 数据</h1>
<h6>[ 文件路径: core/simpleData.html ]</h6>
<div class="content_wrap">
	<div id="treeDiv" class="zTreeDemoBackground left">
		<ul id="tree" class="ztree"></ul>
	</div>
	<div class="right">
		<ul class="info">
			<li class="title"><h2>1、setting 配置信息说明<button onclick="getMyCheckNode()">获取选中节点</button><button onclick="test()">test</button></h2>
				<ul class="list">
				<li class="highlight_red"><button onclick="cancalFocus()">取消聚焦</button>必须设置 setting.data.simple 内的属性，详细请参见 API 文档中的相关内容</li>
				<li>与显示相关的内容请参考 API 文档中 setting.view 内的配置信息</li>
				<li>name、children、title 等属性定义更改请参考 API 文档中 setting.data.key 内的配置信息</li>
				</ul>
			</li>
			<li class="title"><h2>2、treeNode 节点数据说明</h2>
				<ul class="list">
				<li class="highlight_red">简单模式的 JSON 数据需要使用 id / pId 表示节点的父子包含关系，如使用其他属性设置父子关联关系请参考 setting.data.simple 内各项说明
				<div><pre xmlns=""><code>例如：
var nodes = [
	{id:1, pId:0, name: "父节点1"},
	{id:11, pId:1, name: "子节点1"},
	{id:12, pId:1, name: "子节点2"}
];</code></pre></div></li>
				<li>默认展开的节点，请设置 treeNode.open 属性</li>
				<li>无子节点的父节点，请设置 treeNode.isParent 属性</li>
				<li>其他属性说明请参考 API 文档中 "treeNode 节点数据详解"</li>
				</ul>
			</li>
			<li class="title"><h2>3、其他说明</h2>
				<ul class="list">
				<li>Demo 中绝大部分都采用简单 JSON 数据模式，以便于大家学习</li>
				</ul>
			</li>
		</ul>
	</div>
</div>	

<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>


</body>
</html>