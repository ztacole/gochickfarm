package com.zetta.gochickfarm.ui.screen.animal.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zetta.gochickfarm.R
import com.zetta.gochickfarm.ui.components.AppButton
import com.zetta.gochickfarm.ui.components.AppDropdown
import com.zetta.gochickfarm.utils.formatDate
import com.zetta.gochickfarm.utils.shimmerLoading
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddFeeding: (Int) -> Unit,
    viewModel: AnimalDetailViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val animalUiState = viewModel.animalUiState
    val feedingLogsUiState = viewModel.feedingLogsUiState
    val breedingLogsUiState = viewModel.breedingLogsUiState
    val statusChangeUiState = viewModel.statusChangeUiState

    LaunchedEffect(statusChangeUiState.message) {
        if (statusChangeUiState.message != null) Toast.makeText(context, statusChangeUiState.message, Toast.LENGTH_SHORT).show()
    }

    val scope = rememberCoroutineScope()
    val changeStatusSheetState = rememberModalBottomSheetState()
    val lazyListState = rememberLazyListState()

    val shouldFetchMore by remember {
        derivedStateOf {
            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            val totalItems = lazyListState.layoutInfo.totalItemsCount - 4
            lastVisibleItem.index >= totalItems
        }
    }

    LaunchedEffect(shouldFetchMore) {
        if (shouldFetchMore) viewModel.loadMoreSelectedTab()
    }

    val statusColor = when (animalUiState.animal?.status) {
        "Hidup" -> MaterialTheme.colorScheme.primaryContainer
        "Terjual" -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.errorContainer
    }
    val onStatusColor = when (animalUiState.animal?.status) {
        "Hidup" -> MaterialTheme.colorScheme.primary
        "Terjual" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }
    val statusOptions = listOf("Hidup", "Terjual", "Mati")

    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = 4.dp, top = 4.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Detail Animal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            when {
                animalUiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(MaterialTheme.shapes.small)
                            .height(248.dp)
                            .shimmerLoading(),
                    )
                }
                animalUiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(248.dp)
                            .background(
                                MaterialTheme.colorScheme.errorContainer,
                                MaterialTheme.shapes.small
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = animalUiState.errorMessage,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
                animalUiState.animal != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(
                            containerColor = statusColor
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(if (animalUiState.animal.species == "Ayam") R.drawable.ic_chicken else R.drawable.ic_goat),
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "TAG: ${animalUiState.animal.tag}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${animalUiState.animal.species} ${animalUiState.animal.sex}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(
                                color = onStatusColor,
                                thickness = 2.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            DetailRow("Age", animalUiState.animal.age)
                            DetailRow("Weight", "${animalUiState.animal.weight} Kg")
                            DetailRow("Status", animalUiState.animal.status)
                        }
                    }
                }
            }

            PrimaryTabRow(
                selectedTabIndex = animalUiState.selectedTab,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = animalUiState.selectedTab == 0,
                    onClick = { viewModel.setSelectedTab(0) },
                    text = { Text("Feeding Logs") }
                )
                Tab(
                    selected = animalUiState.selectedTab == 1,
                    onClick = { viewModel.setSelectedTab(1) },
                    text = { Text("Breeding Logs") }
                )
            }

            PullToRefreshBox(
                isRefreshing = if (animalUiState.selectedTab == 0)
                    feedingLogsUiState.isRefreshing else breedingLogsUiState.isRefreshing,
                onRefresh = { viewModel.refreshSelectedTab() },
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when(animalUiState.selectedTab) {
                        0 -> {
                            when {
                                feedingLogsUiState.isLoading -> {
                                    items(4) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(64.dp)
                                                .clip(MaterialTheme.shapes.small)
                                                .shimmerLoading()
                                        )
                                    }
                                }
                                feedingLogsUiState.feedingLogs.isEmpty() -> {
                                    item {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Text(
                                                text = "No feeding logs found",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                                feedingLogsUiState.errorMessage != null -> {
                                    item {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = feedingLogsUiState.errorMessage,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Red
                                            )
                                        }
                                    }
                                }
                                else -> {
                                    items(feedingLogsUiState.feedingLogs) { item ->
                                        HistoryCard(
                                            date = formatDate(item.date),
                                            content = "${item.amount} Kg ${item.feed} - Weight: ${item.newWeight} Kg"
                                        )
                                    }
                                    if (feedingLogsUiState.isLoadingMore) {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator()
                                            }
                                        }
                                    }

                                    if (feedingLogsUiState.endReached) {
                                        item {
                                            Column(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.text_end_reached),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = Color.Gray,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        1 -> {
                            when {
                                breedingLogsUiState.isLoading -> {
                                    items(4) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(64.dp)
                                                .clip(MaterialTheme.shapes.small)
                                                .shimmerLoading()
                                        )
                                    }
                                }
                                breedingLogsUiState.breedingLogs.isEmpty() -> {
                                    item {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Text(
                                                text = "No breeding logs found",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                                breedingLogsUiState.errorMessage != null -> {
                                    item {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = breedingLogsUiState.errorMessage,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Red
                                            )
                                        }
                                    }
                                }
                                else -> {
                                    items(breedingLogsUiState.breedingLogs) { item ->
                                        HistoryCard(
                                            date = formatDate(item.matingDate),
                                            content = "${item.animalPair.sex}: ${item.animalPair.tag} - ${item.offspringCount} offspring"
                                        )
                                    }
                                    if (breedingLogsUiState.isLoadingMore) {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator()
                                            }
                                        }
                                    }

                                    if (breedingLogsUiState.endReached) {
                                        item {
                                            Column(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.text_end_reached),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = Color.Gray,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }

            if (animalUiState.animal?.status == "Hidup") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppButton(
                        text = "Change Status",
                        onClick = { viewModel.showStatusChangeBottomSheet() },
                        outlineMode = true,
                        modifier = Modifier.weight(1f),
                    )
                    AppButton(
                        text = "Feed",
                        onClick = { onNavigateToAddFeeding(animalUiState.animal.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (statusChangeUiState.showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.dismissStatusChangeBottomSheet() },
                    sheetState = changeStatusSheetState,
                    properties = ModalBottomSheetProperties(
                        shouldDismissOnBackPress = true,
                        shouldDismissOnClickOutside = false,
                    )
                ) {
                    Text(
                        text = "Change Status",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    AppDropdown(
                        items = statusOptions,
                        selectedItem = statusChangeUiState.status,
                        onItemSelected = { viewModel.updateStatusInputState(it) },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        placeholder = "Select Status",
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppButton(
                            text = "Cancel",
                            onClick = {
                                scope.launch { changeStatusSheetState.hide() }.invokeOnCompletion {
                                    if (!changeStatusSheetState.isVisible) {
                                        viewModel.dismissStatusChangeBottomSheet()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            outlineMode = true
                        )
                        AppButton(
                            text = "Save",
                            onClick = {
                                scope.launch {
                                    viewModel.updateStatus(statusChangeUiState.status)
                                    changeStatusSheetState.hide()
                                }.invokeOnCompletion {
                                    if (!changeStatusSheetState.isVisible) {
                                        viewModel.dismissStatusChangeBottomSheet()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun HistoryCard(date: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}