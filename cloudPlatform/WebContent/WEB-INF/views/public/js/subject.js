$(function() {
	layui.use('form', function() {
		layui.form.on('select(major)', function(data){
			  vue.major = data.value;
			  getDataByPage();
		});
	});
	
    vue = new Vue(
			{
				el:'#app_table',
				data: {
					subject: [],
					orgs: [],
					totalNumber:1,
					totalPage:1,
					currentPage:1,
					pageNumber:14,
					like:'',
					major: null
				}
			}
	)
    hideLoad('.content');
	$.ajax({
		url:"/cloud/organization/getOrgByType",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{type: 2
		     },
		success:function(result){
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:1000}); 
				return;
			}
			vue.orgs = result.data;
		    hideLoad('.content');
		},
		error:function() {
			layer.msg('系统错误!', {icon: 2,time:1000}); 
		    hideLoad('.content');
		}
	});
    
    getDataByPage();
    
})

function view(obj) {
	var $tr = $(obj).parents('tr:eq(0)');
	var name = $tr.find('.name').html();
	var type = $tr.find('.type').html();
	var view_major = "";
	$tr.find('.major').find('span').each(function() {
		view_major = view_major + $(this).html();
	});
	$('#view_name').html(name);
	$('#view_type').html(type);
	$('#view_major').html(view_major);
	$('#view').modal('show');
}

//显示新增模态框
function showAddModal() {
	$('#add_modal').modal('show');
}

//显示专业选择
function showSubMajor(type) {
	$('#' + type + '_major').show();
}

//隐藏专业选择
function hideSubMajor(type) {
	$('#' + type + '_major').hide();
}

//更新单条数据
function updateOne(obj) {
	$('#update_modal').find('.layui-form').remove();
	var $tr = $(obj).parents('tr:eq(0)');
	var id = $tr.attr('data-subid');
	var name = $tr.find('.name').text();
	var orgId = $tr.find('.major').attr('data-orgid');
	var type = $tr.find('.type').attr('data-type');
	$('#update_modal').attr('data-subid', id);
	$('#update_name').val(name);
	$('#update_type').val(type);
	$('#update_type').trigger('change');
	
	var $addItemBtn = $('#update_modal').find('.layui-btn-group>button:eq(0)');
	if(orgId != null && orgId != "") {
		var orgArray = orgId.split(',');
		for(var i in orgArray) {
			if(orgArray[i] != "") {	
				addSelectedOrg($addItemBtn, orgArray[i]);
			}
		}
	}
	$('#update_modal').modal('show');
}

//删除数据确认
function deleteConfirm(ids) {
	 layer.confirm('确定删除该学科吗？', {
 	    btn: ['确定','取消'], //按钮
 	    shade: false //不显示遮罩
	       	}, function(){
	       		$.ajax({
	       			url:"/cloud/subject/deleteSubject",
	       			dataType:'json',
	       			type:"post",
	       			contentType: "application/x-www-form-urlencoded;charset=utf-8",
	       			data:{ids:ids},
	       			success:function(result){
	       				if(result.code != '0000') {
	       					layer.msg(result.msg, {icon: 2,time:1000}); 
	       					return;
	       				}
	       	    	    layer.msg('删除学科成功', {icon: 1,time:1000}); 
	       	    	    getDataByPage();
	       	    	 	resetCheckbox()
	       			},
	       			error:function() {
	       				layer.msg('删除学科失败', {icon: 2,time:1000});
	       			}
	       		});
  		});
}

//删除单条数据
function deleteOne(obj) {
	var id = $(obj).parents('tr:eq(0)').attr('data-subid');
	deleteConfirm(id);
}

//删除选中数据
function deleteChecked() {
	var ids = "";
	$('.table-mailbox>tbody').find('input[type="checkbox"]').each(function() {
		var checked = $(this).is(':checked');
		if(checked) {
			var id = $(this).parents('tr:eq(0)').attr('data-subid');
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

//学科添加所属专业(未选择)
function addOrg(obj) {
	var $org = $('#template_org').html();
	var $option = "";
	if(vue.orgs.length > 0) {
		for(var i in vue.orgs) {
			$option = $option + normalOption(vue.orgs[i].organization.org_id,vue.orgs[i].organization.org_name);
		}
		$org = replaceParam($org, 'option', $option);
		$(obj).parent().before($org);
		layui.form.render('select');
	}else {
		alert('班级列表未加载');
	}
}

//学科添加所属专业(选择)
function addSelectedOrg(obj,orgid) {
	var $org = $('#template_org').html();
	var $option = "";
	if(vue.orgs.length > 0) {
		for(var i in vue.orgs) {
			if(vue.orgs[i].organization.org_id == orgid){
				$option = $option + selectedOption(vue.orgs[i].organization.org_id,vue.orgs[i].organization.org_name);
			}else {
				$option = $option + normalOption(vue.orgs[i].organization.org_id,vue.orgs[i].organization.org_name);
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
//未选中选项
function normalOption(value,text) {
	return '<option value="' + value + '">' + text + '</option>';
}

//选中选项
function selectedOption(value,text) {
	return '<option value="' + value + '" selected>' + text + '</option>';
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

//分页查询数据
function getDataByPage() {
    showLoad('body');
	$.ajax({
		url:"/cloud/subject/queryByPage",
		dataType:'json',
		type:"post",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		data:{currentPage:vue.currentPage,
			  pageNumber:vue.pageNumber,
			  like:vue.like,
			  majorId: vue.major
		     },
		success:function(result){
			if(result.code != '0000') {
				layer.msg(result.msg, {icon: 2,time:1000}); 
				return;
			}
			console.log(result)
			vue.subject = result.data.subject;
			updatePage(result.data.page);
		    hideLoad('body');
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

//检查课程表单空值
function valiedAddEntity(type) {
	cleanError();
	var key = true;
	var name = $('#'+type+'_name').val();
	var sub_type = $('#'+type+'_type').val();
	var orgs = [];
	$('#'+type+'_modal').find('.add-org-select').each(function () {
		orgs.push($(this).val())
	})
	
	if(name == '') {
		key = false;
		showError('#'+type+'_name', '课程名称不能为空');
	}
	if(sub_type == '') {
		key = false;
		showError('#'+type+'_type', '请选择学科类型');
	}
	if(orgs.length <= 0 && sub_type == 1) {
		key = false;
		showError('#'+type+'_org_title', '请选择所属专业');
	}
	if(validRepeat(orgs)) {
		key = false;
		showError('#'+type+'_org_title', '专业选择重复');
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

//重置checkbox
function resetCheckbox() {
	$('.table-mailbox').find('input[type="checkbox"]').prop("checked",false);
}

$(function() {
	$('#add_btn').on('click', function() {
		if(valiedAddEntity('add')) {
			var name = $('#add_name').val();
			var sub_type = $('#add_type').val();
			var orgs = [];
			$('#add_modal').find('.add-org-select').each(function () {
				orgs.push($(this).val())
			})
			showLoad('.modal-content');
			$.ajax({
				url:"/cloud/subject/addSubject",
				dataType:'json',
				type:"post",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				data:{sub_name: name,
					  sub_type: sub_type,
					  orgs: orgs.toString()},
				success:function(result){
					hideLoad('.modal-content');
					if(result.code != '0000') {
						layer.msg(result.msg, {icon: 2,time:3000}); 
						return;
					}
					layer.msg("新增课程成功", {icon: 1,time:1000}); 
					$('#add_modal').modal('hide');
             	    getDataByPage();
				},
				error:function() {
					hideLoad('.modal-content');
					layer.msg('系统错误!', {icon: 2,time:3000}); 
				}
			});	
		}
	});
	
	$('#update_btn').on('click', function() {
		if(valiedAddEntity('update')) {
			var id = $('#update_modal').attr('data-subid');
			var name = $('#update_name').val();
			var sub_type = $('#update_type').val();
			var orgs = [];
			$('#update_modal').find('.add-org-select').each(function () {
				orgs.push($(this).val());
			})
			showLoad('.modal-content');

			$.ajax({
				url:"/cloud/subject/updateSubject",
				dataType:'json',
				type:"post",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",
				data:{sub_id: id,
					  sub_name: name,
					  sub_type: sub_type,
					  orgs: orgs.toString()},
				success:function(result){
					hideLoad('.modal-content');
					if(result.code != '0000') {
						layer.msg(result.msg, {icon: 2,time:3000}); 
						return;
					}
					layer.msg("新增课程成功", {icon: 1,time:1000}); 
					$('#update_modal').modal('hide');
             	    getDataByPage();
				},
				error:function() {
					hideLoad('.modal-content');
					layer.msg('系统错误!', {icon: 2,time:3000}); 
				}
			});	
		}
	})
	
	$('#add_type').on('change', function() {
		if($(this).val() == 1) {
			showSubMajor('add');
		}else {
			hideSubMajor('add');
		}
	});
	
	$('#update_type').on('change', function() {
		if($(this).val() == 1) {
			showSubMajor('update');
		}else {
			hideSubMajor('update');
		}
	});
	
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

//全选/全不选按钮
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
	getDataByPage();
}

//根据跳转页码输入框的值跳转到指定页码
function forwareByInput() {
	if(validNum($('.input-forware').val())) {
		vue.currentPage = $('.input-forware').val();
		getDataByPage();
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