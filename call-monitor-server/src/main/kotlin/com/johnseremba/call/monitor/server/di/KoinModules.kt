package com.johnseremba.call.monitor.server.di

import android.content.Context
import androidx.room.Room
import com.johnseremba.call.monitor.server.data.CallLogManager
import com.johnseremba.call.monitor.server.data.CallMonitor
import com.johnseremba.call.monitor.server.data.CallMonitorFSM
import com.johnseremba.call.monitor.server.data.local.CallLogDatabase
import com.johnseremba.call.monitor.server.data.preferences.CallMonitorServicePreferenceStorage
import com.johnseremba.call.monitor.server.data.preferences.PreferenceStorage
import com.johnseremba.call.monitor.server.data.repo.LocalRepository
import com.johnseremba.call.monitor.server.data.repo.Repository
import com.johnseremba.call.monitor.server.domain.GetIpAddressUseCase
import com.johnseremba.call.monitor.server.domain.GetOngoingCallUseCase
import com.johnseremba.call.monitor.server.domain.SetFirstTimeLaunchDateUseCase
import com.johnseremba.call.monitor.server.routes.call_logs.GetCallLogsUseCase
import com.johnseremba.call.monitor.server.routes.root.GetCallMonitorStartDateUseCase
import com.johnseremba.call.monitor.server.routes.root.GetSupportedServicesUseCase
import com.johnseremba.call.monitor.server.routes.status.GetCallStatusUseCase
import com.johnseremba.call.monitor.server.service.HttpServiceWorker
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

internal val coreModule = module {
    single { HttpServiceWorker(getIpAddressUseCase = get(), setFirstTimeLaunchDateUseCase = get()) }
    single { CallLogManager(context = get(), callMonitorFSM = get(), repository = get(), dispatcher = get()) }
    single { CallMonitorFSM() as CallMonitor }
    single { LocalRepository(database = get(), dispatcher = get()) as Repository }
    factory { GetIpAddressUseCase() }
    factory { GetOngoingCallUseCase() }
    factory { GetSupportedServicesUseCase(getIpAddressUseCase = get(), getCallMonitorStartDateUseCase = get()) }
    factory { GetCallMonitorStartDateUseCase(preferenceStorage = get()) }
    factory { SetFirstTimeLaunchDateUseCase(preferenceStorage = get()) }
    factory { GetCallStatusUseCase(callMonitorFSM = get()) }
    factory { GetCallLogsUseCase(repository = get()) }
    single { Dispatchers.IO }
    single { get<Context>().getSharedPreferences("call_monitor_service_prefs", Context.MODE_PRIVATE) }
    single { CallMonitorServicePreferenceStorage(sharedPreferences = get()) as PreferenceStorage }
}

internal val databaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>().applicationContext,
            CallLogDatabase::class.java,
            "call_log_db"
        ).build()
    }

    single {
        val database = get<CallLogDatabase>()
        database.callLogDao()
    }
}
