package com.johnseremba.call.monitor.server.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

internal class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootCompletedReceiver", "starting service HttpService...")
            CallMonitorHttpService.startService(context)
        }
    }
}
