package com.zetta.gochickfarm.ui.screen.auth

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.zetta.gochickfarm.R
import com.zetta.gochickfarm.ui.components.AppButton
import com.zetta.gochickfarm.ui.components.AppTextField
import com.zetta.gochickfarm.ui.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val imeVisible = WindowInsets.isImeVisible
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var emailErrorMessage by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = if (imeVisible) 0.dp else 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo_gochickfarm),
                contentDescription = null,
                modifier = Modifier,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Spacer(Modifier.height(96.dp))

            Text(
                text = stringResource(R.string.auth_text_email),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.height(4.dp))
            AppTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.auth_input_email_placeholder),
                isError = isEmailError,
                errorMessage = emailErrorMessage,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.None
                ),
                keyboardActions = KeyboardActions {
                    if (email.isBlank()) {
                        isEmailError = true
                        emailErrorMessage = getString(context, R.string.auth_input_required)
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        isEmailError = true
                        emailErrorMessage = getString(context, R.string.auth_input_email_invalid)
                    } else {
                        isEmailError = false
                        emailErrorMessage = ""
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                }
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.auth_text_password),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.height(4.dp))
            AppTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.auth_input_password_placeholder),
                isError = isPasswordError,
                errorMessage = passwordErrorMessage,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None
                ),
                keyboardActions = KeyboardActions {
                    if (password.isBlank()) {
                        isPasswordError = true
                        passwordErrorMessage = getString(context, R.string.auth_input_required)
                    } else {
                        isPasswordError = false
                        passwordErrorMessage = ""
                        focusManager.clearFocus()
                    }
                },
                hideValue = isPasswordVisible,
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible },
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.auth_text_forgot_password),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* TODO */ }
            )
            Spacer(Modifier.height(24.dp))

            AppButton(
                text = stringResource(R.string.auth_text_login),
                onClick = {
                    if (email.isBlank()) {
                        isEmailError = true
                        emailErrorMessage = getString(context, R.string.auth_input_required)
                    } else {
                        isEmailError = false
                        emailErrorMessage = ""
                    }
                    if (password.isBlank()) {
                        isPasswordError = true
                        passwordErrorMessage = getString(context, R.string.auth_input_required)
                    } else {
                        isPasswordError = false
                        passwordErrorMessage = ""
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && !isEmailError) {
                        isEmailError = true
                        emailErrorMessage = getString(context, R.string.auth_input_email_invalid)
                    }
                    if (isEmailError || isPasswordError) return@AppButton

                    onLoginSuccess()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showSystemUi = true, backgroundColor = 0xFFF5FBF5)
@Composable
private fun LightPreview() {
    AppTheme {
        LoginScreen({})
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES, backgroundColor = 0xFF0F1511)
@Composable
private fun DarkPreview() {
    AppTheme { LoginScreen({}) }
}