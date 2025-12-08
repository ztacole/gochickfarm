package com.zetta.gochickfarm.data.remote

import com.zetta.gochickfarm.data.model.Transaction
import com.zetta.gochickfarm.data.model.TransactionDetail
import com.zetta.gochickfarm.data.model.TransactionRequest
import com.zetta.gochickfarm.network.BaseMessageResponse
import com.zetta.gochickfarm.network.BaseResponse
import com.zetta.gochickfarm.network.MetaResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import safeApiCall

class TransactionService(private val client: HttpClient) {
    companion object {
        private const val BASE_ROUTE = "transactions"

        private const val PAGE_PARAM = "page"
        private const val LIMIT_PARAM = "limit"
        private const val TYPE_PARAM = "type"
    }

    suspend fun getTransactions(page: Int, limit: Int, type: String? = null): Result<MetaResponse<List<Transaction>>> = safeApiCall {
        client.get(BASE_ROUTE) {
            parameter(PAGE_PARAM, page)
            parameter(LIMIT_PARAM, limit)
            if (type != null) parameter(TYPE_PARAM, type)
        }
    }

    suspend fun getTransactionById(id: Int): Result<BaseResponse<TransactionDetail>> = safeApiCall {
        client.get("$BASE_ROUTE/$id")
    }

    suspend fun createNewTransaction(description: String, amount: Int, date: String, type: String, animalIds: List<Int>? = null): Result<BaseMessageResponse> = safeApiCall {
        client.post(BASE_ROUTE) {
            setBody(TransactionRequest(description, amount, date, type, animalIds))
        }
    }
}