package com.my.kotlinstudy._9泛型

/**
 * out 相当于java里面的 <? extend>
 * in 相当于java里面的 <? super>
 */

/**
 * 协变
 */
interface Producer<out T> {
    fun produce(): T
}