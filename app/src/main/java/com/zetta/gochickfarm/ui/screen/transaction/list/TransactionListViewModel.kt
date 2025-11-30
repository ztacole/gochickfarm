package com.zetta.gochickfarm.ui.screen.transaction.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.data.model.Transaction
import com.zetta.gochickfarm.data.repository.TransactionRepository
import com.zetta.gochickfarm.utils.Constants
import kotlinx.coroutines.launch

class TransactionListViewModel(private val repository: TransactionRepository): ViewModel() {
    var uiState by mutableStateOf(TransactionListUiState())
        private set

    private var currentPage = 1

    init {
        fetchTransactions()
    }

    private fun fetchTransactions() {
        if (uiState.isLoading || uiState.isLoadingMore || uiState.endReached) return
        uiState = uiState.copy(isLoadingMore = true)

        viewModelScope.launch {
            if (currentPage == 1) {
                uiState = uiState.copy(isLoading = true)
            }

            repository.getTransactions(
                currentPage,
                Constants.PAGINATION_LIMIT,
                if (uiState.type == "Semua") null else uiState.type
            ).onSuccess {
                val updatedTransactions = if (currentPage == 1) it.data else uiState.transactions + it.data
                currentPage = it.meta.currentPage

                uiState = uiState.copy(
                    transactions = updatedTransactions,
                    isLoading = false,
                    isLoadingMore = false,
                    isRefreshing = false,
                    endReached = it.meta.currentPage == it.meta.totalPages
                )
            }.onFailure {
                uiState = uiState.copy(
                    errorMessage = it.message,
                    isLoading = false,
                    isLoadingMore = false,
                    isRefreshing = false
                )
            }
        }

        if (!uiState.endReached) currentPage++
    }

    fun refreshTransactions() {
        currentPage = 1
        uiState = uiState.copy(
            transactions = emptyList(),
            isRefreshing = true,
            endReached = false
        )
        fetchTransactions()
    }

    fun loadMoreTransactions() {
        fetchTransactions()
    }

    fun updateTypeFilter(type: String) {
        uiState = uiState.copy(type = type)
        refreshTransactions()
    }

    data class TransactionListUiState(
        val transactions: List<Transaction> = emptyList(),
        val type: String = "Semua",
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false,
        val isRefreshing: Boolean = false,
        val endReached: Boolean = false,
        val errorMessage: String? = null
    )
}