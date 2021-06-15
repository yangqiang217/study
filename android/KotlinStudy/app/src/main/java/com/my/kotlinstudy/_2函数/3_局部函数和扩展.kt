package com.my.kotlinstudy._2函数

import java.lang.IllegalArgumentException

fun main() {

}

class User {
    lateinit var name: String
    lateinit var address: String
}

/**
 * 带重复代码的函数
 */
fun saveUser(user: User) {
    if (user.name.isEmpty()) {
        throw IllegalArgumentException("name is empty")
    }
    if (user.address.isEmpty()) {
        throw IllegalArgumentException("address is empty")
    }
}

/**
 * 优化重复代码的
 */
fun saveUser2(user: User) {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            //局部函数可以访问所在函数所有参数和变量
            throw IllegalArgumentException("$fieldName of $user is empty")
        }
    }

    validate(user.name, "name")
    validate(user.address, "address")
}

/**
 * 继续优化，做成User的扩展函数
 */
fun User.validate() {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            //局部函数可以访问所在函数所有参数和变量
            throw IllegalArgumentException("$fieldName of $this is empty")
        }
    }
}