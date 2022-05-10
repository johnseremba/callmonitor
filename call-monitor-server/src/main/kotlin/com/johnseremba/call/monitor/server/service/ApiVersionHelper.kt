package com.johnseremba.call.monitor.server.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

internal sealed class ApiVersionHelper {
    abstract fun startService(context: Context, intent: Intent?)
    abstract fun configureChannel(channelId: String?, channelName: String?, notificationManager: NotificationManager)

    object PreOreo : ApiVersionHelper() {
        override fun startService(context: Context, intent: Intent?) {
            context.startService(intent)
        }

        override fun configureChannel(channelId: String?, channelName: String?, notificationManager: NotificationManager) {
            // Not supported
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    object PostOreo : ApiVersionHelper() {
        override fun startService(context: Context, intent: Intent?) {
            context.startForegroundService(intent)
        }

        override fun configureChannel(channelId: String?, channelName: String?, notificationManager: NotificationManager) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        fun fromVersionCode(): ApiVersionHelper {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) PostOreo else PreOreo
        }
    }
}
