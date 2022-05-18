package com.johnseremba.call.monitor.server.di

import com.johnseremba.call.monitor.server.domain.GetIpAddressUseCase
import com.johnseremba.call.monitor.server.service.HttpServiceWorker
import org.koin.dsl.module

internal val coreModule = module {
    single { HttpServiceWorker(get()) }
    factory { GetIpAddressUseCase() }
}
