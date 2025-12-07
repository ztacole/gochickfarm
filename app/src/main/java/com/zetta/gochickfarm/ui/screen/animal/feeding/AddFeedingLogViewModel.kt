package com.zetta.gochickfarm.ui.screen.animal.feeding

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.data.model.SimpleAnimal
import com.zetta.gochickfarm.data.model.SimpleFeed
import com.zetta.gochickfarm.data.repository.AnimalRepository
import com.zetta.gochickfarm.data.repository.FeedRepository
import kotlinx.coroutines.launch

class AddFeedingLogViewModel(
    private val animalRepository: AnimalRepository,
    private val feedRepository: FeedRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var uiState by mutableStateOf(AddFeedingLogUiState())
        private set
    var inputState by mutableStateOf(AddFeedingLogInputState())
        private set

    val animalId = savedStateHandle.get<String>("animalId")
    val animalTag = savedStateHandle.get<String>("animalTag")

    init {
        inputState = inputState.copy(
            animalId = if (animalId == "{animalId}") null else animalId?.toIntOrNull(),
            animalTag = if (animalTag == "{animalTag}") null else animalTag
        )
        fetchAnimalList()
        fetchFeedList()
    }

    private fun fetchAnimalList() {
        viewModelScope.launch {
            uiState = uiState.copy(animalListUiState = uiState.animalListUiState.copy(isLoading = true))
            animalRepository.getAllWithoutPagination()
                .onSuccess {
                    uiState = uiState.copy(
                        animalListUiState = uiState.animalListUiState.copy(
                            isLoading = false,
                            animals = it
                        )
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        animalListUiState = uiState.animalListUiState.copy(isLoading = false)
                    )
                }
        }
    }

    private fun fetchFeedList() {
        viewModelScope.launch {
            uiState = uiState.copy(feedListUiState = uiState.feedListUiState.copy(isLoading = true))
            feedRepository.getAllWithoutPagination()
                .onSuccess {
                    uiState = uiState.copy(
                        feedListUiState = uiState.feedListUiState.copy(
                            isLoading = false,
                            feeds = it.map { feed ->
                                SimpleFeed(
                                    id = feed.id,
                                    name = "${feed.name} - ${feed.quantity}Kg",
                                    quantity = feed.quantity
                                )
                            }
                        )
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        feedListUiState = uiState.feedListUiState.copy(isLoading = false)
                    )
                }
        }
    }

    fun createFeedingLog() {
        viewModelScope.launch {
            uiState = uiState.copy(isProcessing = true)
            animalRepository.createFeedingLog(
                inputState.animalId!!,
                inputState.feedId!!,
                inputState.amount!!,
                inputState.newWeight!!,
                inputState.healthNotes ?: "-"
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
            }
        }
    }

    fun updateAnimalInput(animalTag: String) {
        val animal = uiState.animalListUiState.animals.find { it.tag == animalTag } ?: return
        inputState = inputState.copy(
            animalId = animal.id,
            animalTag = animal.tag
        )
    }

    fun updateFeedInput(feedName: String) {
        val feed = uiState.feedListUiState.feeds.find { it.name == feedName } ?: return
        inputState = inputState.copy(
            feedId = feed.id,
            feedName = feedName
        )
    }

    fun updateAmountInput(amount: String) {
        inputState = inputState.copy(amount = amount.toDoubleOrNull())
    }

    fun updateNewWeightInput(newWeight: String) {
        inputState = inputState.copy(newWeight = newWeight.toDoubleOrNull())
    }

    fun updateHealthNotesInput(healthNotes: String) {
        inputState = inputState.copy(healthNotes = healthNotes)
    }

    data class AnimalListUiState(
        val isLoading: Boolean = false,
        val animals: List<SimpleAnimal> = emptyList()
    )

    data class FeedListUiState(
        val isLoading: Boolean = false,
        val feeds: List<SimpleFeed> = emptyList()
    )

    data class AddFeedingLogInputState(
        val animalId: Int? = null,
        val animalTag: String? = null,
        val feedId: Int? = null,
        val feedName: String? = null,
        val amount: Double? = null,
        val newWeight: Double? = null,
        val healthNotes: String? = null
    )

    data class AddFeedingLogUiState(
        val animalListUiState: AnimalListUiState = AnimalListUiState(),
        val feedListUiState: FeedListUiState = FeedListUiState(),
        val isProcessing: Boolean = false,
        val isSuccess: Boolean = false,
        val errorMessage: String? = null
    )
}