<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title></title>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
</head>
<body>
<h1>client</h1>
<a href="<%=basePath%>logout">logout</a>
<button onclick="test()">haha</button>
<button onclick="cross()">cross</button>
<button onclick="saveUser()">saveUser</button>
<button onclick="logout()">logout</button>
<button onclick="saveOrg()">saveOrg</button>
<button onclick="delOrg()">delOrg</button>
<button onclick="saveLc()">saveLc</button>
<button onclick="delLc()">delLc</button>
<button onclick="saveSub()">saveSub</button>
<button onclick="delSub()">delSub</button>
<script type="text/javascript">

function delSub() {
	$.ajax({
		  type: 'POST',
		  url: "http://192.168.1.106:8080/springTest/cross/delSub",
		  data: {
			  sub_id: 16
		  },
		  success: function(result) {
			  console.log(result)
		  }
	});
}

function saveSub() {
	$.ajax({
		  type: 'POST',
		  url: "http://192.168.1.106:8080/springTest/cross/saveSub",
		  data: {
			  sub_id: 16,
			  sub_name: "生理"
		  },
		  success: function(result) {
			  console.log(result)
		  }
	});
}

function delLc() {
	$.ajax({
		  type: 'POST',
		  url: "http://192.168.1.106:8080/springTest/cross/delLc",
		  data: {
			  lc_id: 1
		  },
		  success: function(result) {
			  console.log(result)
		  }
	});
}

function saveLc() {
	$.ajax({
		  type: 'POST',
		  url: "http://192.168.1.106:8080/springTest/cross/saveLc",
		  data: {
			  lc_id: 1,
			  lc_name: "R17",
			  lc_createdate: "2018-02-02"
		  },
		  success: function(result) {
			  console.log(result)
		  }
	});
}


function delOrg() {
	$.ajax({
		  type: 'POST',
		  url: "http://192.168.1.106:8080/springTest/cross/delOrg",
		  data: {
			  section_type: 2,
			  section_oid: 2
		  },
		  success: function(result) {
			  console.log(result)
		  }
	});
}

function saveOrg() {
	$.ajax({
		  type: 'POST',
		  url: "http://192.168.1.106:8080/springTest/cross/saveOrg",
		  data: {
			  section_name: "叼你老豆班",
			  section_parent: 1,
			  section_type: 2,
			  section_oid: 1,
			  section_updatetime: new Date()
			  
		  },
		  success: function(result) {
			  console.log(result)
		  }
	});
}

function logout() {
	$.ajax({
		  type: 'POST',
		  url: "http://192.168.1.104:8080/springTest/cross/logout",
		  xhrFields: {withCredentials: true},
		  success: function(result) {
			  console.log(result)
		  }
	});
}

function saveUser() {
	$.ajax({
		  type: 'POST',
		  url: "http://192.168.1.104:8080/springTest/cross/saveUser",
		  data: {
			 	username: "jiegechishishi",   //用户名
			 	password: "admin", 	//密码
			 	nickname: "jiegechishishi", 	//昵称
			 	avatar: "head/rterterter.png", 		//头像
			 	sex: 0, 				//性别，0:未设置，1:男，2:女
			 	phone:"13812345678", //电话号码
			 	email:"123456@qq.com",	//邮箱
			 	type:0,				// 用户类型，0学生 1教师
			 	group_id:199		//用户组id，0:无，1:管理员，2:学生，3;家长，4:领导 
			 	},
		  success: function(result) {
			  console.log(result)
		  }
	});
}

function cross() {
	$.ajax({
		  type: 'POST',
		  url: "http://192.168.1.104:8080/springTest/cross/setLogin",
		  xhrFields: {withCredentials: true},
		  data: {username:'jiegechishishi'},
		  success: function(result) {
			  console.log(result)
		  }
	});
}

function test() {
	$.ajax({
		  type: 'GET',
		  url: "/cloud/api/QueryUserById",
		  data: {id:1},
		  beforeSend: function (XMLHttpRequest) {
       		XMLHttpRequest.setRequestHeader("token", getCookie("CREATORTOKEN"));
           },
		  success: function(result) {
			  console.log(result)
		  }
	});
}


function getCookie(c_name)
{
if (document.cookie.length>0)
  {
  c_start=document.cookie.indexOf(c_name + "=")
  if (c_start!=-1)
    { 
    c_start=c_start + c_name.length+1 
    c_end=document.cookie.indexOf(";",c_start)
    if (c_end==-1) c_end=document.cookie.length
    return unescape(document.cookie.substring(c_start,c_end))
    } 
  }
return ""
}
</script>
</body>
</html>