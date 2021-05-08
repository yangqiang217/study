/*
* 普通函数    window
* 构造函数    实例对象
* 对象方法调用    该方法所属对象
* 事件绑定方法    绑定事件对象
* 定时器函数     window
* 立即执行函数    window
* */

//call/apply/bind可以改变this指向
//call：调用某函数并修改函数运行时this的指向:
console.log("-----------call");
//fun.call(thisArg, arg1, arg2, ...);
//thisArg: 当前函数this的指向对象
function f_call(param) {
    console.log("ff: " + param);
    this.fun();//this现在指向o_call
}
var o_call = {
    name: "andy",
    fun: function () {
        console.log("fun");
    }
}
f_call.call(o_call, "param");

//apply，参数是数组
console.log("-----------apply");
//fun.apply(thisArg, [argsArray]);
function f_apply(param) {
    console.log("f_apply: " + param);
    this.fun();//this现在指向o_call
};
var o_apply = {
    name: "andy",
    fun: function () {
        console.log("fun");
    }
};
f_apply.apply(o_apply, ["shit"]);
//apply应用,利用apply借助Math求数组最大值
var arr = [1, 2, 123];
Math.max.apply(Math, arr);

//bind
console.log("-----------bind")
//fun.bind(thisArg, arg1, arg2, ...)
//返回由指定的this和初始化参数改造的原函数副本
//bind不会立即调用
function f_bind(param) {
    console.log("f_bind: " + param);
    this.fun();//this现在指向o_call
}
var o_bind = {
    name: "andy",
    fun: function () {
        console.log("fun");
    }
};
var f_bind2 = f_bind.bind(o_bind, "para_bind");//绑定但不调用
f_bind2();//这里不需要参数了
//bind应用:按钮点击了后禁用若干时间后开启
/*
var btn = document.querySelector("button");
btn.onclick = function () {
    this.disabled = true;//this指向btn
    setTimeout(function () {
        this.disabled = false;//这里的this指向window
    }.bind(this), 3000)
}*/
