package com.my.kotlinstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.my.kotlinstudy._10协程.multiCoroutine
import com.my.kotlinstudy._10协程.singleCoroutine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener {
            singleCoroutine()
        }

        btn2.setOnClickListener {
            multiCoroutine()
        }
    }
}