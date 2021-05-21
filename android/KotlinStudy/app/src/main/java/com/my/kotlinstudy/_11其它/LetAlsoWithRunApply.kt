package com.my.kotlinstudy._10协程

/**
 * https://www.jianshu.com/p/e3ae07461a21
 * let主要用来判空
 * also和let类似，但是返回值是传入值
 *
 * with主要用来简写，不用x.bbb()只写bbb()
 * run是with和let的结合，判空+简写
 * apply和run类似，但返回值是传入值
 */
fun main11() {
    _let()
//    _also()
//    _with()
//    _run()
//    _apply()
}

/**
let
作用1：使用it替代object对象去访问其公有的属性 & 方法
object.let{
   it.todo()
}

作用2：判断object为null的操作
object?.let{//表示object不为null的条件下，才会去执行let函数体
   it.todo()
}

注：返回值 = 最后一行 / return的表达式
 */
fun _let() {
    val v: String? = null

    //判空的方式
    if (v != null) {
        val v1 = println(v.substring(1))
    }

    //?的方式
    val v2 = v?.substring(1)

    //let
    val v3 = v?.let {
        it.substring(1)
    }

    val v4 = v?.let {
        println(it)
    } ?: run {
        println("empty")
    }
}

/**
类似let函数，但区别在于返回值：

let函数：返回值 = 最后一行 / return的表达式
also函数：返回值 = 传入的对象的本身
 */
fun _also() {
    val v: String? = "1"
    var v1 = v?.let {
        it.substring(1)
        "sss"//v1是sss
    }
    var v2 = v?.also {
        it.substring(1)
        "sss"//v2是v
    }
    println("v1: $v1")
    println("v2: $v2")

    //交换两数：
    var a = 1
    var b = 2
    a = b.also {
        b = a
    }
}

/**
调用同一个对象的多个方法 / 属性时，可以省去对象名重复，直接调用方法名 / 属性即可
with(object){
   // ...
 }
返回值 = 函数块的最后一行 / return表达式
 */
fun _with() {
    val v = "name"
    val v1 = with(v) {
        println(length)
        substring(1)
    }
    println(v1)
}

/**
结合了let、with两个函数的作用，即：

调用同一个对象的多个方法 / 属性时，可以省去对象名重复，直接调用方法名 / 属性即可
定义一个变量在特定作用域内
统一做判空处理
object.run{
// ...
}
// 返回值 = 函数块的最后一行 / return表达式
 */
fun _run() {
    val v: String? = "name"
    val v1 = v?.run {
        println(length)
        substring(1)//v1=ame
    }
    println(v1)
}

/**
 * 单独的run
 */
fun running() {
    val list = listOf("1", "2")

    kotlin.run {
        println("in kotlin.run 1")
        println("in kotlin.run 2")
    }
    kotlin.run shit@ {
        println("in kotlin.run@ 1")
        println("in kotlin.run@ 2")
        return@shit
    }
    run {
        println("in run 1")
        println("in run 2")
    }
    run shit@ {//@shit标签作用是符合if的条件退出是只中断这个{}里的，after run还是会执行。没有标签没法实现满足一定条件就不走后面这种逻辑
        list.forEachIndexed { index, s ->
            if (index == 1) {
                println("index is 1")
                return//退出整个方法
            }
        }
        println("in run@ 1")
        println("in run@ 2")
        return@shit
    }
    println("after run")
}

/**
对象实例初始化时需要对对象中的属性进行赋值 & 返回该对象
与run函数类似，但区别在于返回值：
run函数返回最后一行的值 / 表达式
apply函数返回传入的对象的本身
 */
fun _apply() {
    val v: String? = "name"
    val v1 = v?.apply {
        println(length)
        substring(1)//v1=name
    }
    println(v1)
}