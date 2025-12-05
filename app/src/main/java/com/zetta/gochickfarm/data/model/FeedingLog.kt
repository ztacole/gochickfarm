package com.zetta.gochickfarm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
