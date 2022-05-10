package com.johnseremba.call.monitor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnseremba.call.monitor.R
import com.johnseremba.call.monitor.di.AppKoinComponent

class MainActivity : AppCompatActivity(), AppKoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
