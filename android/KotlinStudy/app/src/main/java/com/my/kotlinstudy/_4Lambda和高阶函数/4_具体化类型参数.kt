package com.my.kotlinstudy._4Lambda和高阶函数

fun main() {
    val node = TreeNode()
    //call不太优雅
    node.findParentOfType(TreeNode::class.java)
    //reified
    node.findParentOfType2<TreeNode>()
}

class TreeNode() {
    val parent: TreeNode = TreeNode()
}

/**
 * 用反射检查节点类型
 */
fun <T> TreeNode.findParentOfType(clazz: Class<T>): T? {
    var p = parent
    while (p != null && !clazz.isInstance(p)) {
        p = p.parent
    }
    return p as T?
}

/**
 * reified的写法：
 * 不用传参数进来检查，能从泛型拿到泛型的Class对象，就好像是一个普通class。java是传进来的
 * 普通函数不能用reified
 */
inline fun <reified T> TreeNode.findParentOfType2(): T? {
    var p = parent
    while (p != null && p !is T) {
        p = p.parent
    }
    return p as T?
}