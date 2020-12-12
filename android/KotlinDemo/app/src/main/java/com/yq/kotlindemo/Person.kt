package com.yq.kotlindemo

class Person {
    var name: String = ""
        get() = field.toUpperCase()
        //todo 怎么只暴露get接口
        set(value) {
            field = "name: $value"
        }
}