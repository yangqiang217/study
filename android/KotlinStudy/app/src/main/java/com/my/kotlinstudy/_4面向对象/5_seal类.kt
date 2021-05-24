package com.my.kotlinstudy._4面向对象


/**
 * 密封类，对可能创建的子类做出限制，所有直接子类必须嵌套在父类中
 * seal类的所有子类在编译器就知道了，所以第三方库无法继承seal类
 * 默认open, abstract，无法被直接初始化，可以有abstract方法
 * 所有子类必须声明在seal类的同一文件，但继承seal子类的类可以放在别处
 * 作用：when的时候就不需要else分支了
 *
 * 有点像枚举，但是枚举每个取值是单例，seal的子类可以是多例
 */
sealed class Expr {
//    abstract fun a()
    constructor() {//默认private，不能public

    }
}
class Num(val value: Int) : Expr()
class Sum(val left: Expr, val right: Expr) : Expr()

fun eval(e: Expr) : Int =
    //when涵盖了所有可能情况(必须涵盖，否则有返回值的when会报错)，不需要默认else分支
    when (e) {
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
    }