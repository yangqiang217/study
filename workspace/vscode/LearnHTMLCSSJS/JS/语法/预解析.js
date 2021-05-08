//1.
// console.log(num);//直接报错

//2
console.log(num);//不报错，undefined
var num = 10;


//3.没问题
f();
function f() {
    console.log("in f")
}
//4.直接报错f2 is not a function
fun();
var fun = function () {
    console.log("in fun")
}


//预解析：js引擎会把所有var和function提升到当前作用域最前面，然后代码执行
//1.变量提升：把变量声明提升到当前作用域最前面，不提升赋值，所以2里面实际是：
var num;
console.log(num);
num = 10;
//4实际上是：
var fun;
fun();
fun = function () {
    //...
}
//2.函数提升：把函数声明提升到当前作用域最前面，不调用，如3

//实例：输出undefined
var nn = 10;
fun();
function ff() {
    console.log(nn);
    var nn = 20;
}