package com.zetta.gochickfarm.network

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)
