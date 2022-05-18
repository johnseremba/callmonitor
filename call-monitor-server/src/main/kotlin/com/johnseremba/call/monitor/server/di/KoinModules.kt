package com.johnseremba.call.monitor.server.di

import com.johnseremba.call.monitor.server.data.CallLogManager
import com.johnseremba.call.monitor.server.data.CallMonitor
import com.johnseremba.call.monitor.server.data.CallMonitorFSM
import com.johnseremba.call.monitor.server.data.repo.LocalRepository
import com.johnseremba.call.monitor.server.data.repo.Repository
import com.johnseremba.call.monitor.server.domain.GetIpAddressUseCase
import com.johnseremba.call.monitor.server.domain.GetOngoingCallUseCase
import com.johnseremba.call.monitor.server.service.HttpServiceWorker
import org.koin.dsl.module

internal val coreModule = module {
    single { HttpServiceWorker(getIpAddressUseCase = get()) }
    single { CallLogManager(context = get(), callMonitorFSM = get(), repository = get()) }
    single { CallMonitorFSM() as CallMonitor }
    single { LocalRepository() as Repository }
    factory { GetIpAddressUseCase() }
    factory { GetOngoingCallUseCase() }
}
