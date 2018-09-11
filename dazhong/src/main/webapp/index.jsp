<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title></title>
<style type="text/css">

</style>
</head>
<body>
<h2>Hello World!</h2>
<form action="/dazhong/api/1" method="post">
	<input type="hidden" name="_method" value="DELETE">
	<input type="submit" value="DELETE提交">
</form>

<form action="/dazhong/api/1" method="post" enctype="multipart/form-data">
	<input type="hidden" name="_method" value="PUT">
	<input type="file" name="file" value="文件">
	<input id="put" type="submit" value="PUT提交">
</form>

<script type="text/javascript" src="/dazhong/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
$("#put1").on("click",function(){
    $.ajax({
        url: "/dazhong/api/2",
        type : "post",
        data : {
            _method : "put",
        },
        success : function(data){
            
        },
        dataType : "json",
        error : function(data){
        }
    })
});

var common = {};

common.ajax = function(param) {
	var ajaxParam = $.extend({timeout:1000},
			{complete:function() {
				console.log('必定执行')
			}},
			param);
	
	$.ajax(ajaxParam);
}

common.ajax({
    url: "/dazhong/api/2",
    type : "post",
    data : {
        _method : "delete",
    },
    success : function(data){
        
    },
    dataType : "json",
    error : function(data){
    }
});
</script>
</body>
</html>
