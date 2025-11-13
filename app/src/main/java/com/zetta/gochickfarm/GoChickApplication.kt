package com.zetta.gochickfarm

import android.app.Application
import com.zetta.gochickfarm.di.appModule
import com.zetta.gochickfarm.di.networkModule
import com.zetta.gochickfarm.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GoChickApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GoChickApplication)
            modules(networkModule)
            modules(appModule)
            modules(viewModelModule)
        }
    }
}