package com.zetta.gochickfarm.di

import com.zetta.gochickfarm.network.ApiClient
import com.zetta.gochickfarm.network.TokenDataStore
import kotlinx.coroutines.flow.firstOrNull
import org.koin.dsl.module

val networkModule = module {
    single {
        ApiClient(
            tokenProvider = { get<TokenDataStore>().tokenFlow.firstOrNull() }
        ).client
    }
}