package com.johnseremba.call.monitor.server.data.preferences

import android.content.SharedPreferences

internal class CallMonitorServicePreferenceStorage(private val sharedPreferences: SharedPreferences) : PreferenceStorage {
    companion object {
        const val DATE_OF_FIRST_LAUNCH = "date_of_first_launch"
        const val IS_FIRST_TIME_LAUNCH = "is_first_time_launch"
    }

    override fun getLong(key: String): Long {
        return sharedPreferences.getLong(key, 0L)
    }

    override fun put(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun put(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }
}

internal interface PreferenceStorage {
    fun getLong(key: String): Long
    fun getBoolean(key: String): Boolean
    fun put(key: String, value: Long)
    fun put(key: String, value: Boolean)
}
