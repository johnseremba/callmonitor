package com.johnseremba.call.monitor.server.service

import android.util.Log
import com.johnseremba.call.monitor.server.domain.GetIpAddressUseCase
import com.johnseremba.call.monitor.server.domain.SetFirstTimeLaunchDateUseCase

private const val TAG = "HttpServiceWorker"

internal class HttpServiceWorker(
    private val getIpAddressUseCase: GetIpAddressUseCase,
    private val setFirstTimeLaunchDateUseCase: SetFirstTimeLaunchDateUseCase,
) {
    companion object {
        const val PORT = 8080
    }

    fun getIpV4Address(): String {
        return getIpAddressUseCase().also {
            Log.v(TAG, "Ip address: $it")
        }
    }

    fun checkFirstTimeLaunch() {
        setFirstTimeLaunchDateUseCase()
    }
}
