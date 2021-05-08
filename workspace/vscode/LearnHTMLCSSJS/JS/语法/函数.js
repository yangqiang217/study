//函数的声明式写法
function f(a, b) {//不需要定义返回类型
    console.log(arguments);//将所有参数放在这，即便外边传的参数个数不对，也都会存在这
    return a + b;
}
console.log(f(1, 14));
f(1, 2, 3, 5);//不会有问题，参数自动保存到arguments中


//函数的表达式写法
//命名式，可能用的少
var abc = function f1() {
    console.log("abc")
}
//匿名式
var abc2 = function () {
    console.log("abc2")
}
// f1();//不可调用
abc();
abc2();

console.log("f name: " + f.name);
console.log("abc name: " + abc.name);
console.log("abc2 name: " + abc2.name);


//立即执行函数表达式，在声明完后立马调用一次，最大的作用就是创建了独立的作用域
//写法1，执行完立即销毁，外面不能调用，所以名字也不需要了
// (function imme1() {
(function () {
    console.log("imme1");
})();//句末分号必须的
// 写法2，不行了？？
(function () {
    console.log("imme2");
}());
//写法3，这时var imme3就是undefined了，不能再调用imme3()
var imme3 = function () {
    console.log("imme3");
}();



//高阶函数
//以函数作为参数或者return的是函数
//1.函数作为参数，常用于回调
function f_param(callback) {
    callback && callback();
}
f_param(function () {
    //...
});