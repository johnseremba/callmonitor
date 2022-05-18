package com.johnseremba.call.monitor.server.data

import java.util.*

internal data class CallEntry(
    val beginning: Date?,
    val duration: String?,
    val number: String?,
    val name: String?,
    val timesQueried: Int,
    val isIncoming: Boolean
)

internal sealed class CallState {
    object Idle : CallState()
    object Ringing : CallState()
    object CallAnswered : CallState()
    object CallEnded : CallState()
    object MissedCall : CallState()
}
