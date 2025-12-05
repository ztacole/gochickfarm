package com.zetta.gochickfarm.ui.screen.animal.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.data.model.Animal
import com.zetta.gochickfarm.data.model.BreedingLog
import com.zetta.gochickfarm.data.model.FeedingLog
import com.zetta.gochickfarm.data.repository.AnimalRepository
import com.zetta.gochickfarm.utils.Constants
import kotlinx.coroutines.launch

class AnimalDetailViewModel(
    private val repository: AnimalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val animalId = savedStateHandle.get<Int>("animalId") ?: 0

    var animalUiState by mutableStateOf(AnimalUiState())
        private set
    var feedingLogsUiState by mutableStateOf(FeedingLogsUiState())
        private set
    var breedingLogsUiState by mutableStateOf(BreedingLogsUiState())
        private set
    var statusChangeUiState by mutableStateOf(StatusChangeUiState())
        private set

    private var currentFeedingLogPage = 1
    private var currentBreedingLogPage = 1

    init {
        fetchAnimal()
        fetchSelectedTab()
    }

    private fun fetchSelectedTab() {
        if (animalUiState.selectedTab == 0) fetchFeedingLogs()
        else fetchBreedingLogs()
    }

    private fun fetchAnimal() {
        viewModelScope.launch {
            animalUiState = animalUiState.copy(isLoading = true)

            repository.getAnimalById(animalId)
                .onSuccess {
                    animalUiState = animalUiState.copy(animal = it, isLoading = false)
                }
                .onFailure {
                    animalUiState = animalUiState.copy(errorMessage = it.message, isLoading = false)
                }
        }
    }

    private fun fetchFeedingLogs() {
        if (feedingLogsUiState.isLoading || feedingLogsUiState.isLoadingMore || feedingLogsUiState.endReached) return
        feedingLogsUiState = feedingLogsUiState.copy(isLoadingMore = true)

        viewModelScope.launch {
            if (currentFeedingLogPage == 1)
                feedingLogsUiState = feedingLogsUiState.copy(isLoading = true)

            repository.getFeedingLogs(
                animalId,
                currentFeedingLogPage,
                Constants.PAGINATION_LIMIT
            ).onSuccess {
                val updatedFeedingLogs =
                    if (currentFeedingLogPage == 1) it.data else feedingLogsUiState.feedingLogs + it.data
                currentFeedingLogPage = it.meta.currentPage

                feedingLogsUiState = feedingLogsUiState.copy(
                    feedingLogs = updatedFeedingLogs,
                    isLoading = false,
                    isLoadingMore = false,
                    isRefreshing = false,
                    endReached = it.meta.currentPage == it.meta.totalPages
                )
            }.onFailure {
                feedingLogsUiState = feedingLogsUiState.copy(
                    errorMessage = it.message,
                    isLoading = false,
                    isLoadingMore = false,
                    isRefreshing = false
                )
            }
        }
    }

    private fun fetchBreedingLogs() {
        if (breedingLogsUiState.isLoading || breedingLogsUiState.isLoadingMore || breedingLogsUiState.endReached) return
        breedingLogsUiState = breedingLogsUiState.copy(isLoadingMore = true)

        viewModelScope.launch {
            if (currentBreedingLogPage == 1)
                breedingLogsUiState = breedingLogsUiState.copy(isLoading = true)

            repository.getBreedingLogs(
                animalId,
                currentBreedingLogPage,
                Constants.PAGINATION_LIMIT
            ).onSuccess {
                val updatedBreedingLogs =
                    if (currentBreedingLogPage == 1) it.data else breedingLogsUiState.breedingLogs + it.data
                currentBreedingLogPage = it.meta.currentPage

                breedingLogsUiState = breedingLogsUiState.copy(
                    breedingLogs = updatedBreedingLogs,
                    isLoading = false,
                    isLoadingMore = false,
                    isRefreshing = false,
                    endReached = it.meta.currentPage == it.meta.totalPages
                )
            }.onFailure {
                breedingLogsUiState = breedingLogsUiState.copy(
                    errorMessage = it.message,
                    isLoading = false,
                    isLoadingMore = false,
                    isRefreshing = false
                )
            }
        }
    }

    fun updateStatus(status: String) {
        statusChangeUiState = statusChangeUiState.copy(isLoading = true)

        viewModelScope.launch {
            repository.updateStatus(
                animalId,
                status
            ).onSuccess {
                statusChangeUiState = statusChangeUiState.copy(
                    isLoading = false,
                    isSuccess = true,
                    showDialog = true,
                    message = "Status updated successfully",
                    showBottomSheet = false
                )
                animalUiState = animalUiState.copy(animal = animalUiState.animal?.copy(status = status))
            }.onFailure {
                statusChangeUiState = statusChangeUiState.copy(
                    message = it.message,
                    isLoading = false,
                    isSuccess = false,
                    showDialog = true,
                    showBottomSheet = false
                )
            }
        }
    }

    fun showStatusChangeBottomSheet() {
        statusChangeUiState = statusChangeUiState.copy(showBottomSheet = true)
    }

    fun dismissStatusChangeBottomSheet() {
        statusChangeUiState = statusChangeUiState.copy(showBottomSheet = false)
    }

    fun dismissStatusChangeDialog() {
        statusChangeUiState = statusChangeUiState.copy(showDialog = false, status = "Hidup")
    }

    fun updateStatusInputState(status: String) {
        statusChangeUiState = statusChangeUiState.copy(status = status)
    }

    fun refreshFeedingLogs() {
        currentFeedingLogPage = 1
        feedingLogsUiState = feedingLogsUiState.copy(
            feedingLogs = emptyList(),
            isRefreshing = true,
            endReached = false
        )
        fetchFeedingLogs()
    }

    fun refreshBreedingLogs() {
        currentBreedingLogPage = 1
        breedingLogsUiState = breedingLogsUiState.copy(
            breedingLogs = emptyList(),
            isRefreshing = true,
            endReached = false
        )
        fetchBreedingLogs()
    }

    fun loadMoreFeedingLogs() = {
        if (!feedingLogsUiState.endReached) currentFeedingLogPage++
        fetchFeedingLogs()
    }

    fun loadMoreBreedingLogs() = {
        if (!breedingLogsUiState.endReached) currentBreedingLogPage++
        fetchBreedingLogs()
    }

    fun setSelectedTab(tab: Int) {
        animalUiState = animalUiState.copy(selectedTab = tab)
        fetchSelectedTab()
    }
}

data class AnimalUiState(
    val animal: Animal? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    var selectedTab: Int = 0
)

data class FeedingLogsUiState(
    val feedingLogs: List<FeedingLog> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val endReached: Boolean = false
)

data class BreedingLogsUiState(
    val breedingLogs: List<BreedingLog> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val endReached: Boolean = false
)

data class StatusChangeUiState(
    val status: String = "Hidup",
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false,
    val showDialog: Boolean = false,
    val showBottomSheet: Boolean = false
)
