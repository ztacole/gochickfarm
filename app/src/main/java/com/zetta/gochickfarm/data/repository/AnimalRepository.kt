package com.zetta.gochickfarm.data.repository

import com.zetta.gochickfarm.data.model.Animal
import com.zetta.gochickfarm.data.remote.AnimalService
import com.zetta.gochickfarm.network.MetaResponse

class AnimalRepository(private val api: AnimalService) {
    suspend fun getAnimals(page: Int, limit: Int, search: String? = null, status: String? = null, species: String? = null): Result<MetaResponse<List<Animal>>> {
        val response = api.getAnimals(page, limit, search, status, species)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("No data found"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}