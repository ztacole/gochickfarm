package com.zetta.gochickfarm.ui.screen.transaction.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.data.model.SimpleAnimal
import com.zetta.gochickfarm.data.repository.AnimalRepository
import com.zetta.gochickfarm.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val repository: AnimalRepository
) : ViewModel() {

    var uiState by mutableStateOf(AddTransactionUiState())
        private set

    var inputState by mutableStateOf(AddTransactionInputState())
        private set

    init {
        fetchAnimalTags()
    }

    private fun fetchAnimalTags() {
        uiState = uiState.copy(loadingAnimalTags = true)

        viewModelScope.launch {
            repository.getAllWithoutPagination()
                .onSuccess {
                    uiState = uiState.copy(
                        animalTags = it,
                        loadingAnimalTags = false
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        loadingAnimalTags = false,
                        errorMessage = it.message
                    )
                }
        }
    }

    fun updateTransactionType(type: String) {
        inputState = inputState.copy(transactionType = type)
    }

    fun updateDescription(desc: String) {
        inputState = inputState.copy(description = desc)
    }

    fun updateTotal(total: String) {
        inputState = inputState.copy(total = total)
    }

    fun updateDate(date: String) {
        inputState = inputState.copy(date = date)
    }

    fun toggleAnimalTag(tag: String) {
        val current = inputState.selectedTags.toMutableList()

        val animal = uiState.animalTags.find { it.tag == tag } ?: return
        if (current.contains(animal)) {
            current.remove(animal)
        }

        current.add(animal)

        inputState = inputState.copy(selectedTags = current)
    }

    fun removeAnimalTag(tag: String) {
        val animal = uiState.animalTags.find { it.tag == tag } ?: return
        inputState = inputState.copy(
            selectedTags = inputState.selectedTags.filter { it.id != animal.id }
        )
    }

    fun submit() {
        uiState = uiState.copy(isProcessing = true)

        viewModelScope.launch {
            transactionRepository.createNew(
                type = inputState.transactionType,
                description = inputState.description,
                amount = inputState.total.toInt(),
                date = inputState.date,
                animalIds = if (inputState.transactionType == "Pengeluaran") null else inputState.selectedTags.map { it.id }
            ).onSuccess {
                uiState = uiState.copy(
                    isProcessing = false,
                    isSuccess = true
                )
            }.onFailure {
                uiState = uiState.copy(
                    isProcessing = false,
                    errorMessage = it.message
                )
                println("DEBUG: ${it.message}")
            }
        }
    }


    // ----------- UI MODEL -----------

    data class AddTransactionInputState(
        val transactionType: String = "",
        val selectedTags: List<SimpleAnimal> = emptyList(),
        val description: String = "",
        val total: String = "",
        val date: String = "",
    )

    data class AddTransactionUiState(
        val transactionTypeList: List<String> = listOf("Pengeluaran", "Pemasukan"),
        val animalTags: List<SimpleAnimal> = emptyList(),
        val loadingAnimalTags: Boolean = false,
        val isProcessing: Boolean = false,
        val isSuccess: Boolean = false,
        val errorMessage: String? = null
    )
}