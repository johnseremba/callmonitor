package com.johnseremba.call.monitor.server.data.repo

import android.util.Log
import com.johnseremba.call.monitor.server.data.CallEntry
import com.johnseremba.call.monitor.server.data.local.CallLogDatabase
import com.johnseremba.call.monitor.server.data.local.toCallEntry
import com.johnseremba.call.monitor.server.data.local.toCallLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

private const val TAG = "LocalRepository"

internal class LocalRepository(
    private val database: CallLogDatabase,
    dispatcher: CoroutineDispatcher
) : Repository {

    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcher)

    override suspend fun saveCallLog(entry: CallEntry): Boolean {
        Log.v(TAG, "saveCallLog: $entry")
        return coroutineScope.async {
            if (entry.beginning == null || entry.number == null) {
                Log.e(TAG, "Invalid log entry! missing phone number or date")
                return@async false
            }
            database.callLogDao().insertCallLog(entry.toCallLog())
            return@async true
        }.await()
    }

    override suspend fun getCallLogs(): List<CallEntry> {
        Log.v(TAG, "getCallLogs")
        return coroutineScope.async {
            return@async database.callLogDao().getAllLogs().map { it.toCallEntry() }
        }.await()
    }
}
