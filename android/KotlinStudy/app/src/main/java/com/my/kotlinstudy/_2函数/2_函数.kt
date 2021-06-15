@file:JvmName("StringFunc")

package com.my.kotlinstudy._2函数

import java.lang.StringBuilder

fun main() {
    val list = arrayListOf(1, 2, 3)
    //命名参数
    list.joinToString(pre = "", suf = "")

//    joinToString(list, "a")//error 这样"a"不能自动识别为suf而是sep，要想使用任意个数参数，需要函数所有同类型参数都有默认


    //调用扩展函数
    println("shit".lastChar())
    //调用扩展属性
    val sb = StringBuilder("shit")
    sb.lastChar

    max(1, 2, 3, 4, 5)
    val list1 = listOf(1, 2)
//    max(*list1)
}

/**
 * 参数默认val，无法被赋值
 */
fun para(para: String) {
//    para = 1// error
}

/**
 * 顶层变量，const val = public static final
 */
const val LINE = "\n"
/**
 * 顶层函数，生成的java是上面StringFunc.joinToString，静态的，可以直接给java调用
 */
fun <T> Collection<T>.joinToString(sep: String = ""/*默认参数，可以代替重载*/,
                     pre: String = "", suf: String): String {
    val res = StringBuilder(pre)
    for ((idx, ele) in withIndex()) {
        if (idx > 0) {
            res.append(sep)
        }
        res.append(ele)
    }
    res.append(suf)
    return res.toString()
}

/**
 * 只能String集合触发
 */
fun Collection<String>.join(sep: String = ""/*默认参数，可以代替重载*/,
    pre: String = "", suf: String) = joinToString(sep, pre, suf)

/**
 * 扩展函数，就是类的成员函数，不过定义在类的外面
 * 静态函数的一个高效语法糖
 * 仍旧不能访问私有或者保护的成员
 * java中调用：StringFunc.lastChar("xxx")
 * 扩展函数无法被继承和重写，即便有同名的，调的还是声明时候那个对象的：
 * val view: View = Button()
 * view.showOff() 调用View的扩展函数，java调用：
 * XXKt.showOff(view);
 */
fun String.lastChar(): Char = get(length - 1)

/**
 * 扩展属性
 * 不能直接初始化
 * java调用，需要显式调用getter：StringFunc.getLastChar("xx")
 */
val String.lastChar: Char
    get() = get(length - 1)
/**
 * var 因为StringBuilder内容可变
 */
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        setCharAt(length - 1, value)
    }

/**
 * 可变参数个数
 */
fun max(vararg value: Int) {
}