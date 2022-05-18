package com.johnseremba.call.monitor.ui

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import androidx.appcompat.app.AppCompatActivity
import com.johnseremba.call.monitor.databinding.ActivityMainBinding
import com.johnseremba.call.monitor.di.AppKoinComponent
import com.johnseremba.call.monitor.server.service.ServiceCommunicationApi
import com.johnseremba.call.monitor.server.service.getServiceIntent
import com.johnseremba.call.monitor.server.service.toServiceMessage

class MainActivity : AppCompatActivity(), AppKoinComponent {
    private lateinit var binding: ActivityMainBinding
    private var service: Messenger? = null
    private val serviceMessenger: Messenger = Messenger(IncomingHandler())

    private inner class IncomingHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val message = msg.toServiceMessage<ServiceCommunicationApi.Out>()
            handleIncomingMessages(message)
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            service = Messenger(iBinder)
            service?.send(ServiceCommunicationApi.In.RegisterClient(serviceMessenger).toMessage())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            doUnbindService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        doBindService()
    }

    override fun onDestroy() {
        doUnbindService()
        super.onDestroy()
    }

    private fun doBindService() {
        val intent = getServiceIntent(this)
        this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun doUnbindService() {
        service?.send(ServiceCommunicationApi.In.UnRegisterClient(serviceMessenger).toMessage())
        if (service != null) {
            unbindService(serviceConnection)
            service = null
        }
    }

    private fun handleIncomingMessages(message: ServiceCommunicationApi.Out) {
        when (message) {
            is ServiceCommunicationApi.Out.ServerConfiguration -> {
                with(binding) {
                    tvIpAddress.text = message.connectionObj.ipAddress
                    tvPort.text = message.connectionObj.port
                }
            }
            else -> Unit
        }
    }
}
