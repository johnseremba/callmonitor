package com.johnseremba.call.monitor.server.data.local

import androidx.room.TypeConverter
import java.util.Date

internal class DateConverter {
    @TypeConverter
    fun toDate(date: Long): Date = Date(date)

    @TypeConverter
    fun fromDate(date: Date) = date.time
}
