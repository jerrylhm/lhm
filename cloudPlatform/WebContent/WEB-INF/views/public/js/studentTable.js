$(function() {	
	$('.class-modal').on('click', function(event) {
		if(!$(this).hasClass('active')) {
			showClassModel($(this));
			$('.class-modal').unbind('click');
		}
	})

	$('.modal-close').on('click', function(event) {
	hideClassModel($('.class-modal'));
	})

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

var COOKIE_CLASSID = "CREATOR_CLASSID";
var COOKIE_CLASSNAME = "CREATOR_CLASSNAME";
var COOKIE_TTID = "CREATOR_TTID";
var COOKIE_TTNAME = "CREATOR_TTNAME";
var COOKIE_ORGID = "CREATOR_ORGID";
var COOKIE_ORGNAME = "CREATOR_ORGNAME";

var normalType = 1;      //普通节点类型
var classRoomType = 0;   //课室节点类型

var tts = [];
var ttKey = false;
var orgs = [];
var orgKey = false;
var adds = [];
var addKey = false;
var currentWeek;

layui.use(['layer', 'form'], function(){
	  var layer = layui.layer
	  ,form = layui.form;
	  form.on('select(changeTt)', function(data){
		  addCookie(COOKIE_TTID, data.value);
		  addCookie(COOKIE_TTNAME, $(data.elem).find('option[value=' + data.value + ']').html());
		  initTable();
	  });
	  form.on('select(changeClass)', function(data){
		  addCookie(COOKIE_ORGID, data.value);
		  addCookie(COOKIE_ORGNAME, $(data.elem).find('option[value=' + data.value + ']').html());
		  getStudentTable();
	  });  
	  
      initClassByCookie();
	  getTimeTables();
      getOrgs();
      getTeachers();
      initClassSelect();
	  initTreeData();
	  initTable();
});

   vue_add = new Vue(
		{
			el:'#app',
			data: {
				week_day: [{name:"周一",index:"1"},
						   {name:"周二",index:"2"},
						   {name:"周三",index:"3"},
						   {name:"周四",index:"4"},
						   {name:"周五",index:"5"},
						   {name:"周六",index:"6"},
						   {name:"周日",index:"7"}],
				week_indexs: [],
				week_nodes: [],
				teachers: [],
				timetables: []
				
			}
		}
)
   
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
		onClick: initStudentTable
	},
	view: {
		fontCss: getFontCss,
	}
}
   
var zTreeSetting_add = {
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
		onClick: initAddidForAdd
	},
	view: {
		fontCss: getFontCss,
	}
}
   
var zTreeSetting_update = {
	data : {
		simpleData : {
			enable : true,
			idKey : "add_id",
			pIdKey : "add_pid",
			rootPId : 0
		},
		key : {
			name : "add_name"
		}
	},
	callback : {
		onClick : initAddidForUpdate
	},
	view : {
		fontCss : getFontCss,
	}
}
   
//初始化新增模态框的教师选择框
function initAddidForAdd(event, treeId, treeNode) {
   if(treeNode.add_type == classRoomType) {
	   var classId = treeNode.add_id;
	   $('#ztree_title_add').html(treeNode.add_name);
	   $('#add_addid').attr('data-addid', classId);
	   hideClassModel($('.class-modal'));
   }  
}
   
//初始化更新模态框的教室选择框   
function initAddidForUpdate(event, treeId, treeNode) {
   if(treeNode.add_type == classRoomType) {
	   var classId = treeNode.add_id;
	   $('#ztree_title_update').html(treeNode.add_name);
	   $('#update_addid').attr('data-addid', classId);
	   hideClassModel($('.class-modal'));
   }  
}
   
//初始化班级筛选条件
function initClassSelect() {
	var itv = setInterval(function() {
		if(orgKey) {
			var orgId = getCookie(COOKIE_ORGID);
			if(orgId != null && orgId != "") {
				addOrgOptionSelected('.child', orgId);
			}else {
				addOrgOption('.child');
			}
			
			clearInterval(itv);
		}
	}, 100);   
}
   
//教室节点点击事件
function initStudentTable(event, treeId, treeNode) {
   if(treeNode.add_type == classRoomType) {
	   var classId = treeNode.add_id;
	   $('.st-head-btn1').attr('data-classid', classId);
	   $('.st-head-btn1>span').html(treeNode.add_name);
	   addCookie(COOKIE_CLASSID, treeNode.add_id);
	   addCookie(COOKIE_CLASSNAME, treeNode.add_name);
	   getStudentTable();
   }  
}  
   
//清除教室筛选条件
function cleanAddid() {
	   $('.st-head-btn1').attr('data-classid', null);
	   $('.st-head-btn1>span').html("点击选择教室");
	   addCookie(COOKIE_CLASSID, '', 0);
	   addCookie(COOKIE_CLASSNAME, '', 0);
	   getStudentTable();
}

//搜索高亮
function getFontCss(treeId, treeNode) {
 	return (!!treeNode.highlight) ? {color:"#A60000"} : {color:"#333"};
}
 
//筛选节点
function filterNormal(node) {
	return (node.add_type == normalType);
}

function filterClassRoom(node) {
 	return (node.add_type == classRoomType);
}
 
//设置图标
function addIcon(id,nodes,icon) {
	var zTreeObj = $.fn.zTree.getZTreeObj(id);
	for(var i=0;i<nodes.length;i++) {
		nodes[i].icon = icon;
		zTreeObj.updateNode(nodes[i]);
	}
}	
	 
//模糊查找节点
function searchNodes(id,input_selector) {
	var treeObj = $.fn.zTree.getZTreeObj(id);
	var searchKey = $(input_selector).val();
	nodes = treeObj.transformToArray(treeObj.getNodes());
	for(var key in nodes) {
		nodes[key].highlight = false;
		treeObj.updateNode(nodes[key]);
	}
	
	if(searchKey != '') {
		var nodes = treeObj.getNodesByParamFuzzy("add_name", searchKey, null);
		for(var key in nodes) {
			nodes[key].highlight = true;
			treeObj.updateNode(nodes[key]);
			if(nodes[key].children == null) {	
				treeObj.expandNode(nodes[key].getParentNode(), true, false, true);
			}else {
				treeObj.expandNode(nodes[key], true, false, true);
			}
		}
	}
	
}

//一键打开或关闭节点
function openNode(ztreeId,open) {
	var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
	treeObj.expandAll(open);
}

//根据cookie保存的教室信息初始化课表
function initClassByCookie() {
	var classId = getCookie(COOKIE_CLASSID);
	var className = getCookie(COOKIE_CLASSNAME)
	if(classId != null && classId != "") {
		$('.st-head-btn1').attr('data-classid', classId);
		$('.st-head-btn1').find('span').html(className)
	}
}

//获取场景树节点数据
function initTreeData() {
	$.post("/cloud/organization/listAddress",{},function(rs) {
		addKey = true;
		if(rs.code == "0000") {
			adds = rs.data;
			//初始化树数据
			var zTreeObj = $.fn.zTree.init($("#ztree"), zTreeSetting, rs.data);
			var zTreeObj1 = $.fn.zTree.init($("#ztree_add"), zTreeSetting_add, rs.data);
			var zTreeObj2 = $.fn.zTree.init($("#ztree_update"), zTreeSetting_update, rs.data);
			//自定义图标
			initCustomIcon('ztree');
			initCustomIcon('ztree_add');
			initCustomIcon('ztree_update');
		}else {
			layer.msg(rs.msg, {icon: 2,time:3000}); 
		}
	}).fail(function() {
		layer.msg("系统错误!", {icon: 2,time:3000}); 
	});
}

//初始化图标
function initCustomIcon(id) {
	var zTreeObj = $.fn.zTree.getZTreeObj(id);
	var normalNodes = zTreeObj.getNodesByFilter(filterNormal)
	addIcon(id,normalNodes,"/cloud/images/tree_normal.png");
	
	var classRoomNodes = zTreeObj.getNodesByFilter(filterClassRoom);
	addIcon(id,classRoomNodes,"/cloud/images/tree_classroom.png");
}

//获取作息表列表
function getTimeTables() {
	$.ajax({
		url:"/cloud/timetable/queryTimeTables",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{},
		success:function(result){
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:3000}); 
				return;
			}
			tts = result.data;			
			var ttId = getCookie(COOKIE_TTID);
			if(ttId != null && ttId != "") {
				addTtsOptionSelected('.st-head-right', ttId);
			}else {
				addTtsOption('.st-head-right', tts);
			}
			ttKey = true;
			
			vue_add.timetables = tts;
			layui.form.render('select');
		},
		error:function() {
			layer.msg('系统错误!', {icon: 2,time:3000}); 
		    hideLoad('body');
		}
	});
}

//获取教师列表
function getTeachers() {
	$.ajax({
		url:"/cloud/studenttable/getTeachers",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{},
		success:function(result){
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:3000}); 
				return;
			}
			vue_add.teachers = result.data;
			
			setTimeout(function() {
				layui.form.render('select');
			}, 100);
		},
		error:function() {
			layer.msg('系统错误!', {icon: 2,time:3000}); 
		    hideLoad('body');
		}
	});
}

//初始化课表视图
function initTable() {
	var itv = setInterval(function() {
		if(ttKey) {
			if(tts.length > 0) {
				var ttId = $('.select-tt').val();
				$('#add_ttid').val(ttId);
				layui.form.render('select')
				for(var i in tts) {		
					if(tts[i].tt_id == ttId) {
						vue_add.week_nodes = tts[i].nodes;
						vue_add.week_indexs = createIndexArray(tts[i].tt_num);
					    var week = $('#head_input').val();
					    var itv1 = setInterval(function() {
					    	if(ttKey && orgKey && addKey) {
					    		clearInterval(itv1); 
					    		getStudentTable();
					    	}
						}, 100);
					}
				}
			}
			clearInterval(itv);
		}
	}, 100);
}

//添加cookie（有效期30天）
function addCookie(key, value) {
	document.cookie=key + "=" + encryptByDES('' + value) + "; expires=" + 30*24*3600*1000;
}

//添加cookie（自定义有效时间）
function addCookie(key, value, exdays) {
	document.cookie=key + "=" + encryptByDES('' + value) + "; expires=" + exdays*24*3600*1000;
}

//根据键获取cookie
function getCookie(key)
{
	var arr,reg=new RegExp("(^| )"+key+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg)) {
		return decryptByDESModeEBC(unescape(arr[2]));
	}
	else
	return null;
}

//创建元素为1~i的数组
function createIndexArray(index) {
	var result = [];
	for(var i=1;i<=index;i++) {
		result.push(i);
	}
	return result;
}

//获取课程信息
function getStudentTable() {
	if(!(ttKey && orgKey && addKey)) {
		alert("数据未加载完成！")
		return;
	}
	var ttid = $('.select-tt').val();
	var orgid = $('.select-org').val();
	var addid = $('.st-head-btn1').attr('data-classid');
	var week = currentWeek;
	cleanStItem();
	showLoad('body');
	$.ajax({
		url:"/cloud/studenttable/getStudentTable",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{ttId:ttid,
			  addId:addid,
			  orgId: orgid,
		      week:week},
		success:function(result){
			hideLoad('body');
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:3000}); 
				return;
			}
			var ymd = result.data.ymd;
			var sts = result.data.sts;
			var week = result.data.week;			
			currentWeek = parseInt(week);
			var itv = setInterval(function() {
				if(orgKey) {
					createStItem(sts, week, ymd);
				    clearInterval(itv);
				}
			}, 200);
		},
		error:function() {
			layer.msg('系统错误!', {icon: 2,time:3000}); 
		    hideLoad('body');
		}
	});
}

//清空所有课程
function cleanStItem() {
	$('.st-body-content').each(function() {
		$(this).attr('data-stid', null);
		$(this).attr('data-num', null);
		$(this).find('.st-name').html('');
		$(this).find('.st-teacher').html('');
		$(this).find('.st-add').html('');
		$(this).find('.st-org').remove();
	})
}

//创建课程信息
function createStItem(sts, week, ymd) {
	$('#head_input').val(week);
	updateYmd(ymd);
	for(var i in sts) {
		initSt(sts[i]);
	}
}

//初始化课程
function initSt(st) {
	$('.st-body-content').each(function() {
		var data_index = $(this).attr('data-index');
		var data_day = $(this).attr('data-day');
		var indexs = st.indexs;
		for(var i in indexs) {
			if(indexs[i].index_index == data_index && indexs[i].index_day == data_day) {
				$(this).attr('data-weeks', st.st_weeks);
				$(this).attr('data-stid', st.st_id);
				$(this).attr('data-num', st.st_num);
				$(this).find('.st-name').html(st.st_name);
				var teacherName;
				for(var j in vue_add.teachers) {
					if(vue_add.teachers[j].ur_id == st.st_teacherid) {
						teacherName = vue_add.teachers[j].ur_nickname;
					}
				}
				$(this).find('.st-teacher').html(teacherName).attr('data-teacherid', st.st_teacherid);
				
				var addName;
				for(var k in adds) {		
					if(adds[k].add_id == st.st_addid) {
						addName = adds[k].add_name;
					}
				}
				$(this).find('.st-add').html(addName).attr('data-addid', st.st_addid);
				createOrgItem(st, this);
			}
		}
	})
}


//创建课程信息的班级
function createOrgItem(st, obj) {
	var st_org = st.st_orgid;
	if(st_org != null) {
		var orgArray = st_org.split(',');
		for(var i in orgArray) {
			for(var j in orgs) {
				if(orgArray[i] == orgs[j].ele.org_id) {
					var $org = '<span data-orgid="' + orgs[j].ele.org_id + '" class="st-org">' + orgs[j].name + '</span>';
					$(obj).find('.st-orgs').append($org);
				}
			}
		}
	}
}

//更新课表的年月日信息
function updateYmd(ymd) {
	$('.y').html(ymd[0].year);
	ymd.sort(function(a,b) {
		return a.day - b.day;
	});
	for(var i in ymd) {
		$('.md').each(function() {
			if($(this).attr('data-day') == (parseInt(i) + 1)) {
				$(this).html(ymd[i].month + '月' + ymd[i].day + '日');
			}
		});
	}
}

//获取班级列表
function getOrgs() {
    showLoad('.modal-content');
	$.ajax({
		url:"/cloud/studenttable/getOrgs",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{},
		success:function(result){
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:3000}); 
				return;
			}
			orgs = result.data;
		    hideLoad('.modal-content');
		    orgKey = true;
		},
		error:function() {
			layer.msg('系统错误!', {icon: 2,time:3000}); 
		    hideLoad('.modal-content');
		    orgKey = true;
		}
	});
}

//打开新增课程模态框
function showModal(obj) {
	cleanError();
	if($(obj).attr('data-stid') == null || $(obj).attr('data-stid') == "") {
		initAddModal(obj);
		$('#add_modal').modal('show');
	}else {
		initUpdateModal(obj);
		$('#update_modal').modal('show')
	}
}

//初始化新增模态框
function initAddModal(obj) {
	resetAddModal();
	var index = $(obj).attr('data-index');
	var day = $(obj).attr('data-day');

	$('#add_day').val(day);
	resetAddIndex();
	$('#add_index_begin').val(index).trigger("change");
}

//初始化更新模态框
function initUpdateModal(obj) {
	resetUpdateModal();
	var st_id = $(obj).attr('data-stid');
	var st_ttid = $('.select-tt').val();
	var st_teacherid = $(obj).find('.st-teacher').attr('data-teacherid');
	var st_addid = $(obj).find('.st-add').attr('data-addid');
	var st_addname = $(obj).find('.st-add').html();
	var st_org = [];
	var index = $(obj).attr('data-index');
	var day = $(obj).attr('data-day');
	var beginIndex = index;
	var endIndex = index;
	var indexArray = [];
	var weeks = $(obj).attr('data-weeks');
	
	
	$('#update_ttid').val(st_ttid)
	$('#update_modal').attr('data-stid', st_id);
	$('#update_name').val($(obj).find('.st-name').html());
	$('#update_teacherid').val(st_teacherid);
	$('#update_addid').attr('data-addid', st_addid);
	$('#ztree_title_update').html(st_addname)
	$('#update_num').val($(obj).attr('data-num'));
	$('#update_day').val(day);
	
	$('.st-body-content[data-stid=' + st_id + ']').each(function() {
		indexArray.push($(this).attr('data-index'));
	})
	
	$(obj).find('.st-org').each(function() {
		st_org.push($(this).attr('data-orgid'));
	})
	for(var i in st_org) {
		var $addItemBtn = $('#update_modal').find('.layui-btn-group>button:eq(0)');
		addSelectedOrg($addItemBtn, st_org[i])
	}
	
	$('#update_index_begin').val(indexArray[0]).trigger("change");
	$('#update_index_end').val(indexArray[indexArray.length - 1]);
	
	setUpdateWeeks(weeks);
}

function setUpdateWeeks(weeks) {
	$('#update_table_week').find('.table-week').removeClass('active');
	var weekArray = weeks.split(',');
	for(var i in weekArray) {
		$('#update_table_week').find('.table-week[data-week="' + weekArray[i] + '"]').addClass('active');
	}
}

//重置新增模态框课节
function resetAddIndex() {
	$('#add_index_begin').val(1);
	$('#add_index_end').val(1);
	$('#add_index_end').find('option').attr('disabled', null);
}

//显示错误信息
function showError(ele, msg) {
	var $error = '<span class="msg-error"><i class="fa fa-close"></i><span class="msg-content">' + msg + '</span></span>';
	$(ele).parent().append($error);
}

//清空错误信息
function cleanError() {
	$('.msg-error').remove();
}

//打开和关闭教室选择框
function showCrs() {
	$('#btn1_mask').show();
	if($('.st-table').hasClass('active')) {
		$('.st-table').removeClass('active');
		$('.st-head-content').animate({height:"0px"},500,'linear',function() {
			$('.st-head-content').removeClass('active');
			$('.st-table').animate({left:"0px"},200,'linear',closeCrs());
		});
	}else {
		$('.st-table').addClass('active');
		$('.st-table').animate({left:"600px"},200,'linear', function() {
			$('.st-head-content').addClass('active');
			$('.st-head-content').animate({height:"600px"},500,'linear',openCrs());
		});
	}

	
}

//打开教室选择框后回调函数
function openCrs() {
	$('#btn1_mask').hide();
}

//关闭教室选择框后回调函数
function closeCrs() {
	$('#btn1_mask').hide();
}

//检查课程表单空值
function valiedAddEntity(type) {
	cleanError();
	var key = true;
	var ttid = $('#'+type+'_ttid').val();
	var addid = $('#'+type+'_addid').attr('data-addid');
	var name = $('#'+type+'_name').val();
	var teacher = $('#'+type+'_teacher').val();
	var num = $('#'+type+'_num').val();
	var index_begin = $('#'+type+'_index_begin').val();
	var index_end = $('#'+type+'_index_end').val();
	var weeks = [];
	$('#'+type+'_modal').find('.table-week').each(function() {
		if($(this).hasClass('active')) {
			weeks.push($(this).attr('data-week'));
		}
	})
	var orgs = [];
	$('#'+type+'_modal').find('.add-org-select').each(function () {
		orgs.push($(this).val())
	})
	
	if(ttid == '') {
		key = false;
		showError('#'+type+'_ttid', '请选择课表');
	}
	if(addid == '') {
		key = false;
		showError('#'+type+'_addid', '请选择教室');
	}
	if(name == '') {
		key = false;
		showError('#'+type+'_name', '课程名称不能为空');
	}
	if(teacher == '') {
		key = false;
		showError('#'+type+'_teacher', '任课教师不能为空');
	}
	if(num == '' || !validNum(num)) {
		key = false;
		showError('#'+type+'_num', '上课人数设置非法');
	}
	if(index_begin > index_end) {
		key = false;
		showError('#'+type+'_day', '时间设置非法');
	}
	if(weeks.length <= 0 && type == 'add') {
		key = false;
		showError('#'+type+'_table_week', '请选择上课周数');
	}
	if(orgs.length <= 0) {
		key = false;
		showError('#'+type+'_org_title', '请选择上课班级');
	}
	if(validRepeat(orgs)) {
		key = false;
		showError('#'+type+'_org_title', '班级选择重复');
	}
	return key;
}

//检查数组是否含有重复元素，有则返回true
function validRepeat(array) {
	for(var i=0;i<array.length;i++) {
		for(var j=i+1;j<array.length;j++) {
			if(array[i] == array[j]) {
				return true;
			}
		}
	}
	return false;
}

//验证数据是否数字
function validNum(str) {
	var reg = /^[0-9]+$/;
	if (!reg.test(str)) {
		return false;
	}
	return true;
}

//重置新增模态框
function resetAddModal() {
	$('#add_name').val('');
	$('#add_teacher').val('');
	$('#add_num').val('');
	
	$('.add-org-select').parent('.layui-form').remove();
}

//重置新增模态框
function resetUpdateModal() {
	$('#update_name').val('');
	$('#update_num').val('');
	$('#update_addid').attr('data-addid', null);
	$('#ztree_title_update').html('点击选择');
	$('.add-org-select').parent('.layui-form').remove();
}

//初始化课表选择下拉框(未选择)
function addTtsOption(obj) {
	var $select = $('#template_layui_select').html();
	var $option = "";
	if(tts.length > 0) {
		for(var i in tts) {
			$option = $option + normalOption(tts[i].tt_id,tts[i].tt_name);
		}
		$select = replaceParam($select, 'option', $option);
		$(obj).append($select);
		layui.form.render('select');
		
	}else {
		alert('请先前往作息表管理添加作息表！');
	}
}

//初始化课表选择下拉框(选择)
function addTtsOptionSelected(obj,ttid) {
	var $select = $('#template_layui_select').html();
	var $option = "";
	if(tts.length > 0) {
		for(var i in tts) {
			if(tts[i].tt_id == ttid){
				$option = $option + selectedOption(tts[i].tt_id,tts[i].tt_name);
			}else {
				$option = $option + normalOption(tts[i].tt_id,tts[i].tt_name);
			}
		}
		$select = replaceParam($select, 'option', $option);
		$(obj).append($select);
		layui.form.render('select');
	}else {
		alert('班级列表未加载');
	}
}

//课程添加上课班级(未选择)
function addOrgOption(obj) {
	var $org = $('#template_class').html();
	var $option = "";
	if(orgs.length > 0) {
		for(var i in orgs) {
			$option = $option + normalOption(orgs[i].ele.org_id,orgs[i].name);
		}
		$org = replaceParam($org, 'option', $option);
		$(obj).parent().before($org);
		layui.form.render('select');
	}else {
		alert('班级列表未加载');
	}
}

//课程添加上课班级(选择)
function addOrgOptionSelected(obj,orgid) {
	var $org = $('#template_class').html();
	var $option = "";
	if(orgs.length > 0) {
		for(var i in orgs) {
			if(orgs[i].ele.org_id == orgid){
				$option = $option + selectedOption(orgs[i].ele.org_id,orgs[i].name);
			}else {
				$option = $option + normalOption(orgs[i].ele.org_id,orgs[i].name);
			}
		}
		$org = replaceParam($org, 'option', $option);
		$(obj).parent().before($org);
		layui.form.render('select');
	}else {
		alert('班级列表未加载');
	}
}

//课程添加上课班级(未选择)
function addOrg(obj) {
	var $org = $('#template_org').html();
	var $option = "";
	if(orgs.length > 0) {
		for(var i in orgs) {
			$option = $option + normalOption(orgs[i].ele.org_id,orgs[i].name);
		}
		$org = replaceParam($org, 'option', $option);
		$(obj).parent().before($org);
		layui.form.render('select');
	}else {
		alert('班级列表未加载');
	}
}

//课程添加上课班级(选择)
function addSelectedOrg(obj,orgid) {
	var $org = $('#template_org').html();
	var $option = "";
	if(orgs.length > 0) {
		for(var i in orgs) {
			if(orgs[i].ele.org_id == orgid){
				$option = $option + selectedOption(orgs[i].ele.org_id,orgs[i].name);
			}else {
				$option = $option + normalOption(orgs[i].ele.org_id,orgs[i].name);
			}
		}
		$org = replaceParam($org, 'option', $option);
		$(obj).parent().before($org);
		layui.form.render('select');
	}else {
		alert('班级列表未加载');
	}
}

//课程移除上课班级
function removeOrg(obj) {
	var $org = $(obj).parent().prev('.layui-form');
	if($org) {
		$org.remove();
	}
}

$(function() {
	$('#add_btn').on('click', function() {		
		if(valiedAddEntity('add')) {
			var st_ttid = $('#add_ttid').val();
			var st_addid = $('#add_addid').attr('data-addid');
			var st_name = $('#add_name').val();
			var st_teacherid = $('#add_teacherid').val();
			var st_num = $('#add_num').val();
			var st_day = parseInt($('#add_day').val());
			var st_index_begin = parseInt($('#add_index_begin').val());
			var st_index_end = parseInt($('#add_index_end').val());
			var weeks = [];
			$('#add_modal').find('.table-week').each(function() {
				if($(this).hasClass('active')) {
					weeks.push($(this).attr('data-week'));
				}
			})
			var orgs = [];
			$('#add_modal').find('.add-org-select').each(function () {
				orgs.push($(this).val())
			})
			showLoad('.modal-content');
			$.ajax({
				url:"/cloud/studenttable/addStudentTable",
				dataType:'json',
				type:"post",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				data:{st_ttid: st_ttid,
					  st_addid:st_addid,
					  st_name:st_name,
					  st_teacherid:st_teacherid,
					  st_num:st_num,
					  day:st_day,
					  st_index_begin:st_index_begin,
					  st_index_end:st_index_end,
					  st_weeks:weeks.toString(),
					  st_orgid:orgs.toString()},
				success:function(result){
					hideLoad('.modal-content');
					if(result.code != '0000') {
						layer.msg(result.msg, {icon: 2,time:3000}); 
						return;
					}
					layer.msg("新增课程成功", {icon: 1,time:1000}); 
					$('#add_modal').modal('hide');
					forwareWeek(currentWeek);
				},
				error:function() {
					hideLoad('.modal-content');
					layer.msg('系统错误!', {icon: 2,time:3000}); 
				}
			});			
		}
	})
	
	$('#update_btn').on('click', function() {
		if(valiedAddEntity('update')) {
			var st_id = $('#update_modal').attr('data-stid');
			var st_ttid = $('#update_ttid').val();
			var st_addid = $('#update_addid').attr('data-addid');
			var st_name = $('#update_name').val();
			var st_teacherid = $('#update_teacherid').val();
			var st_num = $('#update_num').val();
			var day = $('#update_day').val();
			var st_index_begin = $('#update_index_begin').val();
			var st_index_end = $('#update_index_end').val();
			var weeks = [];
			$('#update_modal').find('.table-week').each(function() {
				if($(this).hasClass('active')) {
					weeks.push($(this).attr('data-week'));
				}
			})
			var orgs = [];
			$('#update_modal').find('.add-org-select').each(function () {
				orgs.push($(this).val())
			})
			showLoad('.modal-content');
			$.ajax({
				url:"/cloud/studenttable/updateStudentTable",
				dataType:'json',
				type:"post",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				data:{st_id:st_id,
					  st_ttid:st_ttid,
					  st_addid:st_addid,
					  st_name:st_name,
					  st_teacherid:st_teacherid,
					  st_num:st_num,
					  day:day,
					  st_index_begin:st_index_begin,
					  st_index_end:st_index_end,
					  st_weeks:weeks.toString(),
					  st_orgid:orgs.toString()},
				success:function(result){
					hideLoad('.modal-content');
					if(result.code != '0000') {
						layer.msg(result.msg, {icon: 2,time:3000}); 
						return;
					}
					layer.msg("更新课程成功", {icon: 1,time:1000}); 
					$('#update_modal').modal('hide');
					forwareWeek(currentWeek);
				},
				error:function() {
					hideLoad('.modal-content');
					layer.msg('系统错误!', {icon: 2,time:3000}); 
				}
			});		
		}
	})
	
	$('#add_index_begin').on('change', function() {
		var begin = $(this).val();
		$('#add_index_end').find('option').each(function() {
			if($(this).val() < begin) {
				$(this).attr('disabled', 'disabled');
			}else {
				$(this).attr('disabled', null);
			}
		})
		if($('#add_index_end').val() < begin) {
			$('#add_index_end').val(begin);
		}
	})
	
	$('#update_index_begin').on('change', function() {
		var begin = $(this).val();
		$('#update_index_end').find('option').each(function() {
			if($(this).val() < begin) {
				$(this).attr('disabled', 'disabled');
			}else {
				$(this).attr('disabled', null);
			}
		})
		if($('#update_index_end').val() < begin) {
			$('#update_index_end').val(begin);
		}
	})
	
	$('.table-week-check').on('click', function() {
		var type = $(this).attr('data-type');
		var $table_week = $(this).parent().prev().find('.table-week');
		if(type == 'single') {
			$table_week.each(function () {
				var week = parseInt($(this).attr('data-week'));
				if(week%2 != 0) {
					$(this).addClass('active');
				}else {
					$(this).removeClass('active');
				}
			})
		}else if(type == 'double') {
			$table_week.each(function () {
				var week = parseInt($(this).attr('data-week'));
				if(week%2 == 0) {
					$(this).addClass('active');
				}else {
					$(this).removeClass('active');
				}
			})
		}else if(type == 'check') {
			$table_week.each(function () {
				$(this).addClass('active');
			})
		}else if(type == 'uncheck') {
			$table_week.each(function () {
				$(this).removeClass('active');
			})
		}
		
	});
	
	$('.table-week').on('click', function() {
		if($(this).hasClass('active')) {
			$(this).removeClass('active');
		}else {
			$(this).addClass('active');
		}
	});
	
	$('#head_input').on('keydown', function(event) {
		if(event.keyCode == 13) {
			var week = $(this).val();
			if(validNum(week) && week != 0) {
				forwareWeek(week);
			}else {
				alert('周数不合法')
			}
		}
	})
	
	$('#searchInput').on('keydown', function(event) {
		if(event.keyCode == 13) {
			searchNodes('ztree', '#searchInput');
		}
	})
});

//未选中选项
function normalOption(value,text) {
	return '<option value="' + value + '">' + text + '</option>';
}

//选中选项
function selectedOption(value,text) {
	return '<option value="' + value + '" selected>' + text + '</option>';
}

//根据元素id初始化laydate插件
function initLayDate(ele,type) {
	if(type == 'date') {
		laydate.render({
			  elem: ele,
			  type: 'date',
			  format: 'yyyy-MM-dd'
		});
	}else if(type == 'time') {
		laydate.render({
			  elem: ele,
			  type: 'time',
			  range: '~',
			  format: 'HH:mm'
		});
	}
}

//上一周
function lastWeek() {
	if(currentWeek > 1) {
		forwareWeek(currentWeek - 1);
	}
}

//下一周
function nextWeek() {
	forwareWeek(currentWeek + 1);
}

//跳转周数
function forwareWeek(week) {
	if(week != null && week != "") {
		if(week > 25) {
			week = 25;			
		}else if(week < 1) {
			week = 1;
		}
		currentWeek = week;
		getStudentTable();
	}
}

//替换字符串中所有'{属性名}'
function replaceParam(str, param, value) {
	return str.replace(new RegExp('{' + param + '}',"gm"), value);
}