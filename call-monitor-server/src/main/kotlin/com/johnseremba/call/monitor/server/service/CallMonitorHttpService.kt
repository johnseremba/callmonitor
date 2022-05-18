package com.johnseremba.call.monitor.server.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.core.app.NotificationCompat
import com.johnseremba.call.monitor.server.R
import com.johnseremba.call.monitor.server.di.coreModule
import com.johnseremba.call.monitor.server.routes.callMonitorRoutes
import com.johnseremba.call.monitor.server.service.HttpServiceWorker.Companion.PORT
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import org.koin.dsl.koinApplication
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.Koin

internal class CallMonitorHttpService : Service() {
    private var messenger: Messenger? = null
    private var serviceHandler: ServiceHandler? = null

    val serviceWorker: HttpServiceWorker by inject(HttpServiceWorker::class.java)

    override fun onCreate() {
        serviceHandler = ServiceHandler(Looper.getMainLooper())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initializeHttpService()
        startForeground()
        isRunning.set(true)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        messenger = Messenger(serviceHandler)
        if (!isRunning.get()) {
            initializeHttpService()
            isRunning.set(true)
        }
        return messenger?.binder
    }

    override fun onDestroy() {
        koinApplication().close()
        super.onDestroy()
    }

    private fun initializeHttpService() {
        Thread {
            InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
            embeddedServer(Netty, PORT) {
                install(ContentNegotiation) { gson {} }
                install(Koin) { modules(coreModule) }
                install(Routing) { callMonitorRoutes() }
            }.start(wait = true)
        }.start()
    }

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

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                ServiceCommunicationApi.IN_REGISTER_CLIENT_ID -> ServiceClientManager.registerClient(
                    msg.replyTo,
                    serviceWorker.getIpV4Address(),
                    PORT.toString()
                )
                ServiceCommunicationApi.IN_UN_REGISTER_CLIENT_ID -> ServiceClientManager.unRegisterClient(msg.replyTo)
                else -> super.handleMessage(msg)
            }
        }
    }

    companion object {
        private const val TAG = "CallMonitorService"
        private const val NOTIFICATION_CHANNEL_ID = "call_monitor_http_service_channel_id"
        private const val NOTIFICATION_ID = 1

        private val isRunning = AtomicBoolean(false)

        @JvmStatic
        internal fun startService(context: Context) {
            if (!isRunning.get())
                try {
                    val intent = getServiceIntent(context)
                    ApiVersionHelper.fromVersionCode().startService(context, intent)
                    isRunning.set(true)
                } catch (exception: Exception) {
                    Log.e(TAG, "Unable to start service", exception)
                }
        }
    }
}

fun getServiceIntent(context: Context): Intent = Intent(context, CallMonitorHttpService::class.java)
