package com.my.kotlinstudy._4面向对象

import java.io.File

typealias StringSet = HashSet<String>
typealias KMap<K> = HashMap<K, MutableList<File>>
typealias MyHandler = (Int, String, Any) -> Unit
typealias Predicate<T> = (T) -> Boolean
typealias InnerA = AliasA.Inner
class AliasA {
    class Inner
}

fun main() {
    val strSet = StringSet()
    val kmap = KMap<String>()

    val innera = InnerA()

    val f: (Int) -> Boolean = { it > 0}
}

fun foo(p: Predicate<Int>) {
    p.invoke(3)
    p(3)
}