package com.zetta.gochickfarm.data.remote

import com.zetta.gochickfarm.data.model.Feed
import com.zetta.gochickfarm.network.MetaResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import safeApiCall

class FeedService(private val client: HttpClient) {
    companion object {
        private const val BASE_ROUTE = "feeds"
    }

    suspend fun getFeeds(page: Int, limit: Int, search: String? = null): Result<MetaResponse<List<Feed>>> = safeApiCall {
        client.get(BASE_ROUTE) {
            parameter("page", page)
            parameter("limit", limit)
            parameter("search", search)
        }
    }
}