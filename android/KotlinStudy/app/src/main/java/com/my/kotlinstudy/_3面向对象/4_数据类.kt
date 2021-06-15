package com.my.kotlinstudy._3面向对象

fun main() {
//    val client2 = Client2("name", 1)
//    println(client2)


//    val b = BaseImpl(10)
//    Derived(b).print2()

//    val byLazy = ByLazy()
//    byLazy.print()

    val e = Example()
    e.p = "ppp"
    println(e.p)
}

class Client(val name: String, val postalCode: Int) {
    override fun toString(): String {
        return "Client(name=$name, postalCode=$postalCode)"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Client)
            return false
        return name == other.name && postalCode == other.postalCode
    }

    //复制val变量的类
    fun copy(name: String = this.name, postalCode: Int = this.postalCode) = Client(name, postalCode)
}

/**
 * data类会自动重写满足Client中要求的toString、equals、hashCode
 */
data class Client2(val name: String, val postalCode: Int)
