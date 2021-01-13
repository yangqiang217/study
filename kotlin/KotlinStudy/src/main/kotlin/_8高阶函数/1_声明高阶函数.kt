package _8高阶函数

/**
 * 高阶函数：任何以lambda或者函数引用作为参数的函数、或返回值为lambda或函数引用的函数、或两者都满足的函数
 * 例如filter将一个判断式函数作为参数，就是一个高阶函数：
 * list.filter { x > 0 }
 */
fun main() {
    twoAndThree({x, y -> x + y})//or
    twoAndThree { x, y -> x + y }

    filter { c -> c == 'c' }
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