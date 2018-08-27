//显示加载动画
function showLoad(ele) {
	$(ele).prepend('<div class="load1-space"><div class="load1"><i class="fa fa-spinner fa-spin fa-3x"></i></div></div>');
}
//隐藏加载动画
function hideLoad(ele) {
	$(ele).children('.load1-space').remove();
}