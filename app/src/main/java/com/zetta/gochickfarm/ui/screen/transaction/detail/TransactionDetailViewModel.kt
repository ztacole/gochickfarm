package com.zetta.gochickfarm.ui.screen.transaction.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.data.model.TransactionDetail
import com.zetta.gochickfarm.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    private val repository: TransactionRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val transactionId: Int = savedStateHandle.get<Int>("transactionId") ?: 0

    var uiState by mutableStateOf(TransactionDetailUiState())
        private set

    init {
        fetchData()
    }

    private fun fetchData() {
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch {
            repository.getById(transactionId)
                .onSuccess {
                    uiState = uiState.copy(transaction = it, isLoading = false)
                }
                .onFailure {
                    uiState = uiState.copy(errorMessage = it.message, isLoading = false)
                }
        }
    }

    data class TransactionDetailUiState(
        val transaction: TransactionDetail? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )
}