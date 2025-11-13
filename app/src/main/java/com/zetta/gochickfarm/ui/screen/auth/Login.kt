package com.zetta.gochickfarm.ui.screen.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun Login(
    onNavigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: AuthViewModel = koinViewModel()

    LoginScreen(
        loginUiState = viewModel.loginUiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onLoginClick = viewModel::signIn,
        onNavigateToDashboard = onNavigateToDashboard,
        onClearAuthError = viewModel::clearAuthError,
        modifier = modifier
    )
}