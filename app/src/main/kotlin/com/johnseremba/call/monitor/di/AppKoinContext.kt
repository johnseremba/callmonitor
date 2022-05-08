package com.johnseremba.call.monitor.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.dsl.koinApplication

object AppKoinContext {
    lateinit var koin: Koin
        private set

    @Synchronized
    fun init(context: Context): AppKoinContext {
        koin = koinApplication {
            androidContext(context)
            modules(coreModule)
        }.koin
        return this
    }
}

interface AppKoinComponent : KoinComponent {
    override fun getKoin(): Koin = AppKoinContext.koin
}
