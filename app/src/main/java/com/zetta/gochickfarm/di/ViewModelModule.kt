package com.zetta.gochickfarm.di

import com.zetta.gochickfarm.ui.screen.auth.AuthViewModel
import com.zetta.gochickfarm.ui.screen.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashboardViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
}