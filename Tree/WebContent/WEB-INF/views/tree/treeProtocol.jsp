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
<style type="text/css">
   .protocol-model {
      display:none;
   }
   .protocol-item:hover {
      cursor: pointer;
      background-color: rgba(200,200,200,0.5);
   }
</style>
</head>

<body>

                   <!-- 协议模板 --------------------------------------------------------------------------------------------------------->
                   <div class="col-sm-10 col-sm-offset-1 protocol-item protocol-model" style="border: 1px solid #ccc;height: 40px;margin-top: 20px">
	                     <span class="col-xs-3" style="margin-top: 10px;">
						    <label style="display: inline;float: left;font-size: 10px;color: #a4a5a7;font-weight: 500;">显示名称：</label>
						    <span class="protocol-name" style="font-size: 10px;color: #131212;">${p.name }</span>
						  </span>
						  <span class="col-xs-3" style="margin-top: 10px;">
						    <label style="display: inline;float: left;font-size: 10px;color: #a4a5a7;font-weight: 500;">标识名：</label>
						    <span class="protocol-identification" style="font-size: 10px;color: #131212;">${p.identification}</span>
						  </span>
						  <span class="col-xs-3" style="margin-top: 10px;">
						    <label style="display: inline;float: left;font-size: 10px;color: #a4a5a7;font-weight: 500;">备注：</label>
						    <span class="protocol-remark" style="font-size: 10px;color: #131212;">${p.remark}</span>
						  </span>
	                </div>
                    <!------------------------------------------------------------------------------------------------------------------->
   <div class="row">
    			<div class="col-sm-5 col-sm-offset-1 tr" style="padding-right: 0px;">               
		              <span id="pro_node_name" style="font-size: 25px;color:#5382ab;">${nodeName }(id:${nodeId})</span>                
		        </div>
    			<div class="col-sm-2 col-sm-offset-3 tr" style="padding-right: 5px;">               
		              <a type="button" class="btn btn-default" style="margin-left: 10px" data-toggle="modal" data-target="#protocol_add">
		                + 新建数据点
		              </a>                   
		        </div>
	</div>
    <div class="row protocol-space">
               <c:forEach items = "${protocol }" var = "p">
	                <div class="col-sm-10 col-sm-offset-1 protocol-item" id="protocol_${p.identification }" style="border: 1px solid #ccc;height: 40px;margin-top: 20px">
	                     <span class="col-xs-3" style="margin-top: 10px;">
						    <label style="display: inline;float: left;font-size: 10px;color: #a4a5a7;font-weight: 500;">显示名称：</label>
						    <span class="protocol-name" style="font-size: 10px;color: #131212;">${p.name }</span>
						  </span>
						  <span class="col-xs-3" style="margin-top: 10px;">
						    <label style="display: inline;float: left;font-size: 10px;color: #a4a5a7;font-weight: 500;">标识名：</label>
						    <span class="protocol-identification" style="font-size: 10px;color: #131212;">${p.identification}</span>
						  </span>
						  <span class="col-xs-6" style="margin-top: 10px;">
						    <label style="display: inline;float: left;font-size: 10px;color: #a4a5a7;font-weight: 500;">备注：</label>
						    <span class="protocol-remark" style="font-size: 10px;color: #131212;">${p.remark}</span>
						  </span>
	                </div>
                </c:forEach>
    </div>
    	<!--新增模态框  -->
	<div class="modal fade" tabindex="-1" role="dialog" data-backdrop="false" id="protocol_add">
	 	<div class="modal-dialog modal-xs">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>创建数据点</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
		 			 <div>
		 			    显示名称:<input class="input-sm form-control modal-input" type="text" id=add_protocol_name placeholder="输入数据点显示名称">
		 			 </div>
		 			 <div style="margin-top: 15px;">
		 			    标识名:<input class="input-sm form-control modal-input" type="text" id=add_protocol_identification placeholder="输入数据点标识名">
		 			 </div>
		 			 <div style="margin-top: 15px;">
		 			    备注:<textarea class="form-control modal-input" id=add_protocol_remark rows="8" placeholder="输入备注"></textarea>
		 			 </div>
		 		</div>
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="addProtocol()">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 <!--更新模态框  -->
	<div class="modal fade" tabindex="-1" role="dialog" data-backdrop="false" id="protocol_update">
	 	<div class="modal-dialog modal-xs">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>更新数据点</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
		 			 <div>
		 			    显示名称:<input class="input-sm form-control modal-input" type="text" id=update_protocol_name placeholder="输入数据点显示名称">
		 			 </div>
		 			 <div style="margin-top: 15px;">
		 			    标识名:<input class="input-sm form-control modal-input" type="text" id=update_protocol_identification placeholder="输入数据点标识名">
		 			 </div>
		 			 <div style="margin-top: 15px;">
		 			    备注:<textarea class="form-control modal-input" id=update_protocol_remark rows="8" placeholder="输入备注"></textarea>
		 			 </div>
		 			 <input type="hidden" id=update_protocol_oldid placeholder="输入数据点标识名">
		 		</div>
		 		<div class="modal-footer">
		 		    <button type="button" class="btn btn-sm btn-danger" onclick="deleteProtocol()">删除</button>
		 			<button type="button" class="btn btn-sm btn-success" onclick="updateProtocol()">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	<!-- 全局js -->
	<script src="<%=basePath%>hAdmin/js/jquery.min.js?v=2.1.4"></script>
	<script src="<%=basePath%>hAdmin/js/bootstrap.min.js?v=3.3.6"></script>
    <script type="text/javascript">
         var nodeId = "${nodeId}";
    
         //添加协议
         function addProtocol() {
        	 var name = $('#add_protocol_name').val();
        	 var identification = $('#add_protocol_identification').val();
        	 var remark = $('#add_protocol_remark').val();
        	 var valid = false;
        	 
        	 if(identification == '') {
        		 parent.layer.alert('标识名不能为空', {
					    icon: 2,
					    skin: 'layer-ext-moon' 
				  })
				  return;
        	 }      
        	 //验证标识名是否存在
        	 $.ajax({
		 			url:"<%=basePath%>tree/validProtocol",
		 			dataType:'text',
		 			type:"post",
		 			async:false,
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{identification:identification, action:'add', nodeId:'${nodeId}'},
		 			success:function(result){
						if(parseInt(result) > 0) {
						    valid = true;
						}else {
							parent.layer.msg('标识名已存在!', {icon: 2}); 	
				        	$('#protocol_add').modal('hide');
				        	valid = false;
						}
		 			}
		 		});
        	 
        	 if(valid) {
        		 ajaxAddProtocol(name ,identification, remark); 
        	 }	 
         }        
         
         //通过ajax新增协议
         function ajaxAddProtocol(name ,identification, remark) {
        	 $.ajax({
		 			url:"<%=basePath%>tree/addProtocol",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{name:name, identification:identification, remark:remark, nodeId:'${nodeId}'},
		 			success:function(result){
						if(parseInt(result) > 0) {
						    parent.layer.msg('协议添加成功!', {icon: 1}); 	
				        	 $('#protocol_add').modal('hide');
				        	 addProtocolHTML(name ,identification, remark)
						} else{
						    parent.layer.msg('协议添加失败', {icon: 2});
						    $('#protocol_add').modal('hide');
						}
						
		 			}
		 		});
         }
         
         //在页面上添加协议标签
         function addProtocolHTML(name ,identification, remark) {
			    $protocol = $('.protocol-model').clone(true);
			    $protocol.removeClass('protocol-model');
			    $protocol.attr('id', 'protocol_' + identification);
			    $protocol.find('.protocol-name').html(name);
			    $protocol.find('.protocol-identification').html(identification);
			    $protocol.find('.protocol-remark').html(remark);
			    $('.protocol-space').append($protocol);
         }
         
         //初始化更新协议模态框
         function initUpdateProtocolModal(name, id, remark) {
        	    $('#update_protocol_name').val(name);
        	    $('#update_protocol_identification').val(id);
        	    $('#update_protocol_remark').val(remark);
        	    $('#update_protocol_oldid').val(id);       	    
         }
         
         //更新协议
         function updateProtocol() {
        	 var name = $('#update_protocol_name').val();
     	     var id = $('#update_protocol_identification').val();
     	     var remark = $('#update_protocol_remark').val();
     	     var oldId = $('#update_protocol_oldid').val();
        	 var valid = false;
        	 
        	 if(id == '') {
        		 parent.layer.alert('标识名不能为空', {
					    icon: 2,
					    skin: 'layer-ext-moon' 
				  })
				  return;
        	 }  
        	 
     	        //验证标识名是否存在
        	    $.ajax({
		 			url:"<%=basePath%>tree/validProtocol",
		 			dataType:'text',
		 			type:"post",
		 			async:false,
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{identification:id, oldId:oldId, action:'update', nodeId:'${nodeId}'},
		 			success:function(result){
						if(parseInt(result) > 0) {
						    valid = true;
						}else {
							parent.layer.msg('标识名已存在!', {icon: 2}); 	
				        	$('#protocol_add').modal('hide');
				        	valid = false;
						}
		 			}
		 		});
     	        if(valid) {
     	        	ajaxUpdateProtocol(name, id, remark, oldId);
     	        }
         }
         
         //通过ajax更新协议
         function ajaxUpdateProtocol(name, id, remark, oldId) {
        	 $.ajax({
		 			url:"<%=basePath%>tree/updateProtocol",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{name:name, identification:id, remark:remark, oldId:oldId, nodeId:'${nodeId}'},
		 			success:function(result){
		 				if(parseInt(result) > 0) {
						    parent.layer.msg('协议更新成功!', {icon: 1}); 	
				        	 $('#protocol_update').modal('hide');
				        	 updateProtocolItem(name,id,remark,oldId);
						} else{
						    parent.layer.msg('协议更新失败', {icon: 2});
						    $('#protocol_update').modal('hide');
						}
		 			}
		 		});
         }
         
         //更新页面上的协议标签
         function updateProtocolItem(name,id,remark,oldId) {
        	   $('#protocol_' + oldId).find('.protocol-name').html(name);
        	   $('#protocol_' + oldId).find('.protocol-identification').html(id);
        	   $('#protocol_' + oldId).find('.protocol-remark').html(remark);
        	   $('#protocol_' + oldId).attr('id', 'protocol_' + id);   
         }
         
         //删除协议
         function deleteProtocol() {
        	 var id = $('#update_protocol_oldid').val(); 
        	 parent.layer.confirm('确定删除该数据点？', {
 	       	    btn: ['确定','取消'], //按钮
 	       	    shade: false //不显示遮罩
 		       	}, function(){
 		       	 $.ajax({
 			 			url:"<%=basePath%>tree/deleteProtocol",
 			 			dataType:'text',
 			 			type:"post",
 			 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
 			 			data:{id:id,nodeId:'${nodeId}'},
 			 			success:function(result){
 							if(parseInt(result) > 0) {
 						    	$('#protocol_' + id).remove();
 						    	parent.layer.msg('删除数据点成功!', {icon: 1});  
 						    	$('#protocol_update').modal('hide');
 							} else{
 								parent.layer.msg('删除数据点失败', {icon: 2});
 								$('#protocol_update').modal('hide');
 							}
 			 			}
 			 		});
 		       	}, function(){
 		
 		       	});
         }
         
         $(function() {
        	 $('.protocol-item').bind('click', function() {
        		 $('#protocol_update').modal('show');
        		 var name = $(this).find('.protocol-name').html();
        		 var id = $(this).find('.protocol-identification').html();
        		 var remark = $(this).find('.protocol-remark').html();
        		 initUpdateProtocolModal(name, id, remark);
        	 });
         })
    </script>
</body>

</html>
