package com.zetta.gochickfarm.ui.screen.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.zetta.gochickfarm.R
import com.zetta.gochickfarm.data.model.Summary
import com.zetta.gochickfarm.ui.theme.AppTheme
import com.zetta.gochickfarm.utils.formatRupiah
import com.zetta.gochickfarm.utils.shimmerLoading

@Composable
fun DashboardScreen(
    summaryUiState: DashboardViewModel.SummaryUiState,
    onRefresh: () -> Unit,
    onNavigateToAddFeeding: () -> Unit,
    onNavigateToBreeding: () -> Unit,
    onNavigateToTransaction: () -> Unit,
    onNavigateToGoatSearch: (category: String) -> Unit,
    onNavigateToChickenSearch: (category: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shortcutItems = listOf(
        ShortcutItem(
            icon = Icons.Default.Grass,
            title = "Feeding",
            onClick = onNavigateToAddFeeding
        ),
        ShortcutItem(
            icon = Icons.Default.Pets,
            title = "Breeding",
            onClick = onNavigateToBreeding
        ),
        ShortcutItem(
            icon = Icons.Default.MonetizationOn,
            title = "Transactions",
            onClick = onNavigateToTransaction
        )
    )

    Surface {
        PullToRefreshBox(
            isRefreshing = summaryUiState.isRefreshing,
            onRefresh = onRefresh
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                overscrollEffect = null
            ) {
                // Banner
                item { BannerSection() }

                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset { IntOffset(x = 0, y = -56) },
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ShortcutSection(shortcutItems)
                            when {
                                summaryUiState.isLoading -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(220.dp)
                                                .clip(MaterialTheme.shapes.large)
                                                .shimmerLoading()
                                        )
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(106.dp)
                                                    .clip(MaterialTheme.shapes.large)
                                                    .shimmerLoading()
                                            )
                                            Box(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(106.dp)
                                                    .clip(MaterialTheme.shapes.large)
                                                    .shimmerLoading()
                                            )
                                        }
                                    }
                                }
                                summaryUiState.errorMessage != null -> {
                                    Surface(
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Text(
                                            text = summaryUiState.errorMessage,
                                            modifier = Modifier.padding(16.dp),
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodyMedium,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                summaryUiState.summary != null -> {
                                    Column {
                                        SummarySection(summaryUiState.summary)
                                    }
                                }
                            }
                            Column {
                                CategorySection(
                                    onNavigateToChickenSearch = onNavigateToChickenSearch,
                                    onNavigateToGoatSearch = onNavigateToGoatSearch
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BannerSection() {
    Box(Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.goat_banner),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(0.2f),
                            MaterialTheme.colorScheme.primary.copy(0.65f),
                            MaterialTheme.colorScheme.primary,
                        )
                    )
                )
        )
        Image(
            painter = painterResource(R.drawable.logo_gochickfarm),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp)
                .statusBarsPadding()
                .align(Alignment.CenterStart)
        )
    }
}

@Composable
private fun CategorySection(
    onNavigateToGoatSearch: (category: String) -> Unit,
    onNavigateToChickenSearch: (category: String) -> Unit
) {
    Text(
        text = stringResource(R.string.dashboard_text_category),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
    )
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .weight(1f),
            shape = MaterialTheme.shapes.large
        ) {
            Image(
                painter = painterResource(R.drawable.chicken_nav_card),
                contentDescription = "Chicken",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(96.dp)
                    .clip(MaterialTheme.shapes.large),
                colorFilter = ColorFilter.tint(Color.Black.copy(0.4f), blendMode = BlendMode.Darken)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.text_chicken),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = stringResource(R.string.text_alive),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                    )
                }
                IconButton(
                    onClick = { onNavigateToChickenSearch("Ayam") },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "Search Chicken",
                    )
                }
            }
        }
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .weight(1f),
            shape = MaterialTheme.shapes.large
        ) {
            Image(
                painter = painterResource(R.drawable.goat_nav_card),
                contentDescription = "Goat",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(96.dp)
                    .clip(MaterialTheme.shapes.large),
                colorFilter = ColorFilter.tint(Color.Black.copy(0.4f), blendMode = BlendMode.Darken)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.text_goat),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = stringResource(R.string.text_alive),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                    )
                }
                IconButton(
                    onClick = { onNavigateToGoatSearch("Kambing") },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "Search Goat"
                    )
                }
            }
        }
    }
}

@Composable
private fun SummarySection(
    summary: Summary
) {
    Text(
        text = stringResource(R.string.dashboard_text_today_summary),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    )
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .weight(1f),
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = stringResource(R.string.dashboard_text_total_income),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp, top = 16.dp)
            )
            Text(
                text = formatRupiah(summary.todayIncome),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(Modifier.height(23.dp))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.dashboard_text_sold_chicken),
                        style = MaterialTheme.typography.labelSmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = "${summary.todaySoldChickenCount}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                VerticalDivider(modifier = Modifier.height(20.dp))
                Column {
                    Text(
                        text = stringResource(R.string.dashboard_text_sold_goat),
                        style = MaterialTheme.typography.labelSmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = "${summary.todaySoldGoatCount}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = stringResource(R.string.dashboard_text_chicken_count),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
                Text(
                    text = "${summary.chickenCount}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = stringResource(R.string.dashboard_text_goat_count),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
                Text(
                    text = "${summary.goatCount}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
        }
    }
}

data class ShortcutItem(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)

@Composable
private fun ShortcutSection(
    shortcutItems: List<ShortcutItem>,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithCache{
                onDrawBehind {
                    drawRoundRect(
                        color = primaryColor,
                        size = Size(width = size.width - 48.dp.toPx(), 4.dp.toPx()),
                        topLeft = Offset(24.dp.toPx(), size.height + 2.dp.toPx()),
                        cornerRadius = CornerRadius(x = 16.dp.toPx(), y = 16.dp.toPx())
                    )
                }
            }
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        shortcutItems.map { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = item.onClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .size(56.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DashboardPreview() {
    AppTheme {
        DashboardScreen(
            summaryUiState = DashboardViewModel.SummaryUiState(
                summary = Summary(
                    5,
                    2,
                    7,
                    2,
                    2000000
                )
            ),
            {},
            {},
            {},
            {},
            {},
            {}
        )
    }
}