package com.zetta.gochickfarm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    val id: Int,
    val name: String,
    val quantity: Double,
    @SerialName("price_per_unit")
    val price: Int,
)
