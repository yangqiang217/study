main(List<String> args) {
  //算术运算符基本和java一样，有个取整：
  // int a = 13;
  // int b = 5;
  // int c = a / b; 报错
  // print(a / b); 2.6
  // print(a ~/ b); 2：取整：a/b不能自动转成int

  // 赋值运算符，有个 ??=，d??=33表示如果b为空或者没赋值就把33赋给b
  // int d; java里不赋值的int会默认是0，这里不是，直接打印d是null
  // d ??= 33;
  // print(d);
  // 衍生
  // var a;
  // var b = a ?? 10;//a是空的话b就是10，a不是空的话b就是a

  // 支持三目运算符

  // 类型转换
  // 1.Number与String
  // String str = '13';
  // var myNum = int.parse(str);
  // String numStr = myNum.toString();
  // print(myNum is int); //注意is的用法
  // print(numStr is String);

  // 2.其它类型和bool类型
  // String str = '';
  // if (str.isEmpty) {
  //   //只能判断是''，如果是null依然会crash
  //   print('empty');
  // }
  // 关于NaN
  // var a = 0 / 0;
  // if (a.isNaN) {}

  //try catch
  // try {
  //   String a = '';
  //   int n = int.parse(a);
  // } catch (err) {
  //   print(err);
  // }
}
