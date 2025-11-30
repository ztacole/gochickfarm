package com.zetta.gochickfarm.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaResponse<out T>(
    val data: T,
    val meta: Meta,
    val message: String,
    val success: Boolean
)

@Serializable
data class Meta(
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("per_page")
    val perPage: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_items")
    val totalItems: Int
)
