//一个函数，它有权访问另一个函数作用域中的变量
function fn() {
    var num = 10;

    // function inner() {
    //     console.log("inner")
    // }
    //
    // return inner;
    return function () {
        console.log("inner");
    }
}

var fun = fn();
//上面相当于:
// var fun = function inner() {
//
// }
fun();//这样一调用，就在全局访问num拿到了num的值
//所以闭包的作用：延伸了变量作用范围

//案例
//1.给一个list中的所有控件添加点击事件
var list = [];
//这种方式由于func是延时执行的，最后每个控件拿到的i都是list.length -  1
for (var i = 0; i < list.length; i++) {
    list[i].onclick = function () {
        console.log(i);
    }
}
//用闭包：因为A函数用了B函数的值，所以产生了闭包
//立即执行函数也称为小闭包
for (var i = 0; i < list.length; i++) {
    (function (i) {//B函数
        list[i].onclick = function () {//A函数
            console.log(i);
        }
    })(i);
}
//2. 3s后打印所有元素内容
//同样打印处所有的i都是最大的那个
for (var i = 0; i < list.length; i++) {
    setTimeout(function () {
        console.log(list[i]);
    }, 3000);
}
//用闭包
for (var i = 0; i < list.length; i++) {
    (function (i) {
        setTimeout(function () {
            console.log(list[i]);
        }, 3000);
    })(i);
}