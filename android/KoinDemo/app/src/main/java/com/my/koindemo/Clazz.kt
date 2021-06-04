package com.my.koindemo

interface Tool {
    fun use()
}

interface Flammable {
    fun burn()
}

class Stove : Tool, Flammable {
    override fun use() {
    }

    override fun burn() {
    }

}

class Chef(val stove: Stove) {

    fun getVal(): String {
        return "i am chef"
    }
}