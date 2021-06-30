package com.my.kotlinstudy

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.my.kotlinstudy._8协程.excep_async
import com.my.kotlinstudy._8协程.excep_launch
import com.my.kotlinstudy._8协程.excep_launch_handle
import com.my.kotlinstudy.viewmodel.MainViewModel
import com.my.kotlinstudy.viewmodel.factory.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val anim = ObjectAnimator.ofFloat(view, "rotation", 0F, 360F)
        anim.repeatCount = ValueAnimator.INFINITE
        anim.interpolator = LinearInterpolator()
        anim.duration = 1000
        anim.start()

        btn1.setOnClickListener {
            viewModel.request().observe(this, {
                Log.d("yqtest", "in observe thread: ${Thread.currentThread().name}")//main
                btn1.text = it.toString()
            })
        }

        btn2.setOnClickListener {
//            viewModel.request2()
//            excep_launch()
            excep_launch_handle()
//            excep_async()
        }
    }
}