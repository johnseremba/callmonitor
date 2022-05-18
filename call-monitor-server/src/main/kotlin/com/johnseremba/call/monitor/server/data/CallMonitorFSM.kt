package com.johnseremba.call.monitor.server.data

import android.util.Log
import java.util.*

private const val TAG = "CallMonitorFSM"

internal class CallMonitorFSM : CallMonitor {
    private var currentState: CallState = CallState.Idle

    private var callStartTime: Date? = null
    private var callEndTime: Date? = null
    private var isIncoming: Boolean = false
    private var callNumber: String? = ""
    private var callName: String? = ""
    private var queryCount: Int = 0

    override fun getCurrentState() = currentState

    override fun setCallRingingState(number: String, name: String, isIncoming: Boolean) {
        Log.v(TAG, "setCallRingingState")
        currentState = CallState.Ringing
        callStartTime = Date()
        callNumber = number
        callName = name
        queryCount = 0
    }

    override fun setCallAnsweredState(isIncoming: Boolean) {
        Log.v(TAG, "setCallAnsweredState")
        currentState = CallState.CallAnswered
        callStartTime = Date()
        this.isIncoming = isIncoming
    }

    override fun setCallEndedState() {
        Log.v(TAG, "setCallEndedState")
        currentState = CallState.CallEnded
        callEndTime = Date()
    }

    override fun setMissedCallState() {
        Log.v(TAG, "setCallEndedState")
        currentState = CallState.MissedCall
        callEndTime = callStartTime
    }

    override fun queryStatus() {
        Log.v(TAG, "queryStatus")
        if (currentState is CallState.Ringing || currentState is CallState.CallAnswered) {
            ++queryCount
        }
    }

    override fun getCallLog(): CallEntry {
        return CallEntry(
            beginning = callStartTime,
            duration = callStartTime?.secondsBetween(callEndTime) ?: "0",
            number = callNumber,
            name = callName,
            timesQueried = queryCount,
            isIncoming = isIncoming
        )
    }

    private fun Date.secondsBetween(date: Date?): String {
        if (date == null) return "0"
        val seconds = (this.time - date.time) / 1000
        return seconds.toString()
    }
}

internal interface CallMonitor {
    fun setCallRingingState(number: String, name: String, isIncoming: Boolean)

    fun setCallAnsweredState(isIncoming: Boolean)

    fun setCallEndedState()

    fun setMissedCallState()

    fun getCurrentState(): CallState

    fun queryStatus()

    fun getCallLog(): CallEntry
}
