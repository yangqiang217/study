package com.my.kotlinstudy._6泛型

/**
 * out 相当于java里面的 <? extend>
 * in 相当于java里面的 <? super>
 */
fun main() {
    val sons = listOf(Son())
    val anys: List<Any> = listOf(Son())
    f(sons)
    f(anys)
}

fun demoOut(strs: Producer<String>) {
    /*
    如果没有out，则认为Producer<String>不是Producer<Any>的子类，所以没法传，在java里要用<? extend Object>
    称为：declaration-site variance
     */
    val obj: Producer<Any> = strs
}

fun demoIn(x: Consumer<Number>) {
    x.consume(1.1)
    val y: Consumer<Double> = x
}

/**
 * 协变
 */
interface Producer<out T> {
    fun produce(): T
}

/**
 * 逆变
 */
interface Consumer<in T> {
    fun consume(p: T)
}

open class Father {

}
class Son : Father() {

}

fun <T : Any> f(list: List<T>) {

}