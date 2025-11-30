package com.zetta.gochickfarm.ui.screen.animal.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zetta.gochickfarm.R
import com.zetta.gochickfarm.ui.components.AppDropdown
import com.zetta.gochickfarm.ui.components.AppTextField
import com.zetta.gochickfarm.utils.shimmerLoading
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: AnimalListViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    val lazyListState = rememberLazyListState()

    val statusList = listOf("Hidup", "Mati", "Terjual")
    val speciesList = listOf("Semua", "Kambing", "Ayam")

    val shouldFetchNextPage: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            val totalItems = lazyListState.layoutInfo.totalItemsCount - 4
            lastVisibleItem.index >= totalItems
        }
    }

    LaunchedEffect(shouldFetchNextPage) {
        if (shouldFetchNextPage)
            viewModel.loadMoreAnimals()
    }

    Surface {
        PullToRefreshBox(
            uiState.isRefreshing,
            onRefresh = { viewModel.refreshAnimals() },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 88.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = "Animal List",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(top = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        AppTextField(
                            value = uiState.search ?: "",
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = "Find animal by tag",
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(Icons.Rounded.Search, contentDescription = null)
                            },
                            outlineMode = true
                        )
                        Spacer(Modifier.height(8.dp))
                        AppDropdown(
                            placeholder = "Filter by species",
                            items = speciesList,
                            selectedItem = uiState.species,
                            onItemSelected = { viewModel.updateSpeciesFilter(it) },
                        )
                        Spacer(Modifier.height(8.dp))
                        AppDropdown(
                            placeholder = "Filter by status",
                            items = statusList,
                            selectedItem = uiState.status,
                            onItemSelected = { viewModel.updateStatusFilter(it) },
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
                item { Spacer(Modifier.height(8.dp)) }
                when {
                    uiState.isLoading -> {
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
                    uiState.animals.isEmpty() -> {
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "No animal found",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    uiState.errorMessage != null -> {
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = uiState.errorMessage,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            }
                        }
                    }
                    else -> {
                        item {
                            Text(
                                text = "Data Found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        items(uiState.animals) { animal ->
                            AnimalCard(
                                tag = animal.tag,
                                gender = animal.sex,
                                weight = animal.weight,
                                status = animal.status,
                                species = animal.species,
                                onClick = { onNavigateToDetail(animal.tag) }
                            )
                        }

                        if (uiState.isLoadingMore) {
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

                        if (uiState.endReached) {
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
        }
    }
}

@Composable
fun AnimalCard(
    tag: String,
    gender: String,
    weight: Double,
    status: String,
    species: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(if (species == "Ayam") R.drawable.ic_chicken else R.drawable.ic_goat),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TAG: $tag",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$gender | $weight",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AssistChip(
                onClick = { },
                label = { Text(status) },
                border = null,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = when (status) {
                        "Hidup" -> MaterialTheme.colorScheme.primaryContainer
                        "Terjual" -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.errorContainer
                    },
                    labelColor = when (status) {
                        "Hidup" -> MaterialTheme.colorScheme.onPrimaryContainer
                        "Terjual" -> MaterialTheme.colorScheme.onTertiaryContainer
                        else -> MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            )
        }
    }
}