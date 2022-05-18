package com.johnseremba.call.monitor.server.data.repo

import android.util.Log
import com.johnseremba.call.monitor.server.data.CallEntry

private const val TAG = "LocalRepository"

internal class LocalRepository : Repository {

    override fun saveCallLog(entry: CallEntry): Boolean {
        Log.v(TAG, "saveCallLog: $entry")
        return true
    }

    override fun getCallLogs(): List<CallEntry> {
        Log.v(TAG, "getCallLogs")
        return emptyList()
    }
}
