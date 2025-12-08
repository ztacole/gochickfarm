package com.zetta.gochickfarm.ui.screen.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.data.model.Summary
import com.zetta.gochickfarm.data.repository.DashboardRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepository
) : ViewModel() {
    var summaryUiState by mutableStateOf(SummaryUiState())
        private set

    init {
        fetchSummary()
    }

    private fun fetchSummary() {
        viewModelScope.launch {
            summaryUiState = summaryUiState.copy(isLoading = true)
            repository.getSummary()
                .onSuccess { summaryUiState = summaryUiState.copy(summary = it, isLoading = false, isRefreshing = false) }
                .onFailure { summaryUiState = summaryUiState.copy(errorMessage = it.message, isLoading = false, isRefreshing = false) }
        }
    }

    fun refreshSummary() {
        summaryUiState.copy(isRefreshing = true)
        fetchSummary()
    }

    data class SummaryUiState(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val summary: Summary? = null,
        val errorMessage: String? = null
    )
}