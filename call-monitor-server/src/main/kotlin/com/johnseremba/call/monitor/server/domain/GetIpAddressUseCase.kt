package com.johnseremba.call.monitor.server.domain

import android.util.Log
import java.net.NetworkInterface
import java.util.*

private const val TAG = "GetIpAddressUseCase"

internal class GetIpAddressUseCase {
    operator fun invoke(): String {
        try {
            Collections.list(NetworkInterface.getNetworkInterfaces()).forEach { network ->
                Collections.list(network.inetAddresses).forEach { inetAddress ->
                    if (!inetAddress.isLoopbackAddress) {
                        val address = inetAddress.hostAddress.orEmpty()
                        if (address.indexOf(':') < 0) return address
                    }
                }
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to get Ip Address", exception)
        }
        return ""
    }
}
