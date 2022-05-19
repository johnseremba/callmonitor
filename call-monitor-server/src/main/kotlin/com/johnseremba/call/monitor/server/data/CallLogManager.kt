package com.johnseremba.call.monitor.server.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.RemoteException
import android.provider.ContactsContract.PhoneLookup
import android.telephony.TelephonyManager
import android.util.Log
import com.johnseremba.call.monitor.server.data.repo.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "CallLogManager"

internal class CallLogManager(
    private val context: Context,
    private val callMonitorFSM: CallMonitor,
    private val repository: Repository,
    dispatcher: CoroutineDispatcher
) {

    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcher)
    private var lastCallState = TelephonyManager.CALL_STATE_IDLE

    fun onCallStateChanged(state: Int, number: String?) {
        Log.v(TAG, "onCallStateChanged: $state : $number")
        if (lastCallState == state || number == null) return
        lastCallState = state

        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                callMonitorFSM.setCallRingingState(number, getContactName(number), true)
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                if (lastCallState != TelephonyManager.CALL_STATE_RINGING)
                    callMonitorFSM.setCallAnsweredState(false)
                else
                    callMonitorFSM.setCallAnsweredState(true)
            }
            TelephonyManager.CALL_STATE_IDLE -> {
                if (lastCallState != TelephonyManager.CALL_STATE_RINGING)
                    callMonitorFSM.setMissedCallState()
                else
                    callMonitorFSM.setCallEndedState()

                coroutineScope.launch {
                    repository.saveCallLog(callMonitorFSM.getCallLog())
                }
            }
        }
    }

    private fun getContactName(number: String): String {
        val contactUri: Uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        val projection = arrayOf(PhoneLookup.DISPLAY_NAME, PhoneLookup.NUMBER, PhoneLookup.HAS_PHONE_NUMBER)

        val cursor: Cursor
        try {
            cursor = context.contentResolver.query(
                contactUri,
                projection,
                null,
                null,
                null
            ) ?: return ""

            val name = if (cursor.moveToFirst()) {
                val column = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)
                cursor.getString(column)
            } else ""
            cursor.close()
            return name
        } catch (exception: SecurityException) {
            Log.e(TAG, "SecurityException, could be missing READ_CONTACTS permission", exception)
        } catch (exception: RemoteException) {
            Log.e(TAG, "Failed to get contact name", exception)
        }
        return ""
    }
}
