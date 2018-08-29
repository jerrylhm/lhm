var timeNums = [1,2,3,4,5,6,7,8,9,10,11,12];

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

$(function() {

	//初始化课表初始日期选择框
	initLayDate('#add_start', 'date');
	initLayDate('#add_end', 'date');
	initLayDate('#update_start', 'date');
	initLayDate('#update_end', 'date');
	
	
    vue = new Vue(
			{
				el:'#app_table',
				data: {
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
})

//分页查询数据
function getDataByPage() {
    showLoad('body');
	$.ajax({
		url:"/cloud/term/queryByPage",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{currentPage:vue.currentPage,
			  pageNumber:vue.pageNumber,
			  like:vue.like
		     },
		success:function(result){
			hideLoad('body');
			var terms = result.data.terms;
			for(var i in terms) {
				terms[i].term_start = millisecondToDateString(terms[i].term_start);
				terms[i].term_end = millisecondToDateString(terms[i].term_end);
			}
			
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:2000}); 
				return;
			}
			vue.terms = result.data.terms;
			updatePage(result.data.page);
		},
		error:function() {
			layer.msg('系统错误!', {icon: 2,time:2000}); 
		    hideLoad('body');
		}
	});
}

//毫秒转字符串(yyyy-MM-dd)
function millisecondToDateString(millisecond) {
	return new Date(millisecond).format('yyyy-MM-dd');
}

//日期字符串转日期
function dateStringToDate(str) {
	return new Date(str.replace(/-/g, "/"));
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

//验证表单数据
function validData(term_name,add_start,term_end) {
	if(term_name == '') {
		alert('学期名称不能为空!');
		return false;
	}else if(add_start == '') {
		alert('开学日期不能为空！');
		return false;
	}else if(term_end == '') {
		alert('结束日期不能为空');
		return false;
	}else if(term_end <= add_start) {
		alert('结束日期必须大于开学日期');
		return false;
	}
	return true;
}

//添加课表
function add() {
	var term_name = $('#add_name').val();
	var term_start = $('#add_start').val();
	var term_end = $('#add_end').val();
	if(validData(term_name,term_start,term_end)) {
		ajaxAddTerm(term_name, term_start, term_end);
	}
}

function ajaxAddTerm(term_name, term_start, term_end) {
	showLoad('#add_modal');
	$.ajax({
			url:"/cloud/term/addTerm",
			dataType:'json',
			type:"post",
			contentType: "application/x-www-form-urlencoded;charset=utf-8",
			data:{term_name:term_name,
				  term_start:dateStringToDate(term_start),
				  term_end:dateStringToDate(term_end)},
			success:function(result){
				hideLoad('#add_modal');
				if(result.code != '0000') {
					layer.msg(result.msg, {icon: 2,time:2000}); 
					return;
				}
				getDataByPage();
				$('#add_modal').modal('hide');
				layer.msg("新增学期成功", {icon: 1,time:2000}); 
			},
			error:function() {
				hideLoad('#add_modal');
				layer.msg("系统错误！", {icon: 2,time:2000}); 
			}
		});
}
//编辑课表
function updateOne(obj) {
	var $tr = $(obj).parents('tr:eq(0)');
	var id = $tr.attr('data-id');
	var term_name = $tr.find('.term-name').text();
	var term_start = $tr.find('.term-start').text();
	var term_end = $tr.find('.term-end').text();
	
	$('#update_modal').attr('data-id', id);
	$('#update_name').val(term_name);
	$('#update_start').val(term_start);
	$('#update_end').val(term_end);
	$('#update_modal').modal('show');
}

function update() {
	var term_id = $('#update_modal').attr('data-id');
	var term_name = $('#update_name').val();
	var term_start = $('#update_start').val();
	var term_end = $('#update_end').val();
	
	if(validData(term_name,term_start,term_end)) {
		ajaxUpdateTerm(term_id, term_name, term_start, term_end);
	}
}

//ajax更新数据
function ajaxUpdateTerm(term_id, term_name, term_start, term_end) {
	showLoad('#update_modal');
	$.ajax({
		url:"/cloud/term/updateTerm",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{term_id: term_id,
			  term_name: term_name,
			  term_start: dateStringToDate(term_start),
			  term_end: dateStringToDate(term_end)},
		success:function(result){
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:2000}); 
				return;
			}
			getDataByPage();
			hideLoad('#update_modal');
			layer.msg("更新学期成功！", {icon: 1,time:2000});
			$('#update_modal').modal('hide');
		},
		error:function() {
			hideLoad('#update_modal');
			layer.msg('系统出错', {icon: 2,time:2000}); 
		}
	});
}

//删除数据确认
function deleteConfirm(ids) {
	 layer.confirm('确定删除课表吗？', {
 	    btn: ['确定','取消'], //按钮
 	    shade: false //不显示遮罩
	       	}, function(){
	       		$.ajax({
	       			url:"/cloud/term/deleteTerm",
	       			dataType:'json',
	       			type:"post",
	       			contentType: "application/x-www-form-urlencoded;charset=utf-8",
	       			data:{ids:ids},
	       			success:function(result){
	       				if(result.code != '0000') {
	       					layer.msg(result.msg, {icon: 2,time:1000}); 
	       					return;
	       				}
	       	    	    layer.msg('批量删除学期成功!', {icon: 1,time:1000}); 
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
	var id = $(obj).parents('tr:eq(0)').attr('data-id');
	deleteConfirm(id);
}

//删除选中数据
function deleteChecked() {
	var ids = "";
	$('.table-mailbox>tbody').find('input[type="checkbox"]').each(function() {
		var checked = $(this).is(':checked');
		if(checked) {
			var id = $(this).parents('tr:eq(0)').attr('data-id');
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

//重置checkbox
function resetCheckbox() {
	$('.table-mailbox').find('input[type="checkbox"]').prop("checked",false);
}

$(function() {
	$('#add_btn').on('click', function() {
		add();
	})
	
	$('#update_btn').on('click', function() {
		update();
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

//替换字符串中所有'{属性名}'
function replaceParam(str, param, value) {
	return str.replace(new RegExp('{' + param + '}',"gm"), value);
}