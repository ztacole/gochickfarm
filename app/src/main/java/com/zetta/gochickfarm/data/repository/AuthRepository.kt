package com.zetta.gochickfarm.data.repository

import android.util.Log
import com.zetta.gochickfarm.data.model.User
import com.zetta.gochickfarm.data.remote.AuthService
import com.zetta.gochickfarm.network.SessionManager

class AuthRepository(
    private val api: AuthService,
    private val sessionManager: SessionManager
) {
    suspend fun login(email: String, password: String): Result<String> {
        val response = api.login(email, password)
        return if (response.isSuccess) {
            response.getOrNull()?.data?.let {
                sessionManager.saveSession(it)
                Result.success("Login successful")
            } ?: Result.failure(Exception("Login failed"))
        } else {
            Log.e("TAG", "login: ${response.exceptionOrNull()}", )
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun getMe(): Result<User> {
        val response = api.getMe()
        return if (response.isSuccess) {
            response.getOrNull()?.data?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Failed to fetch user data"))
        } else Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
    }
}