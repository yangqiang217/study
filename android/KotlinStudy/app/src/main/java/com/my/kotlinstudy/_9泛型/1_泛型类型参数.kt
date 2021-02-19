package com.my.kotlinstudy._9泛型

fun main() {
    process(null)
//    process1(null)//error
}

/**
 * 类似于java的<T extends Number>
 */
fun <T : Number> List<T>.sum(): Int {
    val i: Int = 0
    return i
}

/**
 * value是可空的
 */
fun <T> process(value: T) {
    println(value?.hashCode())
}
/**
 * 此时value不可空
 */
fun <T : Any> process1(value: T) {
    println(value.hashCode())
}