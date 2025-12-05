package com.zetta.gochickfarm.data.remote

import com.zetta.gochickfarm.data.model.Animal
import com.zetta.gochickfarm.data.model.BreedingLog
import com.zetta.gochickfarm.data.model.FeedingLog
import com.zetta.gochickfarm.data.model.UpdateStatusRequest
import com.zetta.gochickfarm.network.BaseMessageResponse
import com.zetta.gochickfarm.network.BaseResponse
import com.zetta.gochickfarm.network.MetaResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import safeApiCall

class AnimalService(private val client: HttpClient) {
    companion object {
        private const val BASE_ROUTE = "animals"

        private const val PAGE_PARAM = "page"
        private const val LIMIT_PARAM = "limit"
        private const val SEARCH_PARAM = "search"
        private const val STATUS_PARAM = "status"
        private const val SPECIES_PARAM = "species"

        private const val FEEDING_LOG_ROUTE = "feeding-logs/${BASE_ROUTE}"
        private const val BREEDING_LOG_ROUTE = "breeding-logs/${BASE_ROUTE}"
    }

    suspend fun getAnimals(page: Int, limit: Int, search: String? = null, status: String? = null, species: String? = null): Result<MetaResponse<List<Animal>>> = safeApiCall {
        client.get(BASE_ROUTE) {
            parameter(PAGE_PARAM, page)
            parameter(LIMIT_PARAM, limit)
            parameter(SEARCH_PARAM, search)
            parameter(STATUS_PARAM, status)
            parameter(SPECIES_PARAM, species)
        }
    }

    suspend fun getAnimalById(id: Int): Result<BaseResponse<Animal>> = safeApiCall {
        client.get("$BASE_ROUTE/$id")
    }

    suspend fun getFeedingLogs(animalId: Int, page: Int, limit: Int): Result<MetaResponse<List<FeedingLog>>> = safeApiCall {
        client.get("$FEEDING_LOG_ROUTE/$animalId") {
            parameter(PAGE_PARAM, page)
            parameter(LIMIT_PARAM, limit)
        }
    }

    suspend fun getBreedingLogs(animalId: Int, page: Int, limit: Int): Result<MetaResponse<List<BreedingLog>>> = safeApiCall {
        client.get("$BREEDING_LOG_ROUTE/$animalId") {
            parameter(PAGE_PARAM, page)
            parameter(LIMIT_PARAM, limit)
        }
    }

    suspend fun updateStatus(animalId: Int, status: String): Result<BaseMessageResponse> = safeApiCall {
        client.patch("$BASE_ROUTE/$animalId/status") {
            setBody(UpdateStatusRequest(status))
        }
    }
}