package com.zetta.gochickfarm

import android.app.Application
import com.zetta.gochickfarm.di.appModule
import com.zetta.gochickfarm.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GoChickFarmApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GoChickFarmApp)
            modules(appModule)
            modules(networkModule)
        }
    }
}