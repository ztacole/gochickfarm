package com.zetta.gochickfarm.di

import com.zetta.gochickfarm.ui.screen.animal.list.AnimalListViewModel
import com.zetta.gochickfarm.ui.screen.auth.AuthViewModel
import com.zetta.gochickfarm.ui.screen.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }

    viewModel { DashboardViewModel(get()) }

    viewModel { AnimalListViewModel(get()) }
}