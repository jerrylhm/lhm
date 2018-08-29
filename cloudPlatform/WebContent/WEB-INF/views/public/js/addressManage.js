/**
 * 场所管理
 */
var defaultAddress = {
		add_id: 0,
		add_name: "未选中",
		add_type: 0,
		add_pid: 0
		
};
var normalType = 1;      //普通节点类型
var classRoomType = 0;   //课室节点类型
//节点类型与名称映射
var typeMap = ["课室","场所"];
//console.log(typeMap);
//初始化新增模态框信息
var defaultAddModal = {
		name: "",
		camera: "",
		timetable: 0,
		showParent: false,
		isAddClass: false,
		timetables: new Array
};

//初始编辑模态框信息
var defaultUpdateModal = {
		name: "",
		camera: "",
		timetable: 0,
		showParent: false,
		isAddClass: false,
		timetables: new Array(),
		address: {},
		parent: {},
}
//初始化分页参数
var defaultPage = {
		index: 1,
		pageCount: 0,
		indexOptions: new Array(1),
		selectIndex: 1
}; 
//批量设置课表设置范围选择
var defaultRanges = [{
	id: 1,
	name: "下一级课室节点",
	isChecked: true
},{
	id: 2,
	name: "后代所有课室节点"
},{
	id: 3,
	name: "所有课室节点"
}];
//初始化Vue
var vue = new Vue({
	el: "#address",
	data: {
		typeMap: typeMap,           //节点类型与名称映射
		address: cloneObject(defaultAddress),          //当前选中的场所，深拷贝默认地址
		isClassRoom: true,               //是否课室标志
		addModal: cloneObject(defaultAddModal),                    //新增模态框中的信息
		haveChildren: false,                       //是否有子节点
		addressChildren: new Array(),            //子节点列表
		checkAll: false,
		page: cloneObject(defaultPage),
		updateModal: cloneObject(defaultUpdateModal),                    //编辑模态框的信息
		searchValue: "",                       //搜索值
		cameraTitle: "未设置",                  //课室ip信息
		timetableTitle: "未设置",
		setModal: {
			ranges: cloneObject(defaultRanges),
			timetable: 0,
			timetables: new Array(),
		},
		
	},
	methods: {
		changePage: function() {
			//改变分页下拉框
			getChildren(this.page.selectIndex);
		},
		pre: function() {
			//上一页
			if(this.page.index <= 1) {
				msgFail("当前已是首页");
				return false;
			}
			getChildren(this.page.index - 1);
		},
		next: function() {
			//下一页
			if(this.page.index >= this.page.pageCount) {
				msgFail("当前已是尾页");
				return false;
			}
			getChildren(this.page.index + 1);
		},
		showUpdateModal: function(address) {
			showUpdateModal(address);
		},
		addAddress: function() {
			addAddress();
		},
		showDelete: function(address,isSelf) {
			showDelete(address,isSelf);
		},
		changeCheck: function() {
			//全选/全不选
			for(var i=0;i<this.addressChildren.length;i++) {
				this.addressChildren[i].isChecked = !this.checkAll;
			}
		},
		updateAddress: function() {
			updateAddress();
		}
	},
	
});

//初始化layer
var layer;
var form;
layui.use(["layer","form"],function() {
	layer = layui.layer;
	form = layui.form;
	form.on('switch(add_classroom)',function(data) {
		vue.addModal.isAddClass = !vue.addModal.isAddClass;
	});
	
	//监听新增模态框中的下拉框
	form.on("select(add_timetable)",function(data) {
//		console.log(data.value);
		vue.addModal.timetable = data.value;
	});
	
	//监听编辑模态框中的开关
	form.on("switch(update_classroom)",function(data) {
		vue.updateModal.isAddClass = !vue.updateModal.isAddClass;
//		console.log(vue.updateModal.isAddClass);
	});
	
	
	//监听编辑模态框中的课表选择下拉框
	form.on("select(update_timetable)",function(data) {
//		console.log(data.value);
		vue.updateModal.timetable = data.value;
	});
	
	//监听批量设置课表模态框课表选择下拉框
//	form.on("select(set_timetable)",function(data) {
//		//vue.setModal.timetable = data.value;
//		var value = $("input[name=range]:checked").val();
//		console.log("value:" + value);
//		//form.val("setForm",{range: value});
//	});
	
//	form.on("radio(set_range)",function(data) {
//		console.log(data.elem);
//		console.log(data.value);
//		//选中对应的值
//		var index = 0;
//		$("input[name=range]").each(function() {
//			index ++;
//			if($(this).val() == data.value) {
//				console.log("index:" + index);
//				$(this).prop("checked",true);
//			}
//		});
//		
//	});
});
//初始化ztree
var zTreeObj;
//zTree配置
var zTreeSetting = {
		data: {
			simpleData: {
				enable: true,
				idKey: "add_id",
				pIdKey: "add_pid",
				rootPId: 0
			},
			key: {
				name: "add_name"
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


$(document).ready(function() {
	initTreeData(selectFirst);
	//防止a标签多次点击选择文本
	$("a").on("selectstart",function() {
		return false;
	});
});



//获取场景树节点数据
function initTreeData(callback) {
	$.post("../organization/listAddress",{},function(rs) {
//		console.log(rs);
		if(rs.code == "0000") {
			//初始化树数据
			zTreeObj = $.fn.zTree.init($("#tree"), zTreeSetting, rs.data);
			//自定义图标
			initCustomIcon();
			callback();
		}else {
			msgFail("场所加载失败");
		}
	}).fail(function() {
		msgFail("场所初始化失败,请检查网络");
	});
}
//选中第一个节点
function selectFirst() {
	var node = cloneObject(defaultAddress);
	var nodes = zTreeObj.getNodes();
	if(nodes != null && nodes.length > 0) {
		node = nodes[0];
	}
	selectNode(node);
}

//选中当前选中的
function selectCurrent() {
	//判断当前是否选中
	var nodes = zTreeObj.getSelectedNodes();
	if(nodes.length > 0) {
		selectNode(nodes[0]);
	}else {
		selectFirst();
	}
}

//选中指定节点
function selectNodes(nodes) {
	if(nodes.length > 0) {
		var nodesGet = zTreeObj.getNodesByParam("add_id",nodes[0].add_id,null);
		selectNode(nodesGet[0]);
	}else {
		selectFirst();
	}
}

//根据节点选中
function selectNode(node) {
	//展开该节点
	zTreeObj.expandNode(node, true, true, true);
	//选中节点
	if(node.add_id != 0) {
		zTreeObj.selectNode(node);
	}
	//初始化操作面板
	initOperaPane(node)
}

//初始化右边面板
function initOperaPane(node) {
//	console.log(node);
	vue.address = node;
	setDisabled(node);
	getChildren(1);
	vue.page = cloneObject(defaultPage);
	vue.checkAll = false;
	
	//初始化课室信息
	initClassRoom(node);
}

//初始化课室信息
function initClassRoom(node) {
	if(node.add_type == normalType) {
		return false;
	}
	if(node.add_camera == null || node.add_camera == "") {
		vue.cameraTitle = "未设置";
	}else {
		vue.cameraTitle = node.add_camera;
	}
	if(node.add_ttid == null || node.add_ttid == 0) {
		vue.timetableTitle = "未设置";
		return false;
	}
	//获取课表信息
	var data = {
			id: node.add_ttid
	};
	$.post("../organization/getTimeTableByTtid",data,function(rs) {
		if(rs.code == "0000") {
			vue.timetableTitle = rs.data.tt_name + "(第一周时间：" + rs.data.tt_firstweek + ")";
		}
	},"json");
}


//设置右边面板按钮是否disabled
function setDisabled(node) {
	if(node.add_type == normalType) {
		vue.isClassRoom = false;
	}else {
		vue.isClassRoom = true;
	}
}

//根据页数获取子节点列表
function getChildren(index) {
	var data = {
			index: index,
			count: 8,
			id: vue.address.add_id
	}
	$.post("../organization/getAddressChildren",data,function(rs) {
//		console.log(rs.data);
		if(rs.code == "0000") {
			var data = rs.data.address;
//			console.log(data);
			vue.addressChildren = data;
			if(data.length > 0) {
				vue.haveChildren = true;
			}else {
				vue.haveChildren = false;
			}
			//设置分页变量
			vue.page.index = rs.data.index;
			vue.page.pageCount = rs.data.pageCount;
			vue.page.indexOptions = new Array(rs.data.pageCount);
			vue.page.selectIndex = rs.data.index;
		}else {
			msgFail("子节点加载失败");
		}
	},"json");
}

//筛选节点
function filterNormal(node) {
	return (node.add_type == normalType);
}

function filterClassRoom(node) {
	return (node.add_type == classRoomType);
}

//设置图标
function addIcon(nodes,icon) {
	for(var i=0;i<nodes.length;i++) {
		nodes[i].icon = icon;
		zTreeObj.updateNode(nodes[i]);
	}
}

//初始化图标
function initCustomIcon() {
	var normalNodes = zTreeObj.getNodesByFilter(filterNormal)
	addIcon(normalNodes,"../images/tree_normal.png");
	
	var classRoomNodes = zTreeObj.getNodesByFilter(filterClassRoom);
	addIcon(classRoomNodes,"../images/tree_classroom.png");
}

//弹出新增模态框
function showAddModal(isRoot) {
	if(!isRoot) {
		//检查是否选中节点
		if(!checkChosen()) {
			return false;
		}
	}
	
	//vue.addModal = vue.address;
	var addModal = cloneObject(defaultAddModal);
//	console.log(addModal);
	if(isRoot) {
		//新增根节点
		addModal.showParent = false;
		addModal.title = "根节点";
		addModal.pid = 0;
	}else {
		//新增子节点
		addModal.title = "场所";
		addModal.showParent = true;
		addModal.pid = vue.address.add_id;
	}
	
	vue.addModal.isAddClass = false;
	vue.addModal = addModal
	//初始化表单
	form.val("addForm",{add:false});
	
	//获取课表列表
	getTimeTable(addModalCallback);
	$("#addModal").modal();
}

//获取课表列表
function getTimeTable(callback) {
	$.post("../organization/listTimeTable",{},function(rs) {
//		console.log(rs);
		if(rs.code == "0000") {
			callback(rs.data);
		}else {
			msgFail("课表列表加载失败！");
		}
	});
}

function addModalCallback(data) {
	
	vue.addModal.timetables = data;
	//数据加载完成后渲染
	vue.$nextTick(function() {
		form.render("select","addForm");
	});
	
}

//编辑模态框查询课表后回调函数
function updateModalCallback(data) {
	vue.updateModal.timetables = data;
	vue.$nextTick(function() {
		form.render("select","updateForm");
	});
}

//点击新增模态框中的新增按钮
function addAddress() {
//	console.log(vue.addModal);
	var addModal = vue.addModal;
	//判断名称是否为空
	if(addModal.name == "") {
		msgFail("场所名称不能为空");
		return false;
	}
	//判断添加的类型
	var type = normalType;
	if( addModal.isAddClass) {
		type = classRoomType;
	}
	var title = typeMap[type];
	var data = {
			name: addModal.name,
			pid: addModal.pid,
			camera: addModal.camera,
			timetable: addModal.timetable,
			isClassRoom: addModal.isAddClass
	};
	$.post("../organization/addAddress",data,function(rs) {
		if(rs.code == "0000") {
			//判断是否为根节点
			if(addModal.pid == 0) {
				//根节点
				zTreeObj.addNodes(null,rs.data);
			}else {
				var nodes = zTreeObj.getNodesByParam("add_id",vue.address.add_id);
				if(nodes.length > 0) {
					zTreeObj.addNodes(nodes[0],rs.data);
					getChildren(vue.page.index);
				}
			}
			msgSuccess(title + "添加成功");
			$("#addModal").modal("hide");
			//更新图标
			initCustomIcon();
		}else {
			msgFail(title + "添加失败");
		}
	},"json");
}


//点击编辑按钮，弹出编辑模态框
function showUpdateModal(address) {
	//检查是否选中节点
	if(!checkChosen()) {
		return false;
	}
	vue.updateModal.address = address;
	if(address.add_pid == 0) {
		vue.updateModal.showParent = false;
	}else {
		vue.updateModal.showParent = true;
		//查找父节点
		var nodes = zTreeObj.getNodesByParam("add_id",address.add_pid,null);
//		console.log("nodes:" + nodes.length);
		if(nodes.length > 0) {
			vue.updateModal.parent = nodes[0];
		}else {
			vue.updateModal.parent = {};
		}
	}
	vue.updateModal.name = address.add_name;
	vue.updateModal.isAddClass = false;
	var camera = "";
	var timetable = 0;
	if(address.add_type == classRoomType) {
		vue.updateModal.isAddClass = true;
		camera = address.add_camera;
		timetable = address.add_ttid;
	}
	vue.updateModal.camera = camera;
	vue.updateModal.timetable = timetable;
	
	//初始化表单
	form.val("updateForm",{classroom: vue.updateModal.isAddClass});
//	console.log(address.add_pid);
//	console.log(vue.updateModal.showParent);
	//加载课表列表
	getTimeTable(updateModalCallback);
	$("#updateModal").modal();
}
//点击编辑模态框的编辑按钮
function updateAddress() {
//	console.log("编辑：");
//	console.log(vue.updateModal);
	var updateModal = vue.updateModal;
	if(updateModal.name == "") {
		msgFail("名称不能为空");
		return false;
	}
	//是否编辑本身
	var isSelf = false;
	if(updateModal.address.add_id == vue.address.add_id) {
		isSelf = true;
	}
	
	//是否从普通节点转课室
	var isTransToClassRoom = false;
	if(updateModal.address.add_type == normalType && updateModal.isAddClass == true) {
		isTransToClassRoom = true;
	}
	
	var data = {
			id: updateModal.address.add_id,
			name: updateModal.name,
			pid: updateModal.address.add_pid,
			camera: updateModal.camera,
			timetable: updateModal.timetable,
			isClassRoom: updateModal.isAddClass	
	};
	$.post("../organization/updateAddress",data,function(rs) {
//		console.log("编辑完成：");
//		console.log(rs);
		if(rs.code == "0000") {
			var address = rs.data;
			msgSuccess("场所编辑成功");
			var nodes = zTreeObj.getNodesByParam("add_id",address.add_id,null);
			
			if(nodes.length > 0) {
//				console.log(nodes[0]);
				copyKeyValue(nodes[0],address);
//				console.log(nodes[0]);
				if(isSelf) {
					zTreeObj.updateNode(nodes[0]);
					selectNode(nodes[0]);
				}else {
					//编辑子节点
					zTreeObj.updateNode(nodes[0]);
					getChildren(vue.page.index);
				}
				
				//判断是否从普通节点转课室
				if(isTransToClassRoom) {
					//删除子节点
					zTreeObj.removeChildNodes(nodes[0]);
				}
			}
			initCustomIcon();
			$("#updateModal").modal("hide");
		}else {
			msgFail("场所编辑失败");
		}
	},"json");
}

//将一个键值对的键值复制到另一个键值对
function copyKeyValue(copyMap,copyedMap) {
	for(var key in copyedMap) {
		copyMap[key] = copyedMap[key];
	}
}

//点击删除按钮
function showDelete(address,isSelf) {
	if(isSelf) {
		if(!checkChosen()) {
			return false;
		}
	}
	layer.confirm("确定删除【" + address.add_name + "】及其子节点吗？", {
		  btn: ['删除','取消'], //按钮
		  icon: 3,
		  offset: "250px",
		  title: "提示"
		}, function(index) {
			var idArray = [address.add_id];
			deleteAddress(idArray,isSelf);
		},function(index) {
			layer.close(index);
		});
}

//删除选中
function deleteCheck() {
	var checkNum = 0;
	var idArray = new Array();
	for(var i=0;i<vue.addressChildren.length;i++) {
		if(vue.addressChildren[i].isChecked) {
			checkNum ++;
			idArray.push(vue.addressChildren[i].add_id);
		}
	}
	if(checkNum == 0) {
		msgFail("请选择子节点");
		return false;
	}
	
	layer.confirm("确定删除所选中的场所及其子节点吗？共选中" + checkNum + "个子节点", {
		  btn: ['删除','取消'], //按钮
		  icon: 3,
		  offset: "250px",
		  title: "提示"
		}, function(index) {
			deleteAddress(idArray,false);
		},function(index) {
			layer.close(index);
		});
	
}

//ajax删除组织
function deleteAddress(idArray,isSelf) {
	var idGroup = "";
	var nodes = new Array();
	for(var i=0;i<idArray.length;i++) {
		idGroup += idArray[i] + ",";
		var nodesGet = zTreeObj.getNodesByParam("add_id",idArray[i],null);
		if(nodesGet.length > 0) {
			nodes.push(nodesGet[0]);
		}
	}
	idGroup = idGroup.substring(0,idGroup.length-1);
	var data = {
			idGroup: idGroup
	};
	$.post("../organization/deleteAddress",data,function(rs) {
		var msg = "";
		if(isSelf) {
			//删除自身
			msg = vue.address.add_name ;
		}else {
			msg = "所选子节点"
		}
		if(rs.code == "0000") {
//			console.log(rs.data);
			msgSuccess(msg + "删除成功，共删除" + rs.data.num + "个节点");
			for(var i=0;i<nodes.length;i++) {
				zTreeObj.removeChildNodes(nodes[i]);
				zTreeObj.removeNode(nodes[i]);
			}
			if(isSelf) {
				
				//重新选中节点
				selectFirst();
			}else {
				//刷新子节点
				getChildren(1);
				
			}
		}else {
			msgFail(msg + "删除失败");
		}
	},"json");
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

//弹出成功提示框
function msgSuccess(msg) {
	layer.msg(msg,{icon:1,offset:"200px"});
}

//弹出失败提示框
function msgFail(msg) {
	layer.msg(msg,{icon:2,offset:"200px"});
}

//获取对象的副本，深拷贝
function cloneObject(object) {
	return $.extend(true,{},object)
}

//检查是否选中节点
function checkChosen() {
	
	 var nodes = zTreeObj.getSelectedNodes();
//	 console.log("检查节点:" + nodes.length);
	 if(nodes.length <= 0) {
		 msgFail("未选择组织节点，请选择！");
		 return false;
	 }
	 return true;
}


//搜索高亮
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#A60000"} : {color:"#333"};
}

//点击搜索按钮，设置节点高亮
function searchNodes() {
	initHighLight()
	if(vue.searchValue == "") {
		return false;
	}
	var nodes = zTreeObj.getNodesByParamFuzzy("add_name",vue.searchValue,null);
	updateNodes(nodes,true);
	
}

//节点全部恢复默认
function initHighLight() {
	var nodes = zTreeObj.getNodesByParamFuzzy("add_name","",null);
	updateNodes(nodes,false);
}

//更新节点
function updateNodes(nodeList,isHighLight) {
	for(var i=0;i<nodeList.length;i++) {
		nodeList[i].highlight = isHighLight;
		zTreeObj.updateNode(nodeList[i]);
		if(isHighLight) {
			//获取父亲节点
			var parents = zTreeObj.getNodesByParam("add_id",nodeList[i].add_pid,null);
			if(parents.length > 0) {
				zTreeObj.expandNode(parents[0],true,false,false);
			}
		}
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
	//获取当前选中的节点
	var nodes = zTreeObj.getSelectedNodes();
	initTreeData(function() {
		selectNodes(nodes);
	});
	msgSuccess("场所节点刷新成功");
}

//点击取消高亮
function cancelHighLight() {
	initHighLight();
}

//点击批量设置课表，弹出模态框
function showSetModal() {
	vue.setModal.timetable = 0;
	getTimeTable(setModalCallBack);
	
	//设置默认选中课表
	form.val("setForm",{set_timetable: vue.setModal.timetable});
	
	
	$("#setModal").modal();
//	console.log(vue.setModal.ranges);
}
//批量设置课表模态框获取课表后回调函数
function setModalCallBack(data) {
	vue.setModal.timetables = data;
	//数据加载完成后渲染
	
	vue.$nextTick(function() {
		//单选框选中第一个
		$("input[name=range]").each(function() {
			if($(this).val() == 1) {
				$(this).prop("checked",true);
			}
		});
		form.render(null,"setForm");
	});
}

//点击批量设置课表模态框的完成按钮
function setTimeTable() {
	var value = $("input[name=range]:checked").val();
	var timetableValue = $("#timetable").val();
	
	//检查选中值的有效性
//	console.log("value:" + value);
//	console.log("课表：" + timetableValue);
	if(timetableValue == 0) {
		msgFail("请选择课表");
		return false;
	}
	if(value == 1 || value == 2) {
		if(!checkChosen()) {
			return false;
		}
	}
	var timetable = getObject(vue.setModal.timetables,"tt_id",timetableValue).tt_name;
	var msg = "";
	var range = getObject(defaultRanges,"id",value).name;
	if(value == 1 || value == 2) {
		msg = "确定将【" + vue.address.add_name + "】的【" + range + "】所有课室课表设置为【" + timetable + "】吗？";
	}else {
		msg = "确定将【" + range + "】所有课室课表设置为【" + timetable + "】吗？";
	}
	layer.confirm(msg, {
		  btn: ['确定','取消'], //按钮
		  icon: 3,
		  offset: "250px",
		  title: "提示"
		},function(index) {
			ajaxSetTimeTable(vue.address.add_id, timetableValue,value);
		},function(index) {
			layer.close(index);
		});
}

//ajax批量设置课表
function ajaxSetTimeTable(id,timetable,flag) {
	var data = {
		id: id,
		timetable: timetable,
		flag: flag	
	};
	$.post("../organization/batchSetTimeTable",data,function(rs) {
		if(rs.code == "0000") {
//			console.log(rs);
			msgSuccess("批量修改课表成功，共修改了" + rs.data.length + "个课室课表");
			for(var i=0;i<rs.data.length;i++) {
				var address = rs.data[i];
				var nodes = zTreeObj.getNodesByParam("add_id",address.add_id,null);
				if(nodes.length > 0) {
					nodes[0].add_ttid = address.add_ttid;
					zTreeObj.updateNode(nodes[0]);
					//更新当前选中节点
					if(address.add_id == vue.address.add_id) {
						vue.address = nodes[0];
						//更新课室信息
						initClassRoom(vue.address);
					}
				}
			}
			//关闭模态框
			$("#setModal").modal("hide");
		}else {
			msgFail("批量修改课表失败");
		}
		
	},"json");
}

//根据键值在json数组中寻找对应的json
function getObject(array,key,value) {
	for(var i=0;i<array.length;i++) {
		if(array[i][key] == value) {
			return array[i];
		}
	}
	return null;
}

var tipIndex = 0;
//给是否课堂添加提示
var msg = "温馨提示：课室类型节点可编辑摄像头IP和选择课表，且课室类型节点是最后一层节点，将无法创建子节点";
addHoverEvent("#add_tip", msg);
addHoverEvent("#update_tip", msg);
//设置聚焦事件
function addHoverEvent(dom,msg) {
	$(dom).hover(function() {
		tipIndex = layer.tips(msg,dom,{
			 tips: [2,'#78BA32'],
			 time: 0
		});
	},function() {
		layer.close(tipIndex);
	});
}
