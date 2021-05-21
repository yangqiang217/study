package com.my.kotlinstudy._10协程

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // 假设我们在这里做了一些有用的事
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // 假设我们在这里也做了一些有用的事
    return 29
}

/**
 * 如果需要按顺序调用它们:
 */
fun main31() = runBlocking {
    val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("answer is ${one + two}")
    }
    println("completed in $time")
}

/**
 * 使用 async 并发
 * 如果 doSomethingUsefulOne 与 doSomethingUsefulTwo 之间没有依赖，并且我们想更快的得到结果，让它们进行 并发 吗
 *
 * 在概念上，async 就类似于 launch。它启动了一个单独的协程，这是一个轻量级的线程并与其它所有的协程一起并发的工作。
 * 不同之处在于 launch 返回一个 Job 并且不附带任何结果值，而 async 返回一个 Deferred —— 一个轻量级的非阻塞 future，
 * 这代表了一个将会在稍后提供结果的 promise。你可以使用 .await() 在一个延期的值上得到它的最终结果，
 * 但是 Deferred 也是一个 Job，所以如果需要的话，你可以取消它。
 *
 * 使用协程进行并发总是显式的。
 */
fun main32() = runBlocking {
    val time = measureTimeMillis {
        //让one和two同时执行
        val one = async {//如果不用async，那就和上一个函数一样，one和two是按顺序执行的
            doSomethingUsefulOne()
        }
        val two = async {
            doSomethingUsefulTwo()
        }
        println("answer is ${one.await() + two.await()}")
    }
    println("completed in $time")
}

/**
 * 惰性async
 * async 可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
 * 在这个模式下，只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调用的时候。运行下面的示例：
 */
fun main33() = runBlocking {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) {
            doSomethingUsefulOne()
        }
        val two = async(start = CoroutineStart.LAZY) {
            doSomethingUsefulTwo()
        }
        one.start()//注意，如果我们只是在 println 中调用 await，而没有在单独的协程中调用 start，这将会导致顺序行为，直到 await 启动该协程 执行并等待至它结束
        two.start()
        println("answer is ${one.await() + two.await()}")
    }
    println("completed in $time")
}

//async 风格的函数(强烈不推荐)
/**
 * somethingUsefulOneAsync 函数的返回值类型是 Deferred<Int>
 * 这些 xxxAsync 函数不是 挂起 函数。它们可以在任何地方使用。 然而，它们总是在调用它们的代码中意味着异步（这里的意思是 并发 ）执行。
 */
fun somethingUsefulOneAsync() = GlobalScope.async {
    doSomethingUsefulOne()
}
// somethingUsefulTwoAsync 函数的返回值类型是 Deferred<Int>
fun somethingUsefulTwoAsync() = GlobalScope.async {
    doSomethingUsefulTwo()
}

fun main34() {
    val time = measureTimeMillis {
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()
        runBlocking {
            println("answer is ${one.await() + two.await()}")
        }
    }
    println("completed in $time")
}