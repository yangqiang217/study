main(List<String> args) {
  //和java一样，所有都是对象，所有类都是obj的子类，且是单继承
  //但是没有public、private、protected，但是可以通过_把属性或者方法定义成私有，前提是类必须是写在单独文件中的

  // var p = new Person('yq', 1);//实例对象的时候可以没有new关键字
  // print(p.getInfo());

  // var p1 = new Person.shit('yq', 1);
  // var p1 = new Person('yq', 1);
  // print(p1._name);
  // p1.getInfo();
  // //调用getter和setter
  // p1.info;
  // p1.info = 1;

  // // 对象操作符
  // // 1.?
  // Person p2;
  // p2?._name = 'shit'; //如果p2是null，就不会调用
  // // 2.is 和java的instanceof一样
  // if (p2 is Person) {}
  // // 3.as强制类型转换，就像java的强转
  // var p3;
  // (p3 as Person)._name = 'shit';
  // // 4. ...级联操作，用的多
  // p2
  //   .._name = 'yq'
  //   ..age = 1
  //   ..getInfo();

  //with
  C c = new C('yq', 1);
  c.a();
  print(c.aa);
}

class Person {
  static String type = 'human'; //和java一样，静态的不能访问非静态的

  String _name;
  int age;

  // 构造函数
  Person(String n, int a) {
    this._name = n;
    this.age = a;
  }
  // 可以简写为：
  // Person(this.name, this.age);

  // 命名构造函数
  // Person.shit(String n, int a) {
  //   print('in shit');
  //   this._name = n;
  //   this.age = a;
  // }

  //构造函数初始化列表，在调用构造函数之前调用var p = new Person();
  // Person()
  //     : _name = 'yq',
  //       age = 1 {
  //   print('in list name: $_name');
  // }

  String getInfo() {
    return this._name;
  }

  //getter，不要()
  get info {
    return this.age;
  }

  set info(int a) {
    this.age = a;
  }
}

// 继承
// 也用extends
class Student extends Person {
  // 这种写法类似于上面说的初始化列表，把父类的东西赋值给子类
  Student(String n, int a) : super(n, a); //父类有的话子类必须要
  // Student(String n, int a) : super.shit(n, a);//对应父的命名构造函数

  @override
  String getInfo() {
    return super.getInfo();
  }
}

// 抽象类，不能被实例化
// 可以多态：Animal d = new Dog();此时d只能调用父类里面的方法
// 如果里面都是抽象方法，就被当做接口
abstract class Animal {
  eat(); //抽象方法，不用abstract
  void fun() {
    print('');
  }
}

class Dog extends Animal {
  @override
  eat() {}
}

// 接口
// dart接口没有interface关键字，而是普通类或者抽象类作为接口被实现。同样使用implements
// 如果实现类是普通类，会将普通类中的方法全部覆写一遍
// 一般实现接口用抽象类
abstract class City {
  String name;

  String getName();
  String getLoc();
  void shit() {}
}

abstract class Capital {
  String country;
}

class Tokoy implements City, Capital {
  @override
  String name;
  @override
  String country;

  @override
  String getLoc() {}

  @override
  String getName() {}

  @override
  void shit() {}
}

//mixins：类中混入其它功能，实现本来原生不能实现的类似多继承的功能。不是继承也不是接口，而是一种全新的特性
// 注意：
// with后面的类必须是直接集成Obj，即A和B不能有extends，但可以有implements
// A和B不能有构造函数
// 一个类可以mixins多个mixins的类
// extends和mixins的类里面有相同的方法时，C调用的是mixins里面的。而AB有相同方法时，C调用的是with后面排序较前面的
// c is A 和 c is B都是true
class A {
  int aa = 1;
  void a() {
    print('a');
  }
}

class B {
  void b() {
    print('b');
  }
}

//可以调用AB里面所有的属性和方法
class C extends Person with A, B {
  C(String n, int a) : super(n, a);
}
