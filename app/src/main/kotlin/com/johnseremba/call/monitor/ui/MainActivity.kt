package com.johnseremba.call.monitor.ui

import android.os.Bundle
import com.johnseremba.call.monitor.R
import com.johnseremba.call.monitor.base.ui.BaseActivity

class MainActivity : BaseActivity() {

    override fun onSafeCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
    }
}
