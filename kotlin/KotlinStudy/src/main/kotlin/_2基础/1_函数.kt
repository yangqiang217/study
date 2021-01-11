package _2基础

fun main() {
    funAsVal()
}

/**
 * 完整的函数声明
 */
fun multiply(x: Int, y: Int): Int {
    return x * y
}

/**
 * 表达式函数体
 */
fun max(a: Int, b: Int): Int = if (a > b) a else b
//或省略返回类型，编译器会推导出（只有表达式函数体的返回可以省略，有返回的代码块函数体不行）：
fun max1(a: Int, b: Int) = if (a > b) a else b

/**
 * 函数当变量
 */
fun funAsVal() {
    val sum = fun (x: Int, y: Int): Int {return x + y}
    println(sum)
    println(sum(1, 1))
}