package com.zetta.gochickfarm.ui.screen.animal.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.data.model.Animal
import com.zetta.gochickfarm.data.repository.AnimalRepository
import com.zetta.gochickfarm.utils.Constants
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class AnimalListViewModel(
    private val repository: AnimalRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var uiState by mutableStateOf(AnimalListUiState())
        private set

    private val speciesFilter = savedStateHandle.get<String>("species") ?: "Semua"

    private var currentPage = 1
    private val searchQueryFlow = MutableStateFlow(uiState.search)

    init {
        uiState = uiState.copy(species = if (speciesFilter == "{species}") "Semua" else speciesFilter)
        fetchAnimals()

        viewModelScope.launch {
            searchQueryFlow
                .drop(1)
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    refreshAnimals()
                }
        }
    }

    private fun fetchAnimals() {
        if (uiState.isLoading || uiState.isLoadingMore || uiState.endReached) return
        uiState = uiState.copy(isLoadingMore = true)
        viewModelScope.launch {
            if (currentPage == 1) {
                uiState = uiState.copy(isLoading = true)
            }

            repository.getAnimals(
                currentPage,
                Constants.PAGINATION_LIMIT,
                uiState.search,
                uiState.status,
                if (uiState.species != "Semua") uiState.species else null
            ).onSuccess {
                val updatedAnimals = if (currentPage == 1) it.data else uiState.animals + it.data
                currentPage = it.meta.currentPage

                uiState = uiState.copy(
                    animals = updatedAnimals,
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

            if (!uiState.endReached) currentPage++
        }
    }

    fun refreshAnimals() {
        currentPage = 1
        uiState = uiState.copy(
            animals = emptyList(),
            isRefreshing = true,
            endReached = false
        )
        fetchAnimals()
    }

    fun loadMoreAnimals() {
        fetchAnimals()
    }

    fun updateSearchQuery(query: String) {
        uiState = uiState.copy(search = query.ifEmpty { null })
        searchQueryFlow.value = uiState.search
    }

    fun updateStatusFilter(status: String) {
        uiState = uiState.copy(status = status)
        refreshAnimals()
    }

    fun updateSpeciesFilter(species: String) {
        uiState = uiState.copy(species = species)
        refreshAnimals()
    }

    data class AnimalListUiState(
        val isLoading: Boolean = false,
        val animals: List<Animal> = emptyList(),
        val search: String? = null,
        val status: String = "Hidup",
        val species: String = "Semua",
        val isLoadingMore: Boolean = false,
        val isRefreshing: Boolean = false,
        val endReached: Boolean = false,
        val errorMessage: String? = null
    )
}