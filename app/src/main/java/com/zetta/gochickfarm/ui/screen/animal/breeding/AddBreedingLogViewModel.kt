package com.zetta.gochickfarm.ui.screen.animal.breeding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.data.model.OffspringAnimal
import com.zetta.gochickfarm.data.model.SimpleAnimal
import com.zetta.gochickfarm.data.repository.AnimalRepository
import kotlinx.coroutines.launch

class AddBreedingLogViewModel(
    private val repository: AnimalRepository
): ViewModel() {
    var uiState by mutableStateOf(AddBreedingLogUiState())
        private set

    var inputState by mutableStateOf(AddBreedingInputState())
        private set

    init {
        fetchMaleList()
        fetchFemaleList()
    }

    private fun fetchMaleList() {
        uiState = uiState.copy(maleListLoading = true)
        viewModelScope.launch {
            repository.getAllWithoutPagination(inputState.selectedSpecies, "Jantan")
                .onSuccess {
                    uiState = uiState.copy(
                        maleList = it,
                        maleListLoading = false
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        maleListLoading = false,
                        errorMessage = it.message
                    )
                }
        }
    }

    private fun fetchFemaleList() {
        uiState = uiState.copy(femaleListLoading = true)
        viewModelScope.launch {
            repository.getAllWithoutPagination(inputState.selectedSpecies, "Betina")
                .onSuccess {
                    uiState = uiState.copy(
                        femaleList = it,
                        femaleListLoading = false
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        femaleListLoading = false,
                        errorMessage = it.message
                    )
                }
        }
    }

    fun updateSpecies(species: String) {
        inputState = inputState.copy(
            selectedSpecies = species,
            maleTag = null,
            femaleTag = null,
            maleId = null,
            femaleId = null,
            matingDate = null,
            offspringAnimals = emptyList()
        )
        fetchMaleList()
        fetchFemaleList()
    }

    fun updateMale(male: String) {
        val animal = uiState.maleList.find { it.tag == male } ?: return
        inputState = inputState.copy(maleTag = male, maleId = animal.id)
    }

    fun updateFemale(female: String) {
        val animal = uiState.femaleList.find { it.tag == female } ?: return
        inputState = inputState.copy(femaleTag = female, femaleId = animal.id)
    }

    fun updateMatingDate(date: String) {
        inputState = inputState.copy(matingDate = date)
    }

    fun addOffspring() {
        inputState = inputState.copy(
            offspringAnimals = inputState.offspringAnimals + OffspringInput()
        )
    }

    fun removeOffspring(index: Int) {
        val updated = inputState.offspringAnimals.toMutableList()
        updated.removeAt(index)
        inputState = inputState.copy(offspringAnimals = updated)
    }

    fun updateOffspringGender(index: Int, gender: String) {
        val list = inputState.offspringAnimals.toMutableList()
        list[index] = list[index].copy(sex = gender)
        inputState = inputState.copy(offspringAnimals = list)
    }

    fun updateOffspringWeight(index: Int, weight: String) {
        val list = inputState.offspringAnimals.toMutableList()
        list[index] = list[index].copy(weight = weight)
        inputState = inputState.copy(offspringAnimals = list)
    }

    fun submitBreedingLog() {
        uiState = uiState.copy(isProcessing = true)

        viewModelScope.launch {
            repository.createBreedingLog(
                inputState.maleId!!,
                inputState.femaleId!!,
                inputState.matingDate!!,
                inputState.offspringAnimals.size,
                inputState.offspringAnimals.map {
                    OffspringAnimal(
                        sex = it.sex,
                        weight = it.weight.toDouble()
                    )
                }
            )
            uiState = uiState.copy(isProcessing = false, isSuccess = true)
        }
    }

    data class OffspringInput(
        val sex: String = "",
        val weight: String = ""
    )

    data class AddBreedingInputState(
        val selectedSpecies: String = "Ayam",
        val maleId: Int? = null,
        val maleTag: String? = null,
        val femaleId: Int? = null,
        val femaleTag: String? = null,
        val matingDate: String? = null,
        val offspringAnimals: List<OffspringInput> = emptyList()
    )

    data class AddBreedingLogUiState(
        val speciesList: List<String> = listOf("Ayam", "Kambing"),
        val maleList: List<SimpleAnimal> = emptyList(),
        val maleListLoading: Boolean = false,
        val femaleList: List<SimpleAnimal> = emptyList(),
        val femaleListLoading: Boolean = false,
        val isProcessing: Boolean = false,
        val isSuccess: Boolean = false,
        val errorMessage: String? = null
    )
}