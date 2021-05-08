import 'dart:ffi';

main(List<String> args) {
  //变量可以用java的int、String等，也可以用var，类似js
  double a = 1.1;
  // print(a);

  /*常量可以用const、final，在定义基本类型时没区别
   const必须用常量初始化
   final可以用变量，final是运行时常量，且惰性初始化，即在运行时第一次使用前才初始化
  */
  // final a = new DateTime(2020); 不报错
  // const b = new DateTime(2020); 报错
  // 或
  // int c = 1;
  // const b = c; 报错
  // final d = c; 不报错

  num e = 1.1;
  print(e);
}
