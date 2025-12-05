package com.zetta.gochickfarm.data.model

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
data class UpdateStatusRequest(
    val status: String
)