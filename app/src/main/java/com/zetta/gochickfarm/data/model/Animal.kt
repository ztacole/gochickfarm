package com.zetta.gochickfarm.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Animal(
    val id: String,
    val tag: String,
    val species: String,
    val age: String,
    val sex: String,
    val weight: Double,
    val birthdate: String,
    val status: String
)
