package com.my.kotlinstudy

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        btn1.setOnClickListener {
            viewModel.run().observe(this, {
                btn1.text = it.toString()
            })
        }

        btn2.setOnClickListener {
        }
    }
}