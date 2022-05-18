package com.johnseremba.call.monitor.server.service

import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import com.johnseremba.call.monitor.server.service.ServiceCommunicationApi.Out.ServerConfiguration

private const val TAG = "ServiceClientManager"

internal object ServiceClientManager {
    private val clients = mutableListOf<Messenger>()

    fun registerClient(messenger: Messenger, ipAddress: String, port: String) {
        clients.add(messenger)
        sendMessage(messenger, ServerConfiguration(ServerConnectionObj(ipAddress, port)).toMessage())
    }

    fun unRegisterClient(messenger: Messenger) {
        clients.remove(messenger)
    }

    private fun sendMessage(client: Messenger, message: Message) {
        try {
            client.send(message)
        } catch (exception: RemoteException) {
            Log.e(TAG, "Failed to send message to client! Dropping client", exception)
            clients.remove(client)
        }
    }
}
