{
    //es5及之前没有块级作用域的概念，块里面的东西外面也能访问到，除非放在函数里
    var a = 'shit';
}
console.log(a)