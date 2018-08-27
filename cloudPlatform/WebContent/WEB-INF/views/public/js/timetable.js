var timeNums = [1,2,3,4,5,6,7,8,9,10,11,12];
$(function() {
	layui.use([ 'form', 'laydate', 'layer' ], function() {
		var layer = layui.layer;
		var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
		var laydate = layui.laydate;

		//初始化课表初始日期选择框
		for(var i=1;i<=timeNums.length;i++) {
			initLayDate('#add_time_' + i, 'time');
			initLayDate('#update_time_' + i, 'time');
		}
	});

    vue = new Vue(
			{
				el:'#app_table',
				data: {
					timetable: [],
					terms: [],
					totalNumber:1,
					totalPage:1,
					currentPage:1,
					pageNumber:14,
					like:''
				}
			}
	)
	
	getDataByPage();
    getTerm();
})

//获取所有学期
function getTerm() {
    showLoad('.modal');
	$.ajax({
		url:"/cloud/term/query",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{},
		success:function(result){
			hideLoad('.modal');
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:1000}); 
				return;
			}
		    var terms = result.data;
		    for(var i in terms) {
		    	terms[i].term_start = millisecondToDateString(terms[i].term_start);
		    	terms[i].term_end = millisecondToDateString(terms[i].term_end);
		    }
		    vue.terms = terms;
		},
		error:function() {
			layer.msg('系统错误!', {icon: 2,time:1000}); 
		    hideLoad('.modal');
		}
	});
}

//分页查询数据
function getDataByPage() {
    showLoad('body');
	$.ajax({
		url:"/cloud/timetable/queryByPage",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{currentPage:vue.currentPage,
			  pageNumber:vue.pageNumber,
			  like:vue.like
		     },
		success:function(result){
			hideLoad('body');
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:1000}); 
				return;
			}
			
			var timetable = result.data.timetable;
			for(var i in timetable) {
				timetable[i].tt_updatedate = millisecondToDateString(timetable[i].tt_updatedate);
			}
			
			vue.timetable = timetable;
			updatePage(result.data.page);
		   
		},
		error:function() {
			layer.msg('系统错误!', {icon: 2,time:1000}); 
		    hideLoad('body');
		}
	});
}

//更新分页
function updatePage(page) {
	vue.totalNumber = page.totalNumber;
	vue.totalPage = page.totalPage;
	vue.currentPage = page.currentPage;
	vue.pageNumber = page.pageNumber;
}

//显示新增模态框
function showAddModal() {
	$('#add_modal').modal('show');
}

//根据课节数初始化课节
function numSelected(type) {
	if(type == 'add') {
		var num = $('#add_num').val();
		for(var i=1;i<=num;i++) {
			$('#add_time_' + i).parents('tr:eq(0)').fadeIn();
		}
		for(var i=parseInt(num) + 1;i<=timeNums.length;i++) {
			$('#add_time_' + i).parents('tr:eq(0)').fadeOut();
		}
	}else if(type == 'update') {
		var num = $('#update_num').val();
		for(var i=1;i<=num;i++) {
			$('#update_time_' + i).parents('tr:eq(0)').fadeIn();
		}
		for(var i=parseInt(num) + 1;i<=timeNums.length;i++) {
			$('#update_time_' + i).parents('tr:eq(0)').fadeOut();
		}
	}

}

//根据元素id初始化laydate插件
function initLayDate(ele,type) {
	if(type == 'date') {
		layui.laydate.render({
			  elem: ele,
			  type: 'date',
			  format: 'yyyy-MM-dd'
		});
	}else if(type == 'time') {
		layui.laydate.render({
			  elem: ele,
			  type: 'time',
			  range: '~',
			  format: 'HH:mm'
		});
	}
}

//验证表单数据
function validData(tt_name,tt_termid,num) {
	if(tt_name == '') {
		alert('课表名称不能为空!');
		return false;
	}else if(tt_termid == '' || tt_termid == 0) {
		alert('所属学期不能为空！');
		return false;
	}else if(num == '0') {
		alert('课节数必须大于0！');
		return false;
	}
	return true;
}

//获取新增课节
function getAddNodes(nodes) {
		var key = true;
		$('#add_table>tbody').find('tr').each(function() {
			if(!key) {
				return;
			}
			var $tr = $(this);
			if($tr.css('display') != 'none') {
				var node = {index:$tr.attr('data-index'),time:$tr.find('input').val()}
				if(node.time == '') {
					key = false;
					return;
				}
				nodes.push(node)
			}
		})
		return key;
}

//生成课节的请求参数
function createNodes(nodes) {
	var lastBegin;
	var lastEnd;
	var result = []
	for(var i in nodes) {	
		var times = nodes[i].time.split('~');
		var begin = times[0].replace(' ','');
		var end = times[1].replace(' ','');
		if(i > 0) {
			if(lastEnd > begin) {
				alert('第' + nodes[i].index + '节课时间设置不符合逻辑！');
				return false;
			}
		}
		result.push({node_index:nodes[i].index,node_start:begin,node_end:end});
		lastBegin = begin;
		lastEnd = end;
	}
	return result;
}

//添加课表
function addTimeTable() {
	var tt_name = $('#add_name').val();
	var tt_termid = $('#add_termid').val();
	var num = $('#add_num').val();
		if(validData(tt_name,tt_termid,num)) {
			var nodes = [];
			var key = getAddNodes(nodes);
			if(!key) {
				alert('有课节的上课时间未选择！');
			}else {
				var nodeArray = createNodes(nodes);
				if(nodeArray) {
					$("#add_btn").attr('disabled',true);
					showLoad('#add_modal');
	   				$.ajax({
	   					url:"/cloud/timetable/addTimeTable",
	   					dataType:'json',
	   					type:"post",
	   					contentType: "application/x-www-form-urlencoded;charset=utf-8",
	   					data:{tt_name:tt_name,
	  						  tt_termid:tt_termid,
	  						  tt_num:num,
	  						  nodes:JSON.stringify(nodeArray)},
	   					success:function(result){
	   						$("#add_btn").attr('disabled',false);
							$('#add_modal').modal('hide');
	                	    layer.msg('课表添加成功', {icon: 1,time:1000}); 
	                	    hideLoad('#add_modal');
	                	    vue.currentPage = 1;
	                	    getDataByPage();
	   					},
	   					error:function() {
	   						$("#add_btn").attr('disabled',false);
	   						$('#add_modal').hide();
	   						layer.msg('系统错误!', {icon: 2,time:1000}); 
	   						hideLoad('#add_modal');
	   					}
	   				});
				}

			}
		}
}

//编辑课表
function updateOne(obj) {
	var $tr = $(obj).parents('tr:eq(0)');
	var id = $tr.attr('data-ttid');
	var name = $tr.find('.name').text();
	var num = $tr.find('.tt-num').text();
	var termid = $tr.find('.tt-termid').attr('data-termid');
	$('#update_modal').attr('data-ttid', id);
	$('#update_name').val(name);
	$('#update_num').val(num);
	$('#update_termid').find("option").attr('selected', null);
	$('#update_termid').find("option[value='" + termid + "']").attr('selected', '');
	layui.form.render('select');
		$.ajax({
   			url:"/cloud/timetable/getNodeByttId",
   			dataType:'json',
   			type:"post",
   			contentType: "application/x-www-form-urlencoded;charset=utf-8",
   			data:{id:id},
   			success:function(result){
   				if(result.code != '0000') {
   					layer.msg(result.msg, {icon: 2,time:1000}); 
   					return;
   				}
				initUpdateNode(result.data);
   			},
   			error:function() {
   				alert('系统错误')
   			}
   		});
	
	$('#update_modal').modal('show');
}

//初始化编辑课表的课节
function initUpdateNode(nodes) {
	$('#update_table>tbody>tr').hide();
	for(var i in nodes) {
		var index = nodes[i].node_index;
		var $tr = $('#update_table>tbody').find('tr[data-index=' + index + ']');
		$tr.show();
		$tr.find('input').val(nodes[i].node_start + ' ~ ' + nodes[i].node_end);
	}
}

//更新课表
function updateTimeTable() {
	var id = $('#update_modal').attr('data-ttid');
	var name = $('#update_name').val();
	var termid = $('#update_termid').val();
	var num = $('#update_num').val();
	var nodes = [];
	nodes = createUpdateNodes(nodes, id);
	if(validData(name,termid,num)) {
		if(!nodes) {
			alert('有课节的上课时间未选择！');
			return;
		}
		if(validNodes(nodes)) {
			$('#update_modal').modal('show');
			$.ajax({
				url:"/cloud/timetable/updateTimeTable",
				dataType:'json',
				type:"post",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				data:{tt_id:id,
					  tt_name:name,
				      tt_termid:termid,
				      tt_num:num,
				      nodes:JSON.stringify(nodes)},
				success:function(result){
	   				if(result.code != '0000') {
	   					layer.msg(result.msg, {icon: 2,time: 2000}); 
	   					return;
	   				}
					$('#update_modal').modal('hide');
             	    layer.msg('课表更新成功', {icon: 1,time: 2000}); 
             	    hideLoad('#update_modal');
             	    getDataByPage();
				},
				error:function() {
					$('#update_modal').hide();
					layer.msg('系统错误!', {icon: 2,time: 2000}); 
					hideLoad('#update_modal');
				}
			});
		}
	}
}

function createUpdateNodes(nodes,ttid) {
	$('.update-tr').each(function() {
		if($(this).css('display') != 'none') {
			var index = $(this).attr('data-index');
			
			var time = $(this).find('input').val();
			if(time == '') {
				return false;
			}
			var timeArray = time.split(' ~ ');
			var begin = timeArray[0];
			var end = timeArray[1];
			
			var node = {node_index:index,node_start:begin,node_end:end,node_ttid:ttid};
			nodes.push(node);
		}
	})
	return nodes;
}

//检查课节是否合法
function validNodes(nodes) {
	var lastBegin;
	var lastEnd;
	for(var i in nodes) {	
		var begin = nodes[i].node_start;
		var end = nodes[i].node_end;
		if(i > 0) {
			if(lastEnd > begin) {
				alert('第' + nodes[i].index + '节课时间设置不符合逻辑！');
				return false;
			}
		}
		lastBegin = begin;
		lastEnd = end;
	}
	if(nodes.length <= 0) {
		return false;
	}else {
		return true;
	}
}

//删除数据确认
function deleteConfirm(ids) {
	 layer.confirm('确定删除课表吗？', {
 	    btn: ['确定','取消'], //按钮
 	    shade: false //不显示遮罩
	       	}, function(){
	       		$.ajax({
	       			url:"/cloud/timetable/deleteTimeTable",
	       			dataType:'json',
	       			type:"post",
	       			contentType: "application/x-www-form-urlencoded;charset=utf-8",
	       			data:{ids:ids},
	       			success:function(result){
	       				if(result.code != '0000') {
	       					layer.msg(result.msg, {icon: 2,time:1000}); 
	       					return;
	       				}
	       	    	    layer.msg('删除课表成功', {icon: 1,time:1000}); 
	       	    	    getDataByPage();
	       	    	 	resetCheckbox()
	       			},
	       			error:function() {
	       				layer.msg('删除课表失败', {icon: 2,time:1000});
	       			}
	       		});
  		});
}

//删除单条数据
function deleteOne(obj) {
	var ttid = $(obj).parents('tr:eq(0)').attr('data-ttid');
	deleteConfirm(ttid);
}

//删除选中数据
function deleteChecked() {
	var ids = "";
	$('.table-mailbox>tbody').find('input[type="checkbox"]').each(function() {
		var checked = $(this).is(':checked');
		if(checked) {
			var id = $(this).parents('tr:eq(0)').attr('data-ttid');
			ids = ids + id + ',';
		}
	})
	if(ids == '') {
		alert('请先选中需要删除的课表');
		return;
	}else {
		ids = ids.substring(0, ids.length - 1);
		deleteConfirm(ids)
	}
}

//课表预览
function view(obj) {
	var $tr = $(obj).parents('tr:eq(0)');
	var ttId = $tr.attr('data-ttid');
	var tt_name = $tr.find('.name').html();
	$('#viewName').html(tt_name);
    var tt_num = $tr.find('.tt-num').html();
    for(var j = 1;j <= timeNums.length;j++){
    	$('#view_tr_'+j).empty();
    }
    
	$.ajax({
			url:"/cloud/timetable/getNodeByttId",
			dataType:'json',
			type:"post",
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data:{id:ttId},
			success:function(result){
				if(result.code != '0000') {
					layer.msg(result.msg, {icon: 2,time:1000}); 
					return;
				}
				var nodes = result.data;
			    for(var i = 0;i < nodes.length;i++){
			    	var start = nodes[i].node_start;
			    	var end = nodes[i].node_end;
			    	$('#view_tr_'+(i+1)).empty();
			    	$('#view_tr_'+(i+1)).append("<td class='trView tdFont'>第"+nodes[i].node_index+"节 "+start+"~"+end+"</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>");
			    }
				$('#view').modal('show');
			},
			error:function() {
				alert('系统错误')
			}
	});
}

//重置checkbox
function resetCheckbox() {
	$('.table-mailbox').find('input[type="checkbox"]').prop("checked",false);
}

$(function() {
	$('#add_btn').on('click', function() {
		addTimeTable();
	})
	
	$('#update_btn').on('click', function() {
		updateTimeTable();
	})
	
	$('#like').on('keydown', function(event) {
		if(event.keyCode == 13) {
			getDataByPage();
		}
	})
	
	$('.input-forware').on('keydown', function(event) {
		if(event.keyCode == 13) {
			forwareByInput();
		}
	})
})

function onCheck(obj) {
	var checked = $(obj).is(':checked');
	$('.table-mailbox').find('input[type="checkbox"]').prop("checked",checked);
}

//分页操作
function forwarePage(type) {
	if(type == 'first') {
		vue.currentPage = 1;
	}else if(type == 'end') {
		vue.currentPage = vue.totalPage;
	}else if(type == 'next') {
		if(vue.currentPage >= vue.totalPage) {
			alert('已经是最后一页了');
			return;
		}
		vue.currentPage = vue.currentPage + 1;
	}else if(type == 'last') {
		if(vue.currentPage <= 1) {
			alert('已经是首页了');
			return;
		}
		vue.currentPage = vue.currentPage - 1;
	}
	getDataByPage(vue.currentPage, vue.pageNumber, vue.like);
}

function forwareByInput() {
	if(validNum($('.input-forware').val())) {
		vue.currentPage = $('.input-forware').val();
		getDataByPage($('.input-forware').val(), vue.pageNumber, vue.like);
	}else {
		alert('请输入正整数页码')
	}
}

//验证数据是否数字
function validNum(str) {
	var reg = /^[1-9][0-9]*$/;
	if (!reg.test(str)) {
		return false;
	}
	return true;
}

//毫秒转字符串(yyyy-MM-dd)
function millisecondToDateString(millisecond) {
	return new Date(millisecond).format('yyyy-MM-dd');
}

//日期字符串转日期
function dateStringToDate(str) {
	return new Date(str.replace(/-/g, "/"));
}

//替换字符串中所有'{属性名}'
function replaceParam(str, param, value) {
	return str.replace(new RegExp('{' + param + '}',"gm"), value);
}

Date.prototype.format = function(format)
{
 var o = {
 "M+" : this.getMonth()+1, //month
 "d+" : this.getDate(),    //day
 "h+" : this.getHours(),   //hour
 "m+" : this.getMinutes(), //minute
 "s+" : this.getSeconds(), //second
 "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
 "S" : this.getMilliseconds() //millisecond
 }
 if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
 (this.getFullYear()+"").substr(4 - RegExp.$1.length));
 for(var k in o)if(new RegExp("("+ k +")").test(format))
 format = format.replace(RegExp.$1,
 RegExp.$1.length==1 ? o[k] :
 ("00"+ o[k]).substr((""+ o[k]).length));
 return format;
}