package com.zetta.gochickfarm.data.remote

import com.zetta.gochickfarm.data.model.Animal
import com.zetta.gochickfarm.network.MetaResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import safeApiCall

class AnimalService(private val client: HttpClient) {
    companion object {
        private const val BASE_ROUTE = "animals"
    }

    suspend fun getAnimals(page: Int, limit: Int, search: String? = null, status: String? = null, species: String? = null): Result<MetaResponse<List<Animal>>> = safeApiCall {
        client.get(BASE_ROUTE) {
            parameter("page", page)
            parameter("limit", limit)
            parameter("search", search)
            parameter("status", status)
            parameter("species", species)
        }
    }
}