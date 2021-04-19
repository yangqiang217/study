package com.my.kotlinstudy._4面向对象

fun main() {
    val a: ClassA = ClassA()

    a.setListener01(object : Listener0 {
        override fun fun0() {
            println("0000")
        }
    })
    //或：只有函数式接口才能这么写，调用java的符合函数式接口规则的接口可以默认是函数式接口
    a.setListener01 {
        println("0000")
        println("0000")
    }

    a.setListener11(object : Listener1 {
        override fun fun1(para1: Int) {
            println("1111: $para1")
        }

    })

    a.setListener21(object : Listener2 {
        override fun fun1(para1: Int, para2: String) {
            println("2222: $para1, $para2")
        }

        override fun fun2(para1: Int) {
            println("2222: $para1")
        }

    })

    a.run()
}

class ClassA {
    var listener0: Listener0? = null
    var listener1: Listener1? = null
    var listener2: Listener2? = null

    fun setListener01(lis: Listener0) {
        listener0 = lis
    }
    fun setListener11(lis: Listener1) {
        listener1 = lis
    }
    fun setListener21(lis: Listener2) {
        listener2 = lis
    }

    fun run() {
        listener0?.fun0()
        listener1?.fun1(para1 = 1)
        listener2?.fun2(para1 = 2)
    }
}

/**
 * 只有一个抽象方法的接口称为函数式接口或 SAM（单一抽象方法）接口。函数式接口可以有多个非抽象成员，但只能有一个抽象成员。
 */
fun interface Listener0 {
    fun fun0()
}
fun interface Listener1 {
    fun fun1(para1: Int)
}
/*fun 报错 */interface Listener2 {
    fun fun1(para1: Int, para2: String)
    fun fun2(para1: Int)
}