<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../admin/adminCss.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册页面</title>
<link rel="stylesheet" href="<%=basePath %>/zTree/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath%>elementui/element-ui.css">
<link rel="stylesheet" href="<%=basePath%>css/register.css">

<!-- <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css"> -->
<link>
</head>
<body>
	<!-- 页面主要使用element-ui的模板，具体文档查看http://element-cn.eleme.io -->
	<div id="app">
		<transition name="el-fade-in">
		<div class="box" v-show="showBox" style="display:none">
			<div class="box-header">
				<div class="title">用户注册</div>
				<div class="tip">
					我已经注册，现在就
					<a class="text-primary" href="<%=basePath%>login">登录</a>
				</div>
			</div>
			
			<div class="box-body">
				<el-form ref="form" id="form" :model="form" label-width="0" :rules="rules" status-icon  method="post" action="<%=basePath %>register/doRegister" enctype="multipart/form-data">
				   <el-form-item prop="username">
				  	  <label class="label-required">用户名</label>
				      <el-input v-model="form.username" placeholder="5~16位英文、数字组合" name="username"></el-input>
				   </el-form-item>
				   
				   <el-form-item prop="password">
				  	  <label class="label-required">密码</label>
				      <el-input v-model="form.password" placeholder="密码长度为5~16位" type="password" name="password"></el-input>
				   </el-form-item>
				   
				    <el-form-item prop="repeatPassword">
				  	  <label class="label-required">密码确认</label>
				      <el-input v-model="form.repeatPassword" placeholder="确认密码" type="password" ></el-input>
				   </el-form-item>
				   
				   <el-form-item prop="nickname">
				  	  <label class="label-required">昵称</label>
				      <el-input v-model="form.nickname" placeholder="英文、数字或汉字组合" name="nickname"></el-input>
				   </el-form-item>
				   
				   <el-form-item prop="email">
				  	  <label class="">邮箱</label>
				      <el-input v-model="form.email" placeholder="输入邮箱" name="email"></el-input>
				   </el-form-item>
				   
				   <el-form-item prop="phone">
				  	  <label class="">手机号码</label>
				      <el-input v-model="form.phone" placeholder="输入手机号码" name="phone"></el-input>
				   </el-form-item>
				   
				   <el-form-item prop="phone">
				  	  <label class="">头像</label>
				      <div class="img-container">
				      	<div class="file-container">
				      		<i class="el-icon-plus file-icon" v-show="showIcon"></i>
				      		<img alt="" class="file-img" id="img_show" src="">
				      		<div class="file-mask">
				      			<span class="fa fa-camera mask-icon"></span>
				      		</div>
				      		<input type="file" class="file-input" title="点击选择头像" accept="image/*" onchange="changePic(this)" name="file">
				      	</div>
				      </div>
				   </el-form-item>
				   
				   <el-form-item prop="phone" class="div-group">
				  	  <label class="label-required label-inline">性别</label>
				      <div class="div-container">
				      	<el-radio-group v-model="form.sex">
					      <el-radio label="0" name="sex"><span class="text-primary">男<i class="fa fa-mars fa-fw"></i></span></el-radio>
					      <el-radio label="1" name="sex"><span class="text-red">女<i class="fa fa-venus fa-fw"></i></span></el-radio>
					   </el-radio-group>
				      </div>
				   </el-form-item>
				   <el-form-item class="div-group">
				   	  <label class="label-required label-inline">用户组</label>
				   	  <div class="div-container label-inline">
				   	  	 <el-tooltip class="item" placement="right-start" effect="dark">
				   	  	 	 <div  slot="content" style="width: 200px;">单独注册学生组不需要管理员审核，选择其他用户组都需要管理员审核通过才能登录。学生组和教师组需要选择班级，学生组最多只能选择一个班级，教师组班级可多选</div>
				   	  		<el-button type="primary"  size="small" title="选择用户组" onclick="showUsergroup()"><i class="fa fa-plus fa-fw"></i>选择用户组</el-button>
				   	  	</el-tooltip>
				   	  </div>
				   	  
				   </el-form-item class="div-group">
				   <el-collapse-transition>
					   <el-form-item v-show="form.usergroupTags.length > 0">
					   		<div class="flex-row">
					   			<label class="label-inline">已选择</label>
					   		 	<div class="div-container">
							   	  	 <el-tag v-for="tag in form.usergroupTags"  :disable-transitions="false" :key="tag" closable size="medium" @close="handleClose(tag)">
							   	  	 	{{tag.ug_name}}
							   	  	 </el-tag>
						   	  	 </div>
					   		</div>
					   </el-form-item>
				   </el-collapse-transition>
				    <transition name="el-fade-in">
				    	<div v-show="isShowClass">
						   <el-form-item class="div-group" >
						   		<label class="label-required label-inline">班级</label>
						   		<div class="div-container ">
						   			<el-button type="success"  size="small" title="选择班级" onclick="showClassDialog()"><i class="fa fa-plus fa-fw"></i>选择班级</el-button>
						   		</div>
						   </el-form-item>
						   
						   <el-collapse-transition>
							   <el-form-item v-show="form.classTags.length > 0" class="div-group">
							   		<div class="flex-row">
							   			<label class="label-inline">已选择</label>
							   		 	<div class="div-container">
									   	  	 <el-tag v-for="tag in form.classTags" type="success"  :disable-transitions="false" :key="tag" closable size="medium" @close="handleClassClose(tag)">
									   	  	 	{{tag.fullname}}
									   	  	 </el-tag>
								   	  	 </div>
							   		</div>
							   </el-form-item>
						   </el-collapse-transition>
					   </div>
				    </transition>
				    <el-form-item>
				    	<el-button :disabled="isDisabled" type="primary" size="medium" class="btn-register" @click="submit('form')">注册</el-button>
				    </el-form-item>
				    
				    <!-- 用于储存用户组和班级 -->
				    <div style="display:none">
				    	<input name="usergroup" id="usergroup">
				    	<input name="userclass"id="useclass">
				    </div>
				</el-form>
			</div>
		</div>
		</transition>
		
		<!-- 用户组选择对话框内容 -->
		<el-dialog
		  title="用户组选择"
		  :visible.sync="showGroupDialog"
		  width="20%">
		  <div class="dialog" >
				<div class="dialog-container">
					<el-checkbox :label="usergroup.ug_id" border v-for="usergroup in usergroups"  v-model="usergroup.isChecked">{{usergroup.ug_name}}</el-checkbox>
					
				</div>
			</div>
		  <span slot="footer" class="dialog-footer">
		 	 <el-button size="small" type="primary" @click="selectUsergroup()">确 定</el-button>
		    <el-button size="small" @click="showGroupDialog = false">取 消</el-button>
		  </span>
		</el-dialog>
		
		<!-- 班级选择对话框 -->
		<el-dialog
		  title="班级选择"
		  :visible="showClassDialog"
		  width="20%"
		  @open="openCallback()">
		  <div class="dialog" >
				<div class="dialog-container">
					<div class="dialog-search">
						<el-input
						    placeholder="请输入组织名称"
						    prefix-icon="el-icon-search"
						   size="small"
						   v-model="searchValue"
						   @change="searchNodes()">
						  </el-input>
					</div>
					<div class="ztree" id="tree"></div>
				</div>
			</div>
		  <span slot="footer" class="dialog-footer">
		 	 <el-button size="small" type="success" onclick="selectClass()">确 定</el-button>
		    <el-button size="small" @click="showClassDialog = false">取 消</el-button>
		  </span>
		</el-dialog>
	</div>
<script type="text/javascript" src="<%=basePath%>zTree/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.backstretch.min.js"></script>
<script type="text/javascript" src="<%=basePath%>layer/layer.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/vue.min.js"></script>
<script type="text/javascript" src="<%=basePath%>elementui/element-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.form.js"></script>
<script type="text/javascript" src="<%=basePath%>js/register.js"></script>
<script type="text/javascript">
	
</script>
</body>
</html>