package com.my.kotlinstudy._3面向对象


fun main() {
    val a: String? = null
    println(a.toString())

    MyClass.print()
}

//扩展方法
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]//this指向MutableList
    this[index1] = this[index2]
    this[index2] = tmp
}

/**
 * 可空接受者
 * 可以直接null.toString()了
 * 系统帮我们重写这个了，所以不用自己写
 */
fun Any?.toString(): String {
    println("ohhhh")
    if (this == null) return "null"
    // after the null check, 'this' is autocast to a non-null type, so the toString() below // resolves to the member function of the Any class
    return toString()
}


//扩展变量
/**
 * 扩展变量不能有backing field，所以不支持直接初始化
 */
val <T> List<T>.lastIndex: Int
    get() = size - 1


//静态扩展
class MyClass {
    companion object {}
}
fun MyClass.Companion.print() {
    println("companion ext")
}