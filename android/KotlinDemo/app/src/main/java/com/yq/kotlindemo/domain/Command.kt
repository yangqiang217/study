package com.yq.kotlindemo.domain

interface Command<T> {
    fun execute(): T
}