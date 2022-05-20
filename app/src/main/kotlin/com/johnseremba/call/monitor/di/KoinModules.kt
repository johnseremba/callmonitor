package com.johnseremba.call.monitor.di

import com.johnseremba.call.monitor.server.api.CallMonitorApi
import com.johnseremba.call.monitor.ui.domain.FetchLogsUseCase
import com.johnseremba.call.monitor.ui.domain.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    single { CallMonitorApi() }
    viewModel { MainActivityViewModel(fetchLogsUseCase = get()) }
    factory { FetchLogsUseCase(api = get()) }
}
