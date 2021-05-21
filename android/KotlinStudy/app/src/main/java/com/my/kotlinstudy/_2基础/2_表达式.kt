package com.my.kotlinstudy._2基础

import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.util.*

/**
 * 所有变量的类型都是引用
 * kot中没有原始类型，只有包装类型，编译器在编译代码的时候会自动优化性能，把对应的包装类型拆箱为原始类型
 */
fun main(args: Array<String>) {
//    for (i in 100 downTo 1 step 2) {
//        println(i)
//    }
    exception()
}

fun value() {
    val a:Int = 1
    val a1 = 1//自动推断
//    a++ error
}

/**
 * if后的括号必须有，里面必须bool，if("a")、if(1)不行
 * kt中if是表达式不是语句，表达式是有值的，java中所有的控制结构都是语句，kt中除了循坏外大多数控制结构都是表达式
 * 相反，java中赋值操作时表达式，在kt中成了语句
 */
fun ifelse(a: Int, b: Int) {
    //表达式返回值，代替java true?1:0
    val max = if (a > b) a else b

    //if分支可以是代码块，最后的表达式作为块的值
    val max2 = if (a > b) {
        print("max is a")
        a//返回值
    } else {
        print("max is b")
        b
    }
}

/**
 * 类似switch case
 */
fun when_(obj: Any?) {
    val arr = arrayOf(1, 2, 3)
    when (obj) {
        0,1,2,3 -> print("$obj obj在0-3")
        in 1..10 -> print("$obj in 1-10")
        in arr -> print("$obj in arr")
        !in 1..3 -> print("$obj not in 1-3")
        "hello" -> print("$obj obj is hello")
        is Char -> {
            print("$obj is char")
        }
        else -> print("$obj else类似java中的default")
    }
}
//带返回值
fun whenReturn(e: Int): Int =
    when (e) {
        1 -> {
            println("is 1")
            1
        }
        is Number -> {
            println("1")
            2//返回2
        }
        else -> {
            println("else")
            3
        }
    }

/**
 * 没有java中的fori循环
 * ...是左右都包括，until不包括右边：1 until 10
 */
fun for_() {
    val arr = arrayOf(1, 2, 3)
    //for each
    for (item in arr) {

    }//或：
    arr.forEach {
        println(it)
    }
    //带i
    for (i in arr.indices) {

    }
    //带i和value
    for ((index, value) in arr.withIndex()) {
//        print("ele at $index is $value")
    }


    //包括1和10
    for (i in 1..10) {
//        println(i)
    }
    //倒数
    for (i in 100 downTo 1 step 2) {
        //100,98,96...4,2
    }

    //map循环
    val map = TreeMap<Char, String>()
    map['a'] = "aa"//类似于java的put
    for ((c, s) in map) {
        println("key: $c, value: $s")
    }
}

//和java类似
fun while_() {}

/**
 * 除了表达式的值，有返回值的函数都要求显示使用return
 */
fun return_() {

}
//函数字面量
fun sum(a: Int, b: Int) = a + b
fun max(a: Int, b: Int) = if (a > b) a else b
//nothing标记无返回的函数(可以省略)：
fun fail(msg: String): Nothing {
    throw IllegalArgumentException(msg)
}

fun operator(x: Int) {
    //Elvis 主要用来null检查，类似于java的true?1:0
    val y = x ?: 0
}

fun exception() {
    try {

    } catch (e: Exception) {

    } finally {

    }

//    throw NullPointerException("null")

    //作为表达式，num的值就是最后一个表达式的值
    val num = try {
        Integer.parseInt("shit")
    } catch (e: Exception) {
        e.printStackTrace()
        1
    }
    println(num)
}

/**
 * 相等性
 */
fun equal() {
    //1.结构相等, 结构相等由 ==（以及其否定形式 !=）操作判断。按照惯例，像 a == b 这样的表达式会翻译成: a?.equals(b) ?: (b === null)
    val a = 1
    val b = 12
    println(a == b)
    //2.引用相等，引用相等由 ===（以及其否定形式 !==）操作判断。a === b 当且仅当 a 与 b 指向同一个对象时求值为 true。
    // 对于运行时表示为原生类型的值 （例如 Int），=== 相等检测等价于 == 检测。
}