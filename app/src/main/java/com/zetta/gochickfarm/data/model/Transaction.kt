package com.zetta.gochickfarm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: Int,
    val description: String,
    @SerialName("total")
    val amount: Int,
    val date: String,
    val type: String
)

@Serializable
data class TransactionDetail(
    val id: Int,
    val description: String,
    @SerialName("total")
    val amount: Int,
    val date: String,
    val type: String,
    val animals: List<AnimalTransaction>
)

@Serializable
data class AnimalTransaction(
    val id: Int,
    val tag: String,
    val species: String,
    val status: String
)

@Serializable
data class TransactionRequest(
    val description: String,
    @SerialName("total")
    val amount: Int,
    val date: String,
    val type: String,
    @SerialName("animal_ids")
    val animalIds: List<Int>? = null
)