var fs =require("fs");

var data = fs.readFileSync("input.txt");//阻塞读取

console.log(data.toString());

fs.readFile("input1.txt", function(error, data) {
	if(error) console.log(error);
	console.log(data.toString());
})

console.log("结束");