package com.zetta.gochickfarm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Animal(
    val id: Int,
    val tag: String,
    val species: String,
    val age: String,
    val sex: String,
    val weight: Double,
    val birthdate: String,
    val status: String
)

@Serializable
data class SimpleAnimal(
    val id: Int,
    val tag: String
)

@Serializable
data class FeedingLog(
    val id: Int,
    val date: String,
    val time: String,
    val feed: String,
    val amount: Double,
    @SerialName("new_weight")
    val newWeight: Double,
    @SerialName("health_notes")
    val healthNotes: String
)

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

@Serializable
data class UpdateStatusRequest(
    val status: String
)

@Serializable
data class FeedingLogRequest(
    @SerialName("animal_id")
    val animalId: Int,
    @SerialName("feed_id")
    val feedId: Int,
    val quantity: Double,
    @SerialName("new_weight")
    val newWeight: Double,
    @SerialName("health_notes")
    val healthNotes: String
)

@Serializable
data class BreedingLogRequest(
    @SerialName("animal_id")
    val animalId: Int,
    @SerialName("pair_id")
    val pairId: Int,
    @SerialName("mating_date")
    val matingDate: String,
    @SerialName("offspring_count")
    val offspringCount: Int,
    @SerialName("offspring_animals")
    val offspringAnimals: List<OffspringAnimal>
)

@Serializable
data class OffspringAnimal(
    val sex: String,
    val weight: Double
)