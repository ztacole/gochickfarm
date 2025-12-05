package com.zetta.gochickfarm.data.repository

import android.util.Log
import com.zetta.gochickfarm.data.model.Animal
import com.zetta.gochickfarm.data.model.BreedingLog
import com.zetta.gochickfarm.data.model.FeedingLog
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

    suspend fun getAnimalById(id: Int): Result<Animal> {
        val response = api.getAnimalById(id)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it.data)
            } ?: Result.failure(Exception("No data found"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun getFeedingLogs(animalId: Int, page: Int, limit: Int): Result<MetaResponse<List<FeedingLog>>> {
        val response = api.getFeedingLogs(animalId, page, limit)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("No data found"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun getBreedingLogs(animalId: Int, page: Int, limit: Int): Result<MetaResponse<List<BreedingLog>>> {
        val response = api.getBreedingLogs(animalId, page, limit)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("No data found"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun updateStatus(animalId: Int, status: String): Result<Unit> {
        val response = api.updateStatus(animalId, status)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(Unit)
            } ?: Result.failure(Exception("Update failed"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}