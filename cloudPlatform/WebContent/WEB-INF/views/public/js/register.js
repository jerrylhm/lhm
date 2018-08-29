/**
 * 注册页面
 */
const TYPE_TEACHER = 2;         //教师类型
const TYPE_STUDENT = 3;          //学生类型
const TYPE_PARENT = 4;           //家长类型

//初始化ztree
var zTreeObj = null;
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
			},
			onExpand: function(event, treeId, treeNode) {
				zTreeObj.expandNode(treeNode,true,true);
			},
			beforeCheck: function(treeId, treeNode) {
				//判断是否学生
				var isStudent = isRoleInArray([TYPE_STUDENT]);
				
				if(isStudent) {
					var nodes = zTreeObj.getCheckedNodes();
					
					//判断是否取消
					for(var i=0;i<nodes.length;i++) {
						if(nodes[i].org_id == treeNode.org_id) {
							return true;
						}
					}
					if(nodes.length >= 1) {
						alertFail("学生用户组只能选择一个班级");
						return false;
					}
				}
				
			}
		},
		view: {
			fontCss: getFontCss,
		},
		check: {
			enable: true,
		}
		
}


$(document).ready(function() {
	//初始化背景图
	$.backstretch([
           "/cloud/images/bkg_1.jpg",
           "/cloud/images/bkg_2.jpg"
       ], {
           fade: 1000,
           duration: 5000
       });
	
	//设置box的margin-top
	var margin = $(this).height() / 7.0;
	$(".box").css("marginTop", margin + "px")
			 .css("marginBottom", margin + "px");
	
	//获取用户组列表
	getUserGroup();
	
	
});

//初始化zTree数据
function initTreeData() {
	//获取组织结构数据
	$.post("/cloud/organization/listOrganizations",{},function(rs) {
		console.log(rs);
		if(rs.code == "0000") {
			//初始化zTree，设置图标
			zTreeObj = $.fn.zTree.init($("#tree"), zTreeSetting, rs.data);
			//自定义图标
			initCustomIcon();
			
			//设置班级可选，其他不可选
			initCheckbox();
		}
	},"json");
}


//定义用户名规则
var checkUsername = function(rule,value,callback) {
	//console.log("value:" + value);
	if(!value) {
		return callback(new Error("用户名不能为空"));
	}
	var regex = /^([0-9A-z]){5,16}$/;
	if(!regex.test(value)) {
		return callback(new Error("用户名必须是5~16位英文、数字组合"));
	}
	//验证用户名是否存在
	var data = {
			username: value
	};
	$.post("/cloud/isExistUsername",data,function(rs) {
		console.log(rs);
		if(rs.code == "0000") {
			if(rs.data.result == 1) {
				return callback();
			}else {
				return callback(new Error("用户名已存在"));
			}
		}
	},"json");
	
}

//定义密码规则
var checkPassword = function(rule,value,callback) {
	if(!value) {
		return callback(new Error("密码不能为空"));
	}
	var regex = /^(\S){5,16}$/;
	if(!regex.test(value)) {
		return callback(new Error("密码长度必须为5~16位"));
	}
	return callback();
}

//定义确认密码规则
var checkRepeat = function(rule,value,callback) {
	if(!value) {
		return callback(new Error("再次输入密码"));
	}
	if(value != vue.form.password) {
		return callback(new Error("两次输入密码不一致"));
	}
	return callback();
}
//昵称规则
var checkNickname = function(rule,value,callback) {
	if(!value) {
		return callback(new Error("昵称不能为空"));
	}
	var regex = /^[\u4e00-\u9fa5A-z0-9]+$/;
	if(!regex.test(value)) {
		return callback(new Error("昵称必须为英文、数字或汉字组合"));
	}
	return callback();
}

//手机号码规则
var checkPhone = function(rule,value,callback) {
	if(!value) {
		return callback();
	}
	var regex = /^[0-9]*$/;
	if(!regex.test(value)) {
		return callback(new Error("请输入正确的手机号码"));
	}
	return callback();
}


//默认表单信息
var defaultForm = {
		username: "",
		password: "",
		repeatPassword: "",
		sex: "0",
		email: "",
		phone: "",
		usergroupTags: new Array(),
		classTags: new Array(),                      //选中的班级
//		usergroupArray: "12",
//		userclass: "0"
}
//初始化vue

var vue = new Vue({
	el: "#app",
	data: {
		showBox: false,
		form: defaultForm,
		showIcon: true,
		usergroups: new Array(),                       //用户组列表,包括勾选状态
		showGroupDialog: false,                            //对话框显示
		showClassDialog: false,                             //班级对话框显示
		organizations: new Array(),                       //组织数组
		isShowClass: false,                          //是否显示班级
		isDisabled: false,                            //是否禁止点击注册按钮
		searchValue: "",                               //搜索值
		rules: {
			username: [{
				validator: checkUsername,trigger:"blur"
			}],
			password: [{
				validator: checkPassword,trigger: "blur"
			}],
			repeatPassword: [{
				validator: checkRepeat,trigger: "blur"
			}],
			email: [{
				type: 'email', message: '请输入正确的邮箱地址', trigger:"blur"
			}],
			nickname:[{
				validator: checkNickname,trigger: "blur",
			}],
			phone: [{
				validator: checkPhone,trigger: "blur"
			}]
		},
		
	},
	methods: {
		handleClose: function(tag) {
			//处理关闭用户组标签
			console.log("点击");
			this.form.usergroupTags.splice(this.form.usergroupTags.indexOf(tag), 1);
			isShowClass();
		},
		handleClassClose: function(tag) {
			//处理关闭班级标签
			this.form.classTags.splice(this.form.classTags.indexOf(tag),1);
		},
		selectUsergroup: function() {
			//点击用户组对话框的确定按钮
			selectUsergroup();
		},
		openCallback: function() {
			openCallback();
		},
		submit: function(form) {
			
			//提交表单
			this.$refs[form].validate(function(valid) {
				if(valid) {
					//提交表单
					submitForm();
				}else {
					alertFail("填写的信息有误，请检查");
					return false;
				}
			});
		},
		searchNodes: function() {
			searchNodes();
		}
	},
	mounted: function() {
		//显示注册页面
		this.showBox = true;
	}
});

//点击改变头像
function changePic(e) {
	//判断是否图片
	var fileName = $(e).val();
	if(fileName == "") {
		return false;
	}
	console.log("filename:" + fileName);
	 if(!/\.(gif|jpg|jpeg|png|GIF|JPG|JPEG|PNG)$/.test(fileName)) {
		 alertFail("图片类型必须是gif,jpeg,jpg,png中的一种");
		 $(e).val("");
		 $("#img_show").attr("src","");
		 vue.showIcon = true;
		 return false;
	 }
	 vue.showIcon = false;
	showImage(e,"img_show");
}

//点击显示用户组选择对话框
function showUsergroup() {
	
	//选中用户组
	for(var i=0;i<vue.usergroups.length;i++) {
		var usergroup = cloneObject(vue.usergroups[i]);
		if(findInArray(vue.form.usergroupTags,"ug_id",vue.usergroups[i].ug_id)) {
			usergroup.isChecked = true;
		}else {
			usergroup.isChecked = false;
			
		}
		//更新视图
		vue.$set(vue.usergroups,i,usergroup);
	}
	console.log(vue.usergroups)
	vue.showGroupDialog = true;
}

//点击用户组选择对话框的确定按钮
function selectUsergroup() {
	vue.form.usergroupTags = new Array();
	//判断用户组选中
	for(var i=0;i<vue.usergroups.length;i++) {
		var usergroup = vue.usergroups[i];
		console.log(usergroup.isChecked);
		if(usergroup.isChecked) {
			vue.form.usergroupTags.push(usergroup);
		}
	}
	vue.showGroupDialog = false;
	isShowClass();
	
	
}

//判断是否学生或教师，是则显示班级选择，否则隐藏
function isShowClass() {
	//判断是否学生或者教师
	var isShow = isRoleInArray([TYPE_TEACHER,TYPE_STUDENT]);
	if(isShow) {
		vue.isShowClass = true;
		//清空班级选择
		vue.form.classTags = new Array();
	}else {
		vue.isShowClass = false;
	}
}

//显示班级选择对话框
function showClassDialog() {
	vue.showClassDialog = true;
	
}
function openCallback() {
	if(zTreeObj == null) {
		initTreeData();
		return false;
	}
	
	//根据当前选中的班级选择节点
	var nodes = zTreeObj.getCheckedNodes();
	//取消选中
	for(var i=0;i<nodes.length;i++) {
		zTreeObj.checkNode(nodes[i],false,false);
	}
	for(var i=0;i<vue.form.classTags.length;i++) {
		var nodeChecked = vue.form.classTags[i];
		zTreeObj.checkNode(nodeChecked,true,false);
	}
}

//点击班级选择对话框中的确定按钮
function selectClass() {
	//获取选中的班级
	var nodes = zTreeObj.getCheckedNodes();
	vue.form.classTags = new Array();
	for(var i=0;i<nodes.length;i++) {
		nodes[i].fullname = getClassFullname(nodes[i]);
		vue.form.classTags.push(nodes[i]);
	}
	
	vue.showClassDialog = false;
}

//根据节点获取班级全名（学院+年级+班级）
function getClassFullname(node) {
	if(node == null) {
		return "";
	}
	var grade = zTreeObj.getNodeByParam("org_id",node.org_pid,null);
	if(grade == null) {
		return node.org_name;
	}
	var acad = zTreeObj.getNodeByParam("org_id",grade.org_pid,null);
	if(acad == null) {
		return grade.org_name + node.org_name;
	}
	
	return acad.org_name + grade.org_name + node.org_name;
	
}

//请求用户组列表
function getUserGroup() {
	$.post("/cloud/register/listUserGroup",{},function(rs) {
		console.log(rs);
		if(rs.code == "0000") {
			//移除id为1的用户组(管理员组)
			var usergroups = new Array();
			for(var i=0;i<rs.data.length;i++) {
				if(rs.data[i].ug_id != 1) {
					usergroups.push(rs.data[i]);
				}
			}
			vue.usergroups = usergroups;
		}else {
			alertFail("用户组列表请求失败");
		}
	},"json");
}


//提交表单
function submitForm() {
	//检查表单数据
	//用户组必须选中一个
	if(vue.form.usergroupTags.length <= 0) {
		alertFail("至少选中一个用户组");
		return false;
	}
	if(vue.isShowClass && vue.form.classTags.length <= 0) {
		alertFail("教师组或学生组请选择班级");
		return false;
	}
	vue.isDisabled = true;
	//显示loading
	 const loading = vue.$loading({
       lock: true,
       text: '注册中',
       spinner: 'fa fa-spinner fa-spin',
       background: 'rgba(0, 0, 0, 0.7)',
       customClass: "load",
     });
	
	//拼接用户组
	var usergroupArray = "";
	for(var i=0;i<vue.form.usergroupTags.length;i++) {
		usergroupArray += vue.form.usergroupTags[i].ug_id + ",";
	}
	$("#usergroup").val(usergroupArray.substring(0,usergroupArray.length - 1));
//	console.log("用户组：" + vue.form.usergroupArray);
	
	//拼接	班级信息
	var userClass = "0";       //默认班级
	if(vue.isShowClass && vue.form.classTags.length > 0) {
		userClass = "";
		for(var i=0;i<vue.form.classTags.length;i++) {
			userClass += vue.form.classTags[i].org_id + ",";
		}
		userClass = userClass.substring(0,userClass.length-1);
	}
//	console.log("班级：" + userClass);
	
	
	//使用jquery.form.js ajax提交表单
	$("#useclass").val(userClass);
	var options = {
		    url: "/cloud/register/doRegister", //提交地址：默认是form的action,如果申明,则会覆盖
		    type: "post",   //默认是form的method（get or post），如果申明，则会覆盖
		    dataType: "json", //html(默认), xml, script, json...接受服务端返回的类型
		    timeout: 3000 ,    //限制请求的时间，当请求大于3秒后，跳出请求
		    success: function(rs) {
		    	//提交成功后的回调函数
		    	//取消加载框
		    	loading.close();
		    	if(rs.code == "0000") {
		    		//判断是否学生身份
		    		if(isRoleInArray([TYPE_STUDENT])) {
		    			alertSuccess("注册成功!正在跳转到登录页面...");
		    		}else {
		    			alertSuccess("注册成功！等待管理员审核...")
		    		}
		    		
		    		//跳转
		    		setTimeout(function() {
		    			window.location.href = "/cloud/login";
		    		},"1500");
		    	}else {
		    		vue.isDisabled = false;
		    		alertFail("注册失败！");
		    	}
		    },  
		    error: function(rs) {
		    	//提交失败执行
		    	loading.close();
		    	vue.isDiabled = false;
		    	alertFail("注册失败！请检查网络设置...");
		    }
		};
	
	$('#form').ajaxSubmit(options)
}

//预览图片
function showImage(e,imgId) {
	var $file = $(e);
	var fileObj = $file[0];
	var windowURL = window.URL || window.webkitURL;
	var dataURL;
	var $img = $("#" + imgId);
	 
	if(fileObj && fileObj.files && fileObj.files[0]){
    	dataURL = windowURL.createObjectURL(fileObj.files[0]);
    	$img.attr('src',dataURL);
	}else{
    	dataURL = $file.val();
    	var imgObj = document.getElementById(imgId);
    	// 两个坑:
    	// 1、在设置filter属性时，元素必须已经存在在DOM树中，动态创建的Node，也需要在设置属性前加入到DOM中，先设置属性在加入，无效；
    	// 2、src属性需要像下面的方式添加，上面的两种方式添加，无效；
    	imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
    	imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;
	 
	}
}


//判断某个字符是否在数组的某个key中
function findInArray(array,key,str) {
	for(var i=0;i<array.length;i++) {
		if(str == array[i][key]) {
			return true;
		}
	}
	return false;
}

//获取对象的副本，深拷贝
function cloneObject(object) {
	return $.extend(true,{},object)
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
	var icons = ["/cloud/images/tree_acad.png","/cloud/images/tree_major.png",
	             "/cloud/images/tree_grade.png","/cloud/images/tree_class.png"];
	for(var i=0;i<4;i++) {
		var nodes = zTreeObj.getNodesByFilter(eval("filter" + i));
		addIcon(nodes,icons[i]);
	}
}

//初始化ztree复选框
function initCheckbox() {
	if(zTreeObj == null) {
		return false;
	}
	var nodes =  zTreeObj.transformToArray(zTreeObj.getNodes());
	for(var i=0;i<nodes.length;i++) {
		//不是班级类型
		if(nodes[i].org_type != 4) {
			zTreeObj.setChkDisabled(nodes[i], true);
		}
	}
}

//搜索高亮
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#A60000"} : {color:"#333"};
}

//根据传入的类型数组判断当前选中用户组是否包含其中一种
function isRoleInArray(types) {
	if(types == null) {
		return false;
	}
	var isRole = false;
	for(var i=0;i<vue.form.usergroupTags.length;i++) {
		var usergroup = vue.form.usergroupTags[i]
		for(var j=0;j<types.length;j++) {
			if(usergroup.ug_id == types[j]) {
				isRole = true;
				break;
			}
		}
	}
	return isRole;
}


//点击搜索按钮，设置节点高亮
function searchNodes() {
	console.log("进入搜索");
	initHighLight();
	console.log(vue.searchValue);
	if(vue.searchValue == "") {
		return false;
	}
	var nodes = zTreeObj.getNodesByParamFuzzy("org_name",vue.searchValue,null);
	console.log(nodes);
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
			zTreeObj.expandNode(nodeList[i],true,true,false);
		}
	}
}
