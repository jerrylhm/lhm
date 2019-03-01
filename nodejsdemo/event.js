var events =require("events");

var eventEmitter = new events.EventEmitter();

//不带参数
eventEmitter.on('speak', function() {
	console.log('大声讲，讲到我听到为止');
})

var doSomeThing = function doing(arg1, arg2) {
	console.log(arg1);
	console.log(arg2);
}

//带参数
eventEmitter.on('fight', doSomeThing);

//eventEmitter.removeListener('fight', doSomeThing);

var liuAndLiang = function eatShit() {
	console.log('非凡风云之吔屎传奇');
	eventEmitter.emit('speak');
}

 function sleep(milliSeconds) {
	var startTime = new Date().getTime();
	while (new Date().getTime() < startTime + milliSeconds) {
	}//暂停一段时间 10000=1S。
}

//显示监听事件数量
console.log(eventEmitter.listenerCount('fight'));

eventEmitter.on('start', liuAndLiang);

eventEmitter.emit('start');//eventEmitter是同步的
eventEmitter.emit('fight', '非凡哥提议下操场玩跑步', '非凡哥积极清理桌面中');