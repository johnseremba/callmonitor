package com.johnseremba.call.monitor.server.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CallLog::class], version = 1)
@TypeConverters(DateConverter::class)
internal abstract class CallLogDatabase : RoomDatabase() {
    abstract fun callLogDao(): CallLogDao
}
