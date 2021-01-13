package _8高阶函数

import java.lang.StringBuilder

/**
 * 高阶函数：任何以lambda或者函数引用作为参数的函数、或返回值为lambda或函数引用的函数、或两者都满足的函数
 * 例如filter将一个判断式函数作为参数，就是一个高阶函数：
 * list.filter { x > 0 }
 */
fun main() {
    twoAndThree({x, y -> x + y})//or
    twoAndThree { x, y -> x + y }

    filter { c -> c == 'c' }

    joinToString<String>()
    joinToString2<String>{ it.toLowerCase() }
    joinToString2<String>(sep = ".", transform = { it.toLowerCase() })
    joinToString3(sep = ".", transform = null)

    val calculator = getCost(Delivery.EXPEDITED)
    val res: Double = calculator(Order(1))
}

/**
 * 函数类型，声明高阶函数
 */
fun funType() {
    val sum = { x: Int, y: Int -> x + y }
    val action = { println(11) }
    //上面其实都是简写声明，显示声明为：(参数类型1, 参数类型2...) -> 返回类型
    val sum1: (Int, Int) -> Int = { x, y -> x + y }
    val action2: () -> Unit = { println(11) }
    //可空的：
    val nullable: (Int) -> Int? = { null }
}

/**
 * 调用高阶函数
 */
fun twoAndThree(operation: (Int, Int) -> Int) {
    val res = operation(2, 3)
    println(res)
}
fun filter(predicate: (Char) -> Boolean): String {
    val c = 'c'
    if (predicate(c)) {
        println("in")
    }
    return "cc"
}


/**
 * 函数类型的参数默认值和null值
 */
//不带默认lambda参数的：
fun <T> joinToString(
    sep: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val res = StringBuilder(prefix)
    res.append(sep)
    res.append(postfix)
    return res.toString()
}
//带默认lambda参数的，给方法传递额外需求：
fun <T> joinToString2(
    sep: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: (T) -> String = { it.toString() }
): String {
    val res = StringBuilder(prefix)
    res.append(sep)
    res.append(transform)
    res.append(postfix)
    return res.toString()
}
//带默认lambda参数的，可空的：
fun joinToString3(
    sep: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: ((Int) -> String)? = null
): String {
    val res = StringBuilder(prefix)
    res.append(sep)

    val ele = 1
    res.append(transform?.invoke(ele) ?: ele.toString())
    res.append(postfix)
    return res.toString()
}


/**
 * 返回函数
 */
enum class Delivery { STANDARD, EXPEDITED }
class Order(val itemCount: Int)
fun getCost(delivery: Delivery): (Order) -> Double {
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }
    }
    return { order -> 11.1 * order.itemCount }
}