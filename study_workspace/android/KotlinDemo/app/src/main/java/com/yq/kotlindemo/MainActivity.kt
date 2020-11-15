package com.yq.kotlindemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private val items = listOf<String>("1", "2", "3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.rv)
        val rv2: RecyclerView = findViewById(R.id.rv)
        val rv_anko: RecyclerView = find(R.id.rv)

        rv.layoutManager = LinearLayoutManager(this)//不用new
        rv.adapter = ForecastListAdapter(items)

        doAsync {
            ForecastRequest("https://www.baidu.com/").run()
            //uiThread如果act被销毁，是不回调这个的
            uiThread {
                longToast("request done")
            }
        }
    }

    fun test() {
        toast1("shit")
        toast1(msg = "shit")

        showToast("extend toast")
        toast("anko extend toast")
        longToast("anko long extend toast")

        val person = Person()
        person.name = "name1"
        textview.text = person.name

        val f1 = Forecast(Date(), 1f, "shit")
//        f1.temperature = 1f
        val f2 = f1.copy(temperature = 30f)

        val (date, temperature, details) = f1
        //相当于：
        val date1 = f1.component1()
        val temperature1 = f1.component2()
        val details1 = f1.component3()
        //上述功能可扩展为map遍历：
        val map = HashMap<Int, Int>()
        for ((key, value) in map) {
            Log.d("map", "key:$key, value:$value")
        }
    }

    fun add(x: Int, y: Int) : Int {
        return x + y
    }
    /** 直接返回值 */
    fun add2(x: Int, y: Int) : Int = x + y

    /** 默认参数，实现不同参数重载 */
    fun toast1(msg: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, length).show()
    }

}

//class M2(a: String, b: String) : MainActivity {
//    fun run() {
//        print("shit")
//    }
//}