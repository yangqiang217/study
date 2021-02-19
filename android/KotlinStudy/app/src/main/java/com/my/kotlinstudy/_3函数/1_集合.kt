package com.my.kotlinstudy._3函数

fun main() {
    val set = hashSetOf(1, 2, 3)
    println(set.javaClass)//java.util.HashSet
    println(set.max())//java中没有，kt中扩展的函数

    val list = arrayListOf(1, 2, 3)
    println(list.javaClass)//java.util.ArrayList
    println(list.last())

    val list2 = listOf(1, 2, 3)
    println(list2.javaClass)//java.util.Arrays$ArrayList
    println(list.last())

    /*to 不是一个特殊结构，而是一个普通函数，调用方式是"中缀调用"
    中缀调用没有额外分隔符，函数名直接放在目标对象名称和参数之间，以下两种等价：
    1.to("one") 和 1 to "one"
    中缀可以与只有一个参数的函数（包括扩展）一起用。允许使用中缀调用的函数需要用infix修饰
    infix fun Any.too(other: Any) = Pair(this, other)
     */
    val map = hashMapOf(1 to "1", 2 to "2")
    println(map.javaClass)//java.util.HashMap
}