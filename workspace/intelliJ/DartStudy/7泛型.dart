main(List<String> args) {
  print(fun(1));
  print(fun<String>('2'));

  // List l1 = new List<String>();
  // l1.add(1); //这里不会检查
  // List l2 = new List<int>(); //不需要包装类型

  //泛型类
  Clazz clazz = new Clazz<int>();
  clazz.add('1');//这里不会检查
}

// 泛型方法
T fun<T>(T pa) {
  return pa;
}

fun1<T>(T pa) {
  return pa;
}

// 自定义泛型类
class Clazz<T> {
  void add(T t) {}
}
//两个T都必须要
class Child<T> implements Clazz<T> {
  @override
  void add(T t) {
  }

}