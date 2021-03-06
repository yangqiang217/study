package com.my.kotlinstudy._6泛型

/**
 * kt泛型没有通配符
 */
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