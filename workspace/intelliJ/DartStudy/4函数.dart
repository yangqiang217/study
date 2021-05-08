main(List<String> args) {
  // print(fun4(1));
  // fun5('yq', age: 13);

  fun7(fun6);

  //只能在main里面使用
  // void innerfunc() {}
}

int fun(int a, int b) {
  return a + b;
}

//或（返回类型不能是var）
int fun2(var a, var b) {
  return a + b;
}

//或更简单：（建议都写上）
fun3(a, b) {
  return a + b;
}

//可选参数和默认参数，可选参数只能在最后，默认参数只能在里面
int fun4(int a, [int b, int c = 13]) {
  if (b != null) {
    return a + b;
  } else {
    return a;
  }
}

// 命名参数, {}内的可传可不传
int fun5(String name, {int age, String sex = 'male'}) {}

// 方法做参数，调用的时候fun7(fun6);，注意fun6后面不加括号
fun6() {
  print('fun6');
}

fun7(a) {
  a();
}

// 箭头函数
fun8() {
  List list = ['1', '2'];
  list.forEach((element) => {
        print(element) //里面只能写一句话
      });
  // 或去掉{}
  list.forEach((element) => print(element));

  list.map((e) => e > 2 ? e * 2 : e);
}

//匿名方法
var fun9 = (int n) {
  print(n);
};

// 自执行方法（必须放在别的方法里）
fun10() {
  ((){
    print('self execute');
  })();
  // 传参数的：
  ((n){
    print(n);
  })(13);
}
