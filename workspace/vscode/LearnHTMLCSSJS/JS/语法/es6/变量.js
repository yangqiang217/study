//var的最大问题
// 1.可以重复声明：
var a = 1;
var a = 2;
// 2.无法限制修改，无常量
// 没有块级作用域，块级作用域里面定义的变量外面还能用:
if (true) {
    var b = 5;
}
console.log(b);

//let、const特点：
// 1.不能重复声明：
let c = 1;
// let c = 2;报错
//let是变量、const是常量
const d = 1;
// d = 2;报错
//2. let/const只在块级作用域内起作用
//3. let声明的变量没有变量提升
// console.log(leta);//会报错
// let leta = 1;
//4. 暂时性死区
// var num = 10;
// if (true) {
//     console.log(num);//报错，因为块中有num的声明了，所以就不会向上查找
//     let num = 20;
// }
//5. const必须有初始值
