package com.johnseremba.call.monitor.server.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface CallLogDao {
    @Query("SELECT * FROM calls ORDER BY call_date DESC")
    suspend fun getAllLogs(): List<CallLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLog(vararg callLog: CallLog)
}
