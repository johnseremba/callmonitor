package com.johnseremba.call.monitor.server.service

import android.os.Bundle
import android.os.Message
import android.os.Messenger
import com.johnseremba.call.monitor.server.service.ServiceCommunicationApi.In

inline fun <reified T : ServiceCommunicationApi> Message.toServiceMessage(): T {
    return when (this.what) {
        ServiceCommunicationApi.IN_REGISTER_CLIENT_ID -> In.RegisterClient(this.replyTo) as T
        ServiceCommunicationApi.IN_UN_REGISTER_CLIENT_ID -> In.UnRegisterClient(this.replyTo) as T
        ServiceCommunicationApi.OUT_SERVER_CONFIGURATION_ID -> ServiceCommunicationApi.Out.ServerConfiguration.getInstanceFromBundle(this.data) as T
        else -> throw IllegalArgumentException("Message not supported by the service")
    }
}

sealed class ServiceCommunicationApi(private val identifier: Int) {
    open fun toMessage(): Message {
        return Message.obtain(null, identifier)
    }

    sealed class In(id: Int) : ServiceCommunicationApi(id) {
        protected fun toMessage(messenger: Messenger): Message {
            val message = super.toMessage()
            message.replyTo = messenger
            return message
        }

        data class RegisterClient(val replyTo: Messenger) : In(IN_REGISTER_CLIENT_ID) {
            override fun toMessage(): Message = toMessage(replyTo)
        }

        data class UnRegisterClient(val replyTo: Messenger) : In(IN_UN_REGISTER_CLIENT_ID) {
            override fun toMessage(): Message = toMessage(replyTo)
        }
    }

    sealed class Out(id: Int) : ServiceCommunicationApi(id) {
        protected fun toMessage(bundle: Bundle): Message {
            val message = super.toMessage()
            message.data = bundle
            return message
        }

        data class ServerConfiguration(val connectionObj: ServerConnectionObj) : Out(OUT_SERVER_CONFIGURATION_ID) {
            override fun toMessage(): Message = toMessage(Bundle().apply {
                putString(IP_ADDRESS, connectionObj.ipAddress)
                putString(PORT, connectionObj.port)
            })

            companion object {
                private const val IP_ADDRESS = "ip_address"
                private const val PORT = "port"

                fun getInstanceFromBundle(bundle: Bundle): ServerConfiguration {
                    return with(bundle) {
                        val ip = getString(IP_ADDRESS, "")
                        val port = getString(PORT, "")
                        ServerConfiguration(ServerConnectionObj(ip, port))
                    }
                }
            }
        }
    }

    companion object {
        const val IN_REGISTER_CLIENT_ID = 1
        const val IN_UN_REGISTER_CLIENT_ID = 2
        const val OUT_SERVER_CONFIGURATION_ID = 3
    }
}

data class ServerConnectionObj(val ipAddress: String, val port: String)
