/**
 * 用户组管理
 */
//默认用户组分页
var defaultGroupPage = {
		index: 1,
		count: 8,
		pageCount: 0,
		groupCount: 0
};

var defaultUserGroup = {
		ug_id: 0,
		ug_name: "未选中",
		ug_ug_permissionid: "0"
};

//默认选中的应用
var defaultApplication = {
		app_id: 0,
		app_name: "未选中",
		app_detail: "",
		app_link: ""
};

//默认用户组模态框
var defaultModal = {
		isCreate: false,
		usergroup: cloneObject(defaultUserGroup),
		name: ""
}

//初始化layer
//layui.use(["layer"],function(){
//	var layer = layui.layer;
//});

//初始化Vue
var vue = new Vue({
	el: "#usergroup",
	data: {
		groupPage: cloneObject(defaultGroupPage),
		usergroups: new Array(),                   //用户组列表
		usergroup: cloneObject(defaultUserGroup),      //选中的用户组
		usergroupIndex: 1,                    //当前选中的用户组索引
		permissions: new Array(),
		applications: new Array(),            //应用列表
		application: cloneObject(defaultApplication),       //当前选中的应用
		permissionMap: {},                //应用与权限之间的映射，以应用id值为键,权限数组为值
		usergroupModal: defaultModal,             //用户组模态框变量
	},
	methods: {
		changeUserGroup: function(index) {
			changeUserGroup(index);
		},
		changeApplication: function(app) {
			activeApplication(app);
		},
		showModal: function(isCreate,usergroup) {           //弹出新增或者编辑模态框
			showModal(isCreate,usergroup);
		},
		first: function() {                         //首页
			if(this.groupPage.index <= 1 ) {
				return false;
			}
			getUserGroupByPage(1,0)
		},
		pre: function() {                    //上一页
			if(this.groupPage.index <= 1) {
				msgFail("当前已是首页");
				return false;
			}
			getUserGroupByPage(this.groupPage.index - 1,0);
		},
		next: function() {                 //下一页
			if(this.groupPage.index >= this.groupPage.pageCount) {
				msgFail("当前已是尾页");
				return false;
			}
			getUserGroupByPage(this.groupPage.index + 1,0);
		},
		last: function() {
			if(this.groupPage.index >= this.groupPage.pageCount) {
				return false;
			}
			getUserGroupByPage(this.groupPage.pageCount,0);
		},
		finishModal: function() {
			finishModal();
		},
		deleteUserGroup: function(usergroup) {
			deleteUserGroup(usergroup);
		},
		authorize: function(permission) {             //授权或者取消授权
			authorize(permission);
		}
	},
	beforeCreate: function() {
		//未创建之前，弹出加载框
		load();
	},
	mounted: function() {
		unload();
	},
});
$(document).ready(function() {
	//获取应用与权限的映射列表
	getAppPerMap();
	//初始化权限列表
	//getPermissions();
	
});

//刷新用户组列表，并且刷新权限
function refreshUserGroup() {
	getUserGroupByPage(vue.groupPage.index,vue.usergroupIndex);
}

//根据页数初始化用户组，并根据index选中
function getUserGroupByPage(pageIndex,chosenIndex) {
	if(chosenIndex == null) {
		chosenIndex = 0;
	}
	var data = {
		index: pageIndex,
		count: 12,
	};
	$.post("../admin/getUserGroupPage",data,function(rs) {
//		console.log(rs);
		if(rs.code == "0000") {
			vue.usergroups = rs.data.usergroup;
			initUserGroup(vue.usergroups,chosenIndex);
			//改变用户组分页
			vue.groupPage.index = rs.data.index;
			vue.groupPage.pageCount = rs.data.pageCount;
			vue.groupPage.groupCount = rs.data.groupCount;
			vue.groupPage.count = rs.data.count;
			//判断当前记录是否为0
			if(vue.usergroups.length == 0 && vue.groupPage.index > 1) {
				//重新请求上一页
				getUserGroupByPage(vue.groupPage.index - 1,0);
			}
		}else {
			msgFail("用户组加载失败");
		}
	},"json").fail(function(error) {
		msgFail("用户组加载失败，请检查网络");
	});
}
//初始化用户组选中
function initUserGroup(usergroup,index) {
	if(usergroup.length < (index + 1)) {
		if(usergroup.length > 0) {
			index = 0;
		}else {
			return false;
		}
	}
	vue.usergroup = usergroup[index];
	vue.usergroupIndex = index;
	//移除其他选中
	$(".usergroup").removeClass("active");
	//数据加载完成后渲染
	vue.$nextTick(function() {
		$("#" + index).addClass("active");
	});
	initUsergroupPermission();
}
//初始化用户组权限选中
function initUsergroupPermission() {
	initPermission(false);
	var ug_permissionid = vue.usergroup.ug_permissionid;
	if(ug_permissionid == null || ug_permissionid == "") {
		return false;
	}
	var ids = ug_permissionid.split(",");
	//遍历权限列表
	for(var i=0;i<vue.permissions.length;i++) {
		if(findInArray(ids,vue.permissions[i].ps_id)) {
			vue.permissions[i].hasPermission = true;
		}
	}
	
}

//判断某个字符是否在数组中
function findInArray(array,str) {
	for(var i=0;i<array.length;i++) {
		if(str == array[i]) {
			return true;
		}
	}
	return false;
}

//点击用户组，改变用户组
function changeUserGroup(index) {
//	$(".usergroup").removeClass("active");
	initUserGroup(vue.usergroups,index);
}

//获取应用与权限之间的映射
function getAppPerMap() {
	$.post("../admin/listPermission",{},function(rs) {
		if(rs.code == "0000") {
			var map = {};
			for(var i=0;i<rs.data.length;i++) {
				var per = rs.data[i];
				if(map[per.ps_appid] == null) {
					map[per.ps_appid] = new Array();
				}
				map[per.ps_appid].push(per);
			}
			vue.permissionMap = map;
//			console.log("应用与权限映射：");
//			console.log(vue.permissionMap);
			
			//获取用户组，并选中第一个
			getUserGroupByPage(1);
			//初始化应用列表 
			getApplication();
			
		}
	},"json")
}

//获取权限数组
function getPermissions() {
	$.post("../admin/listPermission",{},function(rs) {
//		console.log(rs);
		if(rs.code == "0000") {
			vue.permissions = rs.data;
			initPermission(false);
			
			//获取用户组，并选中第一个
			getUserGroupByPage(1);
		}else {
			msgFail("权限列表加载失败");
		}
	},"json");
}

//初始化权限列表的所有权限
function initPermission(flag) {
	for(var i=0;i<vue.permissions.length;i++) {
		vue.permissions[i].hasPermission = false;
	}
}

//获取应用列表
function getApplication() {
	$.post("../admin/listApplication",{},function(rs) {
//		console.log(rs);
		if(rs.code == "0000") {
			vue.applications = rs.data;
			//激活第一个应用
			if(rs.data.length > 0) {
				activeApplication(rs.data[0]);
			}
		}
	},"json");
}

//选中激活应用
function activeApplication(app) {
	//清除其他应用选中
	for(var i=0;i<vue.applications.length;i++) {
		vue.applications[i].isActive = false;
	}
	vue.application = app;
	app.isActive = true;
	
	setPermission();
	initUsergroupPermission();
}

//根据应用设置对应的权限
function setPermission() {
	var id = vue.application.app_id;
	var array = vue.permissionMap[id];
//	console.log("权限数组");
//	console.log(array);
	if(array == null) {
		array = new Array();
	}
	vue.permissions = array;
}

//弹出新增或者编辑模态框
function showModal(isCreate,usergroup) {
//	console.log("isCreate:" + isCreate);
	vue.usergroupModal.isCreate = isCreate;
	
	if(isCreate) {
		vue.usergroupModal.name = "";
	}else {
		vue.usergroupModal.usergroup = usergroup;
		vue.usergroupModal.name = vue.usergroupModal.usergroup.ug_name;
	}
	
	$("#usergroupModal").modal();
}

//点击模态框中的新增或编辑按钮
function finishModal() {
	if(vue.usergroupModal.name == "") {
		msgFail("用户组名称不能为空");
		return false;
	}
	var data = {
			id: vue.usergroupModal.usergroup.ug_id,
			name: vue.usergroupModal.name
	};
	if(vue.usergroupModal.isCreate) {
		//新增
		addUserGroup(data);
	}else {
		//编辑
		updateUserGroup(data);
	}
}

//ajax新增用户组
function addUserGroup(data) {
	$.post("../admin/addUsergroup",data,function(rs){
		if(rs.code == "0000") {
			msgSuccess("用户组添加成功");
			$("#usergroupModal").modal("hide");
			//刷新用户组列表
			getUserGroupByPage(vue.groupPage.index,vue.usergroupIndex);
		}else if(rs.code == "1001") {
			msgFail("用户组已存在，请重输");
			return false;
		}else {
			msgFail("用户组添加失败");
		}
			
	},"json");
}

//ajax编辑用户组
function updateUserGroup(data) {
	$.post("../admin/updateUsergroup",data,function(rs) {
		if(rs.code == "0000") {
			msgSuccess("用户组修改成功");
			//刷新用户组列表
			getUserGroupByPage(vue.groupPage.index,vue.usergroupIndex);
			$("#usergroupModal").modal("hide");
		}else if(rs.code == "1001") {
			msgFail("用户组已存在，请重输");
			return false;
		}else {
			msgFail("用户组修改失败");
		}
	},"json");
}

//点击删除用户组
function deleteUserGroup(usergroup) {
	layer.confirm("确定删除用户组【" + usergroup.ug_name + "】吗？", {
		  btn: ['删除','取消'], //按钮
		  icon: 3,
		  offset: "250px",
		  title: "提示"
		}, function(index) {
			//删除
			ajaxDeleteUserGroup(usergroup.ug_id);
		},function(index) {
			layer.close(index);
		});
}

//ajax删除用户组
function ajaxDeleteUserGroup(id) {
	var data = {
			id: id
	};
	$.post("../admin/deleteUserGroup",data,function(rs) {
		if(rs.code == "0000") {
			//用户组删除成功
			msgSuccess("用户组删除成功");
			getUserGroupByPage(vue.groupPage.index,vue.usergroupIndex);
		}else {
			msgFail("用户组删除失败");
		}
	},"json");
}

//授权或取消授权
function authorize(permission) {
	var permissions = new Array();
	permissions.push(permission);
	authorizeAll(permissions,!permission.hasPermission);
}

//点击一键授权
function quickAuth() {
	authorizeAll(vue.permissions,true);
}

//点击一键取消授权
function quickUnauth() {
	authorizeAll(vue.permissions,false);
}

//根据权限数组进行授权或取消授权
function  authorizeAll(permissions,isAuth) {
//	console.log("点击");
//	console.log(permissions);
	if(permissions == null) {
		return false;
	}
	if(permissions.length == 0) {
		return false;
	}
	//遍历权限数组，获取权限id数组
	var ids = new Array();
	for(var i=0;i<permissions.length;i++) {
		ids.push(permissions[i].ps_id);
	}
	var data = {
		id: vue.usergroup.ug_id,
		permissions: ids,
		isAuth: isAuth
	};
	var msg = "授权";
	if(!isAuth) {
		msg = "取消授权"
	}
	$.post("../admin/updateGroupPermission",data,function(rs) {
		if(rs.code == "0000") {
			msgSuccess(msg + "成功");
			//刷新权限，重新加载用户组列表
			refreshUserGroup();
		}else {
			msgFail(msg + "失败");
		}
	},"json");
	
}



//获取对象的副本，深拷贝
function cloneObject(object) {
	return $.extend(true,{},object)
}

//弹出成功提示框
function msgSuccess(msg) {
	layer.msg(msg,{icon:1,offset:"200px"});
}

//弹出失败提示框
function msgFail(msg) {
	layer.msg(msg,{icon:2,offset:"200px"});
}

//弹出加载
function load() {
	var index = layer.load(1, {
		  shade: [0.7,'#000'] ,
		  offset: "300px"
		});
	return index;
}
//取消加载
function unload() {
	layer.closeAll("loading");
}

