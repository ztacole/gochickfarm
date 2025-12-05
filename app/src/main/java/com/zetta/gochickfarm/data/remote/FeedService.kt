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

        private const val PAGE_PARAM = "page"
        private const val LIMIT_PARAM = "limit"
        private const val SEARCH_PARAM = "search"
    }

    suspend fun getFeeds(page: Int, limit: Int, search: String? = null): Result<MetaResponse<List<Feed>>> = safeApiCall {
        client.get(BASE_ROUTE) {
            parameter(PAGE_PARAM, page)
            parameter(LIMIT_PARAM, limit)
            parameter(SEARCH_PARAM, search)
        }
    }
}