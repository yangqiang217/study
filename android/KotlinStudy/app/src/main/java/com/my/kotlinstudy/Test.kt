package com.my.kotlinstudy

fun main() {

//    Parent("xujiafeng")
    val a = Child("xujiafeng")
    a.name
}

open class Parent(open var name: String) {
    var nameLength: Int

    init {
        nameLength = name.length
    }
}

class Child(override var name: String) : Parent(name) {

    init {
        nameLength = name.length
    }
}