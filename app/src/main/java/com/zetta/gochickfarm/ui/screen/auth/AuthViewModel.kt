package com.zetta.gochickfarm.ui.screen.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.R
import com.zetta.gochickfarm.data.repository.AuthRepository
import com.zetta.gochickfarm.utils.ResourceProvider
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun signIn() {
        viewModelScope.launch {
            clearValidationState()
            if (!validateInput()) return@launch

            loginUiState = loginUiState.copy(isAuthenticating = true)
            repository.login(loginUiState.email, loginUiState.password)
                .onSuccess {
                    loginUiState = loginUiState.copy(
                        authenticationSucceed = true,
                        isAuthenticating = false
                    )
                }
                .onFailure {
                    loginUiState = loginUiState.copy(
                        authenticationErrorMessage = it.message,
                        isAuthenticating = false,
                        password = ""
                    )
                }
        }
    }

    fun clearAuthError() {
        loginUiState = loginUiState.copy(
            authenticationErrorMessage = null
        )
    }

    private fun clearValidationState() {
        loginUiState = loginUiState.copy(
            emailErrorMessage = null,
            passwordErrorMessage = null,
            isEmailError = false,
            isPasswordError = false
        )
    }

    private fun validateInput(): Boolean {
        if (loginUiState.email.isEmpty()) {
            loginUiState =
                loginUiState.copy(
                    emailErrorMessage = resourceProvider.getString(R.string.auth_input_required),
                    isEmailError = true
                )
        }
        if (loginUiState.password.isEmpty()) {
            loginUiState =
                loginUiState.copy(
                    passwordErrorMessage = resourceProvider.getString(R.string.auth_input_required),
                    isPasswordError = true
                )
        }
        if (loginUiState.email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(loginUiState.email)
                .matches()
        ) {
            loginUiState =
                loginUiState.copy(
                    emailErrorMessage = resourceProvider.getString(R.string.auth_input_email_invalid),
                    isEmailError = true
                )
        }

        return !(loginUiState.isEmailError || loginUiState.isPasswordError)
    }

    fun updateEmail(input: String) {
        loginUiState = loginUiState.copy(
            email = input,
            emailErrorMessage = null,
            isEmailError = false
        )
    }

    fun updatePassword(input: String) {
        loginUiState = loginUiState.copy(
            password = input,
            passwordErrorMessage = null,
            isPasswordError = false
        )
    }

    data class LoginUiState(
        val email: String = "",
        val emailErrorMessage: String? = null,
        val isEmailError: Boolean = false,
        val password: String = "",
        val passwordErrorMessage: String? = null,
        val isPasswordError: Boolean = false,
        val isAuthenticating: Boolean = false,
        val authenticationSucceed: Boolean = false,
        val authenticationErrorMessage: String? = null
    )
}