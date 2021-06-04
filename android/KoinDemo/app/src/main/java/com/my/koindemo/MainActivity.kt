package com.my.koindemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.currentScope
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

class MainActivity : AppCompatActivity() {

    private val chef: Chef = get()
    //如果module里面没有 bind Tool::class bind Flammable::class，这里编译会出错
    private val tool: Tool = get()
    private val flammable: Flammable = get()


//    private val stove: Stove by currentScope.inject()

    private val viewModel: MyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            Log.i(TAG, "chef $chef, hashcode: ${chef.hashCode()}")
            Log.i(TAG, "tool $tool, hashcode: ${tool.hashCode()}")
            Log.i(TAG, "flammable $flammable, hashcode: ${flammable.hashCode()}")
            tv.text = chef.getVal()

            //使用scope
            val scope = getKoin().getOrCreateScope("myscope", named("MY_SCOPE"))
            //在同一个 scope 中注入的实例是相同的, stove1 与 stove2 实际上是同一个实例
            val stove1: Stove = scope.get()
            val stove2: Stove = scope.get()
            Log.i(TAG, "stove1 $stove1, hashcode: ${stove1.hashCode()}")
            Log.i(TAG, "stove2 $stove2, hashcode: ${stove2.hashCode()}")
            //当 scope 被关闭时其缓存会被清空，自然下一次重新创建后会注入新的对象。
            scope.close()
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}