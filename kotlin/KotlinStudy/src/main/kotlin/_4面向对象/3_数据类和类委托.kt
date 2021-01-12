package _4面向对象

fun main() {
    val client2 = Client2("name", 1)
    println(client2)
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


/**
 * 委托 by
 * 常规委托实现：挨个把动作给内部变量
 */
class DelegatingCollection<T> : Collection<T> {
    private val innerList = arrayListOf<T>()

    override val size: Int
        get() = innerList.size
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun contains(element: T): Boolean = innerList.contains(element)
    override fun iterator(): Iterator<T> = innerList.iterator()
    override fun containsAll(elements: Collection<T>): Boolean = innerList.containsAll(elements)
}
/** 用by实现简写的委托 */
class DelegatingCollection2<T>(innerList: Collection<T> = ArrayList<T>()) : Collection<T> by innerList {
}
/**
 * 使用委托实例
 * 只实现add和addAll，其它的比如contains、isEmpty等不用写也不会报错，因为委托了
 */
class CountingSet<T>(val innerSet: MutableCollection<T> = HashSet<T>()) : MutableCollection<T> by innerSet {
    var objectAdded = 0
    override fun add(element: T): Boolean {
        objectAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectAdded += elements.size
        return innerSet.addAll(elements)
    }
}