package com.johnseremba.call.monitor.server.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.johnseremba.call.monitor.server.data.CallEntry
import java.util.*

@Entity(tableName = "calls")
internal data class CallLog(
    @ColumnInfo(name = "call_date") val callDate: Date?,
    @ColumnInfo(name = "duration") val duration: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String?,
    @ColumnInfo(name = "caller_name") val callerName: String?,
    @ColumnInfo(name = "times_queried") val timesQueried: Int,
    @ColumnInfo(name = "isIncoming") val isIncoming: Boolean,
    @PrimaryKey val uid: String = UUID.randomUUID().mostSignificantBits.toString(),
)

internal fun CallEntry.toCallLog(): CallLog {
    return CallLog(
        callDate = beginning,
        duration = duration ?: "0",
        phoneNumber = number,
        callerName = name,
        timesQueried = timesQueried,
        isIncoming = isIncoming
    )
}

internal fun CallLog.toCallEntry(): CallEntry {
    return CallEntry(
        beginning = callDate,
        duration = duration,
        number = phoneNumber,
        name = callerName,
        timesQueried = timesQueried,
        isIncoming = isIncoming
    )
}
