package com.zetta.gochickfarm.data.remote

import com.zetta.gochickfarm.data.model.Animal
import com.zetta.gochickfarm.data.model.BreedingLog
import com.zetta.gochickfarm.data.model.BreedingLogRequest
import com.zetta.gochickfarm.data.model.FeedingLog
import com.zetta.gochickfarm.data.model.FeedingLogRequest
import com.zetta.gochickfarm.data.model.OffspringAnimal
import com.zetta.gochickfarm.data.model.SimpleAnimal
import com.zetta.gochickfarm.data.model.UpdateStatusRequest
import com.zetta.gochickfarm.network.BaseMessageResponse
import com.zetta.gochickfarm.network.BaseResponse
import com.zetta.gochickfarm.network.MetaResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
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
        private const val SEX_PARAM = "sex"

        private const val FEEDING_LOG_ROUTE = "feeding-logs/${BASE_ROUTE}"
        private const val BREEDING_LOG_ROUTE = "breeding-logs/${BASE_ROUTE}"
        private const val GET_ALL_WITHOUT_PAGINATION_ROUTE = "$BASE_ROUTE/no-pagination"
        private const val CREATE_FEEDING_LOG_ROUTE = "feeding-logs"
        private const val CREATE_BREEDING_LOG_ROUTE = "breeding-logs"
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

    suspend fun getAllWithoutPagination(species: String?, sex: String?): Result<BaseResponse<List<SimpleAnimal>>> = safeApiCall {
        client.get(GET_ALL_WITHOUT_PAGINATION_ROUTE) {
            parameter(SPECIES_PARAM, species)
            parameter(SEX_PARAM, sex)
        }
    }

    suspend fun createFeedingLog(animalId: Int, feedId: Int, quantity: Double, newWeight: Double, healthNotes: String): Result<BaseMessageResponse> = safeApiCall {
        client.post(CREATE_FEEDING_LOG_ROUTE) {
            setBody(
                FeedingLogRequest(
                    animalId,
                    feedId,
                    quantity,
                    newWeight,
                    healthNotes
                )
            )
        }
    }

    suspend fun createBreedingLog(animalId: Int, pairId: Int, matingDate: String, offspringCount: Int, offspringAnimals: List<OffspringAnimal>): Result<BaseMessageResponse> = safeApiCall {
        client.post(CREATE_BREEDING_LOG_ROUTE) {
            setBody(
                BreedingLogRequest(
                    animalId,
                    pairId,
                    matingDate,
                    offspringCount,
                    offspringAnimals
                )
            )
        }
    }
}