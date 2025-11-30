package com.zetta.gochickfarm.di

import com.zetta.gochickfarm.data.repository.AnimalRepository
import com.zetta.gochickfarm.data.repository.AuthRepository
import com.zetta.gochickfarm.data.repository.DashboardRepository
import com.zetta.gochickfarm.data.repository.FeedRepository
import com.zetta.gochickfarm.utils.ResourceProvider
import org.koin.dsl.module

val appModule = module {
    single { ResourceProvider(get()) }

    single { DashboardRepository(get()) }
    single { AuthRepository(get(), get()) }
    single { AnimalRepository(get()) }
    single { FeedRepository(get()) }
}