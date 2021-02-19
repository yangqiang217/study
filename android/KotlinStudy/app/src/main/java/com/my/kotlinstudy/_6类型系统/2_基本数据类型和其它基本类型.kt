package com.my.kotlinstudy._6类型系统

import java.lang.NullPointerException

fun main() {
//    progress(101)
//    numConvert()
    nothingElvis(null)
}

/**
 * 除了泛型，kot的Int都会编译成int来提高性能
 */
fun progress(progress: Int) {
    //小于0取0，大于100取100
    val percent = progress.coerceIn(0, 100)
    println(percent)
}

/**
 * 可空基本类型
 */
data class Person3(val name: String, val age: Int? = null) {
    fun isOlder(p: Person3): Boolean? {
        if (age == null || p.age == null) {//必须有这个if判断，不然return会报错
            return null
        }
        return age > p.age
    }
}

/**
 * 数字转换
 */
fun numConvert() {
    val i = 1
//    val l: Long = i//error 类型不匹配，即便转成范围更大的也不行
    val l: Long = i.toLong()

    val x = 1
    val list = listOf(1L, 2L)
//    println(x in list)//虽然能编译，但是运行的时候jvm在别的地方crash了
    println(x.toLong() in list)

    val x1: Int = 1
    val y1: Long = 1L
    println(x1 + y1)//不用显式转换可以做运算
}

/**
 * Any 所有类型的基类，类似java的Object，最终也会被编译成Object
 * hashCode、equals、toString也都是来自于Any的
 * Any是非空的，不能赋值为null
 */
fun any() {
    val i: Any = ""
//    val i1: Any = null
    val i1: Any? = null
}


/**
 * Unit，Kot的void
 * 和void的区别：Unit是一个完备的类型，可以作为类型参数，void不行
 */
fun unit(): Unit {
}

interface Processor<T> {
    fun process(): T
}
//这样写就可以process方法不写返回值了
class ProcessorImpl : Processor<Unit> {
    override fun process() {
    }
}


/**
 * Nothing：函数永不返回
 * Nothing类型没有任何值，只有被当做返回类型时使用，或者被当做泛型函数返回值的类型参数才有意义
 * 有些函数返回类型没有意义，因为他们从来不会成功结束
 */
fun fail(msg: String): Nothing {
    throw NullPointerException(msg)
}
//返回Nothing的函数可以放在Elvis的右边来做先决条件检查：
fun nothingElvis(s: String?) {
    val ss = s ?: fail("empty")
    println(ss)
}