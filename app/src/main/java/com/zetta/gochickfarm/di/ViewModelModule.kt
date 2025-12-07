package com.zetta.gochickfarm.di

import androidx.lifecycle.SavedStateHandle
import com.zetta.gochickfarm.ui.screen.animal.breeding.AddBreedingLogViewModel
import com.zetta.gochickfarm.ui.screen.animal.detail.AnimalDetailViewModel
import com.zetta.gochickfarm.ui.screen.animal.feeding.AddFeedingLogViewModel
import com.zetta.gochickfarm.ui.screen.animal.list.AnimalListViewModel
import com.zetta.gochickfarm.ui.screen.auth.AuthViewModel
import com.zetta.gochickfarm.ui.screen.dashboard.DashboardViewModel
import com.zetta.gochickfarm.ui.screen.feed.list.FeedListViewModel
import com.zetta.gochickfarm.ui.screen.transaction.list.TransactionListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }

    viewModel { DashboardViewModel(get()) }

    viewModel { (handle: SavedStateHandle) ->
        AnimalListViewModel(get(), handle)
    }
    viewModel { (handle: SavedStateHandle) ->
        AnimalDetailViewModel(get(), handle)
    }

    viewModel { (handle: SavedStateHandle) ->
        AddFeedingLogViewModel(get(), get(), handle)
    }

    viewModel { AddBreedingLogViewModel(get()) }

    viewModel { FeedListViewModel(get()) }

    viewModel { TransactionListViewModel(get()) }
}