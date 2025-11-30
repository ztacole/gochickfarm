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
