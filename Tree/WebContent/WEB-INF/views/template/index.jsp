<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!--360浏览器优先以webkit内核解析-->


<title>模板管理</title>

<link rel="shortcut icon" href="favicon.ico">
<link href="<%=basePath%>hAdmin/css/bootstrap.min.css?v=3.3.6"
	rel="stylesheet">
<link href="<%=basePath%>font-awesome/css/font-awesome.css?v=4.4.0"
	rel="stylesheet">

<link href="<%=basePath%>hAdmin/css/animate.css" rel="stylesheet">
<link href="<%=basePath%>hAdmin/css/style.css?v=4.1.0" rel="stylesheet">
<link href="<%=basePath %>selected/jquery.searchableSelect.css" rel="stylesheet" type="text/css">

<style type="text/css">
.line {
height: auto;
} 
iframe {
  border: none;
}

.searchable-select-dropdown {
  z-index:100;
}
.attr-name {
  font-size: 10px;color: #a4a5a7;font-weight: 500;
}
.attr-value {
  font-size: 10px;color: #131212;
}
.add-template-item1-model,.add-template-item2-model,.update-template-item1-model,.update-template-item2-model,.template-option-model,.update-template-option-model {
  display: none;
}
.container-table {
  border: 1px solid #ccc;
  padding: 0px 15px;
  border-radius: 5px; 
}
.page-input {
    width: 30px;
    height: 19px;
    padding-left: 2px;
    padding-right: 2px;
}
</style>
<% 
JSONObject attrJSON = new JSONObject();
attrJSON.put("text", "文本");
attrJSON.put("enum", "枚举");
attrJSON.put("boolean", "布尔型");
attrJSON.put("file", "文件");
%>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox">
                    <div class="ibox-content">
                      <div class="row">
                         <div class="col-sm-10"><h2>模板管理</h2></div>
                         <div class="col-sm-2">
	                            <a id=add_modal_button class="btn btn-lg btn-success btn-outline">
	                            + 添加模板
	                        	</a>
                        </div>
                      </div>
                                              
                        <div class="input-group" style="margin-top: 10px;">
                             <form action="<%=basePath %>template/index" id="searchPage" method="post">
                                 <input type="hidden" name="page.currentPage" id="currentPage" value="${searchParam.page.currentPage}">
                                 <input type="hidden" name="page.totalPage" id="totalPage" value="${searchParam.page.totalPage}"> 	
                                 <span id="likeBox" style="display: none">${like }</span>
                                 <input id="input_search" type="text" name="like" placeholder="输入模板名称查找模板" value="${like }" class="input form-control">
                             </form>                          
                            <span class="input-group-btn">
                                        <button type="button" class="btn btn btn-primary" onclick="searchByLike()"> <i class="fa fa-search"></i> 搜索</button>
                            </span>
                        </div>
                        <div class="clients-list">
                            <div class="tab-content">
                                <div id="tab-1" class="tab-pane active">
                                    <div class="slimScrollDiv" style="position: relative; width: auto; height: 100%;"><div class="full-height-scroll" style="width: auto; height: 100%;">
                                        <div class="container-table">
	                                        <div class="table-responsive">
	                                            <table class="table table-striped table-hover">
	                                                <thead>
	                                                  <tr><td style="width: 40%">模板名称</td><td style="width: 30%">所属树</td><td>操作</td></tr>
	                                                </thead>
	                                                <tbody>  
	                                                   <c:forEach items="${templates }" var="t">                                               
		                                                    <tr id=template_${t.tp_id}>
		                                                        <td class="td_name"> ${t.tp_name }</td>
		                                                        <td class="td_tree_id" style="display: none;"> ${t.tp_treeid }</td>
		                                                        <td class="td_tree_name"> ${t.treeName }</td>
		                                                        <td><a class="btn btn-info btn-rounded" onclick="updateTemplate(${t.tp_id})">编辑</a><a class="btn btn-danger btn-rounded" style="margin-left: 15px;" onclick="deleteTemplate(${t.tp_id})">删除</a></td>
		                                                    </tr>
	                                                   </c:forEach>  
	                                                </tbody>
	                                            </table>
	                                            <div class="clearfix container-page">
			<div class="pull-right">
				<ul id="page" class="pagination pagination-sm" style="cursor: pointer;">
					<li>
						<a>第<span style="color:red">${searchParam.page.currentPage}</span>/${searchParam.page.totalPage } 页</a>
					</li>
					<li id="li_first">
						<a onclick="searchPage(1)" title="首页">首页</a>
					</li>
					<li id="li_pre">
						<a onclick="searchPage(${searchParam.page.currentPage-1})" title="上一页">上一页</a>
					</li>
					<li id="li_next">
						<a onclick="searchPage(${searchParam.page.currentPage+1})" title="下一页">下一页</a>
					</li>
					<li id="li_last">
						<a onclick="searchPage(${searchParam.page.totalPage })" title="尾页">尾页</a>
					</li>
					<li>
						<a style="height : 27px;">跳转到
							<input class="page-input" type="text" id="input_page"> 页
							<span onclick="forware(this)" title="跳转"><i class="fa fa-send fa-fw"></i></span>
						</a>
					</li>
				</ul>
			</div>
		</div>
	                                        </div>
                                        </div>
                                    </div><div class="slimScrollBar" style="width: 4px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 7px; z-index: 99; right: 1px; height: 365.112px; background: rgb(0, 0, 0);"></div><div class="slimScrollRail" style="width: 4px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; opacity: 0.2; z-index: 90; right: 1px; background: rgb(51, 51, 51);"></div></div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
            
        </div>
    </div>

	<!--新增模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="addModal">
	 	<div class="modal-dialog modal-lg">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>新增模板</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
                    <div class="form-group row">
		 			    <label class="col-sm-2">模板名称：</label>
		 				<div class="col-sm-6">
		 						<input id="add_template_name" type="text" placeholder="输入模板名称" class="form-control">
		 				</div>
	 				</div>
	 				 <div class="form-group row">
		 			    <label class="col-sm-2">所属树：</label>
		 				<div class="col-sm-6">
		 				      <select id="add_template_tree">
		 						<c:forEach items="${trees }" var="tree">
		 						     <option value="${tree.node_id }">${tree.node_name }</option>
		 						</c:forEach>
		 					  </select>
		 				</div>
	 				</div>
	 				<div class="form-group row add_template-space" style="border: 1px solid #ccc;margin-right: 15px;">
	 				    <div style="margin: 10px 10px;">
	 				        <div class="row add-template-item1-model" style="margin-bottom: 10px;">
	 				        <div class="col-sm-3"><span class="attr-name">属性名称:</span><span class="template-name attr-value"></span></div>
	 				        <div class="col-sm-2"><span class="attr-name">类型:</span><span class="template-type-name attr-value"></span></div>
	 				        <input type="hidden" class="template-type">
	 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-danger delete-template-button">删除</button></div>
	 				        </div>
	 				        <div class="row add-template-item2-model" style="margin-bottom: 10px;">
	 				        <div class="col-sm-3"><span class="attr-name">属性名称:</span><span class="template-name attr-value"></span></div>
	 				        <div class="col-sm-2"><span class="attr-name">类型:</span><span class="template-type-name attr-value"></span></div>
	 				        <div class="col-sm-5"><span class="attr-name">可选项:</span><span class="template-option attr-value"></span></div>
	 				        <input type="hidden" class="template-type">
	 				        <div class="col-sm-2"><button type="button" class="btn btn-outline btn-danger delete-template-button">删除</button></div>
	 				        </div>
	 				        <button type="button" class="btn btn-outline btn-success" id=add_template_button>+ 添加模板属性</button>
	 				    </div>
	 				</div>
	 				
		 		<div class="modal-footer">
		 			<button type="button" id="addNodeBtn" class="btn btn-sm btn-success" onclick="addTemplate()">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 </div>
	 <!--更新模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="updateModal">
	 	<div class="modal-dialog modal-lg">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>更新模板</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
                    <div class="form-group row">
		 			    <label class="col-sm-2">模板名称：</label>
		 				<div class="col-sm-6">
		 						<input id="update_template_name" type="text" placeholder="输入模板名称" class="form-control">
		 				</div>
		 				<div class="col-sm-4">
		 						<button type="button" class="btn btn-outline btn-success" id=update_name_button>修改名称</button>
		 				</div>
	 				</div>
	 				 <div class="form-group row">
		 			    <label class="col-sm-2">所属树：</label>
		 				<div class="col-sm-6">
		 				      <span id="update_template_tree"></span>
		 				</div>
	 				</div>
	 				<div class="form-group row update_template-space" style="border: 1px solid #ccc;margin-right: 15px;">
	 				    <div style="margin: 10px 10px;">
	 				        <div class="row update-template-item1-model" style="margin-bottom: 10px;">
	 				        <div class="col-sm-3"><span class="attr-name">属性名称:</span><span class="template-name attr-value"></span></div>
	 				        <div class="col-sm-2"><span class="attr-name">类型:</span><span class="template-type-name attr-value"></span></div>
	 				        <input type="hidden" class="template-type">
	 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-info edit-template-button">编辑</button></div>
	 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-danger update-delete-button">删除</button></div>
	 				        </div>
	 				        <div class="row update-template-item2-model" style="margin-bottom: 10px;">
	 				        <div class="col-sm-3"><span class="attr-name">属性名称:</span><span class="template-name attr-value"></span></div>
	 				        <div class="col-sm-2"><span class="attr-name">类型:</span><span class="template-type-name attr-value"></span></div>
	 				        <div class="col-sm-4"><span class="attr-name">可选项:</span><span class="template-option attr-value"></span></div>
	 				        <input type="hidden" class="template-type">
	 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-info edit-template-button">编辑</button></div>
	 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-danger update-delete-button">删除</button></div>
	 				        </div>
	 				        <button type="button" class="btn btn-outline btn-success" id=update_template_button>+ 添加模板属性</button>
	 				    </div>
	 				</div>
	 				<input type="hidden" id=update_template_id>
		 	</div>
	 	</div>
	 </div>
	 </div>
	 <!--新增模板模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="add_template_modal" style="margin-top: 80px;">
	 	<div class="modal-dialog ">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>新增模板属性</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	 			    <input type="hidden" id=action_type>
	 				<div class="form-group row">
		 			    <label class="col-sm-3">属性名称：</label>
		 				<div class="col-sm-6">
		 						<input id="attr_name" type="text" placeholder="输入属性名称" class="form-control">
		 				</div>
	 				</div>
	 				<div class="form-group row">
	 				   <label class="col-sm-3">属性类型：</label>
	 				   <select id="attr_type"  style="margin-left: 15px;">
	 				       <option value="text">文本</option>	
	 				       <option value="enum">枚举</option>
	 				       <option value="boolean">布尔型</option>
	 				       <option value="file">文件</option>
	 				   </select> 
	 			    </div>
	 			    <div class="form-group row" id=template_option_group style="display: none;">
	 				   <label class="col-sm-3">可选项：</label>
	 				   <div class="col-sm-6 template-option-space" style="border: 1px solid #ccc;">
		 						<div class="row template-option-model" style="margin: 10px 0px;">
		 						  <div class="col-sm-8"> 
                                   <input type="text" placeholder="输入选项名称" class="form-control template-option-name">
                                  </div>
                                  <div class="col-sm-2"> 
                                   <div class="col-sm-2"><button type="button" class="btn btn-outline btn-danger template-option-button-delete" >删除</button></div>
                                  </div>
				 			    </div> 
				 			    <div class="row" style="margin: 10px 0px;">
				 			    <button type="button" class="btn btn-outline btn-success" id=template_option_button_add>+ 添加</button>
				 			    </div>
		 				</div>
	 			    </div>
		 		</div>
		 		
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" id=edit_add_template>添加</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
     <!--更新模板模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="update_template_modal" style="margin-top: 80px;">
	 	<div class="modal-dialog ">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>更新模板属性</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	 			    <input type="hidden" id=action_type>
	 				<div class="form-group row">
		 			    <label class="col-sm-3">属性名称：</label>
		 				<div class="col-sm-6">
		 						<input id="update_attr_name" type="text" placeholder="输入属性名称" class="form-control">
		 				</div>
	 				</div>
	 				<div class="form-group row">
	 				   <label class="col-sm-3">属性类型：</label>
	 				   <select id="update_attr_type"  style="margin-left: 15px;">
	 				      <c:forEach items="<%=attrJSON%>" var="attr">
	 				       <option value="${attr.getKey() }">${attr.getValue() }</option>	
	 				      </c:forEach>
	 				   </select> 
	 			    </div>
	 			    <div class="form-group row" id=update_template_option_group style="display: none;">
	 				   <label class="col-sm-3">可选项：</label>
	 				   <div class="col-sm-6 update-template-option-space" style="border: 1px solid #ccc;">
		 						<div class="row update-template-option-model" style="margin: 10px 0px;">
		 						  <div class="col-sm-8"> 
                                   <input type="text" placeholder="输入选项名称" class="form-control template-option-name">
                                  </div>
                                  <div class="col-sm-2"> 
                                   <div class="col-sm-2"><button type="button" class="btn btn-outline btn-danger template-option-button-delete" >删除</button></div>
                                  </div>
				 			    </div> 
				 			    <div class="row" style="margin: 10px 0px;">
				 			    <button type="button" class="btn btn-outline btn-success" id=update_template_option_button_add>+ 添加</button>
				 			    </div>
		 				</div>
	 			    </div>
		 		</div>
		 		<input type="hidden" id=attrOldName>
		 		<input type="hidden" id=attrOldType>
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="updateTemplateAttr()">更新</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	<!-- 全局js -->
	<script src="<%=basePath%>hAdmin/js/jquery.min.js?v=2.1.4"></script>
	<script src="<%=basePath%>hAdmin/js/bootstrap.min.js?v=3.3.6"></script>
	<!-- layer javascript -->
    <script src="<%=basePath%>hAdmin/js/plugins/layer/layer.min.js"></script>
    <script src="<%=basePath %>selected/jquery.searchableSelect.js"></script>
	<script type="text/javascript">
        var typeJSON = '<%=attrJSON.toString()%>';
        typeJSON = JSON.parse(typeJSON);
        //判断存放新增时的模板属性的数组
        var add_templates = new Array();
        //判断存放更新时的模板属性的数组
        var update_templates = new Array();
        var add_count = 0;
        var update_count = 0;
        //添加模板属性到页面
        function addTemplateItemHTML(attr_name, attr_type, attr_type_name) {
        	var action = $('#action_type').val();
        	if(action == 'add') {
        		    //如果属性是枚举
		        	if(attr_type == 'enum') {
		        		$item = $('.add-template-item2-model').clone(true);
		        		$item.removeClass('add-template-item2-model');
		        		$item.addClass('add-template-item');
		        		$item.find('.template-name').html(attr_name);
		        		$item.find('.template-type-name').html(attr_type_name);
		        		$item.find('.template-type').val(attr_type);
		        		var options = '';
		        		$('.template-option-item').each(function() {
		            		var name = $(this).find('.template-option-name').val();
		            		if(name == "") {
		                		layer.msg('选项名称不能为空!', {icon: 2,time:2000}); 
		                		return;
		                	}
		            		options = options + name + ',';
		            	});
		        		if(options != '') {
		        			options = options.substring(0, options.length - 1);
		        		}
		        		$item.find('.template-option').html(options);
		        		$('#add_template_button').before($item);
		        	
		        	}else {
		        		$item = $('.add-template-item1-model').clone(true);
		        		$item.removeClass('add-template-item1-model');
		        		$item.addClass('add-template-item');
		        		$item.find('.template-name').html(attr_name);
		        		$item.find('.template-type-name').html(attr_type_name);
		        		$item.find('.template-type').val(attr_type);
		        		$('#add_template_button').before($item);
		        	}
		     //更新模板属性操作时   	
        	}else if(action == 'update') {
	        	if(attr_type == 'enum') {
	        		$item = $('.update-template-item2-model').clone(true);
	        		$item.removeClass('update-template-item2-model');
	        		$item.addClass('update-template-item');
	        		$item.find('.template-name').html(attr_name);
	        		$item.find('.template-type-name').html(attr_type_name);
	        		$item.find('.template-type').val(attr_type);
	        		var options = '';
	        		$('.template-option-item').each(function() {
	            		var name = $(this).find('.template-option-name').val();
	            		if(name == "") {
	                		layer.msg('选项名称不能为空!', {icon: 2,time:2000}); 
	                		return;
	                	}
	            		options = options + name + ',';
	            	});
	        		if(options != '') {
	        			options = options.substring(0, options.length - 1);
	        		}
	        		$item.find('.template-option').html(options);
	        		$('#update_template_button').before($item);
	        	}else {
	        		$item = $('.update-template-item1-model').clone(true);
	        		$item.removeClass('update-template-item1-model');
	        		$item.addClass('update-template-item');
	        		$item.find('.template-name').html(attr_name);
	        		$item.find('.template-type-name').html(attr_type_name);
	        		$item.find('.template-type').val(attr_type);
	        		$('#update_template_button').before($item);
	        	}
    	}
        	
        }
        
        //更新模板属性到页面
        function updateTemplateItemHTML(attr_name, attr_type, attr_type_name) {
        	if(attr_type == 'enum') {
        		$item = $('.update-template-item2-model').clone(true);
        		$item.removeClass('update-template-item2-model');
        		$item.addClass('update-template-item');
        		$item.find('.template-name').html(attr_name);
        		$item.find('.template-type-name').html(attr_type_name);
        		$item.find('.template-type').val(attr_type);
        		var options = '';
        		$('.update-template-option-item').each(function() {
            		var name = $(this).find('.template-option-name').val();
            		if(name == "") {
                		layer.msg('选项名称不能为空!', {icon: 2,time:2000}); 
                		return;
                	}
            		options = options + name + ',';
            	});
        		if(options != '') {
        			options = options.substring(0, options.length - 1);
        		}
        		$item.find('.template-option').html(options);
        		$('#update_template_button').before($item);
        	}else {
        		$item = $('.update-template-item1-model').clone(true);
        		$item.removeClass('update-template-item1-model');
        		$item.addClass('update-template-item');
        		$item.find('.template-name').html(attr_name);
        		$item.find('.template-type-name').html(attr_type_name);
        		$item.find('.template-type').val(attr_type);
        		$('#update_template_button').before($item);
        	}
        }
        
        //更新模板数组
        function updateTpArray() {
        	var array = new Array();
        	var count = 0;
        	$('.update-template-item').each(function() {
        		var name = $(this).find('.template-name').html();
        		array[count++] = name;
        	});
        	var result = {array:array,count:count};
        	return result;
        }
        
        //添加模板数组
        function addTpArray() {
        	var array = new Array();
        	var count = 0;
        	$('.add-template-item').each(function() {
        		var name = $(this).find('.template-name').html();
        		array[count++] = name;
        	});
        	var result = {array:array,count:count};
        	return result;
        }
        
        //添加模板属性到修改模板属性页面
        function updateTpOptionHTML() {
        	$option = $('.update-template-option-model').clone(true);
			$option.removeClass('update-template-option-model');
			$option.addClass('update-template-option-item');
			$('#update_template_option_button_add').before($option);	
			return $option;
        }
        
        //添加模板属性到添加模板属性页面
        function addTpOptionHTML() {
        	$option = $('.template-option-model').clone(true);
			$option.removeClass('template-option-model');
			$option.addClass('template-option-item');
			$('#template_option_button_add').before($option);	
			return $option;
        }
        
        //添加模板
        function addTemplate() {
        	var template_name = $('#add_template_name').val();  
        	var template_tree = $('#add_template_tree').val()
        	if(template_tree == null || template_tree == "") {
        		layer.msg('所属树为空！', {icon: 2});
        		return;
        	}
        	var template_data = "";
			template_data += "[";
			$(".add-template-item").each(function() {
				template_data += "{";
				var template_name = $(this).find('.template-name').html();
				var template_type = $(this).find('.template-type').val();				
				//名称
				template_data += '"';
				template_data += "name";
				template_data += '"';
				template_data += ':';
				template_data += '"';
				template_data += template_name;
				template_data += '"';
				template_data += ',';
				//类型
				template_data += '"';
				template_data += "type";
				template_data += '"';
				template_data += ':';
				template_data += '"';
				template_data += template_type;
				template_data += '"';								
				if(template_type == "enum") {
					var optionArray = $(this).find('.template-option').html();
					//可选项
					template_data += ',';
					template_data += '"';
					template_data += "option";
					template_data += '"';
					template_data += ':';
					template_data += '"';
					template_data += optionArray;
					template_data += '"';
				}
				template_data += '}';
				template_data += ',';
			});			
			if(template_data.substring(template_data.length-1, template_data.length) == ',') {
				template_data = template_data.substring(0, template_data.length - 1);
			}
			template_data += "]";
			
			$.ajax({
	 			url:"<%=basePath%>template/addTemplate",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{tp_name:template_name,tp_data:template_data,tp_treeid:template_tree},
	 			success:function(result){
 					if(parseInt(result) > 0) {
 						layer.msg('新增模板成功', {icon: 1}); 
 					} else{
 						layer.msg('新增模板失败', {icon: 2}); 
 					}
 					$('#addModal').modal('hide');
 					setTimeout(function() {
						window.location.href = "<%=basePath%>template/index";
					},1000);
	 			}
	 		});
        }
        
        //添加模板属性
        function addTemplateAttr() {
        	var templates;
        	var action_type = $('#action_type').val();
        	if(action_type == 'add') {
        		templates = add_templates;
        		count = add_count;
        	}else if(action_type == 'update') {
        		templates = update_templates;
        		count = update_count;
        	}      	
        	
        	var attr_name = $('#attr_name').val();
        	var attr_type = $('#attr_type').val();
        	var attr_type_name = $('#attr_type').find('option:selected').html();
        	if(attr_name == "") {
        		layer.msg('属性名称不能为空!', {icon: 2,time:2000}); 
        		return;
        	}
        	addTemplateItemHTML(attr_name, attr_type, attr_type_name)
        	templates[count++] = attr_name;
        	
        	if(action_type == 'add') {
        		add_templates = addTpArray().array;
        		add_count = addTpArray().count;
        	}else if(action_type == 'update') {
        		update_templates = updateTpArray().array;
        		update_count = updateTpArray().count;
        	}  
        	
        	$('#add_template_modal').modal('hide');
        }
        
        //清除页面上的模板属性
        function cleanTemplateItem(action_type) {
        	if(action_type == 'add') {
        		$('.add-template-item').remove();
        	}else if(action_type == 'update') {
        		$('.update-template-item').remove();
        	}
        }
        
        /**
        *    清空修改模板属性的属性 
        */
        function cleanTemplateOption() {
        	$('.update-template-option-item').remove();
        }
        //新增模板属性到页面
        function addTemplateItem(name,type,option) {
        	eval("var type_name = typeJSON." + type);
        	if(type == 'enum') {
        		$item = $('.update-template-item2-model').clone(true);
        		$item.removeClass('update-template-item2-model');
        		$item.addClass('update-template-item');
        		$item.find('.template-name').html(name);
        		$item.find('.template-type-name').html(type_name);
        		$item.find('.template-type').val(type);
        		$item.find('.template-option').html(option);
        		$('#update_template_button').before($item);
        	}else {
        		$item = $('.update-template-item1-model').clone(true);
        		$item.removeClass('update-template-item1-model');
        		$item.addClass('update-template-item');
        		$item.find('.template-name').html(name);
        		$item.find('.template-type-name').html(type_name);
        		$item.find('.template-type').val(type);
        		$('#update_template_button').before($item);
        	}
        }
        //更新模板
        function updateTemplate(id) {
        	//需要更新模板的节点的id
        	$('#update_template_id').val(id);
        	var tree_name = $('#template_' + id).find('.td_tree_name').html();
        	$('#update_template_tree').html(tree_name);
        	$.ajax({
	 			url:"<%=basePath%>template/getTemplate",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{id:id},
	 			success:function(result){
	 				update_templates = new Array();
                    update_count = 0;
                    
	 				var template = JSON.parse(result);
	 				$('#update_template_name').val(template.tp_name);
	 				cleanTemplateItem('update');
 					var data = template.tp_data;
 					var attrArray = JSON.parse(data);
 					for(var i=0;i<attrArray.length;i++) {
                       var item_name = attrArray[i].name;
                       var item_type = attrArray[i].type;
                       var item_option = attrArray[i].option;
                       addTemplateItem(item_name,item_type,item_option);
                       update_templates[update_count++] = item_name;
 					}
	 			}
	 		});
        	$('#updateModal').modal('show');
        }
        //删除模板
        function deleteTemplate(id) {
        	layer.confirm('确定删除该模板？', {
        	    btn: ['确定','取消'], //按钮
        	    shade: false //不显示遮罩
        	}, function(){
            	$.ajax({
    	 			url:"<%=basePath%>template/deleteTemplate",
    	 			dataType:'text',
    	 			type:"post",
    	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
    	 			data:{id:id},
    	 			success:function(result){
     					if(parseInt(result) > 0) {
     						layer.msg('删除模板成功', {icon: 1}); 
     					} else{
     						layer.msg('删除模板失败', {icon: 2}); 
     					}
     					setTimeout(function() {
							window.location.href = "<%=basePath%>template/index";
						},1000);
    	 			}
    	 		});
        	   
        	}, function(){

        	});
        }
        //更新模板属性
        function updateTemplateAttr() {       	
        	var id = $('#update_template_id').val();
        	var oldName = $('#attrOldName').val();
        	var oldType = $('#attrOldType').val();
        	var attr_name = $('#update_attr_name').val();
        	var attr_type = $('#update_attr_type').val();
        	var attr_type_name = $('#update_attr_type').find('option:selected').html();
        	var options = '';
    		$('.update-template-option-item').each(function() {
        		var name = $(this).find('.template-option-name').val();
        		if(name == "") {
            		layer.msg('选项名称不能为空!', {icon: 2,time:2000}); 
            		return;
            	}
        		options = options + name + ',';
        	});
    		if(options != '') {
    			options = options.substring(0, options.length - 1);
    		}
        	for ( var i in update_templates) {
				if(attr_name == update_templates[i] && attr_name != oldName) {
					layer.msg('属性名称已定义!', {icon: 2,time:2000}); 
					return;
				}
			}
        	$.ajax({
	 			url:"<%=basePath%>template/updateTemplateAttr",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{id:id,oldName:oldName,oldType:oldType,name:attr_name,type:attr_type,option:options},
	 			success:function(result){
 					if(parseInt(result) > 0) {
 						$('#action_type').val('update');
 						layer.msg('修改模板属性成功', {icon: 1}); 	
 						$('.update-template-item').each(function() {
 							if($(this).find('.template-name').html() == oldName) {
 								$(this).remove();
 								updateTemplateItemHTML(attr_name,attr_type,attr_type_name);
 							}
 						});       	
 						
 						$('#update_template_modal').modal('hide');
 					} else{
 						layer.msg('修改模板属性失败', {icon: 2}); 
 					}	
	 			}
	 		});
        }
        //更新页面上的模板属性
        function updateTpItem() {
        	var id = $('#update_template_id').val();
        	var attr_name = $('#attr_name').val();
        	var attr_type = $('#attr_type').val();
        	var attr_type_name = $('#attr_type').find('option:selected').html();
        	var options = '';
    		$('.template-option-item').each(function() {
        		var name = $(this).find('.template-option-name').val();
        		if(name == "") {
            		layer.msg('选项名称不能为空!', {icon: 2,time:2000}); 
            		return;
            	}
        		options = options + name + ',';
        	});
    		if(options != '') {
    			options = options.substring(0, options.length - 1);
    		}
        	for ( var i in update_templates) {
				if(attr_name == update_templates[i]) {
					layer.msg('属性名称已定义!', {icon: 2,time:2000}); 
					return;
				}
			}

			$.ajax({
	 			url:"<%=basePath%>template/addTemplateAttr",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{id:id,name:attr_name,type:attr_type,option:options},
	 			success:function(result){
 					if(parseInt(result) > 0) {
 						layer.msg('新增属性成功', {icon: 1}); 
 						addTemplateAttr();		
 					}else {
 						layer.msg('新增属性失败', {icon: 2}); 
 					}
	 			}
	 		});	
        }
        //新增模板属性到页面
        function addTpItem() {
        	var attr_name = $('#attr_name').val();
        	var attr_type = $('#attr_type').val();
        	var attr_type_name = $('#attr_type').find('option:selected').html();
        	var options = '';
    		$('.template-option-item').each(function() {
        		var name = $(this).find('.template-option-name').val();
        		if(name == "") {
            		layer.msg('选项名称不能为空!', {icon: 2,time:2000}); 
            		return;
            	}
        		options = options + name + ',';
        	});
    		if(options != '') {
    			options = options.substring(0, options.length - 1);
    		}
    		var returnKey = false;
        	for ( var i in add_templates) {
				if(attr_name == add_templates[i]) {
					layer.msg('属性名称已定义!', {icon: 2,time:2000}); 
					return;
				}
			}
        	if(returnKey) {
        		return;
        	}
        	addTemplateAttr();	
        }
        
     	//分页查询文档信息，参数为要查询哪一页
     	function searchPage(currenPage) {

     		if(currenPage<=0){
     			currenPage = 1;
     		}
     		$('#currentPage').val(currenPage);
     		$('#searchPage').submit();	

     	}
     	
     	//跳转到指定页码
     	function forware(obj){
     		var page = $('#input_page').val();

     		if(page!=""&&page<="${searchParam.page.totalPage }"&&page>0){
     			searchPage(page);
     		}else{
     			layer.msg('输入页数超出范围!', {icon: 2,time:2000}); 
     		}			
     	
     	}
     	
     	//触发根据搜索关键字查找
     	function searchByLike(){

     		$('#searchPage').submit();
     		
     	}
        
		//绑定事件
		$(function() {			
			
			$('#add_template_button').on('click', function() {
				$('#action_type').val('add');
				$('#edit_add_template').unbind('click');
				$('#edit_add_template').bind('click', addTpItem);
				$('#add_template_modal').modal('show');
			});
			
			$('#update_template_button').on('click', function() {
				$('#action_type').val('update');
				$('#edit_add_template').unbind('click');
				$('#edit_add_template').bind('click', updateTpItem);
				$('#add_template_modal').modal('show');
			});
			
			$('#add_modal_button').on('click', function() {
				$('#addModal').modal('show');
			})
			
			$('#attr_type').on('change', function() {
				if($(this).val() == 'enum') {
					$('#template_option_group').fadeIn();
				}else {
					$('#template_option_group').fadeOut();
				}
			});
			
			$('#template_option_button_add').bind('click', function() {
				addTpOptionHTML();
			});
			
			$('.template-option-button-delete').on('click', function() {
				$(this).parent().parent().parent().remove();
			});
			
			$('.delete-template-button').on('click', function() {
				$(this).parent().parent().remove();
				add_templates = addTpArray().array;
        		add_count = addTpArray().count;
			});
			
			$('#update_name_button').on('click', function() {
				var name = $('#update_template_name').val();
				var id = $('#update_template_id').val();
				
				if(name == '') {
					layer.msg('模板名称不能为空!', {icon: 2,time:2000}); 
	        		return;
				}
				$.ajax({
    	 			url:"<%=basePath%>template/updateTemplateName",
    	 			dataType:'text',
    	 			type:"post",
    	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
    	 			data:{id:id,name:name},
    	 			success:function(result){
     					if(parseInt(result) > 0) {
     						layer.msg('修改名称成功', {icon: 1}); 
     						$('#template_' + id).find('.td_name').html(name);
     					} else{
     						layer.msg('修改名称失败', {icon: 2}); 
     					}
    	 			}
    	 		});				
			});
			
			$('.update-delete-button').on('click', function() {
				var button = $(this);
				var id = $('#update_template_id').val();
				var name = button.parent().parent().find('.template-name').html();
				layer.confirm('确定删除该模板属性？', {
	        	    btn: ['确定','取消'], //按钮
	        	    shade: false //不显示遮罩
	        	}, function(){
	            	$.ajax({
	    	 			url:"<%=basePath%>template/deletetTemplateAttr",
	    	 			dataType:'text',
	    	 			type:"post",
	    	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	    	 			data:{id:id,name:name},
	    	 			success:function(result){
	     					if(parseInt(result) > 0) {
	     						layer.msg('删除模板属性成功', {icon: 1}); 
	     						button.parent().parent().remove();
	     						update_templates = updateTpArray().array;
	     		        		update_count = updateTpArray().count;
	     					} else{
	     						layer.msg('删除模板属性失败', {icon: 2}); 
	     					}
	    	 			}
	    	 		});
	        	   
	        	}, function(){

	        	});
				
			});
			
			$('.edit-template-button').on('click', function() {	
				var attrOldName = $(this).parent().parent().find('.template-name').html()
				$('#attrOldName').val(attrOldName);
				$('#update_attr_name').val(attrOldName);
				var type_value = $(this).parent().parent().find('.template-type').val();
				$('#attrOldType').val(type_value);
				$('#update_attr_type').find('option[value=' + type_value + ']').prop("selected", true);
				//清空模板属性
				cleanTemplateOption();
				var option = $(this).parent().parent().find('.template-option').html();
				if(type_value == 'enum') {
					$('#update_template_option_group').show();
					var options = option.split(",");
					for(var i in options) {
						if(options[i] != "") {
							var optionItem = updateTpOptionHTML();
							optionItem.find('.template-option-name').val(options[i]);
						}
					}
					
				}else {
					$('#update_template_option_group').hide();
				}
				
				$('#update_template_modal').modal('show');
			});
			
			$('#edit_add_template').bind('click', function() {	
			});
			
			$('#update_attr_type').on('click', function() {
				if($(this).val() == 'enum') {
					$('#update_template_option_group').fadeIn();
				}else {
					$('#update_template_option_group').fadeOut();
				}
			});
			
			$('#update_template_option_button_add').on('click', function() {
				updateTpOptionHTML();
			});
		})
		
	</script>
</body>

</html>
