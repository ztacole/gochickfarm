package com.zetta.gochickfarm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreedingLog(
    val id: Int,
    @SerialName("animal_pair")
    val animalPair: AnimalPair,
    @SerialName("offspring_count")
    val offspringCount: Int,
    @SerialName("mating_date")
    val matingDate: String
)

@Serializable
data class AnimalPair(
    val id: Int,
    val tag: String,
    val sex: String
)
