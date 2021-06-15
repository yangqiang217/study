package com.my.kotlinstudy._1基础

fun main() {
    val ans = 2
    val ans1: Int = 2

    //如果变量没有初始化，需要显式指定类型
    val ans2: Int
    ans2 = 2

    //var的值类型不能变
    var ans3 = 2
//    ans3 = "a" error

    val b: Boolean? = null
    if (b == true) {//true或null，== true不能省

    } else {
        println("null")
    }

    val c: Char = '0'
    println("char a to int: ${c.toInt()}")

    //数组
    val array = arrayOfNulls<String>(3)
    println("array size: ${array.size}")//3

    //原生类型数组，无装箱开销
    val intArray = intArrayOf(1)
    val intArray2 = IntArray(3)
    val intArray3 = IntArray(3) {32}//3个32的数组
    println("int array size: ${intArray2.size}")//3

    //String
    val text = """//保留换行等
for (c in "foo") {
    print(c)
}
    """
    val price = """
${'$'}000
    """
    println(text)
    println(price)
}