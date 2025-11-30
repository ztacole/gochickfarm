package com.zetta.gochickfarm.data.repository

import com.zetta.gochickfarm.data.model.Feed
import com.zetta.gochickfarm.data.remote.FeedService
import com.zetta.gochickfarm.network.MetaResponse

class FeedRepository(private val api: FeedService) {
    suspend fun getFeeds(page: Int, limit: Int, search: String? = null): Result<MetaResponse<List<Feed>>> {
        val response = api.getFeeds(page, limit, search)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("No data found"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}