package com.johnseremba.call.monitor.server.service

import android.util.Log
import com.johnseremba.call.monitor.server.domain.GetIpAddressUseCase

private const val TAG = "HttpServiceWorker"

internal class HttpServiceWorker(
    private val getIpAddressUseCase: GetIpAddressUseCase
) {
    companion object {
        const val PORT = 8080
    }

    fun getIpV4Address(): String {
        return getIpAddressUseCase().also {
            Log.v(TAG, "Ip address: $it")
        }
    }
}
