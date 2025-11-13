package com.zetta.gochickfarm.data.remote

import com.zetta.gochickfarm.data.model.User
import com.zetta.gochickfarm.data.model.UserSession
import com.zetta.gochickfarm.network.BaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import safeApiCall

class AuthService(private val client: HttpClient) {
    companion object {
        private const val BASE_ROUTE = "auth"

        private const val LOGIN = "$BASE_ROUTE/login"
        private const val ME = "$BASE_ROUTE/me"
    }

    @Serializable
    data class LoginRequest(
        val email: String,
        val password: String
    )

    suspend fun login(email: String, password: String): Result<BaseResponse<UserSession>> = safeApiCall {
        client.post(LOGIN) {
            setBody(LoginRequest(email, password))
        }
    }

    suspend fun getMe(): Result<BaseResponse<User>> = safeApiCall { client.get(ME) }
}