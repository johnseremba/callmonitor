package com.johnseremba.call.monitor.server.routes.root

import com.johnseremba.call.monitor.server.data.preferences.CallMonitorServicePreferenceStorage.Companion.DATE_OF_FIRST_LAUNCH
import com.johnseremba.call.monitor.server.data.preferences.PreferenceStorage
import java.util.*

internal class GetCallMonitorStartDateUseCase(
    private val preferenceStorage: PreferenceStorage
) {
    operator fun invoke(): Date? {
        val dateLong = preferenceStorage.getLong(DATE_OF_FIRST_LAUNCH)
        if (dateLong > 0L) return Date(dateLong)
        return null
    }
}