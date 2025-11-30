package com.zetta.gochickfarm.di

import com.zetta.gochickfarm.data.remote.AnimalService
import com.zetta.gochickfarm.data.remote.AuthService
import com.zetta.gochickfarm.data.remote.DashboardService
import com.zetta.gochickfarm.network.ApiClient
import com.zetta.gochickfarm.network.SessionManager
import kotlinx.coroutines.flow.firstOrNull
import org.koin.dsl.module

val networkModule = module {
    single { SessionManager(get()) }
    single { ApiClient(get()).client }

    single { DashboardService(get()) }
    single { AuthService(get()) }
    single { AnimalService(get()) }
}