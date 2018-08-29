<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="title" content="LayoutIt! - Bootstrap可视化布局系统">
<meta name="description" content="LayoutIt! 可拖放排序在线编辑的Bootstrap可视化布局系统">
<meta name="keywords" content="可视化,布局,系统">
<style type="text/css">
/* colorSelector */

.colorSelector{position:absolute;top:0;left:0;width:36px;height:36px;background:url(<%=basePath %>shiseqi/images/select2.png);}

.colorSelector div{position:absolute;top:4px;left:4px;width:28px;height:28px;background:url(<%=basePath %>shiseqi/images/select2.png) center;}

.colorHolder{top:32px;left:0;width:356px;height:0;overflow:hidden;position:absolute;z-index: 100}

.colorHolder .colorpicker{background-image:url(<%=basePath %>shiseqi/images/custom_background.png);position:absolute;bottom:0;left:0;}

.colorHolder .colorpicker_hue div{background-image:url(<%=basePath %>shiseqi/images/custom_indic.gif);}

.colorHolder .colorpicker_hex{background-image:url(<%=basePath %>shiseqi/images/custom_hex.png);}

.colorHolder .colorpicker_rgb_r{background-image:url(<%=basePath %>shiseqi/images/custom_rgb_r.png);}

.colorHolder .colorpicker_rgb_g{background-image:url(<%=basePath %>shiseqi/images/custom_rgb_g.png);}

.colorHolder .colorpicker_rgb_b{background-image:url(<%=basePath %>shiseqi/images/custom_rgb_b.png);}

.colorHolder .colorpicker_hsb_s{background-image:url(<%=basePath %>shiseqi/images/custom_hsb_s.png);display:none;}

.colorHolder .colorpicker_hsb_h{background-image:url(<%=basePath %>shiseqi/images/custom_hsb_h.png);display:none;}

.colorHolder .colorpicker_hsb_b{background-image:url(<%=basePath %>shiseqi/images/custom_hsb_b.png);display:none;}

.colorHolder .colorpicker_submit{background-image:url(<%=basePath %>shiseqi/images/custom_submit.png);}

.colorHolder .colorpicker input{color:#778398;}

#customWidget{position:relative;height:36px;}

/*  useritem  */ 
.draggable {
   width: 12px;background-color:#00bcd4;position: absolute;top:100px;left:100px;
}

.useritem-content {
   margin-left: 12px;white-space:nowrap;
}

.range-content {
   margin: -70px 0px;position: absolute;
}
.range-title {
   margin: -50px 0px;position: absolute;
}
.range-span {
   position: relative;top: -5px;
}
.selecter-content {
   margin: -70px 200px;position: absolute;
}
.selecter-title {
   margin: -50px 200px;position: absolute;
}
.contentSelector {
   margin-left: 70px;margin-top: -20px;
}
.titleSelector {
   margin-left: 70px;margin-top: -10px;
}
.useritem-title {
   border: none;outline: none;width: auto;width: 10px;min-width: 10px;font-size: 20px;color:black;
}
.editeitem {
   z-index:50; 
}
.selecter-open {
   margin: -50px 0px;position: absolute;
}
.selecter-close {
   margin: -50px 120px;position: absolute;
}
.openSelector {
   margin-left: 70px;margin-top: -10px;
}
.closeSelector {
   margin-left: 70px;margin-top: -10px;
}
.useritem-button {
   cursor:pointer;border-radius: 15px;border: 1px solid black;
}
.useritem-p {
   cursor:pointer;text-align: center;line-height: 80px;vertical-align: middle;margin: 0px;color: white;
}
.useritem-buttontitle {
   text-align: center;border: none;outline: none;width: auto;min-width: 10px;font-size: 20px;
}
input[type=range] {
  display: inline-block !important;
}
label {
  font-weight: 400 !important;
}
</style>
  <link href="<%=basePath%>hAdmin/css/bootstrap.min.css?v=3.3.6"
	rel="stylesheet">
	<link href="<%=basePath%>hAdmin/css/style.css?v=4.1.0" rel="stylesheet">
<link href="<%=basePath%>jqueryui/jquery-ui.css" rel="stylesheet">
<script type="text/javascript" src="<%=basePath%>js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>jqueryui/jquery-ui.js"></script>
<link rel="stylesheet" href="<%=basePath %>shiseqi/css/colorpicker.css" type="text/css" />
<script type="text/javascript" src="<%=basePath %>shiseqi/js/colorpicker.js"></script>
<title></title>
</head>

<body>
<a class="btn btn-primary btn-rounded" onclick="editUserItem(true)">开启编辑模式</a>
<a class="btn btn-default btn btn-rounded" onclick="editUserItem(false)">关闭编辑模式</a>
<a class="btn btn-success btn-rounded">保存</a>
<!--  TEXT模板 -->
<div class="model">
<div id="text1" class="draggable" >
  <div class="useritem-content">
	     <div class="editeitem range-div range-content" >
	        <span class="range-span">内容字体大小:</span> 
	       <input class="range content-range" type="range" style="width: 80px;" min="5.0" max="40.0" step="0.5" value="20.0">
	     </div> 
	     <div class="editeitem range-div range-title" >
	        <span class="range-span" >标题字体大小:</span> 
	       <input class="range title-range" type="range" style="width: 80px;" min="5.0" max="40.0" step="0.5" value="20.0">
	     </div>
	     <div class="editeitem color-selecter selecter-content">
	        <span style="position: relative;">内容颜色:</span> 
	        <div class="colorSelector contentSelector"><div style="background-color: red"></div></div>
		    <div class="colorHolder contentHolder"></div>
	     </div>  
	     <div class="editeitem color-selecter selecter-title">
	        <span style="position: relative;">标题颜色:</span> 
	        <div class="colorSelector titleSelector"><div style="background-color: #000000"></div></div>
		    <div class="colorHolder titleHolder"></div>
	     </div>         
     <label id="label1" class="useritem-title" style="display: none;width: auto;">当前温度</label><input class="tree-user-item useritem-title" type="text" value="当前温度"><span class="tree-user-item useritem-content" id="span1" style="color: red;font-size: 15px">25℃</span>
  </div>  
</div>
</div>
<!-- button模板 -->
<div class="model">
<div class="draggable">
  <div class="useritem-content">
     <div class="editeitem color-selecter selecter-open">
        <span style="position: relative;">开启颜色:</span> 
        <div class="colorSelector openSelector"><div style="background-color: #34d827"></div></div>
	    <div class="colorHolder openHolder"></div>
     </div>  
     <div class="editeitem color-selecter selecter-close">
        <span style="position: relative;">关闭颜色:</span> 
        <div class="colorSelector closeSelector"><div style="background-color: rgb(161, 171, 171)"></div></div>
	    <div class="colorHolder closeHolder"></div>
     </div>
     <div class="useritem-button resizable" on-color="#34d827" off-color="rgb(161, 171, 171)"  style="width: 153px;height: 80px;background-color: rgb(161, 171, 171);">
         <p class="useritem-p" style="font-size: 50px;">Off</p>
     </div>
     <input class="useritem-buttontitle" type="text" value="开关" style="width:153px;">
  </div>
</div>
</div>

<div>
<div class="draggable">
  <div class="useritem-content">
     <div class="editeitem color-selecter selecter-open">
        <span style="position: relative;">开启颜色:</span> 
        <div class="colorSelector openSelector"><div style="background-color: #34d827"></div></div>
	    <div class="colorHolder openHolder"></div>
     </div>  
     <div class="editeitem color-selecter selecter-close">
        <span style="position: relative;">关闭颜色:</span> 
        <div class="colorSelector closeSelector"><div style="background-color: rgb(161, 171, 171)"></div></div>
	    <div class="colorHolder closeHolder"></div>
     </div>
     <div class="useritem-button resizable" on-color="#34d827" off-color="rgb(161, 171, 171)"  style="width: 153px;height: 80px;background-color: rgb(161, 171, 171);">
         <p class="useritem-p" style="font-size: 50px;">Off</p>
     </div>
     <input class="useritem-buttontitle" type="text" value="开关" style="width:153px;">
  </div>
</div>
</div>
<script type="text/javascript">
$(function(){
	var json = [];
	var count = 0;
	$('.draggable').each(function() {
		var item = {};
		$(item).attr('id', $(this).attr('id'));
		$(item).attr('html', $(this).parent().html());
		json[count++] = item;
	})
	var str = JSON.stringify(json);
	
	var jsonobj = JSON.parse(str);

})

//点击按钮改变按钮样式
function changeButtonState(obj) {
	if($(obj).hasClass('active')) {
		$(obj).removeClass("active");
		$(obj).css('background-color', $(obj).attr('off-color'));
		$(obj).children('p').html('Off');
	}else {
		$(obj).css('background-color', $(obj).attr('on-color'));
		$(obj).addClass("active");
		$(obj).children('p').html('ON');
	}
}

//点击按钮后执行动作
function buttonHandle(obj) {
	
}

function editUserItem(key) {
	if(key) {
		$(".draggable").draggable('enable');
		$(".resizable").resizable('enable');
		$('.editeitem').css('display', '');
		$('input.useritem-title').css('display', '');
		$('label.useritem-title').css('display', 'none');
		$('.draggable').css('background-color', '#00bcd4');
	}else {
		$(".draggable").draggable('disable');
		$(".resizable").resizable('disable');
		$('.editeitem').css('display', 'none');
		$('input.useritem-title').css('display', 'none');
		$('label.useritem-title').css('display', 'inline');
		$('.draggable').css('background-color', 'rgba(255,255,255,0)');
	}
}

$(function(){
	$(".draggable").draggable({cancel:'.useritem-content'});
	$(".resizable").resizable({resize:function(event, ui){
		var obj = event.target;
		$(obj).children('.useritem-p').css('line-height', $(obj).css('height'));
		$(obj).children('.useritem-p').css('font-size', parseInt($(obj).css('height'))*0.6 + 'px');
		$(obj).next('input').css('width', $(obj).css('width'));
	}});
	
	$('.useritem-button').bind('click', function(event) {
		changeButtonState(this);
		buttonHandle(this);
	})
	$('input.useritem-title').bind('input propertychange', function() {
	    updateInputWidth(this);
	    $(this).prev('label').html($(this).val());
	})

	$('.title-range').bind('input propertychange', function() {
		var font_size = $(this).val();
		$(this).parent().parent().find('.useritem-title').css('font-size', font_size + 'px');
		$('.useritem-title').trigger("propertychange");
	})
	$('.content-range').bind('input propertychange', function() {
		var font_size = $(this).val();
		$(this).parent().parent().find('.useritem-content').css('font-size', font_size + 'px');
		$('.useritem-title').trigger("propertychange");
	})
	
})

//初始化颜色选择器
$(function(){
	
	var selected_text;
	
	var selected_colorSelector;
	
	var selected_type;
	
	var button_type;
	$('.colorHolder').ColorPicker({

		flat: true,

		color: '#000',

		onSubmit: function(hsb, hex, rgb) {
			selected_colorSelector.children('div').css('backgroundColor', '#' + hex);
            if(selected_type == 'text') {
                selected_text.css('color', '#' + hex);
            }else if(selected_type == 'button' && button_type == 'on') {
            	selected_text.attr('on-color', '#' + hex);
            	if(selected_text.hasClass('active')) {
            		selected_text.css('background-color', '#' + hex);
            	}
            }else if(selected_type == 'button' && button_type == 'off') {
            	selected_text.attr('off-color', '#' + hex);
            	if(!selected_text.hasClass('active')) {
            		selected_text.css('background-color', '#' + hex);
            	}
            }
		}

	});

	var widt = false;

	$('.titleSelector').bind('click', function() {
        var colorHolder = $(this).next('.titleHolder');
		
        colorHolder.stop().animate({height: widt ? 0 : 173}, 500);

		widt = !widt;

		selected_text = $(this).parent().parent().find('.useritem-title');
		
		selected_colorSelector = $(this);
		
		selected_type = "text";
	});

	$('.contentSelector').bind('click', function() {
        var colorHolder = $(this).next('.contentHolder');
		
        colorHolder.stop().animate({height: widt ? 0 : 173}, 500);

		widt = !widt;

		selected_text = $(this).parent().parent().find('.useritem-content');
		
		selected_colorSelector = $(this);
		
		selected_type = "text";
	});
	
	$('.openSelector').bind('click', function() {
        var colorHolder = $(this).next('.openHolder');
		
        colorHolder.stop().animate({height: widt ? 0 : 173}, 500);

		widt = !widt;

		selected_text = $(this).parent().parent().find('.useritem-button');
		
		selected_colorSelector = $(this);
		
		selected_type = "button";
		
		button_type = 'on';
	});

	$('.closeSelector').bind('click', function() {
        var colorHolder = $(this).next('.closeHolder');
		
        colorHolder.stop().animate({height: widt ? 0 : 173}, 500);

		widt = !widt;

		selected_text = $(this).parent().parent().find('.useritem-button');
		
		selected_colorSelector = $(this);
		
		selected_type = "button";
		
		button_type = 'off';
	});
});

//初始化组件样式
$(function() {
	$('.useritem-title').trigger("propertychange");
	$('.useritem-buttontitle').each(function(){
		$(this).css('width', $(this).prev('div').css('width'));
	});
})

function updateInputWidth(obj) {
	var text = $(obj).val();
	var realLength = 0;
	var text_length = parseInt($(obj).val().length);
	var text_width = parseInt($(obj).css('font-size'))*1.1;
	for(var i=0; i <text_length; i++) {
		var charCode = text.charCodeAt(i);
		if(charCode >= 0 && charCode <= 255) {
			realLength += 0.5;
		}else {
			realLength += 1;
		}
	}
	$(obj).css('width', realLength*text_width + 'px');
}


</script>
</body>
</html>