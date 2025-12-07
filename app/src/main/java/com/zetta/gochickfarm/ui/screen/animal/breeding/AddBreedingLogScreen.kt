package com.zetta.gochickfarm.ui.screen.animal.breeding

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zetta.gochickfarm.ui.components.AppButton
import com.zetta.gochickfarm.ui.components.AppDatePicker
import com.zetta.gochickfarm.ui.components.AppDialog
import com.zetta.gochickfarm.ui.components.AppDropdown
import com.zetta.gochickfarm.ui.components.AppTextField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddBreedingLogScreen(
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: AddBreedingLogViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    val input = viewModel.inputState

    val context = LocalContext.current
    val isImeVisible = WindowInsets.isImeVisible

    val isInputValid by remember(input) {
        derivedStateOf {
            input.maleTag != null &&
                    input.femaleTag != null &&
                    !input.matingDate.isNullOrEmpty() &&
                    input.offspringAnimals.isNotEmpty() &&
                    input.offspringAnimals.all {
                        it.sex.isNotEmpty() &&
                                it.weight.isNotEmpty() &&
                                it.weight.toDoubleOrNull() != null &&
                                it.weight.toDouble() > 0.0
                    }
        }
    }

    LaunchedEffect(input) {
        println("DEBUG: Input changed -> $input")
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onSaveSuccess()
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // ---- TOP BAR ----
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        start = 4.dp,
                        top = if (isImeVisible) 20.dp else 4.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    "Add Breeding Log",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // ---- FORM ----
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { 
                    Text("Choose species", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                    AppDropdown(
                        items = uiState.speciesList,
                        selectedItem = input.selectedSpecies,
                        onItemSelected = { viewModel.updateSpecies(it) },
                        placeholder = "Select the species"
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Choose male", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(4.dp))
                            AppDropdown(
                                items = uiState.maleList.map { it.tag },
                                selectedItem = input.maleTag ?: "",
                                onItemSelected = { viewModel.updateMale(it) },
                                placeholder = "Male Tag"
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text("Choose female", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(4.dp))
                            AppDropdown(
                                items = uiState.femaleList.map { it.tag },
                                selectedItem = input.femaleTag ?: "",
                                onItemSelected = { viewModel.updateFemale(it) },
                                placeholder = "Female Tag"
                            )
                        }
                    }
                }

                item {
                    Text("Mating Date", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                    AppDatePicker(
                        value = input.matingDate,
                        onValueChange = { viewModel.updateMatingDate(it) },
                        placeholder = "yyyy-MM-dd",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // ---- Dynamic Offspring List ----
                itemsIndexed(input.offspringAnimals, key = { i, _ -> i }) { index, offspring ->

                    AnimatedVisibility(
                        visible = true,
                        modifier = Modifier
                            .animateItem(
                                fadeInSpec = tween(easing = EaseInOut),
                                fadeOutSpec = tween(easing = EaseInOut),
                                placementSpec = null
                            )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text("Anak ${index + 1}", fontWeight = FontWeight.Bold)

                            Spacer(Modifier.height(8.dp))

                            Text("Gender", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(4.dp))
                            AppDropdown(
                                items = listOf("Jantan", "Betina"),
                                selectedItem = offspring.sex,
                                onItemSelected = { viewModel.updateOffspringGender(index, it) },
                                placeholder = "Gender"
                            )

                            Spacer(Modifier.height(8.dp))

                            Text("Weight (Kg)", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(4.dp))
                            AppTextField(
                                value = offspring.weight,
                                onValueChange = { viewModel.updateOffspringWeight(index, it) },
                                placeholder = "Weight (Kg)",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(4.dp))

                            AppButton(
                                text = "Remove",
                                onClick = { viewModel.removeOffspring(index) }
                            )
                        }
                    }
                }

                item {
                    AppButton(
                        text = "Add offspring",
                        onClick = { viewModel.addOffspring() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(
                                fadeInSpec = tween(easing = EaseInOut),
                                fadeOutSpec = tween(easing = EaseInOut),
                                placementSpec = null
                            )
                    )
                }
            }

            // ---- SAVE BUTTON ----
            Box {
                HorizontalDivider(Modifier.fillMaxWidth())
                AppButton(
                    text = "Save",
                    onClick = { viewModel.submitBreedingLog() },
                    enabled = isInputValid,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }

            if (uiState.maleListLoading && uiState.femaleListLoading) {
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