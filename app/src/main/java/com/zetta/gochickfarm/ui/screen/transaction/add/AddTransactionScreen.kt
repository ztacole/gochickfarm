package com.zetta.gochickfarm.ui.screen.transaction.add

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
fun AddTransactionScreen(
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: AddTransactionViewModel = koinViewModel()
) {
    val ui = viewModel.uiState
    val input = viewModel.inputState
    val context = LocalContext.current
    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(ui.isSuccess) {
        if (ui.isSuccess) onSaveSuccess()
    }

    LaunchedEffect(ui.errorMessage) {
        ui.errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    val isInputValid by remember(input) {
        derivedStateOf {
            input.transactionType.isNotEmpty() &&
                    input.total.toIntOrNull() != null &&
                    input.total.toInt() > 0 &&
                    input.date.isNotEmpty()
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


            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Text("Tipe Transaksi")
                    Spacer(Modifier.height(4.dp))
                    AppDropdown(
                        items = ui.transactionTypeList,
                        selectedItem = input.transactionType,
                        placeholder = "Pilih tipe",
                        onItemSelected = { viewModel.updateTransactionType(it) }
                    )
                }

                item {
                    Text("Deskripsi")
                    Spacer(Modifier.height(4.dp))
                    AppTextField(
                        value = input.description,
                        onValueChange = viewModel::updateDescription,
                        placeholder = "Deskripsi"
                    )
                }

                item {
                    Text("Total (Rp)")
                    Spacer(Modifier.height(4.dp))
                    AppTextField(
                        value = input.total,
                        onValueChange = viewModel::updateTotal,
                        placeholder = "Total",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                item {
                    Text("Tanggal")
                    Spacer(Modifier.height(4.dp))
                    AppDatePicker(
                        value = input.date,
                        onValueChange = viewModel::updateDate,
                        placeholder = "Pilih tanggal"
                    )
                }

                item {
                    AnimatedVisibility(
                        visible = input.transactionType == "Pemasukan",
                        modifier = Modifier
                            .animateItem(
                                fadeInSpec = tween(easing = EaseInOut),
                                fadeOutSpec = tween(easing = EaseInOut),
                                placementSpec = null
                            )
                    ) {
                        Column {
                            Text("Tag Animal")
                            Spacer(Modifier.height(4.dp))

                            AppDropdown(
                                items = ui.animalTags
                                    .filterNot { tag -> input.selectedTags.any { it.id == tag.id } }
                                    .map { it.tag },
                                selectedItem = "-",
                                placeholder = "Pilih animal",
                                onItemSelected = { viewModel.toggleAnimalTag(it) }
                            )

                            Spacer(Modifier.height(8.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                input.selectedTags.forEach { tag ->
                                    AssistChip(
                                        onClick = { },
                                        label = { Text(tag.tag) },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = { viewModel.removeAnimalTag(tag.tag) },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Close,
                                                    contentDescription = "Remove",
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        },
                                        shape = MaterialTheme.shapes.small,
                                        border = null,
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ---- SAVE BUTTON ----
            Box {
                HorizontalDivider(Modifier.fillMaxWidth())
                AppButton(
                    text = "Save",
                    onClick = { viewModel.submit() },
                    enabled = isInputValid,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }

            if (ui.loadingAnimalTags || ui.isProcessing) {
                AppDialog(
                    show = true,
                    onDismiss = { },
                    content = {
                        CircularProgressIndicator()
                    }
                )
            }
        }
    }
}
