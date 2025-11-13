package com.zetta.gochickfarm.data.remote

import com.zetta.gochickfarm.network.BaseResponse
import com.zetta.gochickfarm.data.model.Summary
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import safeApiCall

class DashboardService(private val client: HttpClient){
    companion object {
        private const val BASE_ROUTE = "dashboard"

        private const val SUMMARY = "$BASE_ROUTE/summary"
    }

    suspend fun getSummary(): Result<BaseResponse<Summary>> = safeApiCall { client.get(SUMMARY) }
}