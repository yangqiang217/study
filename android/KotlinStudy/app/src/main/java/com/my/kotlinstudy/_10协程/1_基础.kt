package com.my.kotlinstudy._10协程

import kotlinx.coroutines.*

fun main1() {
    GlobalScope.launch {// 在后台启动一个新的协程并继续
        //delay是一个特殊的 挂起函数 ，它不会造成线程阻塞，但是会 挂起 协程，并且只能在协程中使用。
        delay(1000L)
        println("1 in thread ${Thread.currentThread().name}")
    }
    println("2 in thread ${Thread.currentThread().name}")
    runBlocking {//这个表达式阻塞了主线程
        delay(2000L)
        println("3 in thread ${Thread.currentThread().name}")
    }
    println("4 in thread ${Thread.currentThread().name}")
}

fun main() = runBlocking {
    launch(Dispatchers.IO) { // 在 runBlocking 作用域中启动一个新协程
        delay(1000L)
        println("after delay in thread ${Thread.currentThread().name}")
    }
    println("end in thread ${Thread.currentThread().name}")
}

fun main4() = runBlocking {
    launch {
        delay(2000L)
        println("after delay1")
    }

    coroutineScope {
        launch {
            delay(3000L)
            println("after delay2")
        }

        delay(1000L)
        println("after delay3")
    }

    println("end")
}

/**
 * 提取函数
 * 将 launch { …… } 内部的代码块提取到独立的函数中
 * 当你对这段代码执行“提取函数”重构时，你会得到一个带有 suspend 修饰符的新函数。 这是你的第一个挂起函数。
 * 在协程内部可以像普通函数一样使用挂起函数， 不过其额外特性是，同样可以使用其他挂起函数（如本例中的 delay）来挂起协程的执行
 */
fun main5() = runBlocking {
    launch {
        doWorld()
    }
    println("end")
}
suspend fun doWorld() {
    delay(1000L)
    println("after delay")
}