package com.zetta.gochickfarm.data.repository

import android.util.Log
import com.zetta.gochickfarm.data.model.Animal
import com.zetta.gochickfarm.data.model.BreedingLog
import com.zetta.gochickfarm.data.model.FeedingLog
import com.zetta.gochickfarm.data.model.OffspringAnimal
import com.zetta.gochickfarm.data.model.SimpleAnimal
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

    suspend fun getAllWithoutPagination(species: String?, sex: String?): Result<List<SimpleAnimal>> {
        val response = api.getAllWithoutPagination(species, sex)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it.data)
            } ?: Result.failure(Exception("No data found"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun createFeedingLog(animalId: Int, feedId: Int, quantity: Double, newWeight: Double, healthNotes: String): Result<Unit> {
        val response = api.createFeedingLog(animalId, feedId, quantity, newWeight, healthNotes)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(Unit)
            } ?: Result.failure(Exception("Create failed"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun createBreedingLog(animalId: Int, pairId: Int, matingDate: String, offspringCount: Int, offspringAnimals: List<OffspringAnimal>): Result<Unit> {
        val response = api.createBreedingLog(animalId, pairId, matingDate, offspringCount, offspringAnimals)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(Unit)
            } ?: Result.failure(Exception("Create failed"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}