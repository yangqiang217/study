main(List<String> args) {
  //前提：全局变量会常驻内存、污染全局。局部变量不常驻内存，会被回收，不污染全局
  // 想实现的功能：常驻内存，不污染全局
  // 闭包：函数嵌套函数，内部函数会调用外部函数变量或参数，变量或参数不会被系统回收
  // 闭包写法：函数嵌套函数，return里面的函数
  // 特点就是利用了可以返回函数的特性

  // fun();不能直接调，return的没人接
  //将return的赋给f
  var f = fun();
  f();
}

fun() {
  var a = 13; //不污染全局，常驻内存
  return () {
    a++;
    print(a);
  };
}
