package com.my.kotlinstudy._10协程

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import kotlin.system.measureTimeMillis

/**
 * 异步返回多个计算好的值呢？这正是 Kotlin 流（Flow）的用武之地。
 * 使用 Flow 的代码与先前示例的下述区别：
    名为 flow 的 Flow 类型构建器函数。
    flow { ... } 构建块中的代码可以挂起。
    函数 simple 不再标有 suspend 修饰符。
    流使用 emit 函数 发射 值。
    流使用 collect 函数 收集 值。
 */
fun simple1(): Flow<Int> = flow {//非阻塞主线程次序发送数据
    println("start simple 4")
    for (i in 1..3) {
        delay(100)
//        Thread.sleep(1000)//sleep会让整个县城阻塞
        emit(i)// 发送下一个值
//        println("emit $i in thread: ${Thread.currentThread().name}")//main
    }
    println("end simple 4")
}
fun main51() = runBlocking {
//    launch {// 启动并发的协程以验证主线程并未阻塞
//        for (i in 10..13) {
//            println("i am not blocked $i in thread: ${Thread.currentThread().name}")//main
//            delay(1000)
//        }
//    }
    val flow = simple1()//流是冷的: 调用这句不会执行simple4，只有调用collect的时候才执行
    //在不阻塞主线程的情况下每等待 100 毫秒打印一个数字
    flow.collect { println(it) }
    flow.collect { println(it) }
    println("end main")
}

/**
 * 流取消基础
 * 流采用与协程同样的协作取消。流的收集可以在当流在一个可取消的挂起函数（例如 delay）中挂起的时候取消。
 * 以下示例展示了当 withTimeoutOrNull 块中代码在运行的时候流是如何在超时的情况下取消并停止执行
 */
fun main52() = runBlocking {
    withTimeoutOrNull(250) {//250ms后超时
        simple1().collect { println(it) }
    }
    println("end main")
}

/**
 * 流构建器
 */
fun main53() = runBlocking {
    (1..3).asFlow().onEach {
        delay(1000)
    }.collect { println(it) }
}

/**
 * 过渡流操作符
 * 这些操作符也是冷操作符，就像流一样。这类操作符本身不是挂起函数。它运行的速度很快，返回新的转换流的定义。
 */
suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}
fun main54() = runBlocking {
    //map
//    (1..3).asFlow()
//        .map { performRequest(it) }
//        .collect { println(it) }

    //transform: 可以 发射 任意值任意次。比如说，使用 transform 我们可以在执行长时间运行的异步请求之前发射一个字符串并跟踪这个响应：
    (1..3).asFlow()
        .transform {
            emit(it)//map里面就不能发射
            emit(it)
            emit(performRequest(it))
//            performRequest(it)//必须得发射，不然这句不会起作用，不像map
        }
        .collect { println(it) }
}

/**
 * 限长操作符
 */
fun numbers(): Flow<Int> = flow {
    try {//到限制后抛异常。可以不try也没事
        emit(1)
        emit(2)
        println("this line will not execute")
        emit(3)
    } catch (e: Exception) {
        println("in exception")
    } finally {
        println("finally in numbers")
    }
}
fun main55() = runBlocking {
    numbers().take(2).collect { println(it) }
}

/**
 * 末端流操作符
 */
fun main56() = runBlocking {
    val sum = (1..5).asFlow()
        .map {
            println("map it: $it")
            it
        }
        .reduce { a, b ->//先输入前两个，算完a变成上次算的结果，b变成下一个数，继续算，以此类推
            println("reduce, a: $a, b: $b")
            a + b
        }
    println(sum)
}

/**
 * 流上下文
 * 流的收集总是在调用协程的上下文中发生。所以默认的，flow { ... } 构建器中的代码运行在相应流的收集器提供的上下文中。
 *
 * 以下想让耗时操作在 Dispatchers.Default里发生，并通知外面的代码会发生异常。因为withContext用于改变协程上下文，
 * 但flow{}又必须遵循上下文，不允许从其它上下文中emit。用flowOn可以
 */
fun simple2(): Flow<Int> = flow {
    withContext(Dispatchers.Default) {
        for (i in 1..3) {
            Thread.sleep(100)
            emit(i)
        }
    }
}
fun main57() = runBlocking {
    simple2().collect { println(it) }
}

/**
 * flowOn可以解决上述无法切换上下文的问题。
 * 此函数用于更改流发射的上下文。 以下示例展示了更改流上下文的正确方法，该示例还通过打印相应线程的名字以展示它们的工作方式：
 */
fun simple3(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(1000)
        println("flow in thread: ${Thread.currentThread().name}")//DefaultDispatcher-worker-1
        emit(i)
    }
}.flowOn(Dispatchers.Default)
fun main58() = runBlocking {
    simple3().collect {
        println("collect $it in thread: ${Thread.currentThread().name}")//main
    }
    println("end")//最后打印
}

/**
 * 缓冲
 * 一个 simple 流的发射很慢，它每花费m毫秒才产生一个元素；而收集器需要花费n毫秒来处理元素，总花费是所有耗时的累加
 *
 */
fun simple4(): Flow<Int> = flow {//非阻塞主线程次序发送数据
    for (i in 1..3) {
        delay(500)
        println("emit $i")
        emit(i)// 这里如果collect里面delay的10s，那这个方法也要卡10s
    }
}
fun main59() = runBlocking {
    val time = measureTimeMillis {
        simple4()
            .collect {
                delay(3000)//等执行完了才能让emit发送下一个
                println("collect1 $it")
            }
    }

    val time2 = measureTimeMillis {
        simple4()
            .buffer()//不卡emit，让emit自己时间到了就发
            .collect {
                delay(3000)
                println("collect2 $it")
            }
    }
    println(time)
    println(time2)
}

/**
 * conflate
 * 没有必要处理每个值，而是只处理最近一次emit的那个，collect处理完继续拿最近一次emit释放的值，
 * 比如collect里面执行了10s，100个数早就emit完了，collect下一个就是第100个。
 */
fun main510() = runBlocking {
    val time = measureTimeMillis {
        simple4()
            .conflate()
            .collect {
                delay(600)
                println(it)
            }
    }
    println(time)
}

/**
 * collectLatest
 * 新值来了后，上一个没处理完的直接取消(这里是取消在delay里面了)
 */
fun main511() = runBlocking {
    val time = measureTimeMillis {
        simple4()
            .collectLatest {
                println("collecting $it")
                delay(1000)
                println("collected $it")
            }
    }
    println(time)
}

/**
 * zip 组合
 * 以短的一个为准
 * 1 -> one
 * 2 -> two
 * 3 -> three
 */
fun main512() = runBlocking {
    val nums = (1..10).asFlow()
    val strs = flowOf("one", "two", "three")
    nums.zip(strs) { a, b ->
//        println("in zip a: $a, b: $b")
        "$a -> $b"
    }.collect { println(it) }
}

/**
 * combine
 * 和zip不同，zip会以时间长的一个为准，等它完了才一一配对结合，combine时间长的会和时间短的一个最近一次释放的结合
 * 有点conflate的意思
 */
fun main513() = runBlocking {
    val nums = (1..3).asFlow().onEach { delay(100) }
    val strs = flowOf("one", "two", "three").onEach { delay(1000) }
    val startTime = System.currentTimeMillis()
//    nums.zip(strs) { a, b ->
    nums.combine(strs) { a, b ->
        "$a -> $b"
    }.collect {
        println("$it at ${System.currentTimeMillis() - startTime} ms from start")
    }
}

/**
 * 展平
 */
@ExperimentalCoroutinesApi
fun main514() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(500) }
//        .flatMapConcat {//按顺序
//        .flatMapMerge {//前面快的话会先走完
        .flatMapLatest {//类似collectLatest，下一个开始的时候上一个取消
            println("----in flatMapConcat: $it")
            requestFlow(it)
        }
        .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
}
fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(1000)
    emit("$i: Second")
}


/**
 * 收集器 try 与 catch
 */
fun simple5(): Flow<Int> = flow {
    for (i in 1..3) {
        println("emit $i")
        emit(i)
    }
}
fun main515() = runBlocking {
    try {
        simple5().collect {
            println(it)
            check(it <= 1)//在末端操作符 collect 中捕获了异常，并且， 如我们所见，在这之后不再发出任何值
        }
    } catch (e: Exception) {
        println("catch $e")
    }
}


//声明式处理
/**
 * onCompletion 流完全收集时调用，有try后异常也会调用，会传入一个Throwable
 * onCompletion 操作符与 catch 不同，它不处理异常。我们可以看到前面的示例代码，异常仍然流向下游。它将被提供给后面的 onCompletion 操作符，并可以由 catch 操作符处理。
 */
fun main516() = runBlocking {
    simple5()
        .onCompletion {
            if (it != null) {
                println("has exception: $it")
            }
            println("done")
        }
        .catch { println("catch exception") }
        .collect {
            println(it)
            check(it <= 1)//在末端操作符 collect 中捕获了异常，并且， 如我们所见，在这之后不再发出任何值
        }
}

/**
 * launchIn
 * 可以在单独的协程中启动流的收集，这样就可以立即继续进一步执行代码：
 * 可以用onEach-launchIn代替addEventListener
 */
fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(100) }
fun main() = runBlocking {
    events()
        .onEach { println("event: $it") }
//        .collect()//done 会在最后打出
        .launchIn(this)//done会在最前面打出
    println("done")
}