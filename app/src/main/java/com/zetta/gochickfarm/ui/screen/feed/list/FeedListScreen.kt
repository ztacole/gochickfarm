package com.zetta.gochickfarm.ui.screen.feed.list

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.zetta.gochickfarm.R
import com.zetta.gochickfarm.ui.components.AppTextField
import com.zetta.gochickfarm.utils.formatRupiah
import com.zetta.gochickfarm.utils.shimmerLoading
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedListScreen(
    viewModel: FeedListViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    val lazyListState = rememberLazyListState()

    val shouldFetchNextPage: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            val totalItems = lazyListState.layoutInfo.totalItemsCount - 4
            lastVisibleItem.index >= totalItems
        }
    }

    LaunchedEffect(shouldFetchNextPage) {
        if (shouldFetchNextPage)
            viewModel.loadMoreFeeds()
    }

    Surface {
        PullToRefreshBox(
            uiState.isRefreshing,
            onRefresh = { viewModel.refreshFeeds() },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 88.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                overscrollEffect = null
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
                            placeholder = "Find feed by name",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardActions = KeyboardActions {
                                viewModel.updateSearchQuery(uiState.search ?: "")
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            trailingIcon = {
                                Icon(Icons.Rounded.Search, contentDescription = null)
                            },
                            outlineMode = true
                        )
                        Spacer(Modifier.height(8.dp))
                        HorizontalDivider()
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
                    uiState.feeds.isEmpty() -> {
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "No feed found",
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
                        items(uiState.feeds) { feed ->
                            FeedCard(
                                name = feed.name,
                                stock = feed.quantity,
                                price = feed.price
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
fun FeedCard(name: String, stock: Double, price: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small),
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
            Icon(
                imageVector = Icons.Default.Grass,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$stock Kg | ${formatRupiah(price)} / Kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}