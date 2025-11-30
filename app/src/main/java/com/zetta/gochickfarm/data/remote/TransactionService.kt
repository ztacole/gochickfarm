package com.zetta.gochickfarm.data.remote

import com.zetta.gochickfarm.data.model.Transaction
import com.zetta.gochickfarm.network.MetaResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import safeApiCall

class TransactionService(private val client: HttpClient) {
    companion object {
        private const val BASE_ROUTE = "transactions"
    }

    suspend fun getTransactions(page: Int, limit: Int, type: String? = null): Result<MetaResponse<List<Transaction>>> = safeApiCall {
        client.get(BASE_ROUTE) {
            parameter("page", page)
            parameter("limit", limit)
            if (type != null) parameter("type", type)
        }
    }
}