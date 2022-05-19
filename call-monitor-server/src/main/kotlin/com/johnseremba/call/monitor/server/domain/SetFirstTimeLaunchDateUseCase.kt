package com.johnseremba.call.monitor.server.domain

import com.johnseremba.call.monitor.server.data.preferences.CallMonitorServicePreferenceStorage.Companion.DATE_OF_FIRST_LAUNCH
import com.johnseremba.call.monitor.server.data.preferences.CallMonitorServicePreferenceStorage.Companion.IS_FIRST_TIME_LAUNCH
import com.johnseremba.call.monitor.server.data.preferences.PreferenceStorage
import java.util.*

internal class SetFirstTimeLaunchDateUseCase(
    private val preferenceStorage: PreferenceStorage
) {
    operator fun invoke() {
        val isFirstLaunch = !preferenceStorage.getBoolean(IS_FIRST_TIME_LAUNCH)
        if (isFirstLaunch) {
            preferenceStorage.put(IS_FIRST_TIME_LAUNCH, true)
            preferenceStorage.put(DATE_OF_FIRST_LAUNCH, Date().time)
        }
    }
}
