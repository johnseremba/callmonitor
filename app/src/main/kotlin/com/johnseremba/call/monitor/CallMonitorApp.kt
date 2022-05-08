package com.johnseremba.call.monitor

import android.app.Application
import com.johnseremba.call.monitor.di.AppKoinContext

class CallMonitorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppKoinContext.init(this@CallMonitorApp)
    }
}
