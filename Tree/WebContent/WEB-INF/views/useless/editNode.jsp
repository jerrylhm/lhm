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


<title>树管理</title>

<link rel="shortcut icon" href="favicon.ico">
<link href="<%=basePath%>hAdmin/css/bootstrap.min.css?v=3.3.6"
	rel="stylesheet">
<link href="<%=basePath%>font-awesome/css/font-awesome.css?v=4.4.0"
	rel="stylesheet">

<link href="<%=basePath%>hAdmin/css/animate.css" rel="stylesheet">
<link href="<%=basePath%>hAdmin/css/style.css?v=4.1.0" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link href="<%=basePath %>selected/jquery.searchableSelect.css" rel="stylesheet" type="text/css">
<style type="text/css">
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
.ztree li span.button.value {margin-left:2px; margin-right: -1px; vertical-align:top; *vertical-align:middle}
</style>
<style type="text/css">
.line {
height: auto;
} 
iframe {
  border: none;
}

.ztree li span.value-icon {
background-image: url(<%=basePath%>zTree/css/zTreeStyle/img/diy/data.png);
}
.ztree li span.permission-icon {
background-image: url(<%=basePath%>zTree/css/zTreeStyle/img/diy/9.png);
}
.ztree li span.editUrl-icon {
background-image: url(<%=basePath%>zTree/css/zTreeStyle/img/diy/forward.png);
}
.ztree li span.protocol-icon {
background-image: url(<%=basePath%>zTree/css/zTreeStyle/img/diy/protocol.png);
}
.ztree li span.template-icon {
background-image: url(<%=basePath%>zTree/css/zTreeStyle/img/diy/eye.png);
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
.template-item1-model,.template-item2-model,.template-option-model {
  display: none;
}
</style>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="row">
			<div class="col-sm-12">
			 <div class="row" style="margin-bottom: 10px">
			    <div class="col-sm-6">
			            <a href="<%=basePath %>tree/index" class="btn btn-default " type="button" style="border: none"><i class="fa fa-reply"></i>&nbsp;&nbsp;返回</a>
			            <span style="font-size: 21px;margin-left: 7px;position: relative;top: 5px;">树管理&nbsp;&nbsp;/&nbsp;&nbsp;<span style="color: #23b7e5">${tree.node_name }</span></span>                       
			    </div>
             </div>          
			</div>
			<div class="col-sm-3">
                <div class="ibox float-e-margins">
                    <div class="ibox-content mailbox-content">
                        <button class="btn btn-white" type="button" onclick="openNode('tree',true)">一键打开</button>
						<button class="btn btn-white" type="button" onclick="openNode('tree',false)">一键关闭</button>
                        <div class="input-group">
                            <input class="form-control input-sm" id="nodeSearchKey" placeholder="输入节点名称搜索" type="text">
                            <div class="input-group-btn">
                                <button id="searchNode" type="button" class="btn btn-sm btn-primary">
                                                                                                 搜索
                                </button>
                            </div>
                        </div>
                        <ul id="tree" class="ztree" style="font-size: 10px"></ul>
                    </div>
                </div>
            </div>
            <div class="col-sm-9 animated fadeInRight">
						<input id="editingNodeId" type="hidden" value="${tree.node_id }">					
                <div class="mail-box" style="height: 800px">
                        <iframe id="forwareIframe" name="forwareIframe" src="" width="100%" height="100%"></iframe>
                </div>
            </div>
		</div>
	</div>
	<!--数据管理模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="valueModal" style="width: 100%">
	 	<div class="modal-dialog" style="width: 900px">
	 		<div class="modal-content animated fadeIn">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>数据管理</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">	    
	 				<iframe id="treeValueIframe" name="treeValue" src="" width="100%" height="600px"></iframe>
		 		</div>
		 		<div class="modal-footer">
<!-- 		 			<button type="button" id="addNodeBtn" class="btn btn-sm btn-success" onclick="addNode()">确定</button> -->
<!-- 		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button> -->
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 	 <!--编辑协议模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="editProtocolModal">
	 	<div class="modal-dialog" style="width: 900px">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>编辑协议</h4>
	 			</div>
	 			
	 			<div class="modal-body">   
		 				<iframe id="treeProtocolIframe" name="treeProtocol" src="" width="100%" height="600px"></iframe>
<!-- 	 				<div class="col-sm-2 col-sm-offset-10 tr" style="padding-right: 5px;">                -->
<!-- 			              <a type="button" class="btn btn-default" style="margin-left: 10px" data-toggle="modal" data-target="#datapoint_add"> -->
<!-- 			                + 新建协议 -->
<!-- 			              </a>                    -->
<!-- 			        </div> -->
			        
			        
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
	 				<h4>新增节点</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	 				<div class="form-group row">   
	 				    <label class="col-sm-2">节点名称：</label> 
	 				    <div class="col-sm-6">
	 						<input class="input-sm form-control" type="text" id=addName placeholder="输入节点的名称">
	 					</div>	
	 				</div>
	 				<div class="form-group row">
	 				    <label class="col-sm-2">节点跳转地址：</label>
	 				    <div class="col-sm-6">
	 						<input class="input-sm form-control" type="text" id=addUrl placeholder="输入点击节点跳转的地址">
	 					</div>
	 				</div>
	 				<div class="form-group row">
	 				   <label class="col-sm-2">节点类型：</label>
	 				   <select id="addNodeType"  style="margin-left: 15px;">
	 				      <c:forEach items="${types }" var="type">
	 				       <option value="${type.type_id }" pid="${type.type_pid }">${type.type_name_cn }</option>	
	 				       </c:forEach>	   
	 				   </select> 

	 			    </div>
	 			    <div class="equipment-space" style="display:none">
	 			    <div class="form-group row sn">
		 			    <label class="col-sm-2">设备SN：</label>
		 				<div class="col-sm-6">
		 						<input name="node_sn" id="add_sn" type="text" placeholder="输入设备SN值" class="form-control">
		 				</div>
	 				</div>
	 				<div class="form-group row node-class">
	 				    <label class="col-sm-2">设备所属类：</label>
	 				    <div class="col-sm-6">
	 						<input class="input-sm form-control" type="text" id=addClass placeholder="输入设备所属类">
	 					</div>
	 				</div>
	 				<div class="form-group row template">
	 				    <label class="col-sm-2">启用模板：</label>
	 				    <input type="checkbox" id="add_template_checkbox" style="margin-left: 15px;">
	 				<div class="form-group row template-space" style="border: 1px solid #ccc;margin-right: 15px;display: none;">
	 				    <div style="margin: 10px 10px;">
	 				        <div class="row template-item1-model" style="margin-bottom: 10px;">
	 				        <div class="col-sm-3"><span class="attr-name">属性名称:</span><span class="template-name attr-value"></span></div>
	 				        <div class="col-sm-2"><span class="attr-name">类型:</span><span class="template-type-name attr-value"></span></div>
	 				        <input type="hidden" class="template-type">
	 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-danger delete-template-button">删除</button></div>
	 				        </div>
	 				        <div class="row template-item2-model" style="margin-bottom: 10px;">
	 				        <div class="col-sm-3"><span class="attr-name">属性名称:</span><span class="template-name attr-value"></span></div>
	 				        <div class="col-sm-2"><span class="attr-name">类型:</span><span class="template-type-name attr-value"></span></div>
	 				        <div class="col-sm-5"><span class="attr-name">可选项:</span><span class="template-option attr-value"></span></div>
	 				        <input type="hidden" class="template-type">
	 				        <div class="col-sm-2"><button type="button" class="btn btn-outline btn-danger delete-template-button">删除</button></div>
	 				        </div>
	 				        <button type="button" class="btn btn-outline btn-success" id=add_template_button>+ 添加模板属性</button>
	 				    </div>
	 				</div>
	 				<div ></div>
	 				</div>
		 		</div>   
	 				
		 		<div class="modal-footer">
		 			<button type="button" id="addNodeBtn" class="btn btn-sm btn-success" onclick="addNode()">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
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
		 			<button type="button" class="btn btn-sm btn-success" onclick="addTemplateAttr()">添加</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 <!--模板管理模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="update_template_Modal">
	 	<div class="modal-dialog modal-lg">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>模板管理</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">	 				
	 				<div class="form-group row template">
	 				    <label class="col-sm-2">启用模板：</label>
	 				    <input type="checkbox" id="manage_template_checkbox" style="margin-left: 15px;">
		 				<div class="form-group row manage-template-space" style="border: 1px solid #ccc;margin-right: 15px;display: none;">
		 				    <div style="margin: 10px 10px;">
		 				        <div class="row manage-template-item1-model" style="margin-bottom: 10px;">
			 				        <div class="col-sm-3"><span class="attr-name">属性名称:</span><span class="template-name attr-value"></span></div>
			 				        <div class="col-sm-2"><span class="attr-name">类型:</span><span class="template-type-name attr-value"></span></div>
			 				        <input type="hidden" class="template-type">
			 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-success update-template-button">编辑</button></div>
			 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-danger manage-delete-template-button">删除</button></div>
		 				        </div>
		 				        <div class="row manage-template-item2-model" style="margin-bottom: 10px;">
			 				        <div class="col-sm-3"><span class="attr-name">属性名称:</span><span class="template-name attr-value"></span></div>
			 				        <div class="col-sm-2"><span class="attr-name">类型:</span><span class="template-type-name attr-value"></span></div>
			 				        <div class="col-sm-5"><span class="attr-name">可选项:</span><span class="template-option attr-value"></span></div>
			 				        <input type="hidden" class="template-type">
			 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-success update-template-button">编辑</button></div>
			 				        <div class="col-sm-1"><button type="button" class="btn btn-outline btn-danger delete-template-button">删除</button></div>
		 				        </div>
		 				        <button type="button" class="btn btn-outline btn-success" id=manage-add_template_button>+ 添加模板属性</button>
		 				    </div>
		 				</div>
	 				<div ></div>
	 				</div>
		 		</div>   
	 				
		 		<div class="modal-footer">
		 			<button type="button" id="addNodeBtn" class="btn btn-sm btn-success" onclick="addNode()">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
    <!--模板管理新增模板模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="manage_add_template_modal" style="margin-top: 80px;">
	 	<div class="modal-dialog ">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>新增模板属性</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	 				<div class="form-group row">
		 			    <label class="col-sm-3">属性名称：</label>
		 				<div class="col-sm-6">
		 						<input id="manage_attr_name" type="text" placeholder="输入属性名称" class="form-control">
		 				</div>
	 				</div>
	 				<div class="form-group row">
	 				   <label class="col-sm-3">属性类型：</label>
	 				   <select id="manage_attr_type"  style="margin-left: 15px;">
	 				       <option value="text">文本</option>	
	 				       <option value="enum">枚举</option>
	 				       <option value="boolean">布尔型</option>
	 				       <option value="file">文件</option>
	 				   </select> 
	 			    </div>
	 			    <div class="form-group row" id=manage_template_option_group style="display: none;">
	 				   <label class="col-sm-3">可选项：</label>
	 				   <div class="col-sm-6 manage-template-option-space" style="border: 1px solid #ccc;">
		 						<div class="row manage-template-option-model" style="margin: 10px 0px;">
		 						  <div class="col-sm-8"> 
                                   <input type="text" placeholder="输入选项名称" class="form-control manage-template-option-name">
                                  </div>
                                  <div class="col-sm-2"> 
                                   <div class="col-sm-2"><button type="button" class="btn btn-outline btn-danger manage-template-option-button-delete" >删除</button></div>
                                  </div>
				 			    </div> 
				 			    <div class="row" style="margin: 10px 0px;">
				 			    <button type="button" class="btn btn-outline btn-success" id=manage-template_option_button_add>+ 添加</button>
				 			    </div>
		 				</div>
	 			    </div>
		 		</div>
		 		
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="">添加</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	<!--编辑模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="editor">
	 	<div class="modal-dialog ">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>编辑节点名称</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="height:80px;margin-left:30px">
	 				<input class="input-sm form-control" type="text" id="nodeName" placeholder="输入节点的名称">
		 		</div>
		 		
		 		<input type="hidden" id="nodeId">
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="editorNodeName()">保存</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 <!--编辑节点属性模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="editUrlModal">
	 	<div class="modal-dialog modal-lg">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>编辑节点属性</h4>
	 			</div>
	 			<div class="modal-body" style="margin-left:30px">
	 			    <div class="equipment-space-update" style="display: none">
	                <div id="update_sn_div" class="form-group row sn">
			 			    <label class="col-sm-2">设备SN：</label>
			 				<div class="col-sm-6">
			 						<input id="nodeSn" type="text" placeholder="输入设备SN值" class="form-control">
			 				</div>
		 			</div>
		 			<div id="update_class_div" class="form-group row node-class">
			 			    <label class="col-sm-2">设备所属类：</label>
			 				<div class="col-sm-6">
			 						<input id="nodeClass" type="text" placeholder="输入设备所属类名称" class="form-control">
			 				</div>
		 			</div>
		 			</div>
			 		<div class="form-group row">
		 				    <label class="col-sm-2">节点跳转地址：</label>
		 				    <div class="col-sm-6">
		 						<input class="input-sm form-control" type="text" id=nodeUrl placeholder="输入点击节点跳转的地址">
		 					</div>
		 			</div>
	 			</div>
		 		<input type="hidden" id="editUrlNodeId">
		 		<input type="hidden" id="editUrlNodeType">
		 		<input type="hidden" id="editUrlNodeOldSn">
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="editorNodeUrl()">保存</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>

		 <!--权限管理模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="permissionModal">
	 	<div class="modal-dialog">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>权限管理</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	 			    选择想要赋予权限的用户：
	 			    <select class="form-control m-b use-searchable" name="account" id="userList">
	 			       <c:forEach items="${users }" var="user">
	                        <option value="${user.ur_id }">${user.ur_username }</option>
                       </c:forEach>
                    </select>
                    <!-- 加载中动画 -->
                    <div class="ibox-content" id="loading" style="display: none;">
                        <div class="spiner-example">
                            <div class="sk-spinner sk-spinner-three-bounce">
                                <div class="sk-bounce1"></div>
                                <div class="sk-bounce2"></div>
                                <div class="sk-bounce3"></div>
                            </div>
                        </div>
                    </div>
                    <div id="permissionSpace">
                        <button class="btn btn-white" type="button" onclick="openNode('permissionTree',true)">一键打开</button>
						<button class="btn btn-white" type="button" onclick="openNode('permissionTree',false)">一键关闭</button>
	                    <div class="input-group" style="margin-top: 5px">
	                            <input class="form-control input-sm" id="psnodeSearchKey" placeholder="输入节点名称搜索" type="text">
	                            <div class="input-group-btn">
	                                <button id="pssearchNode" type="button" class="btn btn-sm btn-primary">
	                                                                                                 搜索
	                                </button>
	                            </div>
	                    </div>
		 			    <ul id="permissionTree" class="ztree" style="font-size: 10px"></ul>
	 			    </div>
	 			    <input type="hidden" id="alreadEdit" value="${alreadEdit }">
	 			    <input type="hidden" id="editPSUser" value="">
		 		</div>
		 		<div class="modal-footer">
		 			<button type="button" id="savePsBtn" class="btn btn-sm btn-success" onclick="savePermission()">保存</button>
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
    <script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.excheck.js"></script>
    <script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.exedit.js"></script>
    <script src="<%=basePath %>selected/jquery.searchableSelect.js"></script>
	<script type="text/javascript">

	    //初始化下拉框，可以根据option的text搜索
		$(function(){
			$('.use-searchable').searchableSelect();
		});
	
	if('${permission.urnode_iscreator}' != 1) {
		
		$('#permissionManage').remove();
	}
	var btnClickNode;
	var setting = {
			//设置是否允许同时设置多个节点
			view: {
				addHoverDom: addHoverDom,
				removeHoverDom: removeHoverDom,
				selectedMulti: false,
				fontCss: getFontCss,
				showIcon: false
			},
			check: {
				enable: false
				
			},
			data: {
				key: {
					title:'title'
				},
				simpleData: {
					enable: true,
					idKey: 'id',
					pIdKey: 'pId',
					rootPId: 0
				}
			},
			edit: {
				enable: true,
				editNameSelectAll: true,
				showRemoveBtn: showRemoveBtn,
				showRenameBtn: showRenameBtn,
				removeTitle: "删除节点",
				renameTitle: "编辑名称"
			},
			callback: {
				beforeCollapse: zTreeBeforeCollapse,
				beforeExpand: zTreeBeforeExpand,
				beforeDrag: beforeDrag,
				beforeRename: beforeRename,
				beforeRemove: beforeRemove,
				onClick: forwareUrl
			}
			
		};
        eval('var zNodes=' + '${ztree}');

		$(document).ready(function(){
			$.fn.zTree.init($("#tree"), setting, zNodes);
		});

		function showRemoveBtn(treeId, treeNode){
			if(!!treeNode.hasP) {
				return true;
			}else{
				return false;
			}
		}
		
		function showRenameBtn(treeId, treeNode){
			if(!!treeNode.hasP) {
				return true;
			}else{
				return false;
			}
		}
		
		function zTreeBeforeCollapse(treeId, treeNode) {

		    return true;
		};
		function zTreeBeforeExpand(treeId, treeNode) {

		    return true;
		};
		function cancalFocus() {
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getSelectedNodes();
			for(var key in nodes) {
				treeObj.cancelSelectedNode(nodes[key]);
			}
		}
		function beforeDrag(treeId, treeNodes) {
			return false;
		}
		function beforeRemove(treeId, treeNode) {
			return removeNode(treeNode);
		}
		function getFontCss(treeId, treeNode) {
			var color = '';
			var fontWeight = '';
			if(!!treeNode.highlight) {
				color = "#A60000";
				fontWeight = "bold";
			} else {
				color = "#333";
				fontWeight = "normal";
			}
			var backgroundColor = '';
			if(!treeNode.hasP) {
				backgroundColor = "#c9c9ce";
			}
			return {color:color,"font-weight":fontWeight,"background-color":backgroundColor};
		}
		function beforeRename(treeId, treeNode, newName) {
			var suc = false;
			if (newName.length == 0) {
				layer.alert('节点名称不能为空', {
				    icon: 2,
				    skin: 'layer-ext-moon' 
				})
				var zTree = $.fn.zTree.getZTreeObj("tree");
				setTimeout(function(){zTree.editName(treeNode)}, 10);
			} else {
				$.ajax({
		 			url:"<%=basePath%>tree/updateTreeName",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{id:treeNode.id,name:newName},
		 			success:function(result){
	 					if(parseInt(result) > 0) {
	 						suc = true;
	 					} else{
	 						layer.msg('编辑节点时出错了', {icon: 2}); 
	 					}
	 					return suc;
		 			}
		 		});
			}

		}

		function addHoverDom(treeId, treeNode) {
			if(!treeNode.hasP) {
				return;
			}
			//新增节点按钮
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='新增子节点' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			if (btn) btn.bind("click", function(){
				var zTree = $.fn.zTree.getZTreeObj("tree");
				    btnClickNode = treeNode;
					$('#addName').val('')
					$('#addUrl').val('')
					$('#add_sn').val('')
// 					var type_id = treeNode.type.type_id;
// 					if(type_id == 1) {
// 						$('#addNodeType').find('option').css('display','none');
// 						$('#addNodeType option[pid="0"]').css('display','');	
// 						$('#addNodeType').val($('#addNodeType option[pid="0"]:eq(0)').val());
// 						var size = $('#addNodeType option[pid="0"]').
// 					}else {
// 						$('#addNodeType').find('option').css('display','none');
// 						$('#addNodeType option[pid="' + type_id + '"]').css('display','');
// 						$('#addNodeType').val($('#addNodeType option[pid="' + type_id + '"]:eq(0)').val());
// 					}
					$('#addModal').modal('show');
				return false;
			});
			//数据管理按钮
			if (treeNode.editNameFlag || $("#valueBtn_"+treeNode.tId).length>0) return;
			var valueStr = "<span class='button value value-icon' id='valueBtn_" + treeNode.tId
				+ "' title='数据管理' onfocus='this.blur();'></span>";
			sObj.after(valueStr);	
			var valuebtn = $("#valueBtn_"+treeNode.tId);
			if (valuebtn) valuebtn.bind("click", function(){
				var zTree = $.fn.zTree.getZTreeObj("tree");
				    btnClickNode = treeNode;
				    editNodeValue(treeId, treeNode);
					$('#valueModal').modal('show');
				return false;
			});
			
			//节点属性编辑按钮
			if (treeNode.editNameFlag || $("#editUrlBtn_"+treeNode.tId).length>0) return;	
			var editUrlStr = "<span class='button value editUrl-icon' id='editUrlBtn_" + treeNode.tId
				+ "' title='编辑节点属性' onfocus='this.blur();'></span>";
			sObj.after(editUrlStr);
			var editUrlbtn = $("#editUrlBtn_"+treeNode.tId);
			if (editUrlbtn) editUrlbtn.bind("click", function(){
				$('#editUrlNodeId').val(treeNode.id);				
	        	$('#nodeUrl').val(treeNode.myUrl);
	        	var type_id = treeNode.type.type_id;
	        	$('#editUrlNodeType').val(type_id);	        	
	        	if(type_id == 2) {
	        		$('.equipment-space-update').css('display', "");
	        		$('#nodeSn').val(treeNode.sn);
	        		$('#nodeClass').val(treeNode.nodeClass);
	        		$('#editUrlNodeOldSn').val(treeNode.sn);
	        	}else {
	        		$('.equipment-space-update').css('display', "none");
	        	}      	
				editUrl();
				return false;
			});
			
			//模板管理按钮
			if (treeNode.editNameFlag || $("#templateBtn_"+treeNode.tId).length>0 || treeNode.type.type_id == 2) {
					var templateStr = "<span class='button value template-icon' id='templateBtn_" + treeNode.tId
					+ "' title='模板管理' onfocus='this.blur();'></span>";
				sObj.after(templateStr);
				var templatebtn = $("#templateBtn_"+treeNode.tId);
				if (templatebtn) templatebtn.bind("click", function(){
					$('#update_template_Modal').modal('show');
					return false;
				});
			}	
			
			//权限管理编辑按钮
			if (treeNode.editNameFlag || $("#permissionBtn_"+treeNode.tId).length>0 || !treeNode.isCreator) return;			
			var valueStr = "<span class='button value permission-icon' id='permissionBtn_" + treeNode.tId
				+ "' title='权限管理' onfocus='this.blur();'></span>";
			sObj.after(valueStr);
			var permissionbtn = $("#permissionBtn_"+treeNode.tId);
			if (permissionbtn) permissionbtn.bind("click", permissionManage);
			//协议管理按钮
			if (treeNode.editNameFlag || $("#protocolBtn_"+treeNode.tId).length>0) return;			
			var valueStr = "<span class='button value protocol-icon' id='protocolBtn_" + treeNode.tId
				+ "' title='协议管理' onfocus='this.blur();'></span>";
			sObj.after(valueStr);
			var protocolbtn = $("#protocolBtn_"+treeNode.tId);
			if (protocolbtn) protocolbtn.bind("click", {treeId:treeId, treeNode:treeNode},protocolManage);
		};
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
			$("#valueBtn_"+treeNode.tId).unbind().remove();
			$("#permissionBtn_"+treeNode.tId).unbind().remove();
			$("#editUrlBtn_"+treeNode.tId).unbind().remove();
			$("#protocolBtn_"+treeNode.tId).unbind().remove();
			$("#templateBtn_"+treeNode.tId).unbind().remove();
		};
		//编辑节点名称
		function edit(node) {
			var zTree = $.fn.zTree.getZTreeObj("tree"),
			treeNode = node;
			zTree.editName(treeNode);
		};
		var newCount = 0;
		
		function addNode() {
			var name = $('#addName').val();
			var url = $('#addUrl').val();
			var type = $('#addNodeType').val();
			var type_name_cn = $('#addNodeType').find("option:selected").html();
			var sn = $('#add_sn').val();
			var nodeClass = $('#addClass').val();
			var template_checked = $('#add_template_checkbox').is(":checked");
			var template_data = "";
			template_data += "[";
			$(".template-item").each(function() {
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
				template_data += ',';				
				if(template_type == "enum") {
					var optionArray = $(this).find('.template-option').html();
					//可选项
					template_data += '"';
					template_data += "option";
					template_data += '"';
					template_data += ':';
					template_data += '"';
					template_data += optionArray;
					template_data += '"';
					template_data += ',';
					//已选项（默认为第一个选项）
					template_data += '"';
					template_data += "selected";
					template_data += '"';
					template_data += ':';
					template_data += '"';
					template_data += 0;
					template_data += '"';
				}else if(template_type == "text"){
					//内容
					template_data += '"';
					template_data += "content";
					template_data += '"';
					template_data += ':';
					template_data += '"';
					template_data += '"';
				}
				else if(template_type == "boolean"){
					//是否勾选（true或false）
					template_data += '"';
					template_data += "checked";
					template_data += '"';
					template_data += ':';
					template_data += '"';
					template_data += 'false';
					template_data += '"';
				}
				else if(template_type == "file"){
					//文件保存在server端的地址
					template_data += '"';
					template_data += "url";
					template_data += '"';
					template_data += ':';
					template_data += '"';
					template_data += '"';
				}
				template_data += '}';
				template_data += ',';
			});			
			if(template_data.substring(template_data.length-1, template_data.length) == ',') {
				template_data = template_data.substring(0, template_data.length - 1);
			}
			template_data += "]";

			if(url == "") {
				url = '';
			}
			if(name == "") {
				layer.alert('节点名称不能为空', {
				    icon: 3,
				    skin: 'layer-ext-moon' 
				})
				return;
			}
			if(type == 2) {

				$.ajax({
		 			url:"<%=basePath%>tree/validSN",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{sn:sn,action:'add'},
		 			success:function(result){
		 				     if(result > 0) {
		 				    	addNodeHandle(name,url,type,type_name_cn,nodeClass,template_data,template_checked,sn);			    	 
		 				     }else {
		 				    	layer.alert('sn值已存在或sn值为空', {
		 						    icon: 3,
		 						    skin: 'layer-ext-moon' 
		 						})
		 				     }
		 			}
		 		});	
			
			}else {
				addNodeHandle(name,url,type,type_name_cn,nodeClass,template_data,template_checked,'');	
			}
			templates = new Array();
		}
		
		function addNodeHandle(name,url,type,type_name_cn,nodeClass,template,checked,sn) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			var node = btnClickNode;
			layer.tips('新增节点中，请稍等...', '#addNodeBtn', {
			    tips: [1, '#3595CC'],
			    time: 1000
			});
			$('#addNodeBtn').prop('disabled','disabled');
			$.ajax({
	 			url:"<%=basePath%>tree/addTree",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{isParent:false,pid:node.id,name:name,url:url,type:type,sn:sn,nodeClass:nodeClass,template:template,checked:checked},
	 			success:function(result){
	 				eval('var tree=' + result);
 					if(tree.node_id > 0) {
 						var isCreator;
 						tree.isCreator == 1?isCreator = true:isCreator = false;
 						treeNode = zTree.addNodes(node, {id:tree.node_id, pId:tree.node_pid, name:tree.node_name, myUrl:url, type:{type_id:type}, sn:sn, title:tree.node_name+"("+type_name_cn+")", nodeClass:nodeClass, target:"treeValue", hasP:true, isCreator:isCreator});
 						layer.msg('新增节点成功', {icon: 1}); 
 						$('#addModal').modal('hide');
 					} else{
 						layer.msg('新增节点出错了', {icon: 2}); 
 					}
 					$('#addNodeBtn').prop('disabled','');					
	 			}
	 		});	
		}
		
		//删除节点
		function removeNode(node) {		
			var zTree = $.fn.zTree.getZTreeObj("tree");
				 layer.confirm('确定删除节点吗？', {
			       	    btn: ['确定','取消'], //按钮
			       	    shade: false //不显示遮罩
				       	}, function(){
				       		$.ajax({
					 			url:"<%=basePath%>tree/removeTree",
					 			dataType:'text',
					 			type:"post",
					 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					 			data:{id:node.id},
					 			success:function(result){
				 					if(parseInt(result) > 0) {
				 						zTree.removeNode(node,false);
				 						checkEditingNode(node.id);
				 						layer.msg('删除数据成功!', {icon: 1});  
				 					} else{
				 						layer.msg('删除数据失败!', {icon: 2});  
				 					}
					 			}
					 		});	
				       	}, function(){
				       	});	
				 return false;
		}
		
		function editNodeValue(treeId, treeNode) {
			$('#treeValueIframe').attr('src', '<%=basePath%>' + "tree/treeValue?nodeId=" + treeNode.id + "&nodeName=" + treeNode.name);
		}
		//初始化打开权限管理模态框
        function permissionManage() {
        	initPermission();
        	$('#permissionModal').modal('show');
        	return false;
        }

		//初始化权限管理界面
		function initPermission() {
			$('#loading').show();
			$('#permissionSpace').hide();
			var userId = $('#userList').val();
			$('#editPSUser').val(userId);
			initTree(userId);
		}
		
		//初始化协议管理模态框
		function protocolManage(event) {
			editNodeProtocol(event.data.treeId, event.data.treeNode);
			$('#editProtocolModal').modal();
			return false;
		}
		
		function editNodeProtocol(treeId, treeNode) {
			$('#treeProtocolIframe').attr('src', '<%=basePath%>' + "tree/treeProtocol?nodeId=" + treeNode.id);
		}
		//获取正在查看的树的所有节点
		function initTree(userId) {
			    
				$.ajax({
 		 			url:"<%=basePath%>tree/getTree",
 		 			dataType:'text',
 		 			type:"post",
 		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
 		 			data:{treeId:'${tree.node_id}'},
 		 			success:function(result){
 	                  var PTSetting = {
 	                			//设置是否允许同时设置多个节点
 	                			view: {
 	                				selectedMulti: false,
 	               				    fontCss: getFontCss,
 	               				    showIcon: false,
 	               				    showTitle: true
 	                			},
 	                			check: {
 	                				enable: true,
 	                				chkStyle: "checkbox",
 	                				chkboxType: { "Y": "s", "N": "" }               				
 	                			},
 	                			data: {
 	                				key: {
 	               					title:'title'
 	               				    },
 	                				simpleData: {
 	                					enable: true,
 	                					idKey: 'id',
 	                					pIdKey: 'pId',
 	                					rootPId: 0
 	                				}
 	                			},
 	                			edit: {
 	                				enable: false
 	                			},
 	                			callback: {

 	                			}
 	                			
 	                		};
 	                     eval('var PSNodes=' + result);
 	                     $.fn.zTree.init($("#permissionTree"), PTSetting, PSNodes);
 	                     getUserPermission(userId);
 		 			}
 		 		});	
		}		
		
		//获取用户对该树的权限并刷新ztree
		function getUserPermission(userId) {
			$('#loading').show();
			$('#permissionSpace').hide();
			var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
			treeObj.checkAllNodes(false);
			$.ajax({
	 			url:"<%=basePath%>tree/getUserPermission",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{userId:userId,treeId:'${tree.node_id}'},
	 			success:function(result){	 				
                   if(result != '') {
                	   $('#alreadEdit').val(true);
                	   eval('var userPermission=' + result);
                	   var ids = userPermission.urnode_nodeid.split(',');
                	   for(var key in ids) {
                		   var node = treeObj.getNodeByParam("id", ids[key], null);
                		   treeObj.expandNode(node, true, false, true);
                		   treeObj.checkNode(node, true, false);
                	   }
                   } else {
                	   $('#alreadEdit').val(false);
                   }                          
                   $('#loading').hide();
                   $('#permissionSpace').show();
	 			}
	 		});	
		}
		
		//保存权限设置
		function savePermission() {
			var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
			var nodes = treeObj.getCheckedNodes(true);
			var userid = $('#editPSUser').val();
			var alreadEdit = $('#alreadEdit').val();
			var ids = "";
			for(var i = 0;i < nodes.length;i++) {
				if(i == 0) {
					ids = ids + nodes[i].id;
				} else {
					ids = ids + ',' + nodes[i].id;
				}
			}
			layer.tips('保存中，请稍等...', '#savePsBtn', {
			    tips: [1, '#3595CC'],
			    time: 1000
			});
			$('#savePsBtn').prop('disabled','disabled');
			$.ajax({
	 			url:"<%=basePath%>tree/savePermission",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{ids:ids,treeId:'${tree.node_id}',userid:userid,alreadEdit:alreadEdit},
	 			success:function(result){	 				
                   if(result > 0) {
                	   layer.msg('权限设置成功!', {icon: 1,time:1000}); 
                	   $('#savePsBtn').prop('disabled','');
                   }
	 			}
	 		});	
		}
		
		//模糊查找节点
		function searchNode(id,keyId) {
			var treeObj = $.fn.zTree.getZTreeObj(id);
			var searchKey = $('#' + keyId).val();
			nodes = treeObj.transformToArray(treeObj.getNodes());
			
			for(var key in nodes) {
				nodes[key].highlight = false;
				treeObj.updateNode(nodes[key]);
			}
			
			if(searchKey != '') {
				var nodes = treeObj.getNodesByParamFuzzy("name", searchKey, null);
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
		
		//点击节点跳转页面
		function forwareUrl(event, treeId, treeNode){
			if(treeNode.myUrl != "") {
				$('#forwareIframe').attr('src',treeNode.myUrl + "?nodeId=" + treeNode.id);
			}
		}
		
		//ajax保存节点属性地址
		function editorNodeUrl() {
			var nodeid = $('#editUrlNodeId').val();
			var url = $('#nodeUrl').val();
			var sn = $('#nodeSn').val();
			var type = $('#editUrlNodeType').val();
			var nodeClass = $('#nodeClass').val();
			var old_sn = $('#editUrlNodeOldSn').val();
			
			if(type == 2) {
					$.ajax({
			 			url:"<%=basePath%>tree/validSN",
			 			dataType:'text',
			 			type:"post",
			 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			 			data:{sn:sn,action:'update',oldSn:old_sn},
			 			success:function(result){
			 				     if(result > 0) {
			 				    	$.ajax({
			 				 			url:"<%=basePath%>tree/editUrl",
			 				 			dataType:'text',
			 				 			type:"post",
			 				 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			 				 			data:{nodeid:nodeid,url:url,sn:sn,nodeClass:nodeClass},
			 				 			success:function(result){	 				
			 			                   if(result > 0) {
			 			                	   layer.msg('节点属性设置成功!', {icon: 1,time:1000}); 
			 			                	   var zTree = $.fn.zTree.getZTreeObj("tree");
			 			                	   var node = zTree.getNodeByParam("id", nodeid, null);
			 			                	   node.myUrl = url;
			 			                	   node.sn = sn;
			 			                	   node.nodeClass = nodeClass;
			 			                	   zTree.updateNode(node);
			 			                	   $('#editUrlModal').modal('hide');               	   
			 			                   }
			 				 			}
			 				 		});				    	 
			 				     }else {
			 				    	layer.alert('sn值已存在或sn值为空', {
			 						    icon: 3,
			 						    skin: 'layer-ext-moon' 
			 						})
			 				     }
			 			}
			 		});		
			
			}else {
					$.ajax({
				 			url:"<%=basePath%>tree/editUrl",
				 			dataType:'text',
				 			type:"post",
				 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				 			data:{nodeid:nodeid,url:url},
				 			success:function(result){	 				
			                   if(result > 0) {
			                	   layer.msg('节点属性设置成功!', {icon: 1,time:1000}); 
			                	   var zTree = $.fn.zTree.getZTreeObj("tree");
			                	   var node = zTree.getNodeByParam("id", nodeid, null);
			                	   node.myUrl = url;
			                	   zTree.updateNode(node);
			                	   $('#editUrlModal').modal('hide');               	   
			                   }
				 			}
				 		});	
			}
		}
		
		//一键打开或关闭节点
		function openNode(ztreeId,open) {
			var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
			treeObj.expandAll(open);
		}
		
		//把正在编辑的节点的id保存在隐藏输入框
		function saveEditingNodeId(id) {
			$('#editingNodeId').val(id);
		}
		
		//判断删除的节点是否当前编辑节点
		function checkEditingNode(removeId) {
			var id = $('#editingNodeId').val();
			if(removeId == id) {
				$('#treeValueIframe').attr('src','');
			}
		}
		
        //为新增节点按钮绑定事件
        $(function() {
    		$("#permissionManage").bind("click", permissionManage);
    		
        })      
        
        //打开编辑跳转地址模态框
        function editUrl() {       	
        	$('#editUrlModal').modal('show');
        	return false;
        }
        
        var templates = new Array();
        var count = 0;
        
        //添加模板属性到页面
        function addTemplateItemHTML(attr_name, attr_type, attr_type_name) {
        	if(attr_type == 'enum') {
        		$item = $('.template-item2-model').clone(true);
        		$item.removeClass('template-item2-model');
        		$item.addClass('template-item');
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
        		$item = $('.template-item1-model').clone(true);
        		$item.removeClass('template-item1-model');
        		$item.addClass('template-item');
        		$item.find('.template-name').html(attr_name);
        		$item.find('.template-type-name').html(attr_type_name);
        		$item.find('.template-type').val(attr_type);
        		$('#add_template_button').before($item);
        	}
        }
        
        function addTemplateAttr() {
        	var attr_name = $('#attr_name').val();
        	for ( var i in templates) {
				if(attr_name == templates[i]) {
					layer.msg('属性名称已定义!', {icon: 2,time:2000}); 
					return;
				}
			}
        	var attr_type = $('#attr_type').val();
        	var attr_type_name = $('#attr_type').find('option:selected').html();
        	if(attr_name == "") {
        		layer.msg('属性名称不能为空!', {icon: 2,time:2000}); 
        		return;
        	}
        	addTemplateItemHTML(attr_name, attr_type, attr_type_name)
        	templates[count++] = attr_name;
        	$('#add_template_modal').modal('hide');
        }
        
		//绑定事件
		$(function() {			
			$('.content_wrap').on('click',function(e){
				var id = e.target.id;
				id = id.substring(id.length - 4,id.length);
				if(id != 'span') {
					cancalFocus();
				}
			})
			
			$('#userList').on('change',function(e){
				var userId = $(this).val();
				$('#editPSUser').val(userId);
				getUserPermission(userId);
			})
			
			$('.searchable-select-item').on('click',function(e){
				var userId = $(this).attr('data-value');
				$('#editPSUser').val(userId);
				getUserPermission(userId);
			})
			
			$('#searchNode').on("click",function(){
				searchNode('tree','nodeSearchKey');
			});
			
			$('#nodeSearchKey').on("keydown",function(event){
				if(event.keyCode == 13) {
					searchNode('tree','nodeSearchKey');
				}
			});
			
			$('#pssearchNode').on("click",function(){
				searchNode('permissionTree','psnodeSearchKey');
			});
			
			$('#psnodeSearchKey').on("keydown",function(e){
				if(e.keyCode == 13) {
					searchNode('permissionTree','psnodeSearchKey');
				}
			});
			
			$('#addNodeType').on('change', function() {
				var type_id = $(this).val();
				if(type_id == 2) {
					$('.equipment-space').css('display','');
				}else {
					$('.equipment-space').css('display','none');
				}
			});
			
			$('#add_template_checkbox').on('change', function() {
				var checked = $(this).is(':checked');
				if(checked) {		
					$(".template-space").fadeIn();
				}else {
					$(".template-space").fadeOut();
				}
			});
			
			$('#manage_template_checkbox').on('change', function() {
				var checked = $(this).is(':checked');
				if(checked) {		
					$(this).parent().find(".manage-template-space").fadeIn();
				}else {
					$(this).parent().find(".manage-template-space").fadeOut();
				}
			});
			
			$('#add_template_button').on('click', function() {
				$('#add_template_modal').modal('show');
			});
			
			$('#manage_add_template_button').on('click', function() {
				$('#manage-add_template_modal').modal('show');
			});
			
			$('#attr_type').on('change', function() {
				if($(this).val() == 'enum') {
					$('#template_option_group').fadeIn();
				}else {
					$('#template_option_group').fadeOut();
				}
			});
			
			$('#template_option_button_add').on('click', function() {
				$option = $('.template-option-model').clone(true);
				$option.removeClass('template-option-model');
				$option.addClass('template-option-item');
				$('#template_option_button_add').before($option);
			});
			
			$('.template-option-button-delete').on('click', function() {
				$(this).parent().parent().parent().remove();
			});
			
			$('.delete-template-button').on('click', function() {
				$(this).parent().parent().remove();
			})
		})
		
	</script>
</body>

</html>
