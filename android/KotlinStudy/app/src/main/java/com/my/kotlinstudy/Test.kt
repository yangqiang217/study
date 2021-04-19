package com.my.kotlinstudy

fun main() {
//    val map = HashMap<String, Int>()
//
//    map["1"] = 1
//    val a: Int? = map["1"]
//    if (a != null) {
//
//        println(a)
//    }

    val list: List<String>? = null
    val a: String = list?.firstOrNull() ?: "kong"
    println(a)
}

class Person(_age: Int) {
    val age = _age

}