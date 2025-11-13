package com.zetta.gochickfarm.data.repository

import com.zetta.gochickfarm.data.model.Summary
import com.zetta.gochickfarm.data.remote.DashboardService

class DashboardRepository(private val api: DashboardService) {
    suspend fun getSummary(): Result<Summary> {
        val response = api.getSummary()

        return if (response.isSuccess) {
            response.getOrNull()?.data?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Summary is null"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}