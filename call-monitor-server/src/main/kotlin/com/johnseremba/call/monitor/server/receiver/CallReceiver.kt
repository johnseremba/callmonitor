package com.johnseremba.call.monitor.server.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.johnseremba.call.monitor.server.data.CallLogManager
import org.koin.java.KoinJavaComponent.inject

internal class CallReceiver : BroadcastReceiver() {
    private var phoneNumber: String? = null
    private val callLogManager: CallLogManager by inject(CallLogManager::class.java)

    override fun onReceive(context: Context?, intent: Intent) {
        Log.v("CallReceiver: ", "onReceive called")

        val state: Int
        if (intent.action == "android.intent.action.NEW_OUTGOING_CALL") {
            phoneNumber = intent.extras?.getString("android.intent.extra.PHONE_NUMBER")
            state = TelephonyManager.CALL_STATE_RINGING
        } else {
            phoneNumber = intent.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)

            state = when (intent.extras?.getString(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_IDLE -> TelephonyManager.CALL_STATE_IDLE
                TelephonyManager.EXTRA_STATE_OFFHOOK -> TelephonyManager.CALL_STATE_OFFHOOK
                TelephonyManager.EXTRA_STATE_RINGING -> TelephonyManager.CALL_STATE_RINGING
                else -> 0
            }
        }
        callLogManager.onCallStateChanged(state, phoneNumber)
    }
}
