package com.johnseremba.call.monitor.server.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.johnseremba.call.monitor.server.R
import com.johnseremba.call.monitor.server.di.coreModule
import com.johnseremba.call.monitor.server.routes.callMonitorRoutes
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

class CallMonitorHttpService : Service() {

    override fun onCreate() {
        Thread {
            InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
            embeddedServer(Netty, PORT) {
                install(ContentNegotiation) { gson {} }
                install(Koin) { modules(module { coreModule }) }
                install(Routing) { callMonitorRoutes() }
            }.start(wait = true)
        }.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning.set(true)
        startForeground()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForeground() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        ApiVersionHelper.fromVersionCode().configureChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.call_monitor_service_channel_name),
            notificationManager
        )
        startForeground(NOTIFICATION_ID, createNotification())
    }

    private fun createNotification(): Notification = NotificationCompat
        .Builder(this, NOTIFICATION_CHANNEL_ID)
        .setContentTitle(getString(R.string.call_monitor_service_name))
        .setContentText("")
        .setSmallIcon(R.drawable.ic_notification_icon)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    internal companion object {
        private const val TAG = "CallMonitorService"
        private const val NOTIFICATION_CHANNEL_ID = "call_monitor_http_service_channel_id"
        private const val NOTIFICATION_ID = 1

        private const val PORT = 8080
        private val isRunning = AtomicBoolean(false)

        @JvmStatic
        fun startService(context: Context) {
            if (!isRunning.get())
                try {
                    val intent = Intent(context, CallMonitorHttpService::class.java)
                    ApiVersionHelper.fromVersionCode().startService(context, intent)
                } catch (exception: Exception) {
                    Log.e(TAG, "Unable to start service", exception)
                }
        }
    }
}
