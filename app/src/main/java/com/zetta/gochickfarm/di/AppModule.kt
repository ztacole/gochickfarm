package com.zetta.gochickfarm.di

import com.zetta.gochickfarm.network.TokenDataStore
import org.koin.dsl.module

val appModule = module {
    single { TokenDataStore(get()) }
}