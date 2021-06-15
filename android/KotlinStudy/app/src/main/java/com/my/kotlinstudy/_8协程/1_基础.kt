package com.my.kotlinstudy._8协程

import kotlinx.coroutines.*

/**
 * 其实就是封装的线程池，真正耗时的操作还是得放在子线程，轻量的意思只是写法上轻量，性能上等价于线程池
 */
fun main() {
    GlobalScope.launch {// 在后台启动一个新的协程并继续，会在新的线程
        withContext(Dispatchers.IO){
        /* withContext后续代码会等这个结束，GlobalScope.launch后面的会正常走，
        所以就能实现这里请求数据，withContext后面接着更新UI */
//            TimeUnit.SECONDS.sleep(10)
        }
        //delay是一个特殊的 挂起函数 ，它不会造成线程阻塞，但是会 挂起 协程，并且只能在协程中使用。
        delay(1000L)
        println("1 in thread ${Thread.currentThread().name}")//DefaultDispatcher-worker-1
    }
    println("2 in thread ${Thread.currentThread().name}")//main
    runBlocking {//这个表达式阻塞了主线程，直到 runBlocking 内部的协程执行完毕。
        delay(2000L)
        println("3 in thread ${Thread.currentThread().name}")//main
    }
    println("4 in thread ${Thread.currentThread().name}")//main
}

fun main3() = runBlocking {
    launch(Dispatchers.IO) { // 在 runBlocking 作用域中启动一个新协程
        delay(1000L)
        println("after delay in thread ${Thread.currentThread().name}")//DefaultDispatcher-worker-1
    }
    println("end in thread ${Thread.currentThread().name}")//main
}

/**
 * runBlocking 与 coroutineScope 可能看起来很类似，因为它们都会等待其协程体以及所有子协程结束。
 * 主要区别在于，runBlocking 方法会阻塞当前线程来等待， 而 coroutineScope 只是挂起，会释放底层线程用于其他用途。
 * 由于存在这点差异，runBlocking 是常规函数，而 coroutineScope 是挂起函数
 */
fun main4() = runBlocking {
    launch {
        delay(2000L)
        println("after delay1")
    }

    coroutineScope {//创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束
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