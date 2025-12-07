package com.zetta.gochickfarm.data.remote

import com.zetta.gochickfarm.data.model.Feed
import com.zetta.gochickfarm.data.model.SimpleFeed
import com.zetta.gochickfarm.network.BaseResponse
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

        private const val GET_ALL_WITHOUT_PAGINATION_ROUTE = "$BASE_ROUTE/no-pagination"
    }

    suspend fun getFeeds(page: Int, limit: Int, search: String? = null): Result<MetaResponse<List<Feed>>> = safeApiCall {
        client.get(BASE_ROUTE) {
            parameter(PAGE_PARAM, page)
            parameter(LIMIT_PARAM, limit)
            parameter(SEARCH_PARAM, search)
        }
    }

    suspend fun getAllWithoutPagination(): Result<BaseResponse<List<SimpleFeed>>> = safeApiCall {
        client.get(GET_ALL_WITHOUT_PAGINATION_ROUTE)
    }
}