package com.my.kotlinstudy._7集合

import com.my.kotlinstudy._2函数.joinToString
import com.my.kotlinstudy._3面向对象.Person

/**
 * kt中，List的默认实现是ArrayList
 */
fun main1() {
    val set = hashSetOf(1, 2, 3)
    println(set.javaClass)//java.util.HashSet
    println(set.max())//java中没有，kt中扩展的函数

    val list = arrayListOf(1, 2, 3)
    println(list.javaClass)//java.util.ArrayList
    println(list.last())
    println("contains: ${list.contains(1)}")
    println("in: ${1 in list}")
    list.toMutableSet()

    val list2 = listOf(1, 2, 3)
    println(list2.javaClass)//java.util.Arrays$ArrayList
    println(list.last())

    val list3 = List(3, {it * 2})
    val list4 = List(3) { it * 2 }
//    println("list3: $list3")//0 2 4

    /*to
    https://www.kotlincn.net/docs/reference/functions.html
    不是一个特殊结构，而是一个普通函数，调用方式是"中缀调用"
    中缀调用没有额外分隔符，函数名直接放在目标对象名称和参数之间，以下两种等价：
    1.to("one") 和 1 to "one"
    中缀可以与只有一个参数的函数（包括扩展）一起用。允许使用中缀调用的函数需要用infix修饰
    infix fun Any.too(other: Any) = Pair(this, other)
     */
    val map = hashMapOf(1 to "1", 2 to "2")
    println(map.javaClass)//java.util.HashMap
    //+-操作
    val map2 = map + Pair(3, "3")
    val map3 = map2 - Pair(4, "3")//不存在不会报错
    val map4 = map2 - listOf(1, 2)//移除所以以此为key的
    println("after + Pair: $map2")
    println("after - Pair: $map3")
    println("after - listOf: $map4")


    val listMayNull: List<String>? = null
    val a: String = listMayNull?.firstOrNull() ?: "kong"
    println(a)//kong

    //empty返回不可变的list
    val empty = emptyList<String>()

    //copy 拷贝的如果往原集合里加减东西，新集合不变
    val mutableEmpty = empty.toMutableList()
    val readOnlyEmpty = empty.toList()

    //fill，用1替换里面所有数据
    list.fill(1)//1 1 1
}

/**
 * 协变
 * 只读集合是协变的，可以在List<Father>的任何地方传进去List<Son>，即集合类型和元素类型有相同的子类型关系。map在value上是型变的，key上不是
 * 可变集合和java一样不是型变的
 */
fun main2() {
    val l1: List<Son> = ArrayList()
    val l2: MutableList<Son> = ArrayList()
    f1(l1)
//    f2(l2)//error
}

fun f1(list: List<Father>) {}
fun f2(list: MutableList<Father>) {}

open class Father
class Son(val name: String) : Father() {

}

/**
 * list相等：相同位置有相同大小和相同结构的元素（依赖元素的equals方法），==是true，===是false
 * set相等：相同大小且每个元素在另一个set中存在相同元素，则==是true（同样===是false），即和list比set的相等无关顺序了
 * map相等同set
 */
fun main3() {

    val s1 = Son("1")
    val s2 = Son("2")
    val s3 = Son("3")

    val l1 = ArrayList<Son>()
    val l2 = ArrayList<Son>()

    l1.add(s1)
    l1.add(s2)
    l1.add(s3)

    l2.add(s1)
    l2.add(s2)
    l2.add(s3)

    println(l1 == l2)

    val set1 = HashSet<Son>()
    val set2 = HashSet<Son>()

    set1.add(s1)
    set1.add(s2)
    set1.add(s3)

    set2.add(s2)
    set2.add(s1)
    set2.add(s3)

    println(set1 === set2)
}

fun main() {
    val numbers = listOf("one", "two", "three", "four")
    //过滤
    println("filter: " + numbers.filter { it.length > 3 })//[three, four]，不会改变原始问题
    println("filterNot: " + numbers.filterNot { it.length > 3 })
    println("filterIndexed: " + numbers.filterIndexed { index, s -> index % 2 == 0 })
    numbers.filterIsInstance<String>().forEach {
        println("filterIsInstance: " + it.toUpperCase())
    }

    //划分
    val (match, rest) = numbers.partition { it.length > 3 }
    println("match: $match")
    println("rest: $rest")

    /* 检验
    any() 和 none() 也可以不带谓词使用：在这种情况下它们只是用来检查集合是否为空。 如果集合中有元素，any() 返回 true，否则返回 false；none() 则相反。
     */
    //如果至少有一个元素匹配给定谓词，有一个匹配就true
    println("any: " + numbers.any { it.endsWith("e") })//true
    //如果没有元素与给定谓词匹配，有一个匹配就false
    println("none: " + numbers.none { it.endsWith("e") })//true
    //如果所有元素都匹配给定谓词，所有匹配就true
    println("any: " + numbers.all { it.length > 1 })//true

    println(numbers.map { it.substring(0, 2) })//[on, tw, th, fo]
    println(numbers.mapIndexed { index, s -> index.toString() + s })//[0one, 1two, 2three, 3four]
    //map的其它应用：只取列表里元素的某些字段或者扩展简单元素为更复杂对象，不要把map单纯理解为变换，还可能是简化等其它意思
    val versions = listOf(Version(1, 2), Version(2, 4))
    val majors = versions.map { it.major }
    println("majors: $majors")//[1,2]

    //associateWith返回map，key是it，value是传进来的函数的结果
    println("associateWith: ${numbers.associateWith { it.length }}")

    println(numbers.joinToString(sep = "|", pre = "<", suf = ">"))//<one|two|three|four>
    println(numbers.joinToString(separator = "|", prefix = "<", postfix = ">", limit = 2,
        transform = {
            "$it shit"
        })) //<one shit|two shit|...>

    val listString = StringBuffer("shit: ")
    numbers.joinTo(listString)
    println(listString)//shit: one, two, three, four

    val numbers2 = listOf("five", "six", "seven")
    println(numbers.zip(numbers2))//[(one, five), (two, six), (three, seven)]

    val numberSets = listOf(setOf(1, 2, 3), setOf(4, 5, 6), setOf(1, 2))
    println(numberSets.joinToString(sep = "|", pre = "<", suf = ">"))//<[1, 2, 3]|[4, 5, 6]|[1, 2]>
    println(numberSets.flatten())//[1, 2, 3, 4, 5, 6, 1, 2]
    println(numberSets.flatMap {
        it
    })
}

/**
 * 分组
 */
fun main5() {
    val numbers = listOf("one", "two", "three", "four", "five")
    val grouped = numbers.groupBy {
        it.length
    }
    println(grouped.javaClass)//LinkedHashMap
    println(grouped[3])
}

/**
 * 取一部分
 */
fun main6() {
    //slice返回具有给定索引的集合元素列表。 索引既可以是作为区间传入的也可以是作为整数值的集合传入的。
    val numbers = listOf("one", "two", "three", "four", "five", "six")
    println(numbers.slice(1..3))//[two, three, four]
    println(numbers.slice(1 until 3))//[two, three]
    println(numbers.slice(0..4 step 2))//[one, three, five]
    println(numbers.slice(setOf(3, 5, 0)))//[four, six, one]

    //take从头开始获取指定数量的元素, takeLast从尾开始获取指定数量的元素。当调用的数字大于集合的大小时，两个函数都将返回整个集合。
    //drop：要从头或从尾去除给定数量的元素，请调用 drop() 或 dropLast() 函数。
    println("take/drop")
    println(numbers.take(3))//[one, two, three]
    println(numbers.takeLast(3))//[four, five, six]
    println(numbers.drop(2))//[three, four, five, six]
    println(numbers.dropLast(2))//[one, two, three, four]

    //chunked将集合分解为给定大小的“块”
    println("chunked")
    println(numbers.chunked(size = 4))//[[one, two, three, four], [five, six]]
    println(numbers.chunked(size = 4)[1])//[five, six]
    println(numbers.chunked(size = 5, transform = {
        it.size
    }))//[5, 1]

    //windowed
    println("windowed")
    println(numbers.windowed(size = 3))//[[one, two, three], [two, three, four], [three, four, five], [four, five, six]]
}

/**
 * 取一个元素
 * list、set
 */
fun main7() {
    val numbers = listOf("one", "two", "three", "four", "five")
    println("elementAtOrNull: " + numbers.elementAtOrNull(9))//index超出返回null
    println("elementAt: " + numbers.elementAt(1))//index超出会崩溃
    //index超出返回后面的
    println("elementAtOrElse: " + numbers.elementAtOrElse(10) { index -> "$index shit" })
    println("elementAtOrElse: " + numbers.elementAtOrElse(10) { "shit" })

    //按条件取
    println("\n按条件取")
    //first
    println("first: " + numbers.first())
    println("first with lambda: " + numbers.first { it.length > 3 })//three，找不到会抛异常
    println("firstOrNull with lambda: " + numbers.firstOrNull { it.length > 30 })//找不到返回null
    println("find with lambda: " + numbers.find { it.length > 30 })//firstOrNull的别名形式
    //last
    println("last: " + numbers.last())
    println("last with lambda: " + numbers.last { it.length > 4 })//three
    println("lastOrNull with lambda: " + numbers.lastOrNull { it.length > 40 })//找不到返回null
    println("findLast with lambda: " + numbers.findLast { it.length > 40 })//lastOrNull的别名形式

    //随机
    println("\nrandom")
    println("random: " + numbers.random())

    //存在与否
    println("\n存在与否")
    println(numbers.contains("one"))//==
    println("one" in numbers)

    //empty
    println("\nempty")
    println(numbers.isEmpty())
    println(numbers.isNotEmpty())
    println(numbers.isEmpty().not())
}

/**
 * 排序
 */
fun main8() {
    val v1 = Version(1, 2)
    val v2 = Version(1, 3)
    println(v1 > v2)

    val numbers = mutableListOf("one", "two", "three", "four", "five")

    val lengthComparator = Comparator { s1: String, s2: String -> s1.length - s2.length }
    numbers.sortedWith(lengthComparator)
    //or
    numbers.sortedWith { s1: String, s2: String -> s1.length - s2.length }
    //标准库中的 compareBy() 函数
    println("compareBy: " + numbers.sortedWith(compareBy { it.length }))//[one, two, four, five, three]

    println("sorted: " + numbers.sorted())//[five, four, one, three, two]

    println("sortBy: " + numbers.sortedBy { it.length })//[one, two, four, five, three]
    println("sortedByDescending: " + numbers.sortedByDescending { it.length })//[three, four, five, one, two]

    //倒序
    val reversed = numbers.reversed()
    val asReversed  = numbers.asReversed();
    println("reversed: $reversed")//带有元素副本的新集合:[five, four, three, two, one]
    println("asReversed: $asReversed")//[five, four, three, two, one]
    numbers.add("six")
    println("reversed: $reversed")//[five, four, three, two, one]
    println("asReversed: $asReversed")//原列表变了，asReversed的也变[six, five, four, three, two, one]

    //乱序
    println(numbers.shuffled())
}

class Version(val major: Int, val minor: Int): Comparable<Version> {
    override fun compareTo(other: Version): Int {
        if (this.major != other.major) {
            return this.major - other.major
        } else if (this.minor != other.minor) {
            return this.minor - other.minor
        } else return 0
    }

}

fun main9() {
    val numbers = listOf(6, 42, 10, 4)

    println("Count: ${numbers.count()}")
    println("Max: ${numbers.maxOrNull()}")
    println("Min: ${numbers.minOrNull()}")
    println("Average: ${numbers.average()}")
    println("Sum: ${numbers.sum()}")
    println("minby: " + numbers.minByOrNull { it / 2 })

    val strings = listOf("one", "two", "three", "four")
    println("maxWithOrNull: " + strings.maxWithOrNull(compareBy { it.length }))
    println("minWithOrNull: " + strings.minWithOrNull(compareBy { it.length }))

    //对所有元素调用此函数的返回值的总和
    println("sumby: " + numbers.sumBy { it * 2 })

    //fold/reduce
    println("fold: " + numbers.fold(3, {sum: Int, ele: Int -> sum + ele * 2 }))
    println("reduce: " + numbers.reduce { sum, ele -> sum + ele })
}

/**
 * list操作
 */
fun main10() {
    val numbers = listOf(1, 2, 3, 4)
    println(numbers.get(1))//index超出会崩
    println(numbers[1])//index超出会崩
    println(numbers.getOrNull(10))
    println(numbers.getOrElse(10, defaultValue = { index -> index }))

    println("subList: " + numbers.subList(0, 1))

    println("indexOf: " + numbers.indexOf(2))
    println("lastIndexOf: " + numbers.lastIndexOf(2))
    println("indexOfFirst: " + numbers.indexOfFirst { it > 2 })
    println("indexOfLast: " + numbers.indexOfLast { it > 3 })

    println("binarySearch: " + numbers.binarySearch(30))//-5

    val persons = listOf(Person("11"), Person("22"), Person("33"))
    persons.binarySearch(Person("11"), compareBy<Person> { it.name.length }.thenBy { it.name.first() })
}

fun main11() {
    val numbersMap = mutableMapOf("one" to 1, "two" to 2)
    numbersMap["three"] = 3
    println(numbersMap)
}