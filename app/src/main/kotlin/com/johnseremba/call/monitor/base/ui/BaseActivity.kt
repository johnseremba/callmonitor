package com.johnseremba.call.monitor.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnseremba.call.monitor.di.AppKoinComponent

abstract class BaseActivity : AppCompatActivity(), AppKoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSafeCreate(savedInstanceState)
    }

    abstract fun onSafeCreate(savedInstanceState: Bundle?)
}
