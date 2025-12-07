package com.zetta.gochickfarm.data.repository

import com.zetta.gochickfarm.data.model.Transaction
import com.zetta.gochickfarm.data.model.TransactionDetail
import com.zetta.gochickfarm.data.remote.TransactionService
import com.zetta.gochickfarm.network.MetaResponse

class TransactionRepository(private val api: TransactionService) {
    suspend fun getTransactions(page: Int, limit: Int, type: String? = null): Result<MetaResponse<List<Transaction>>> {
        val response = api.getTransactions(page, limit, type)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("No data found"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun getById(id: Int): Result<TransactionDetail> {
        val response = api.getTransactionById(id)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it.data)
            } ?: Result.failure(Exception("No data found"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun createNew(description: String, amount: Int, date: String, type: String, animalIds: List<Int>? = null): Result<Unit> {
        val response = api.createNewTransaction(description, amount, date, type, animalIds)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(Unit)
            } ?: Result.failure(Exception("Create failed"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}