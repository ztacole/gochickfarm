package com.zetta.gochickfarm.ui.screen.animal.feeding

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zetta.gochickfarm.ui.components.AppButton
import com.zetta.gochickfarm.ui.components.AppDialog
import com.zetta.gochickfarm.ui.components.AppDropdown
import com.zetta.gochickfarm.ui.components.AppTextField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddFeedingLogScreen(
    onNavigateBack: () -> Unit,
    onSaveSuccess: (Int) -> Unit,
    viewModel: AddFeedingLogViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val isImeVisible = WindowInsets.isImeVisible

    val uiState = viewModel.uiState
    val inputState = viewModel.inputState

    val isInputValid by remember(inputState) {
        derivedStateOf {
            inputState.amount != null && inputState.newWeight != null &&
                    inputState.animalId != null && inputState.feedId != null
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onSaveSuccess(inputState.animalId!!)
    }

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null)
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = 4.dp, top = if (isImeVisible) 20.dp else 4.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Feed Your Animal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                overscrollEffect = null,
                contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                item {
                    Column {
                        Text(
                            text = "Choose animal",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        AppDropdown(
                            items = uiState.animalListUiState.animals.map { it.tag },
                            selectedItem = inputState.animalTag ?: "",
                            onItemSelected = { viewModel.updateAnimalInput(it) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = "No animal selected"
                        )
                    }
                }
                item {
                    Column {
                        Text(
                            text = "Choose feed",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        AppDropdown(
                            items = uiState.feedListUiState.feeds.map { it.name },
                            selectedItem = inputState.feedName ?: "",
                            onItemSelected = { viewModel.updateFeedInput(it) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = "No feed selected"
                        )
                    }
                }
                item {
                    Column {
                        Text(
                            text = "Feed amount",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        AppTextField(
                            value = inputState.amount?.toString() ?: "",
                            onValueChange = { viewModel.updateAmountInput(it) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = "Amount",
                            suffix = { Text("Kg") },
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Down)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )
                    }
                }
                item {
                    Column {
                        Text(
                            text = "New weight",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        AppTextField(
                            value = inputState.newWeight?.toString() ?: "",
                            onValueChange = { viewModel.updateNewWeightInput(it) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = "New weight",
                            suffix = { Text("Kg") },
                            keyboardActions = KeyboardActions {
                                focusManager.moveFocus(FocusDirection.Down)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )
                    }
                }
                item {
                    Column {
                        Text(
                            text = "Health notes",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        AppTextField(
                            value = inputState.healthNotes ?: "",
                            onValueChange = { viewModel.updateHealthNotesInput(it) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = "Write updated health notes in here (Optional)",
                            keyboardActions = KeyboardActions {
                                focusManager.clearFocus()
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.None
                            ),
                            singleLine = false,
                            minLines = 4
                        )
                    }
                }
            }
            Box {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
                AppButton(
                    text = "Save",
                    onClick = {
                        viewModel.createFeedingLog()
                    },
                    enabled = isInputValid,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                        .fillMaxWidth()
                )
            }

            if (uiState.feedListUiState.isLoading && uiState.animalListUiState.isLoading) {
                AppDialog(
                    show = true,
                    onDismiss = {},
                    content = {
                        CircularProgressIndicator()
                    }
                )
            }

            if (uiState.isProcessing) {
                AppDialog(
                    show = true,
                    onDismiss = {},
                    content = {
                        CircularProgressIndicator()
                    }
                )
            }
        }
    }
}