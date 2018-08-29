/**
 * 系统用户的逻辑处理代码
 * @author ilongli
 * @date   2018/8/1
 */
//初始化变量 
const BASE_PATH = getBasePath();
const BASE_REQ_URL = "userManage/";
const URL = BASE_PATH + BASE_REQ_URL;
//文本国际化
const TEXT_ZH = {
	error_404   : 	"出现错误，请检查你的网络连接。",
	error_500  	: 	"我们出现了一点问题，请稍后重试。",
	error_other : 	"出现错误。",
	alr_first	: 	"已经是第一页了。",
	alr_last	:	"已经是最后一页了。",
	del_success :	"删除用户成功!",
	add_success	:	"新增用户成功!",
	add_fail	:	"新增用户失败，请稍后重试。",
	up_success	:	"修改用户成功!",
	pageNotNull	:	"页码不能为空",
	outOfPage	:	"超出页码范围",
	notSelect	: 	"未选择任何用户",
	re_username	:	"该用户名已被使用",
	select_file	:	"请先选择文件。",
	help		: 	[
		"用户名、密码、昵称、身份和性别为必填项",
		"身份可填学生、教师和家长",
		"身份和班级为多填项，以半角逗号\",\"隔开",
		"删除一行的数据要选中该行->右键->删除才可"
	]
};
//当前选择的文本
const TEXT_CUR = TEXT_ZH;
//角色
const TYPE_GROUP = new Array();
TYPE_GROUP[0] = "全部";
TYPE_GROUP[1] = "管理员";
TYPE_GROUP[2] = "教师";
TYPE_GROUP[3] = "学生";
TYPE_GROUP[4] = "家长";
TYPE_GROUP[5] = "待定";

//全局AJAX请求成功后的延迟执行时间(毫秒)
const DELAY_TIME = 500;

//全局中间变量
//表单
var form;
//layer
var layer;
//ng的ctrl对象
var ele = document.querySelector("[ng-controller=tableCtrl]");
//layer-help文本
var help_text = "<ul>";

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
	callback: {},
	check: {
		enable: true,
		chkboxType: { "Y" : "ps", "N" : "ps" }
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
	$.post(BASE_PATH + "organization/listOrganizations",{},function(rs) {
		zTreeObj = $.fn.zTree.init($("#class_tree"), zTreeSetting, rs.data);
		//自定义图标
		initCustomIcon();
	},"json");
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
	var icons = ["images/tree_acad.png","images/tree_major.png",
	             "images/tree_grade.png","images/tree_class.png"];
	for(var i=0; i<4; i++) {
		var nodes = zTreeObj.getNodesByFilter(eval("filter" + i));
		addIcon(nodes, BASE_PATH + icons[i]);
	}
}

//收起所有
function collapseAll() {
	zTreeObj.expandAll(false);
}

//展开所有
function expandAll() {
	zTreeObj.expandAll(true);
}

//班级树搜索输入框按键回调
function dokeydownClass() {
	angular.element(ele).scope().searchNodes();   
}

//获取basePath
function getBasePath() {
	var curWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPath = curWwwPath.substring(0, pos);
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	var basePath=localhostPath+projectName+"/";
	return basePath;
};

//全局AJAX错误提示
$(document).ajaxError(function(event,xhr,settings,info){
	alertFail(TEXT_CUR.error_other);
});

/**
 * AngularJs代码区开始
 */
var app = angular.module('userApp', ['ngAnimate']);
//设置默认ajax请求头
app.config(['$httpProvider', function ($httpProvider) {
	//修改Content-Type(put和post)
    $httpProvider.defaults.headers.put = {'Content-Type': 'application/x-www-form-urlencoded', 'Accept': '*/*'};
    $httpProvider.defaults.headers.post = {'Content-Type': 'application/x-www-form-urlencoded', 'Accept': '*/*'};
    //序列化(如果有)
    $httpProvider.defaults.transformRequest = function(data) { 
        if (data !== undefined) { 
            return $.param(data);
        }
        return data;
    };
    //添加拦截器，进行全局统一处理
    $httpProvider.interceptors.push('httpInterceptor');
}]);
//设置默认href安全识别(解决Firefox的:unsafe问题)
app.config(['$compileProvider', function ($compileProvider) {
    $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|javascript|mailto|tel|file|sms):/);
    // Angular v1.2 之前使用 $compileProvider.urlSanitizationWhitelist(...)
}]);

//http拦截器，用作出错提示和请求loading效果
app.factory('httpInterceptor', ["$q","$rootScope", function($q,$rootScope){
    return {  
        request: function(request) { 
        	$rootScope.isLoad = true;
            return request;  
        },  
        response: function(response) { 
        	$rootScope.isLoad = false;
            return response;  
        },
        requestError: function(rejection){
        	$rootScope.isLoad = false;
            return $q.reject(rejection);
        },
        responseError: function(rejection) {
        	switch (rejection.status) {
			case 404:
				alertFail(TEXT_CUR.error_404);
				break;
			case 500:
				alertFail(TEXT_CUR.error_500);
				break;
			default:
				alertFail(TEXT_CUR.error_other);
				break;
			}
        	$rootScope.isLoad = false;
        	return $q.reject(rejection);
        }
    } 
}]);

//主要代码区
app.controller('tableCtrl', function($scope, $http, $timeout, $rootScope) {
	//初始化数组和变量
	//不可修改的中间变量值
	$scope.TYPE_GROUP = TYPE_GROUP;
	$scope.ug_id = -1;
	$scope.usergroups = null;
	$scope.showClassTree = false;
	$scope.showUserGroup = false;
	$scope.isUploading = false;			
	$scope.excelUsers = null;
	$scope.errorNum = 0;
	$scope.allowImport = false;
	$scope.importUsers = null;
	$scope.isImporting = false;
	$scope.isPopShow = false;
	$rootScope.isLoad = false;
	$scope.isAddUser = false;			//该值为true为新增用户操作，false时为修改用户操作
	//可修改的默认值
	$scope.SEX_GROUP = ["男", "女"];		//性别组
	$scope.isBlurEffect = false;		//是否开启全局模糊效果，默认不开启
	$scope.orderBy = "ur_createdate";	//默认排序
	$scope.isDESC = true;				//默认降序
	$scope.role = 0;					//角色筛选，默认为全部
	$scope.tarDate = "";				//日期筛序，默认不筛选
	$scope.status = -1;					//审核状态筛选，默认不筛选

	//初始化数据
	$scope.initData = function() {
		updateData(1);
	}

	/**
	 * 数据筛选部分开始
	 */
	//改变排序
	$scope.changeOrderBy = function(orderBy) {
		$scope.isDESC = !$scope.isDESC;
		$scope.orderBy = orderBy;
		updateData(1);
	}
	
	//改变角色
	$scope.changeType = function(role) {
		if(role !== null && role !== "") {
			$scope.role = role;
			updateData(1);
		}
	}
	
	//改变审核状态
	$scope.changeStatus = function(status) {
		if(!!status) {
			$scope.status = status;
			updateData(1);
		}
	}
	
	//改变用户组
	$scope.changeUserGroup = function(ug_id) {
		if(!!ug_id) {
			$scope.ug_id = ug_id;
			updateData(1);
		}
	}
	
	//改变页数
	$scope.changePage = function(tarPage) {

		if(tarPage <= 0) {
			alertTimer(TEXT_CUR.alr_first);
			return -1;
		}
		if(tarPage > $scope.totalPage) {
			alertTimer(TEXT_CUR.alr_last);
			return -1;
		}
		updateData(tarPage);
	}
	
	//跳转页数
	$scope.jumpToPage = function(tarPage) {
		if(tarPage == null) {
			alertTimer(TEXT_CUR.pageNotNull);
			return -1;
		}
		if(tarPage <= 0 || tarPage > $scope.totalPage) {
			alertTimer(TEXT_CUR.outOfPage);
			return -1;
		}
		updateData(tarPage);
	}
	
	//搜索
	$scope.query = function() {
		updateData(1);
	}
	
	//改变日期
	$scope.changeDate = function(value) {
		$scope.tarDate = value;
		updateData(1);
	}
	
	//搜索框内键盘按下事件
	$scope.dokeydown = function(e) {
		var keycode = window.event?e.keyCode:e.which;
		if(keycode === 13) {
			updateData(1);
		}
	}
	
	//页面跳转键盘按下事件
	$scope.dokeydownPage = function(e) {
		var keycode = window.event?e.keyCode:e.which;
		if(keycode === 13) {
			$scope.jumpToPage($scope.jumpPage);
		}
	}
	/**
	 * 数据筛选部分结束
	 */
	
	
	/**
	 * 用户删除部分开始
	 */
	//全选
    $scope.selectAll = function () {
        if($scope.select_all) {
            angular.forEach($scope.users, function(i) {
                i.checked = true;
            });
        }else {
            angular.forEach($scope.users, function(i) {
                i.checked = false;
            });
        }
    };
    
    //单个选择
    $scope.selectOne = function () {
    	for(var n in $scope.users) {
        	if($scope.users[n].checked === false) {
        		$scope.select_all = false;
        		return -1;
        	}
    	}
        $scope.select_all = true;
    }
	
	//删除单个用户
	$scope.deleteOne = function(u) {
		doDelete([u]);
	}
	
	//删除选中用户
	$scope.deleteSelect = function() {
		var delList = new Array();
        angular.forEach($scope.users, function(i) {
            if(i.checked === true) {
            	delList.push(i);
            }
        });
        if(delList.length === 0) {
        	alertTimer(TEXT_CUR.notSelect);
        	return -1;
        }
        doDelete(delList);
	}
	
	// 显示删除用户确认模态框
	function doDelete(delList) {
		$scope.delList = delList;
		$("#delModal").modal("show");
	}
	
	//删除用户
	$scope.deleteUser = function() {
		var ids = new Array();
        angular.forEach($scope.delList, function(i) {
        	ids.push(i.ur_id);
        });
		
		$http({
		    method: "DELETE",
		    url: URL + ids
		}).then(function(response) {
			var result = response.data;
			if(result.code === "0000") {
				alertSuccess(TEXT_CUR.del_success);
				$("#delModal").modal("hide");
				$timeout(function () {
					var targetPage = $scope.curPage;
					if($scope.delList.length === $scope.users.length) {
						targetPage = targetPage - 1;
						if(targetPage <= 0) {
							targetPage = 1;
						}
					}
					updateData(targetPage);
			   }, DELAY_TIME);
			}
		}, errorCallback);
	}
	/**
	 * 用户删除部分结束
	 */
	
	
	/**
	 * 新增用户部分开始
	 */
    //新增用户
    $scope.addOne = function() {
    	$scope.isAddUser = true;
    	$scope.upUser = {ur_sex:0};
		$timeout(function() {
			form.render('radio');
			form.render('checkbox');
		}, 0);
    	$("#updateModal").modal("show");
    }
	
	//新增用户
	$scope.addUser = function(user) {
		$http.get(URL + "isExist/" + user.ur_username).then(function(response) {
			var result = response.data;
			if(result.code === "0000") {
				if(result.data === 1) {
					alertFail(TEXT_CUR.re_username);
					$("#input-username").addClass("layui-form-danger").focus();
					return false;
				}
				$http.post(URL, user).then(function(response) {
					var result = response.data;
					if(result.code === "0000") {
						alertSuccess(TEXT_CUR.add_success);
						$("#updateModal").modal("hide");
						$timeout(function () {
							$scope.orderBy = "ur_createdate";
							$scope.isDESC = true;
							updateData(1);
					   }, DELAY_TIME);
					} else {
						errorShow(result);
					}
				}, errorCallback);
			} else {
				errorShow(result);
			}
		}, errorCallback);
	}
    
    //新增多个用户(用户导入)
    $scope.importUser = function() {
    	$("#importModal").modal("show");
    }
    
    //选择文件
    $scope.selectFile = function() {
    	$("#xlsFile").click();
    }
    
    //选择文件后显示文件名
    $scope.fileChange = function(files) {
    	if(files.length !== 0) {
    		$("#fileName").text(files[0].name).attr("title", files[0].name);
    	} else {
    		$("#fileName").text("").attr("title", "");
    	}
    }
    
    //上传Excel文件
    $scope.uploadFile = function() {
    	//每次重新上传文件后都会重置数据
    	$scope.allowImport = false;
    	$scope.importUsers = null;
    	if(!!$("#xlsFile").val()) {
    		//销毁所有弹出框
    		$("[data-toggle='popover']").popover("destroy");
    		//开始上传
    		$scope.isUploading = true;
        	var formData = new FormData($("#uploadForm")[0]);
    		$http({
    			method: 'post',
                url: URL + "xlsFileUpload",
                data: formData,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity,
                cache: false,
                processData: false,
                contentType: false
    		}).then(function(response) {
    			var result = response.data;
    			if(result.code === "0000") {
    				//显示要导入的用户表格
    				var errorList = result.data.errorList;
    				$scope.excelUsers = result.data.readExcel;
    				$scope.errorNum = errorList.length;
    				//如果没有错误，则允许进行导入，否则会将错误的地方标出出来
    				if($scope.errorNum === 0) {
    					$scope.allowImport = true;
    					$scope.importUsers = result.data.entityList;
    				} else {
    					//timeout延迟同步数据
    					$timeout(function() {
    						var $tbody = $(".iur-table tbody");
    						//将错误的地方标注
    						for(var n in errorList) {
    							var $td = $tbody.find("tr").eq(errorList[n].row-1).find("td").eq(errorList[n].col);
    							addPopover($td, errorList[n].errorMsg);
    						}
    					}, 0);
    				} 
    				$timeout(function() {
						//判断是否出现滚动条
						if($("#importUserTbody").get(0).scrollHeight - $("#importUserTbody").height() > 0) {
							$(".iur-table thead").addClass("thead-scroll-on");
						} else {
							$(".iur-table thead").removeClass("thead-scroll-on");
						}
    				}, 0);
    			} else {
    				errorShow(result);
    			}
        		$scope.isUploading = false;
    		}, errorCallback);
    	} else {
    		alertTimer(TEXT_CUR.select_file);
    	}
    }
    
    //添加弹出框
    function addPopover($obj, content, title) {
    	$obj.addClass("errorTd");
    	$obj.attr("data-toggle", "popover");
    	$obj.attr("data-placement", "auto");
    	$obj.attr("data-container", "#importUserTbody");
    	$obj.attr("data-content", content);
    	if(!!title) {
    		$obj.attr("title", title);
    	}
		//绑定弹出框
    	$obj.popover().on('show.bs.popover', function () {
			$scope.isPopShow = true;
		});
    }
	
    //导入用户
    $scope.doImport = function() {
    	//双重判断
    	if($scope.allowImport && !!$scope.importUsers) {
    		if($scope.importUsers.length !== 0) {
    			$scope.isImporting = true;
    			$.ajax({
    	            url: URL + "importUsers",
    	            type: "POST",
    	            contentType : 'application/json;charset=utf-8', 
    	            dataType:"json",
    	            data: JSON.stringify($scope.importUsers),    
    	            success: function(result) {
    	            	if(result.code === "0000") {
    	            		var errorList = result.data;
    	            		var errorLength = Object.keys(errorList).length;
    	            		var str = "总共新增" + $scope.importUsers.length + "个用户，" 
    	            				+ "成功" + ($scope.importUsers.length-errorLength) + "个，"
    	            				+ "失败" + errorLength + "个。"
    	            		if(errorLength === 0) {
    							alertSuccess(str);
    							$("#importModal").modal("hide");
    							$timeout(function () {
    								$scope.orderBy = "ur_createdate";
    								$scope.isDESC = true;
    								updateData(1);
    							}, DELAY_TIME);
    	            		} else {
    	            			alertFail(str);
    							$timeout(function () {
    								$scope.errorNum = errorLength;
        	            			$scope.allowImport = false;
        	            			$scope.isImporting = false;
    							}, 0);
        						var $tbody = $(".iur-table tbody");
        						for(var key in errorList) {
        							if(errorList[key] === 1) {
        								//用户名被占用
            							var $td = $tbody.find("tr").eq(key).find("td").eq(0);
            							addPopover($td, TEXT_CUR.re_username);
        							} else {
        								//新增时的其它错误
        								var $tr = $tbody.find("tr").eq(key);
        								addPopover($tr, TEXT_CUR.add_fail, "新增失败");
        							}
        						}
    	            		}
    	            	} else {
    	            		errorShow(result);
    	            	}
    	            	$scope.allowImport = false;
    	            	$scope.isImporting = false;
    	            },
    	            error: function(response) {
    	            	errorCallback(response);
    	            }
    	        });
    		}
    	}
    }
    
    //显示帮助
    $scope.showHelp = function() {
    	layer.open({
    		skin: 'layer-help-class',
    		title: '<i class="fa fa-book"></i>帮助-用户导入',
    		resize: false,
    		offset: '200px',
    		content: help_text
    	});
    }
    
    //监听用户导入滚动条滚动事件
    $("#importUserTbody").scroll(function() {
    	if($scope.isPopShow) {
    		$scope.isPopShow = false;
    		$("[aria-describedby]").click();
    	}
    });
	/**
	 * 新增用户部分结束
	 */
    
    
	/**
	 * 修改用户部分开始
	 */
	//修改用户
	$scope.updateOne = function(u) {
		$scope.isAddUser = false;
		$scope.upUser = u;
		$timeout(function() {
			form.render('radio');
			form.render('checkbox');
		}, 0);
		//重置模态框
		$("#reset").click();
		//展开班级节点
		var classids = u.ur_classid.split(",");
		for(var n in classids) {
			var node = zTreeObj.getNodeByParam("org_id", classids[n]);
			if(!!node) {
				zTreeObj.checkNode(node);
				// 获取父亲节点
				var parents = zTreeObj.getNodesByParam("org_id", node.org_pid, null);
				if (parents.length > 0) {
					zTreeObj.expandNode(parents[0], true, false, false);
				}
			}
		}
		//updateGrade(u.ur_classid);
		$("#updateModal").modal("show");
	}
	
	//当修改用户模态框被关闭之后所触发的函数
	$("#updateModal").on("hidden.bs.modal",function () {
		//收起所有节点
		zTreeObj.checkAllNodes(false);
		zTreeObj.expandAll(false);
		//收起班级树窗口和用户组窗口
		$scope.showClassTree = false;
		$scope.showUserGroup = false;
		if(!$scope.isAddUser) {
			//重置模态框
			$("#reset").click();
		}
    })
	
	//判断是否存在
	$scope.isContain = function(idGroup, id) {
		if(!idGroup) {
			return false;
		}
		var ids = idGroup.split(",");
		for(var n in ids) {
			if(ids[n] == id) {
				return true;
			}
		}
		return false;
	}
	
	//修改用户
	$scope.updateUser = function(user) {
		$http.put(URL + $scope.upUser.ur_id, user).then(function(response) {
			var result = response.data;
			if(result.code === "0000") {
				alertSuccess(TEXT_CUR.up_success);
				$("#updateModal").modal("hide");
				$timeout(function () {
					updateData($scope.curPage);
			   }, DELAY_TIME);
			} else {
				errorShow(result);
			}
		}, errorCallback);
	}
	/**
	 * 修改用户部分结束
	 */
	
	
	/**
	 * 新增/修改用户共用部分开始
	 */
	//点击搜索按钮，设置节点高亮
	$scope.searchNodes = function() {
		initHighLight();
		if($scope.searchValue == "") {
			return false;
		}
		var nodes = zTreeObj.getNodesByParamFuzzy("org_name", $scope.searchValue, null);
		updateNodes(nodes, true);
	}

	//节点全部恢复默认
	function initHighLight() {
		var nodes = zTreeObj.getNodesByParamFuzzy("org_name", "", null);
		updateNodes(nodes, false);
	}

	//更新节点
	function updateNodes(nodeList, isHighLight) {
		for (var i = 0; i < nodeList.length; i++) {
			nodeList[i].highlight = isHighLight;
			zTreeObj.updateNode(nodeList[i]);
			if (isHighLight) {
				// 获取父亲节点
				var parents = zTreeObj.getNodesByParam("org_id",
						nodeList[i].org_pid, null);
				if (parents.length > 0) {
					zTreeObj.expandNode(parents[0], true, false, false);
				}
			}
		}
	}
	/**
	 * 新增/修改用户共用部分结束
	 */
	
	
	/**
	 * 其它逻辑处理代码
	 */
	//更新数据
	function updateData(tarPage) {
		$http.post(URL + "getAll", {
			page : tarPage,
			query: $.trim($scope.queryStr),
			role: $scope.role,
			date: $scope.tarDate,
			status: $scope.status,
			ug_id: $scope.ug_id,
			orderBy: $scope.orderBy,
			isDESC: $scope.isDESC
		}).then(function(response) {
			var result = response.data;
			if(result.code === "0000") {
				$scope.users = result.data.users;
		        angular.forEach($scope.users , function(i) {
		        	i.checked = false;
		        });
				$scope.curPage = result.data.curPage;
				$scope.totalPage = result.data.totalPage;
				
				//此处先清空用户组再重新设置用户组，是为了解决Firefox的layui同步问题
				$scope.usergroups = null;
				$timeout(function() {
					$scope.usergroups = result.data.usergroups;
					$timeout(function() {
						form.render('select');
					}, 0);
				}, 0);
			} else {
				errorShow(result);
			}
			
		}, errorCallback);
	}
	
	//错误回调
	function errorCallback(response) {
		//TODO
		$rootScope.isLoad = false;
		$scope.isUploading = false;
		$scope.isImporting = false;
    	$scope.allowImport = false;
	}
	function errorShow(result) {
		alertFail(result.msg + "[" + result.code + "]");
	}
});

//角色过滤器
app.filter('roleFilter', function() {
    return function(text) {
    	var _text = "";
    	var roles = text.split(",");
    	if(!!roles[0]) {
        	for(var n in roles) {
        		_text = _text + TYPE_GROUP[roles[n]] + "，";
        	}
            return _text.substring(0,_text.length-1);
    	} else {
    		return "";
    	}
    }
});
/**
 * AngularJs代码区结束
 */

//初始化动作
(function() {
	//zTree
	initTreeData();
	
	//初始化帮助文本
	for(var n in TEXT_CUR.help) {
		help_text = help_text +  "<li>" + TEXT_CUR.help[n] + "</li>";
	}
	help_text = help_text + "<ul>";
	
	//layui
	layui.use(['form','laydate','layer'], function() {
		form = layui.form;
		var laydate = layui.laydate;
		layer = layui.layer;
		
		//初始化数据
		angular.element(ele).scope().initData();   
		
		//审核状态下拉框
		form.on('select(showStatus)', function(data){
			angular.element(ele).scope().changeStatus(data.value);   
		});
		
		//用户组下拉框
		form.on('select(showUserGroup)', function(data){
			angular.element(ele).scope().changeUserGroup(data.value);   
		});
		
		//表单提交
		form.on('submit(formSubmit)', function(data){			
			//拼接角色和用户组
			var user = data.field;
			var ur_type = "";
			var ur_group = "";
			for(var key in user) {
				if(key.indexOf("ur_group") != -1) {
					ur_group = ur_group + user[key] + ",";
					if(user[key] < 6) {
						ur_type = ur_type + user[key] + ",";
					}
				}
/*				if(key.indexOf("ur_type") != -1) {
					ur_type = ur_type + user[key] + ",";
				} else if(key.indexOf("ur_group") != -1) {
					ur_group = ur_group + user[key] + ",";
				}*/
			}
			user.ur_type = ur_type.substring(0, ur_type.length-1);
			user.ur_group = ur_group.substring(0, ur_group.length-1);
			
			//拼接班级
			var nodes = zTreeObj.getCheckedNodes(true);
			var ur_classid = "";
			for(var n in nodes) {
				if(nodes[n].org_type === 4) {
					ur_classid = ur_classid + nodes[n].org_id + ",";
				}
			}
			user.ur_classid = ur_classid.substring(0, ur_classid.length-1);
			
			
			//判断是否添加用户
			if(angular.element(ele).scope().isAddUser) {
				//添加用户
				angular.element(ele).scope().addUser(user);
			} else {
				//更新用户
				angular.element(ele).scope().updateUser(user);
			}
			return false;
		});
		
		//表单验证
		form.verify({
		  	//数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
			username: function(value, item){ //value：表单的值、item：表单的DOM对象
				if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
					return '用户名不能有特殊字符';
				}
				if(/(^\_)|(\__)|(\_+$)/.test(value)){
					return '用户名首尾不能出现下划线\'_\'';
				}
			},
			//数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
			nickname: [
				/[\u4e00-\u9fa5A-z0-9]+/,
				'昵称必须为英文、数字或汉字组合'
			],
		  	pass: [
		  		/^[\S]{5,16}$/,
		  		'密码必须5到16位，且不能出现空格'
		  	],
		  	phone: [
		  		/(^$)|^1\d{10}$/, 
		  		'请输入正确的手机号'
		  	],
		  	email: [
		  		/(^$)|^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/, 
		  		'邮箱格式不正确'
		  	],
		});
		
		//laydate渲染
		laydate.render({
			elem: '#date', //指定元素
			theme: 'grid',
			done: function(value,date,endDate) {	//选择日期完成之后触发
			  var ele = document.querySelector("[ng-controller=tableCtrl]");
			  angular.element(ele).scope().changeDate(value);
			}
		});
	});
})();