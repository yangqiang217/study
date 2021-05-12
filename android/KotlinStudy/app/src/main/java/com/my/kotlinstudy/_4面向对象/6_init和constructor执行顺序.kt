package com.my.kotlinstudy

/**
 * 对象里面init统一早于constructor，内部顺序按代码顺序
 * companion里面的init早于所有对象的，但如果companion的init之前new了这个对象，那么就会先走对象的
 */
fun main() {
    val a = TestObj()
    a.print()
}

class TestObj {

    init {
        println("TestObj init1")
    }

    constructor() {
        println("TestObj constructor1")
    }

    constructor(a: Int) {
        println("TestObj constructor1")
    }


    init {
        println("TestObj init2")
    }

    fun print() {
    }

    companion object {
        val a = TestObj()

        init {
            println("TestObj init in companion")
        }
    }
}