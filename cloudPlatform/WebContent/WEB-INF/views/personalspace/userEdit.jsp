<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%	
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>个人空间</title>
<link rel="shortcut icon" href="<%=basePath %>images/shortcut.ico">
<link rel="stylesheet" href="<%=basePath %>font-awesome/css/font-awesome.css">
<link rel="stylesheet" href="<%=basePath %>layui/css/layui.css">
<link rel="stylesheet" href="<%=basePath %>css/load.css">
<link rel="stylesheet" href="<%=basePath %>cropper/css/cropper.css">
<link rel="stylesheet" href="<%=basePath %>/zTree/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath %>css/myFont.css">
<style type="text/css">
html {
	height: 100%;
}

body {    
	background: #f4f5f7;
	height: 100%;
	margin: 0;
	-moz-user-select: none; /*火狐*/
	-webkit-user-select: none; /*webkit浏览器*/
	-ms-user-select: none; /*IE10*/
	-khtml-user-select: none; /*早期浏览器*/
	user-select: none;
}

ul {
    margin: 0;
    list-style-type: none;
    padding: 0;
}

iframe {
    border: none;
    width: 100%;
    height: 100%;
}

.main {
    width: 1076px;
    height: 798px;
    border: 1px solid #ccc;
    background: white;
}

.main-head {
	padding: 10px 20px;
    background: #f4f5f7;
    border-bottom: 1px solid #ccc;
    font-size: 14px;
    color: #333;
}

.head-nav {
	position: relative;
	height: 100%;
	display: flex;
}

.nav-item {
	display: flex;
	margin-right: 31px;
}

.nav-item>a {
	display: block;
	padding: 15px 0px;
    position: relative;
    color: #666;
    cursor: pointer;
}

.nav-icon {
	padding: 15px 0px;
    display: inline-block;
    font-size: 20px;
    margin-right: 5px;
    color: #03A9F4;
}

.nav-cursor {
	position: absolute;
    height: 0px;
    border: 2px solid #00a1d6;
    bottom: 5px;
    -webkit-transition: all 0.2s ease;
    transition: all 0.2s ease;
}

.main-body {
    width: 100%;
    height: 709px;
    padding: 20px;
    box-sizing: border-box;
}

.input-2 {
	width: 500px;
}

.class-modal {
	background: white;
	width: 100px;
    height: 15px;
    border: 1px solid #666;
    display: inline-block;
    position: absolute;
    left: 110px;
    top: 0px;
    padding: 8px 20px;
    font-size: 10px;
    text-align: center;
    cursor: pointer;
    -webkit-transition: all 0.5s;
    transition: all 0.5s;
}

.class-modal-title {
	height: 0px;
	display: block;
}

.class-modal-content {
	height: 0px;
	opacity: 0;
	overflow: hidden;
    -webkit-transition: all 0.5s;
    transition: all 0.5s;
}

.close-div {
	overflow: hidden;
}

.modal-close {
	float: right;
    font-size: 15px;
    cursor: pointer;
}

.class-modal-head {
	display: flex;
	text-align: left;
	width: 350px;
    -webkit-transition: all 0.5s;
    transition: all 0.5s;
}

.class-modal-body {
	width: 350px;
	height: 300px;
    -webkit-transition: all 0.5s;
    transition: all 0.5s;
    overflow: auto;
}

.class-modal-foot {
	text-align: right;
	width: 350px;
	-webkit-transition: all 0.5s;
    transition: all 0.5s;
}

.class-modal-search {
    height: 30px;
    width: 120px;
    margin-left: 5px;
}

.search {
	display: flex;
}

.search-button {
	width: 30px;
    height: 30px;
    border-left: none;
}

.class-modal.active {
    cursor: initial;
	width: 350px;
    height: 380px;
    top: -100px;
    background: #fffef9;
    border-color: #ccc;
}

.class-modal.active .class-modal-title {
	height: 0px;
	opacity: 0;
}

.class-modal.active .close-div {
	height: 20px;
}

.class-modal.active .class-modal-content {
	opacity: 1;
	height: 380px;
}

.user-classs {
    width: 600px;
    margin-left: 150px;
}

.user-class {
    box-shadow: 0 0 1px 1px #ccc;
    padding: 2px 20px;
    background: white;
    color: #6f6d6d;
    border-radius: 5px;
    font-size: 13px;
    margin-left: 10px;
    display: inline-block;
    margin-bottom: 5px;
}

.main-foot {
    position: fixed;
    display: flex;
    justify-content: center;
    width: 100%;
    margin-left: -20px;
    bottom: 20px;
}

.ifm {
	display: none;
}

.ifm.active {
	display: block;
}

.error{
	padding: 7px 10px;
    color: red;
}    

/* layui */
.layui-form-item {
	display: flex;
}

.layui-input-block {
	margin-left: 0px;
}

/* Limit image width to avoid overflow the container */
img {
  max-width: 100%; /* This rule is very important, please do not ignore this! */
}

.image-radius {
	widows: 200px;
	height: 200px;
	overflow: hidden;
	border-radius: 200px;
}

.image-rex {
	widows: 200px;
	height: 200px;
	overflow: hidden;
}

.image-space {
    display: flex;
}

.image-perviews {
	display: flex;
    flex-direction: column;
    margin-left: 50px;
}

.image-120 {
	width: 120px;
	height: 120px;
}

.image-100 {
	width: 100px;
	height: 100px;
}

.image-60 {
	width: 60px;
	height: 60px;
}

.perview-item {
    display: flex;
    padding: 10px;
    flex-direction: column;
}

.perview-size {

}
</style>
</head>
<body>
<div class="main">
	<div class="main-head">个人信息</div>
	<div class="main-nav">
  		<div class="head-nav" style="padding: 0px 10px;border-bottom: 1px solid #ccc;">
	    	<div class="nav-item active" data-ifm=".ifm-base"><a>基本信息</a></div>
  			<div class="nav-item" data-ifm=".ifm-image"><a>头像设置</a></div>
  			<div class="nav-item" data-ifm=".ifm-password"><a>密码修改</a></div>
  			<div class="nav-cursor" style="bottom: -3px;"></div>
  		</div>
	</div>
	<div class="main-body">
		<div class="ifm ifm-base">
	  		<div class="body-content" data-urid="${user.ur_id }">
				<div class="layui-form">
				
	  				  <div class="layui-form-item" id="item_username">
						  <label class="layui-form-label">用户名</label>
						  <div class="layui-input-block">
						  <input id="form_username" type="text" name="title" lay-verify="title" disabled="disabled"
							  autocomplete="off" placeholder="请输入用户名" class="layui-input input-2" value="${user.ur_username }">
						  </div>					  
					  </div>
					  
	  				  <div class="layui-form-item" id="item_nickname">
						  <label class="layui-form-label">昵称</label>
						  <div class="layui-input-block">
						  <input id="form_nickname" type="text" name="title" lay-verify="title"
							  autocomplete="off" placeholder="请输入昵称" class="layui-input input-2" value="${user.ur_nickname }">
						  </div>
					  </div>
					  
	   				  <div class="layui-form-item" id="item_email">
						  <label class="layui-form-label">邮箱</label>
						  <div class="layui-input-block">
						  <input id="form_email" type="text" name="title" lay-verify="title"
							  autocomplete="off" placeholder="请输入邮箱" class="layui-input input-2" value="${user.ur_email }">
						  </div>
					  </div>
					  
	   				  <div class="layui-form-item" id="item_phone">
						  <label class="layui-form-label">手机号码</label>
						  <div class="layui-input-block">
						  <input id="form_phone" type="text" name="title" lay-verify="title"
							  autocomplete="off" placeholder="请输入手机号码" class="layui-input input-2" value="${user.ur_phone }">
						  </div>
					  </div>
					  
					  <div class="layui-form-item" id="item_sex">
					    <label class="layui-form-label">性别</label>
					    <div id="form_sex" class="layui-input-block">
					      <input type="radio" name="sex" data-sex="0" value="男" title="男" ${user.ur_sex == 0?'checked':'' }>
					      <input type="radio" name="sex" data-sex="1" value="女" title="女" ${user.ur_sex == 1?'checked':'' }>
					    </div>
					  </div>
					  
	  				  <div class="layui-form-item" id="item_classid" style="position: relative;">
					    <label class="layui-form-label">班级</label>
						<div class="class-modal">
							<span class="class-modal-title">点击选择</span>
							<div class="class-modal-content">
								<div class="close-div">
									<i class="fa fa-times modal-close"></i>
								</div>
								<div class="class-modal-head">
									<button class="layui-btn  layui-btn-primary layui-btn-sm" onclick="expandAll()">展开所有</button>
									<button class="layui-btn  layui-btn-primary layui-btn-sm" onclick="collapseAll()">收起所有</button>
									<div class="input-group input-group-sm search">
										<input type="text" class="layui-input class-modal-search" placeholder="搜索节点" onkeydown="if(event.keyCode == 13) {searchNodes()}">
										<button class="layui-btn layui-btn-primary layui-btn-xs search-button" onclick="searchNodes()"><i class="fa fa-search"></i></button>
									</div>
								</div>
								<div class="class-modal-body">
									<div class="ztree" id="class_tree"></div>
								</div>
								<div class="class-modal-foot">
									<button class="layui-btn layui-btn-sm" onclick="updateClassFromZtree()">确定</button>
									<button class="layui-btn layui-btn-primary layui-btn-sm btn-close" onclick="hideClassModel($('.class-modal'))">取消</button>
								</div>
							</div>
						</div>
						
						<div id="form_classid" class="user-classs">

						</div>
					  </div>
	  			</div>
  			</div>
			<div class="main-foot">
				<button class="layui-btn layui-btn-lg layui-btn-normal" onclick="submitBaseForm()">保存</button>
			</div>
		</div>
		<div class="ifm ifm-image">
<!-- 			<iframe src="/cloud/personalSpace/userImage" style="height: 650px;"></iframe> -->
			<div class="image-space">
				<div class="box" style="width: 600px;height: 400px;">
				  <img id="image" src="<%=basePath%>cropper/picture.jpg">
				</div>
				<div class="image-perviews">
					<div class="perview-item"><span class="perview-size">120 x 120</span><div class="image-perview image-radius image-120"></div></div>
					<div class="perview-item"><span class="perview-size">100 x 100</span><div class="image-perview image-radius image-100"></div></div>
					<div class="perview-item"><span class="perview-size">60 x 60</span><div class="image-perview image-radius image-60"></div></div>
				</div>
			</div>
			<button type="button" class="layui-btn" id="upload_button" style="margin-top: 10px;">
			  <i class="layui-icon">&#xe67c;</i>上传图片
			</button>
			<div class="main-foot">
				<button class="layui-btn layui-btn-lg layui-btn-normal" onclick="uploadImage()">保存</button>
			</div>
		</div>
		<div class="ifm ifm-password">
		  <div class="layui-form-item" id="password_old">
			  <label class="layui-form-label">旧密码</label>
			  <div class="layui-input-block">
			  <input type="password" name="title" lay-verify="title"
				  autocomplete="off" placeholder="请输入旧密码" class="layui-input input-2" value="">
			  </div>					  
		  </div>
		
		  <div class="layui-form-item" id="password_new">
			  <label class="layui-form-label">新密码</label>
			  <div class="layui-input-block">
			  <input type="password" name="title" lay-verify="title"
				  autocomplete="off" placeholder="请输入新密码" class="layui-input input-2" value="">
			  </div>
		  </div>
  		  <div class="layui-form-item" id="password_repeat">
			  <label class="layui-form-label">新密码确认</label>
			  <div class="layui-input-block">
			  <input type="password" name="title" lay-verify="title"
				  autocomplete="off" placeholder="请再次输入新密码" class="layui-input input-2" value="">
			  </div>
		  </div>
		  <div class="main-foot">
			  <button class="layui-btn layui-btn-lg layui-btn-normal" onclick="updatePassword()">保存</button>
		  </div>
		</div>
 	</div>
</div>
</body>
<script type="text/javascript" src="<%=basePath%>js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>layui/layui.js"></script>
<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/load.js"></script>
<script src="<%=basePath%>cropper/js/cropper.js"></script>
<script src="<%=basePath%>cropper/js/jquery-cropper.js"></script>
<script type="text/javascript">
var timer;
var ur_classid = '${user.ur_classid }';
var ajaxCount = 0;
var imageSelect = false;

var $image = $('#image');
var URL = window.URL || window.webkitURL;
var uploadedImageURL;
// Import image
var $inputImage;
var options = {
	    aspectRatio: 1,
	    viewMode:1,
	    minContainerWidth: 600,
	    minContainerHeight: 400,
	    preview: ".image-perview",
	    crop: function (e) {
// 	        console.log(e.detail.x);
// 	        console.log(e.detail.y);
// 	        console.log(e.detail.width);
// 	        console.log(e.detail.height);
// 	        console.log(e.detail.rotate);
// 	        console.log(e.detail.scaleX);
// 	        console.log(e.detail.scaleY);
	    }
	};

$('.nav-cursor').css('width', $('.nav-item.active').css('width'));
$('.nav-cursor').css('left', $('.nav-item.active').position().left);
$($('.nav-item.active').attr('data-ifm')).addClass('active');

$('.nav-item').mouseover(function() {
	$('.nav-cursor').css('width', $(this).css('width'));
	$('.nav-cursor').css('left', $(this).position().left);
	clearTimeout(timer);
})

$('.nav-item').mouseout(function() {
	timer = setTimeout(function() {
		$('.nav-cursor').css('width', $('.nav-item.active').css('width'));
		$('.nav-cursor').css('left', $('.nav-item.active').position().left);
	}, 1000);
})

$('.nav-item').on('click', function() {
	$('.nav-item.active').removeClass('active');
	$(this).addClass('active');
	$('.ifm').removeClass('active');
	$($('.nav-item.active').attr('data-ifm')).addClass('active');
	if($('.nav-item.active').attr('data-ifm') == '.ifm-image') {
		initCropper();
	}
})

$('.class-modal').bind('click', function(event) {
		if(!$(this).hasClass('active')) {
			showClassModel($(this));
			$('.class-modal').unbind('click');
		}
})

$('.modal-close').on('click', function(event) {
	hideClassModel($('.class-modal'));
})

function showClassModel($ele) {
	$ele.addClass('active');
}

function hideClassModel($ele) {
	$ele.removeClass('active');
	setTimeout(function() {
		$ele.bind('click', function() {
			if(!$(this).hasClass('active')) {
				showClassModel($(this));
				$ele.unbind('click');
			}
		})
	}, 100);

}

layui.use(['form', 'layer', 'upload'], function(){
  var form = layui.form;
  var layer = layui.layer;
  var upload = layui.upload;
  
  //自定义验证规则
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

	//执行实例
	var uploadInst = upload.render({
	  elem: '#upload_button' //绑定元素
	  ,auto: false
	  ,done: function(res){

	  }
	  ,error: function(){
	    //请求异常回调
	  }
	});
	
	$inputImage = $('input[type="file"]');
	if (URL) {  
		  $inputImage.change(function () {
			imageSelect = true;
		    var files = this.files;
		    var file;

		    if (!$image.data('cropper')) {
		      return;
		    }

		    if (files && files.length) {
		      file = files[0];

		      if (/^image\/\w+$/.test(file.type)) {
		        uploadedImageName = file.name;
		        uploadedImageType = file.type;

		        if (uploadedImageURL) {
		          URL.revokeObjectURL(uploadedImageURL);
		        }

		        uploadedImageURL = URL.createObjectURL(file);
		        $image.cropper('destroy').attr('src', uploadedImageURL).cropper(options);
		        $inputImage.val('');
		      } else {
		        window.alert('Please choose an image file.');
		      }
		    }
		  });
		} else {
		  $inputImage.prop('disabled', true).parent().addClass('disabled');
		}



});

function initCropper() {
	imageSelect = false;
	$image.attr('src', '/cloud/images/user.gif');
	$image.cropper('destroy').cropper(options);
	setTimeout(function() {
		$image.cropper('clear').cropper('disable');
	}, 100);
	
}

function uploadImage() {
	if(imageSelect == false) {
		layer.msg("请先选择上传图片" , {icon: 3,time:1000});
		return;
	}
   	// Upload cropped image to server if the browser supports `HTMLCanvasElement.toBlob`
   	$('#image').cropper('getCroppedCanvas').toBlob(function (blob) {
   	  var formData = new FormData();
   	  formData.append('image', blob);
   	  
	  showLoad('body');
 	  $.ajax('/cloud/personalSpace/uploadImage', {
 	    method: "POST",
 	    data: formData,
 	    processData: false,
 	    contentType: false,
 	    success: function (result) {
			hideLoad('body');
			if(result.code != '0000') {
				layer.msg(result.msg , {icon: 2,time:1000}); 
				return;
			}
			parent.updateImage(result.data);
			layer.msg("修改头像成功！" , {icon: 1,time:1000});
 	    },
 	    error: function () {
    	  hideLoad('body');
    	  layer.msg('系统错误!' , {icon: 2,time:1000});
 	    }
 	  });
 	});
}

initTreeData();

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
			
		}
	},
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
	$.post("/cloud/organization/listOrganizations",{},function(rs) {
		zTreeObj = $.fn.zTree.init($("#class_tree"), zTreeSetting, rs.data);
		//自定义图标
		initCustomIcon();
		
		checkNodeForClass(ur_classid);
		
		$.post("/cloud/organization/getDetailByClassId",{ids: ur_classid},function(rs) {
			if(rs.code != '0000') {
				alert(result.msg); 
				return;
			}
			var data = rs.data;
			for(var key in data) {
				addClassItem(key, data[key])
			}
		},"json");
	},"json");
}

function cleanClassItem() {
	$('.user-class').remove();
}

function addClassItem(id, detailName) {
	var $class = '<span class="user-class" data-id="' + id + '">' + detailName + '</span>';
	$('.user-classs').append($class);
}

//勾选用户所属班级节点
function checkNodeForClass(ur_classid) {

	var classids = ur_classid.split(",");
	for(var n in classids) {
		var node = zTreeObj.getNodeByParam("org_id", classids[n]);
		if(!!node) {
			zTreeObj.checkNode(node, true, true);
			// 获取父亲节点
			var parents = zTreeObj.getNodesByParam("org_id", node.org_pid, null);
			if (parents.length > 0) {
				zTreeObj.expandNode(parents[0], true, false, false);
			}
		}
	}
}

//初始化自定义图标
function initCustomIcon() {
	var icons = ["images/tree_acad.png","images/tree_major.png",
	             "images/tree_grade.png","images/tree_class.png"];
	for(var i=0; i<4; i++) {
		var nodes = zTreeObj.getNodesByFilter(eval("filter" + i));
		addIcon(nodes, '/cloud/' + icons[i]);
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

function searchNodes() {
	initHighLight();
	var value = $('.class-modal-search').val()
	if(value == "") {
		return false;
	}
	var nodes = zTreeObj.getNodesByParamFuzzy("org_name", value, null);
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

function classFilter(node) {
	
	return (node.org_type == 4 && node.checked == true);
}

//根据ztree中选择的班级更新用户班级显示
function updateClassFromZtree() {
	var treeObj = $.fn.zTree.getZTreeObj("class_tree");
	var nodes = treeObj.getNodesByFilter(classFilter);
	cleanClassItem();
	for(var i in nodes) {
		var name = nodes[i].org_name;
		name = composeClassDetailName(nodes[i],name);
		addClassItem(nodes[i].org_id, name);
	}
	hideClassModel($('.class-modal'));
}

//组成班级详细名称
function composeClassDetailName(node,name) {
	var treeObj = $.fn.zTree.getZTreeObj("class_tree");
	var pNode = treeObj.getNodesByParam("org_id", node.org_pid, null)[0];
	name = pNode.org_name + name;
	if(pNode.org_pid != null && pNode.org_pid != 0) {
		name = composeClassDetailName(pNode,name)
	}
	return name;
}

//验证表单内容
function verfiyBaseForm() {
	cleanError();
	
	var urid = $('.body-content').attr('data-urid');
	var username = $('#form_username').val();
	var nickname = $('#form_nickname').val();
	var email = $('#form_email').val();
	var phone = $('#form_phone').val();
	var sex = $('#form_sex').find(':checked').attr('data-sex');
	var classId = "";
	$('#form_classid').children('.user-class').each(function() {
		classId = classId + $(this).attr('data-id') + ',';							
	})									
	if(classId.length > 0) {
		classId = classId.substring(0, classId.length - 1);
	}
	
	if(/^$/.test(username)) {
		showError('#item_username', '用户名不能为空');
	}else if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(username)){
		showError('#item_username', '用户名不能有特殊字符');
	}else if(/(^\_)|(\__)|(\_+$)/.test(username)){
		showError('#item_username', '用户名首尾不能出现下划线\'_\'');
	}
	
	if(!/^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$/.test(nickname)) {
		showError('#item_nickname', '昵称必须为英文、数字或汉字组合');
	}
	
	if(!/(^$)|^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(email)) {
		showError('#item_email', '邮箱格式不正确');
	}
	
	if(!/(^$)|^1\d{10}$/.test(phone)) {
		showError('#item_phone', '请输入正确的手机号');
	}
	
	if(!/^[1-9]\d*(,\d+)*$/.test(classId)) {
		showError('#item_classid', '请选择班级');
	}
	
}

function updateBase() {
	var urid = $('.body-content').attr('data-urid');
	var username = $('#form_username').val();
	var nickname = $('#form_nickname').val();
	var email = $('#form_email').val();
	var phone = $('#form_phone').val();
	var sex = $('#form_sex').find(':checked').attr('data-sex');
	var classId = "";
	$('#form_classid').children('.user-class').each(function() {
		classId = classId + $(this).attr('data-id') + ',';							
	})									
	if(classId.length > 0) {
		classId = classId.substring(0, classId.length - 1);
	}
	
	showLoad('body');
	$.ajax({
		url:"/cloud/personalSpace/updateBase",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{ur_id:urid, 
			  ur_username:username,
			  ur_nickname:nickname,
			  ur_email:email,
			  ur_phone:phone,
			  ur_sex:sex,
			  ur_classid:classId
		     },
		success:function(result){
			hideLoad('body');
			if(result.code != '0000') {
				layer.msg(result.msg , {icon: 2,time:1000}); 
				return;
			}
			layer.msg("修改个人信息成功！" , {icon: 1,time:1000});
		},
		error:function() {
			hideLoad('body');
			layer.msg('系统出错了！', {icon: 2,time:1000}); 
		}
	});
}

function verfifyUsernameExist(urid, username, callback) {
	ajaxCount = 1;
	$.ajax({
		url:"/cloud/personalSpace/verfifyUsernameExist",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{urid:urid, 
			  username:username
		     },
		success:function(result){
			minusAjaxCount(ajaxCount);
			if(result.code != '0000') {
				return;
			}
		},
		error:function() {
			minusAjaxCount(ajaxCount);
			layer.msg('验证用户名时系统出错了!', {icon: 2,time:1000}); 
		}
	});
	var ajaxItv = setInterval(function() {
		if(ajaxCount == 0) {
			callback();
			clearInterval(ajaxItv);
		}
	}, 100);
}

function minusAjaxCount() {
	if(ajaxCount != null) {
		ajaxCount = ajaxCount - 1;
	}
}

function showError(ele, msg) {
	$(ele).append('<span class="error"><i class="fa fa-close"></i>' + msg + '</span>');
}

function cleanError() {
	$('.error').remove();
}

function submitBaseForm() {
	var urid = $('.body-content').attr('data-urid');
	var username = $('#form_username').val();
	
	verfiyBaseForm();
	verfifyUsernameExist(urid, username, updateBase);
}

function updatePassword() {
	verifyPswFrom();
	var ajaxItv = setInterval(function() {
		if(ajaxCount == 0) {
			clearInterval(ajaxItv);
			if(!$('.ifm-password').find('.error').attr('class')) {
				$.ajax({
					url:"/cloud/personalSpace/updatePassword",
					dataType:'json',
					type:"post",
					contentType: "application/x-www-form-urlencoded;charset=utf-8",
					data:{urid:$('.body-content').attr('data-urid'), 
						  password_new: $('#password_new').find('input').val()
					     },
					success:function(result){
						if(result.code != '0000') {
							layer.msg(result.msg, {icon: 2,time:1000});
							return;
						}
						layer.msg("更新密码成功!", {icon: 1,time:1000});
					},
					error:function() {
						layer.msg('系统出错了!', {icon: 2,time:1000}); 
					}
				});
			}
		}
	}, 100);
}

function verifyPswFrom() {
	cleanError();
	
	var urid = $('.body-content').attr('data-urid');
	var password_old = $('#password_old').find('input').val();
	var password_new = $('#password_new').find('input').val();
	var password_repeat = $('#password_repeat').find('input').val();
	
	if(!/^[\S]{5,16}$/.test(password_new)) {
		showError('#password_new', "密码必须5到16位，且不能出现空格");
	}
	
	if(!/^[\S]{5,16}$/.test(password_repeat)) {
		showError('#password_repeat', "密码必须5到16位，且不能出现空格");
	}
	
	if(password_new != password_repeat) {
		showError('#password_repeat', "两次输入的密码不同");
	}
	
	ajaxCount = 1;
	$.ajax({
		url:"/cloud/personalSpace/verfifyPsw",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{urid:urid, 
			  password_old:password_old
		     },
		success:function(result){
			minusAjaxCount(ajaxCount);
			if(result.code != '0000') {
				showError('#password_old', result.msg);
				return;
			}
		},
		error:function() {
			minusAjaxCount(ajaxCount);
			layer.msg('验证用户名时系统出错了!', {icon: 2,time:1000}); 
		}
	});
}
</script>
</html>