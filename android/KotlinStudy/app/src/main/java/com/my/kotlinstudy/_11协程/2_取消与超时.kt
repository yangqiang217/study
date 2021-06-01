package com.my.kotlinstudy._11协程

import kotlinx.coroutines.*

fun main21() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("job of $i")
            delay(500L)
        }
    }
    delay(1300L)
    println("start cancel")
//    job.cancel()
//    job.join()// 等待作业执行结束
    job.cancelAndJoin()
    println("end")
}

/**
 * 如果协程正在执行计算任务，并且没有检查取消的话，那么它是不能被取消的
 */
fun main22() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个作业并且等待它结束
    println("main: Now I can quit.")

}
//保证能被取消：
fun main23() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // 可以被取消的计算循环
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并等待它结束
    println("main: Now I can quit.")
}
