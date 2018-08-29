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
  .modal-input {
     margin-bottom: 15px;
  }
</style>
</head>

<body>

                        <div class="row">
            <div class="col-sm-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <h5>节点数据</h5>
                        <div class="ibox-tools">
                            <button class="btn btn-info btn-xs" onclick="addModal()">创建新数据</button>
                        </div>
                    </div>
                    <div class="ibox-content">
                        <div class="row m-b-sm m-t-sm">
                            <div class="col-xs-6">
                                <h2 style="color:#0070ff">${nodeName }(id:${nodeId })</h2>
                            </div>
<!--                             <div class="col-xs-6"> -->
<%--                                <form action="<%=basePath%>tree/treeValue" method="post"> --%>
<!--                                 <div class="input-group"> -->
<!--                                     <input placeholder="请输入数据键值" class="input-sm form-control" name="like" type="text"> <span class="input-group-btn"> -->
<%--                                     <input name="nodeId" type="hidden" value="${nodeId }"> --%>
<%--                                     <input name="nodeName" type="hidden" value="${nodeName }"> --%>
<!--                                     <button type="submit" class="btn btn-sm btn-primary"> 搜索</button> </span> -->
<!--                                 </div> -->
<!--                                </form> -->
<!--                             </div> -->
                        </div>
					<div class="row m-b-sm m-t-sm">
                            <div class="col-xs-12">
                            <button id="target_btn" type="button" class="btn btn-outline btn-info">目标信息&nbsp;&nbsp;<i class="fa fa-question-circle"></i></button>
                            <div style="display:none;">
                            <div id="target_message">
                            	<span style="color: #4f9a56;font-size: 15px;">ip:</span><span>${host.host_ip }</span>&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: #4c4eab;font-size: 15px;">端口:</span><span>${host.host_port }</span>
                            </div>
                            </div>
                            </div>
                    </div>
                        <div class="project-list">
                            <table class="table table-hover">
                                       <thead>
                                            <tr>
                                                <th style="width: 5%;"><input class="thead-checkbox" type="checkbox"></th>
                                                <th>内容</th>
                                                <th width="20%"><button type="button" class="btn btn-sm btn-danger" onclick="deleteChecked()">删除选中</button></th>

                                            </tr>
                                        </thead>
                                        
                                   <tbody class="value-space">
                                        <!-- 数据信息模板 --------------------------------------------->
				                            <tr class="value-model" style="display: none">           
                                                <td><input value-id=""  class="tbody-checkbox-model" type="checkbox"></td>                           
		                                        <td class="project-completion">
		                                                <small class="value-data"></small>
		                                        </td>
		                                        <td class="project-actions">
		                                            <button class="btn btn-white btn-sm btn-delete"><i class="fa fa-trash"></i> 删除 </button>
		                                            <button class="btn btn-white btn-sm btn-edit"><i class="fa fa-pencil"></i> 编辑 </button>
		                                        </td>
						                   </tr>
						              <!-------------------------------------------------------------->
	                                   <c:forEach items="${values }" var="value">
		                                    <tr id="tr${value.value_id }">                                
		                                        <td><input value-id="${value.value_id }" class="tbody-checkbox" type="checkbox"></td>       
		                                        <td class="project-completion">
		                                                <small class="value-data">${value.value_data }</small>
		                                        </td>
		                                        <td class="project-actions">
		                                            <button class="btn btn-white btn-sm btn-delete" onclick="deleteOne(${value.value_id })"><i class="fa fa-trash"></i> 删除 </button>
		                                            <button class="btn btn-white btn-sm btn-edit" onclick="editValue(${value.value_id })"><i class="fa fa-pencil"></i> 编辑 </button>
		                                        </td>
		                                    </tr>
	                                    </c:forEach>
                                    </tbody>
                                </table>
                                <c:if test="${values.size()<=0 }">
                                      <h3 id="noData" align="center" >没有查询到数据  </h3>           
			                    </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

	<!--新增协议传输模态框  -->
	<div class="modal fade" tabindex="-1" role="dialog" data-backdrop="false" id="addModal">
	 	<div class="modal-dialog modal-xs">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>创建新数据</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	 			    <c:forEach items="${protocols }" var="p" varStatus="state">
	                     <div>
			 			    <span>${p.identification }</span>(${p.name }):<input class="input-sm form-control modal-input" type="text" id="attr_${state.index }" placeholder="输入${p.name }">
			 			 </div>
		 			 </c:forEach>
		 			 <c:if test="${protocols == null}">
                         <h3 align="center" >请先添加协议  </h3>           
			         </c:if>
		 		</div>
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="addData(this)">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 <!--新增透明传输模态框  -->
	<div class="modal fade" tabindex="-1" role="dialog" data-backdrop="false" id="addUTModal">
	 	<div class="modal-dialog modal-xs">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>创建新数据</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	                     <div>
			 			    <span>透明传输数据</span>:<br>
			 			    <textarea class="input-sm form-control modal-input" rows="5" id="add_UT_data" placeholder="输入数据内容"></textarea>
			 			 </div>
		 		</div>
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="addUTData()">确定</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	 <!--修改模态框  -->
	<div class="modal fade" tabindex="-1" data-backdrop="false" id="updateModal">
	 	<div class="modal-dialog">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>更新数据</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
                  <c:forEach items="${protocols }" var="p" varStatus="state">
	                     <div>
			 			    <span>${p.identification }</span>(${p.name }):<input class="input-sm form-control modal-input" type="text" id="attr_${state.index }_update" placeholder="输入${p.name }">
			 			 </div>
		 			 </c:forEach>
	 			  <input type="hidden" id=updateDataId>
		 		</div>
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="updateValue(this)">保存</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
     <!--修改透明传输模态框  -->
	<div class="modal fade" tabindex="-1" role="dialog" data-backdrop="false" id="updateUTModal">
	 	<div class="modal-dialog modal-xs">
	 		<div class="modal-content">
	 			<div class="modal-header">
	 				<button class="close" data-dismiss="modal">&times;</button>
	 				<h4>修改数据</h4>
	 			</div>
	 			
	 			<div class="modal-body" style="margin-left:30px">
	                     <div>
			 			    <span>透明传输数据</span>:<br>
			 			    <textarea class="input-sm form-control modal-input" rows="5" id="update_UT_data" placeholder="输入数据内容"></textarea>
			 			 </div>
		 		</div>
		 		<div class="modal-footer">
		 			<button type="button" class="btn btn-sm btn-success" onclick="updateUTValue(this)">保存</button>
		 			<button type="button" class="btn btn-sm btn-default" data-dismiss="modal">取消</button>
		 		</div>
		 	</div>
	 	</div>
	 </div>
	<!-- 全局js -->
	<script src="<%=basePath%>hAdmin/js/jquery.min.js?v=2.1.4"></script>
	<script src="<%=basePath%>hAdmin/js/bootstrap.min.js?v=3.3.6"></script>


	<script type="text/javascript">
	   var tsType = "${tsType}";	
	   var length = "${ArrayLength}";
	
	   $(function() {
		   parent.saveEditingNodeId('${nodeId }');
		   $('#target_btn').popover({
	      	  html:true,
	      	  content:$('#target_message')
	       });	
	   })
	    
	   //显示添加数据模态框
	   function addModal() {
		   if(tsType == 0) {
			   $('#addModal').modal('show');
		   }else if(tsType == 1) {
			   $('#addUTModal').modal('show');
		   }
	   }
	   
	   //新增协议传输节点数据
	   function addData() {
		   var jsonStr = '';

		   if(length != "" && length > 0) {
			   jsonStr = '{';
			   for(var i=0; i < length; i++) {
					   jsonStr += '"' + $("#attr_" + i).prev("span").html() + '":';
					   jsonStr += '"' + $("#attr_" + i).val() + '"';
					   if(i != length - 1) {
						   jsonStr += ',';  
					   }
				   }	
			   jsonStr += '}';
			}else {
				return;
			}

				$.ajax({
	 	 			url:"<%=basePath%>tree/addData",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{id:"${nodeId}",data:jsonStr},
		 			success:function(result){
						if(parseInt(result) > 0) {
							addValueItem(jsonStr,result);
							parent.layer.msg('新增数据成功!', {icon: 1});  
						} else{
							parent.layer.msg('新增数据失败!', {icon: 2});  
						}
		 			}
		 		});
		  		  
	   }
	   
	   //新增透传节点数据
	   function addUTData() {
		   var data = $('#add_UT_data').val();
		   $.ajax({
	 			url:"<%=basePath%>tree/addData",
	 			dataType:'text',
	 			type:"post",
	 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	 			data:{id:"${nodeId}",data:data},
	 			success:function(result){
					if(parseInt(result) > 0) {
						addValueItem(data,result);
						parent.layer.msg('新增数据成功!', {icon: 1});  
					} else{
						parent.layer.msg('新增数据失败!', {icon: 2});  
					}
	 			}
	 		});
	   }
	   
	   //添加数据到页面
	   function addValueItem(data,id) {
		   $('#noData').hide();
		   $valueModel = $('.value-model:eq(0)').clone(true);
		    $valueModel.attr('id','tr' + id);
			$valueModel.removeClass("value-model");
			$valueModel.find('.tbody-checkbox-model').attr('value-id',id);
			$valueModel.find('.tbody-checkbox-model').addClass('tbody-checkbox');
			$valueModel.find('.tbody-checkbox-model').removeClass('tbody-checkbox-model');
			$valueModel.find('.value-data').html(data);
			
			$valueModel.find('.btn-delete').attr('onclick','deleteOne(' + id + ')');
			$valueModel.find('.btn-edit').attr('onclick','editValue(' + id + ')');
			$('.value-space').prepend($valueModel);
			$valueModel.show();
			$('#addModal').modal('hide');
			$('#addUTModal').modal('hide');
	   }
	   
	   $(function() {
		   $('.thead-checkbox').bind('change',function() {
			   if($(this).is(':checked')) {
				   $('.tbody-checkbox').prop('checked', true);
			   } else {
				   $('.tbody-checkbox').prop('checked', false);
			   }
		   })
	   })
	   
	   //删除一条节点数据
	   function deleteOne(id) {
		        parent.layer.confirm('确定删除该数据？', {
	       	    btn: ['确定','取消'], //按钮
	       	    shade: false //不显示遮罩
		       	}, function(){
		       	 $.ajax({
			 			url:"<%=basePath%>tree/deleteValue",
			 			dataType:'text',
			 			type:"post",
			 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			 			data:{ids:id},
			 			success:function(result){
							if(parseInt(result) > 0) {
						    	$('#tr' + id).remove();
						    	parent.layer.msg('删除数据成功!', {icon: 1});  
							} else{
								parent.layer.msg('删除数据失败', {icon: 2});
							}
			 			}
			 		});
		       	}, function(){
		
		       	});
	   }
	   
	   //删除勾选上的节点数据
	   function deleteChecked() {
		   var ids = new Array();
		   var i = 0;
		   $('.tbody-checkbox').each(function() {
			   if($(this).is(':checked')) {
				   var id = $(this).attr('value-id');
				   ids[i++] = id;
			   }
		   })
		   var str = groundContext(ids);
		   if(str == "") {
			   parent.layer.alert('请先算中要删除的数据', {
				    icon: 3,
				    skin: 'layer-ext-moon' 
				})
			   return;
		   }
		   
		   parent.layer.confirm('确定删除选中数据？', {
       	    btn: ['确定','取消'], //按钮
       	    shade: false //不显示遮罩
	       	}, function(){
	 		   $.ajax({
		 			url:"<%=basePath%>tree/deleteValue",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{ids:str},
		 			success:function(result){
						if(parseInt(result) > 0) {
							    for(var key in ids) {
							    	$('#tr' + ids[key]).remove();
							    }
							    parent.layer.msg('删除数据成功!', {icon: 1});      
						} else{
							    parent.layer.msg('删除数据失败', {icon: 2});
						}
		 			}
		 		});
	       	   
	       	}, function(){
	
	       	});
	   }
	   
	   //初始化编辑数据模态框
	   function editValue(id) {
		   var data = $('#tr' + id).find('.value-data').html();
		   if(tsType == 0) {
			   var jsonObj =jQuery.parseJSON(data);
			   for(var i=0; i < length; i++) {
				   var identification = $("#attr_" + i).prev("span").html();
	               var value = eval("jsonObj." + identification);
	               $("#attr_" + i + "_update").val(value);
			   }
			   $('#updateDataId').val($('#tr' + id).find('.tbody-checkbox').attr('value-id'));
			   $('#updateModal').modal('show');
		   }else {
			   $('#updateDataId').val($('#tr' + id).find('.tbody-checkbox').attr('value-id'));
			   $('#update_UT_data').val(data);
			   $('#updateUTModal').modal('show');
		   }
		   
		  
	   }
	   //更新协议传输节点数据
	   function updateValue(obj) {
		      $(obj).attr('disabled','disabled');	 
			  var id = $('#updateDataId').val();
			  
			   var jsonStr = '';
			   if(length != "") {
				   jsonStr = '{';
				   for(var i=0; i < length; i++) {
						   jsonStr += '"' + $("#attr_" + i + "_update").prev("span").html() + '":';
						   jsonStr += '"' + $("#attr_" + i + "_update").val() + '"';
						   if(i != length - 1) {
							   jsonStr += ',';  
						   }
				   }	
				   jsonStr += '}';
			  }
			  $.ajax({
		 			url:"<%=basePath%>tree/updateValue",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{id:id,data:jsonStr,nodeId:'${nodeId}'},
		 			success:function(result){
						if(parseInt(result) > 0) {
							$('#tr' + id).find('.value-data').html(jsonStr);
							$('#updateModal').modal('hide');
							$(obj).removeAttr('disabled');
						    parent.layer.msg('更新数据成功!', {icon: 1}); 										    
						} else{
						    parent.layer.msg('更新数据失败', {icon: 2});
						}
						
		 			}
		 		});

		  
	   }
	   
	   //更新透传节点数据
	   function updateUTValue(obj) {
		      $(obj).attr('disabled','disabled');	 
			  var id = $('#updateDataId').val();
			  var data = $('#update_UT_data').val();
			  $.ajax({
		 			url:"<%=basePath%>tree/updateValue",
		 			dataType:'text',
		 			type:"post",
		 			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		 			data:{id:id,data:data,nodeId:'${nodeId}'},
		 			success:function(result){
						if(parseInt(result) > 0) {
							$('#tr' + id).find('.value-data').html(data);
							$('#updateUTModal').modal('hide');
							$(obj).removeAttr('disabled');
						    parent.layer.msg('更新数据成功!', {icon: 1}); 										    
						} else{
						    parent.layer.msg('更新数据失败', {icon: 2});
						}
						
		 			}
		 		});	  
	   }
	   //将数组转换为字符串
	   function groundContext(array) {
		   var result = "";
		   for(var i = 0;i < array.length;i++) {
			   if(i == 0) {
				   result = result + array[i];
			   } else {
				   result = result + ',' + array[i];
			   }
		   }
		   return result;
	   }
	   
	</script>
</body>

</html>
